package com.test.time.packet;

import com.baidu.bjf.remoting.protobuf.annotation.Protobuf;
import com.windforce.annotation.SocketPacket;

@SocketPacket(packetId = 2)
public class TimePacket {

	@Protobuf
	private long time;

	public long getTime() {
		return time;
	}

	public void setTime(long time) {
		this.time = time;
	}

}
