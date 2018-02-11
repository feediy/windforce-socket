package com.test.echo;


import com.windforce.client.Wclient;

import io.netty.channel.ChannelFuture;

public class EchoClient {
	public static void main(String[] args) {
		Wclient client = new Wclient();
		client.addChannelHandler(new EchoClientHandler());
		try {
			ChannelFuture channelFuture = client.connect(22222, "127.0.0.1");
			channelFuture.channel().closeFuture().sync();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
