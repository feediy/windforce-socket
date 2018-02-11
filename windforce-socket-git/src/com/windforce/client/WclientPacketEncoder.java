package com.windforce.client;

import com.windforce.core.WrequestPacket;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

/**
 * 
 * @author Kuang Hao
 * @since v1.0 2018年2月6日
 *
 */
public class WclientPacketEncoder extends MessageToByteEncoder<WrequestPacket> {

	@Override
	protected void encode(ChannelHandlerContext ctx, WrequestPacket msg, ByteBuf out) throws Exception {
		// length
		out.writeInt(msg.getData().length + 2);
		out.writeShort(msg.getPacketId());
		out.writeBytes(msg.getData());
	}

}
