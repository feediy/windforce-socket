package com.test.time;

import io.netty.channel.ChannelFuture;

import java.io.IOException;

import com.windforce.client.Wclient;

public class TimeClient {
	public static void main(String[] args) throws IOException {
		Wclient client = new Wclient();
		client.addChannelHandler(new TimeClientHandler());
		try {
			ChannelFuture channelFuture = client.connect(22222, "127.0.0.1");
			channelFuture.channel().closeFuture().sync();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} finally {
			client.shutdownGracefully();
		}
	}
}
