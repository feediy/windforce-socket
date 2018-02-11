package com.windforce.dispatcher;

import java.lang.reflect.Method;

import org.springframework.util.ReflectionUtils;

import com.baidu.bjf.remoting.protobuf.Codec;
import com.windforce.core.Wsession;

/**
 * 消息处理器描述
 * 
 * @author Kuang Hao
 * @since v1.0 2016年6月3日
 *
 */
@SuppressWarnings("rawtypes")
public class SocketHandlerDefinition {
	private Object bean;
	private Method method;
	private Codec codec;

	public static SocketHandlerDefinition valueOf(Object bean, Method method, Codec codec) {
		SocketHandlerDefinition shd = new SocketHandlerDefinition();
		shd.bean = bean;
		shd.method = method;
		shd.codec = codec;
		return shd;
	}

	public Object invoke(Wsession session, Object packet) {
//		ReflectionUtils.makeAccessible(method);
		return ReflectionUtils.invokeMethod(method, bean, session, packet);
	}

	public Object getBean() {
		return bean;
	}

	public Method getMethod() {
		return method;
	}

	public Codec getCodec() {
		return codec;
	}

}
