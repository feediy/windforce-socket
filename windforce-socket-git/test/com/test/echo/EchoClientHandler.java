package com.test.echo;

import com.test.echo.packet.EchoProto.EchoMessage;
import com.windforce.core.WrequestPacket;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

public class EchoClientHandler extends ChannelInboundHandlerAdapter {

	private final WrequestPacket firstPacket;

	public EchoClientHandler() {
		EchoMessage.Builder echoMessageBuilder = EchoMessage.newBuilder();
		echoMessageBuilder.setGuid(11111l);
		echoMessageBuilder.setName("测试内容");
		firstPacket = WrequestPacket.valueOf((short) 1, echoMessageBuilder.build().toByteArray());
	}

	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		for (int i = 0; i < 1000; i++) {
			ctx.channel().writeAndFlush(firstPacket);
		}
	}

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		ctx.channel().write(msg);
	}

	@Override
	public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
		ctx.channel().flush();
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		cause.printStackTrace();
		ctx.close();
	}

}
