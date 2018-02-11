package com.test.http.timeserver;

import com.windforce.client.WhttpClient;

import io.netty.channel.ChannelFuture;

public class TimeHttpClient {
	public static void main(String[] args) {
		WhttpClient wHttpClient = new WhttpClient();
		wHttpClient.addChannelHandler(new TimeClientHttpChannel());
		try {
			ChannelFuture channelFuture = wHttpClient.connect(8082, "127.0.0.1");
			channelFuture.channel().closeFuture().sync();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
