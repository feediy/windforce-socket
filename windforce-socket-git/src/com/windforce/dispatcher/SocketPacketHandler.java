package com.windforce.dispatcher;

import java.io.File;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.stereotype.Component;

import com.baidu.bjf.remoting.protobuf.ProtobufProxy;
import com.windforce.annotation.SocketClass;
import com.windforce.annotation.SocketMethod;
import com.windforce.annotation.SocketPacket;
import com.windforce.core.SessionManager;
import com.windforce.core.WrequestPacket;
import com.windforce.core.WresponsePacket;
import com.windforce.core.Wsession;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

/**
 * 消息处理器handler
 * 
 * @author Kuang Hao
 * @since v1.0 2016年6月3日
 *
 */
@Sharable
@Component
@SuppressWarnings("unchecked")
public class SocketPacketHandler extends ChannelInboundHandlerAdapter implements BeanPostProcessor {

	private static final Logger logger = LoggerFactory.getLogger(SocketPacketHandler.class);

	private Map<SocketHandleKey, SocketHandlerDefinition> handlerDefinitions = new ConcurrentHashMap<SocketHandleKey, SocketHandlerDefinition>();

	private Map<Class<?>, SocketHandleKey> socketClassKeys = new ConcurrentHashMap<Class<?>, SocketHandleKey>();

	@Autowired
	private SessionManager sessionManager;

	private IdentityEventExecutorGroup identityEventExecutorGroup;

	public void setIdentityEventExecutorGroup(IdentityEventExecutorGroup identityEventExecutorGroup) {
		this.identityEventExecutorGroup = identityEventExecutorGroup;
	}

	@Override
	public Object postProcessAfterInitialization(Object bean, String name) throws BeansException {
		Class<?> clazz = bean.getClass();
		if (clazz.isAnnotationPresent(SocketClass.class)) {
			for (Method method : clazz.getMethods()) {
				if (method.isAnnotationPresent(SocketMethod.class)) {

					Class<?>[] clzs = method.getParameterTypes();
					if (clzs.length != 2) {
						throw new IllegalArgumentException(
								bean.getClass().getName() + "." + method.getName() + "只能拥有两个参数。");
					}
					if (!Wsession.class.isAssignableFrom(clzs[0])) {
						throw new IllegalArgumentException(bean.getClass().getName() + "." + method.getName()
								+ "第一个参数必须为[com.windforce.core.WSession]类型。");
					}
					if (clzs[1].getAnnotation(SocketPacket.class) == null) {
						throw new IllegalArgumentException(
								bean.getClass().getName() + "." + method.getName() + "第二个参数必须为包含SocketClass注解。");
					}
					if (method.getReturnType().getName() != "void") {
						if (method.getReturnType().getAnnotation(SocketPacket.class) == null) {
							throw new IllegalArgumentException(
									bean.getClass().getName() + "." + method.getName() + "返回值必须包含SocketClass注解。");
						}
					}

					Class<?> parameterPacketClass = method.getParameterTypes()[1];
					Class<?> returnPacketClass = method.getReturnType();
					for (Class<?> packetClass : new Class<?>[] { parameterPacketClass, returnPacketClass }) {
						if ("void".equals(packetClass.getName())) {
							continue;
						}
						SocketHandleKey key = socketClassKeys.get(packetClass);
						if (key == null) {
							SocketPacket socketPacket = packetClass.getAnnotation(SocketPacket.class);
							key = SocketHandleKey.valueOf(socketPacket.packetId());
							SocketHandlerDefinition shd = handlerDefinitions.get(key);
							if (shd != null && shd.getClass() != packetClass) {
								throw new IllegalArgumentException(String.format("class[%s]和class[%s],packetId[%s]重复！",
										shd.getClass(), packetClass, socketPacket.packetId()));
							}
							socketClassKeys.put(packetClass, key);
						} else {
							continue;
						}

						if (!handlerDefinitions.containsKey(key)) {
							try {
								handlerDefinitions.put(key, SocketHandlerDefinition.valueOf(bean, method,
										ProtobufProxy.create(packetClass, false, new File("D:/"))));
							} catch (Exception e) {
								e.printStackTrace();
								logger.error("初始化生成SocketHandlerDefinition失败!", e);
								throw new IllegalArgumentException("初始化生成SocketHandlerDefinition失败!");
							}
						} else {
							throw new IllegalArgumentException(
									String.format("SocketHandlerDefinition重复注册。它已经包含在[%s]中却又尝试注册到[%s]。",
											handlerDefinitions.get(key).getBean().getClass(), clazz));
						}
					}
				}
			}
		}
		return bean;
	}

	@Override
	public Object postProcessBeforeInitialization(Object bean, String name) throws BeansException {
		return bean;
	}

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		final WrequestPacket packet = (WrequestPacket) msg;
		SocketHandleKey key = SocketHandleKey.valueOf(packet.getPacketId());
		final SocketHandlerDefinition shd = handlerDefinitions.get(key);
		if (shd == null) {
			logger.error(String.format("没有找到处理[%s]的SocketHandlerDefinition。", key));
			return;
		}
		final Channel channel = ctx.channel();
		final Object message = shd.getCodec().decode(packet.getData());

		final Wsession session = sessionManager.getSession(ctx.channel().id());
		identityEventExecutorGroup.addTask(new AbstractDispatcherHashCodeRunable() {

			@Override
			protected void doRun() {
				try {
					Object returnMessage = shd.invoke(session, message);
					if (returnMessage != null) {
						SocketHandleKey socketHandleKey = socketClassKeys.get(returnMessage.getClass());
						if (socketHandleKey != null) {
							byte[] returnMessageBytes = handlerDefinitions.get(socketHandleKey).getCodec()
									.encode(returnMessage);
							WresponsePacket wp = WresponsePacket.valueOf(socketHandleKey.getPacketId(),
									returnMessageBytes);
							channel.writeAndFlush(wp);
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
					logger.error("SocketHandlerDefinition任务执行失败!", e);
				}

			}

			@Override
			public long timeoutNanoTime() {
				// 3毫秒
				return 3 * 1000 * 1000;
			}

			@Override
			public String name() {
				return "wsocket_" + packet.getPacketId();
			}

			@Override
			public int getDispatcherHashCode() {
				return Math.abs(channel.hashCode());
			}
		});

		ctx.fireChannelRead(msg);
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		logger.error("通信包异常!", cause);
		ctx.close();
	}

}
