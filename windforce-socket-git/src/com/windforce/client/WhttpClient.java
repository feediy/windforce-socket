package com.windforce.client;

import java.util.ArrayList;
import java.util.List;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpRequestEncoder;
import io.netty.handler.codec.http.HttpResponseDecoder;

/**
 * http异步客服端
 * 
 * @author Kuang Hao
 * @since v1.0 2016年6月3日
 *
 */
public class WhttpClient {

	private static EventLoopGroup eventLoopGroup = new NioEventLoopGroup();
	private static Bootstrap b = new Bootstrap();

	private Channel channel;

	private List<ChannelHandler> extraHandlers = new ArrayList<ChannelHandler>();

	public void addChannelHandler(ChannelHandler ch) {
		extraHandlers.add(ch);
	}

	public ChannelFuture connect(int port, String host) throws InterruptedException {
		b.group(eventLoopGroup).channel(NioSocketChannel.class).option(ChannelOption.TCP_NODELAY, true)
				.handler(new ChannelInitializer<SocketChannel>() {
					@Override
					protected void initChannel(SocketChannel sc) throws Exception {
						sc.pipeline().addLast("decoder", new HttpResponseDecoder());
						sc.pipeline().addLast("http-aggregator", new HttpObjectAggregator(512 * 1024));
						sc.pipeline().addLast("encoder", new HttpRequestEncoder());
						for (ChannelHandler ch : extraHandlers) {
							sc.pipeline().addLast(ch);
						}
					}
				});
		ChannelFuture channelFuture = b.connect(host, port).sync();
		channel = channelFuture.channel();
		return channelFuture;
	}

	public void sendPacket(Object packet) {
		channel.writeAndFlush(packet);
	}
}
