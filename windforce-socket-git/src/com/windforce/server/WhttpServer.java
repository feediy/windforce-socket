package com.windforce.server;

import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpRequestDecoder;
import io.netty.handler.codec.http.HttpResponseEncoder;

/**
 * HTTP 服务器
 * 
 * @author Kuang Hao
 * @since v1.0 2016年6月3日
 *
 */
@Component
public class WhttpServer {
	private static final Logger logger = LoggerFactory.getLogger(WhttpServer.class);

	private List<ChannelHandlerAdapter> channeHandles = new ArrayList<>();

	public void bind(final int port) throws InterruptedException {
		bossGroup = new NioEventLoopGroup();
		workerGroup = new NioEventLoopGroup();
		ServerBootstrap b = new ServerBootstrap();
		b.group(bossGroup, workerGroup).channel(NioServerSocketChannel.class)
				.childHandler(new ChannelInitializer<SocketChannel>() {
					@Override
					protected void initChannel(SocketChannel ch) throws Exception {
						ch.pipeline().addLast("http-decoder", new HttpRequestDecoder());
						ch.pipeline().addLast("http-aggregator", new HttpObjectAggregator(512 * 1024));
						ch.pipeline().addLast("http-encoder", new HttpResponseEncoder());
						for (ChannelHandlerAdapter cha : getChanneHandles()) {
							ch.pipeline().addLast(cha);
						}
					}
				});
		ChannelFuture future = b.bind(new InetSocketAddress(port)).sync();
		channelFutures.add(future);
		System.out.println("HTTP服务器启动，网址是 : " + "http://localhost:" + port);
	}

	private List<ChannelFuture> channelFutures = new ArrayList<>();

	private EventLoopGroup bossGroup;

	private EventLoopGroup workerGroup;

	public void shutdownGracefully() {
		try {
			for (ChannelFuture cf : channelFutures) {
				if (cf.channel() != null) {
					try {
						cf.channel().close();
					} catch (Exception e) {
						logger.error("通信server channel关闭异常", e);
					}
				}
			}
		} finally {
			bossGroup.shutdownGracefully();
			workerGroup.shutdownGracefully();
		}
	}

	public List<ChannelHandlerAdapter> getChanneHandles() {
		return channeHandles;
	}

	public void setChanneHandles(List<ChannelHandlerAdapter> channeHandles) {
		this.channeHandles = channeHandles;
	}
}
