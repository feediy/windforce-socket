package com.test.http.timeserver;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpHeaderNames;
import io.netty.handler.codec.http.HttpHeaderValues;
import io.netty.handler.codec.http.HttpMethod;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpVersion;
import io.netty.handler.codec.http.QueryStringDecoder;
import io.netty.util.CharsetUtil;

@Sharable
public class TimeHttpServerChannel extends ChannelInboundHandlerAdapter {

	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		System.out.println(String.format("[%s]连接服务器!", ctx.channel().remoteAddress()));
		// super.channelActive(ctx);
	}

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {

		FullHttpRequest fullHttpRequest = (FullHttpRequest) msg;

		/**
		 * 在服务器端打印请求信息
		 */
		System.out.println("VERSION: " + fullHttpRequest.protocolVersion().text());
		System.out.println("REQUEST_URI: " + fullHttpRequest.uri());
		Iterator<Entry<String, String>> iterators = fullHttpRequest.headers().iteratorAsString();
		while (iterators.hasNext()) {
			Entry<String, String> entry = iterators.next();
			System.out.println("HEADER: " + entry.getKey() + '=' + entry.getValue());
		}

		StringBuilder responseContent = new StringBuilder();
		/**
		 * 服务器端返回信息
		 */
		responseContent.setLength(0);
		responseContent.append("WELCOME TO THE WILD WILD WEB SERVER\r\n");
		responseContent.append("===================================\r\n");

		responseContent.append("VERSION: " + fullHttpRequest.protocolVersion().text() + "\r\n");
		responseContent.append("REQUEST_URI: " + fullHttpRequest.uri() + "\r\n\r\n");
		responseContent.append("\r\n\r\n");
		Iterator<Entry<String, String>> iterators1 = fullHttpRequest.headers().iteratorAsString();
		while (iterators1.hasNext()) {
			Entry<String, String> entry = iterators1.next();
			System.out.println("HEADER: " + entry.getKey() + '=' + entry.getValue() + "\r\n");
		}
		responseContent.append("\r\n\r\n");

		if (fullHttpRequest.method().equals(HttpMethod.GET)) {
			// get请求
			QueryStringDecoder decoderQuery = new QueryStringDecoder(fullHttpRequest.uri());
			Map<String, List<String>> uriAttributes = decoderQuery.parameters();
			for (Entry<String, List<String>> attr : uriAttributes.entrySet()) {
				for (String attrVal : attr.getValue()) {
					responseContent.append("URI: " + attr.getKey() + '=' + attrVal + "\r\n");
				}
			}

			responseContent.append("END OF GET CONTENT");
			writeResponse(responseContent, fullHttpRequest, ctx.channel());
			return;
		}
		// else if (fullHttpRequest.method().equals(HttpMethod.POST)) {
		// //post请求
		// decoder = new HttpPostRequestDecoder(factory, fullHttpRequest);
		// readingChunks =
		// HttpHeaders.isTransferEncodingChunked(fullHttpRequest);
		// responseContent.append("Is Chunked: " + readingChunks + "\r\n");
		// responseContent.append("IsMultipart: " + decoder.isMultipart() +
		// "\r\n");
		//
		// try {
		// while (decoder.hasNext()) {
		// InterfaceHttpData data = decoder.next();
		// if (data != null) {
		// try {
		// writeHttpData(data);
		// } finally {
		// data.release();
		// }
		// }
		// }
		// } catch (EndOfDataDecoderException e1) {
		// responseContent.append("\r\n\r\nEND OF POST CONTENT\r\n\r\n");
		// }
		// writeResponse(ctx.channel());
		// return;
		else {
			System.out.println("discard.......");
		}
		// ctx.fireChannelRead(msg);
	}

	private void writeResponse(StringBuilder responseContent, FullHttpRequest fullHttpRequest, Channel channel) {
		// Convert the response content to a ChannelBuffer.
		ByteBuf buf = Unpooled.copiedBuffer(responseContent.toString(), CharsetUtil.UTF_8);

		// Decide whether to close the connection or not.
		boolean close = fullHttpRequest.headers().contains(HttpHeaderNames.CONNECTION, HttpHeaderValues.CLOSE, true)
				|| fullHttpRequest.protocolVersion().equals(HttpVersion.HTTP_1_0) && !fullHttpRequest.headers()
						.contains(HttpHeaderNames.CONNECTION, HttpHeaderValues.KEEP_ALIVE, true);

		// Build the response object.
		FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK, buf);
		response.headers().set(HttpHeaderNames.CONTENT_TYPE, "text/plain; charset=UTF-8");

		if (!close) {
			// There's no need to add 'Content-Length' header
			// if this is the last response.
			response.headers().set(HttpHeaderNames.CONTENT_LENGTH, buf.readableBytes() + "");
		}

		// Write the response.
		ChannelFuture future = channel.writeAndFlush(response);
		// Close the connection after the write operation is done if necessary.
		if (close) {
			future.addListener(ChannelFutureListener.CLOSE);
		}
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		cause.printStackTrace();
		ctx.channel().close();
	}

}
