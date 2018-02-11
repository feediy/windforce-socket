package com.windforce.core;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

import com.windforce.filter.firewall.FirewallRecord;

import io.netty.channel.Channel;

/**
 * 会话
 * 
 * @author Kuang Hao
 * @since v1.0 2016年6月3日
 *
 */
public class Wsession {

	private static final AtomicInteger SEQ = new AtomicInteger(1);

	private int id;

	private Channel channel;

	/** 流量记录 */
	private FirewallRecord firewallRecord = new FirewallRecord();

	private Map<String, Object> attributes = new ConcurrentHashMap<String, Object>();

	public static Wsession valueOf(Channel channel) {
		Wsession session = new Wsession();
		session.channel = channel;
		session.id = SEQ.incrementAndGet();
		return session;
	}

	private Wsession() {
	}

	public Channel getChannel() {
		return channel;
	}

	public void setChannel(Channel channel) {
		this.channel = channel;
	}

	public Map<String, Object> getAttributes() {
		return attributes;
	}

	public void setAttributes(Map<String, Object> attributes) {
		this.attributes = attributes;
	}

	public FirewallRecord getFirewallRecord() {
		return firewallRecord;
	}

	public void setFirewallRecord(FirewallRecord firewallRecord) {
		this.firewallRecord = firewallRecord;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

}
