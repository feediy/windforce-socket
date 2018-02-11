package com.test.udp;

import java.net.InetSocketAddress;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.DatagramPacket;
import io.netty.channel.socket.nio.NioDatagramChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.util.CharsetUtil;

public class ChineseProverbClient {
	public void run(int port) throws Exception {

		EventLoopGroup group = new NioEventLoopGroup();
		try {
			Bootstrap b = new Bootstrap();
			// 允许广播
			b.group(group).channel(NioDatagramChannel.class).option(ChannelOption.SO_BROADCAST, true)
					.handler(new LoggingHandler(LogLevel.INFO));// 设置消息处理器
			Channel ch = b.bind(0).sync().channel();
			// 向网段内的所有机器广播UDP消息。
			ByteBuf bytebuf = Unpooled.buffer();
			bytebuf.writeInt(1);
			bytebuf.writeInt(32);
			bytebuf.writeInt(Integer.MAX_VALUE);
			bytebuf.writeInt(11);
			bytebuf.writeInt(12);
			bytebuf.writeLong(1);
			// ch.writeAndFlush(new
			// DatagramPacket(Unpooled.copiedBuffer("abcde", CharsetUtil.UTF_8),
			ch.writeAndFlush(new DatagramPacket(bytebuf, new InetSocketAddress("255.255.255.255", port))).sync();
			if (!ch.closeFuture().await(15000)) {
				System.out.println("查询超时！");
			}
		} catch (Exception e) {
			e.printStackTrace();
			group.shutdownGracefully();
		}
	}

	public static void main(String[] args) throws Exception {
		int port = 8080;

		new ChineseProverbClient().run(port);
	}
}
