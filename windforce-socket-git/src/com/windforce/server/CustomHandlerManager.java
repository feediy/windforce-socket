package com.windforce.server;

import io.netty.channel.ChannelHandlerAdapter;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.stereotype.Component;

/**
 * 用户自定义handler管理器
 * @author Kuang Hao
 * @since v1.0 2016年6月3日
 *
 */
@Component
public final class CustomHandlerManager {

	private List<ChannelHandlerAdapter> handlers = new ArrayList<ChannelHandlerAdapter>();

	@PostConstruct
	public void init() {
		doInit();
	}

	private void doInit() {
		// TODO
	}

	public List<ChannelHandlerAdapter> getHandlers() {
		return handlers;
	}

	public void setHandlers(List<ChannelHandlerAdapter> handlers) {
		this.handlers = handlers;
	}

}
