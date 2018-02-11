package com.windforce.client;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.windforce.core.WresponsePacket;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

/**
 * |size-int-4|packetId-short-2|data|
 * 
 * @author Kuang Hao
 * @since v1.0 2018年1月31日
 *
 */
public class WclientPacketDecoder extends ByteToMessageDecoder {

	@SuppressWarnings("unused")
	private Logger logger = LoggerFactory.getLogger("socket");

	/**
	 * length+packetId = 6
	 */
	private static final int MIN_READABLE = 4 + 2;

	@Override
	protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
		in.markReaderIndex();
		int readableBytes = in.readableBytes();

		if (readableBytes < MIN_READABLE) {
			return;
		}

		int size = in.readInt();
		if (in.readableBytes() < size) {
			in.resetReaderIndex();
			return;
		}

		short packetId = in.readShort();

		byte[] data = new byte[in.readableBytes()];
		in.readBytes(data);

		WresponsePacket response = WresponsePacket.valueOf(packetId, data);
		out.add(response);

	}

}
