package com.test.echo;

import java.util.concurrent.atomic.AtomicLong;

import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

@Sharable
public class EchoServerCountHandler extends ChannelInboundHandlerAdapter {

	private AtomicLong count = new AtomicLong(0l);

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		if (count.addAndGet(1l) % 100000 == 0) {
			System.out.println(count.get());
		}
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		cause.printStackTrace();
		ctx.channel().close();
	}

}
