package com.windforce.coder;

import com.windforce.core.WresponsePacket;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

/**
 * 
 * @author Kuang Hao
 * @since v1.0 2018年2月6日
 *
 */
public class WpacketEncoder extends MessageToByteEncoder<WresponsePacket> {

	@Override
	protected void encode(ChannelHandlerContext ctx, WresponsePacket msg, ByteBuf out) throws Exception {
		// length
		out.writeInt(msg.getData().length + 2);
		out.writeShort(msg.getPacketId());
		out.writeBytes(msg.getData());
	}

}
