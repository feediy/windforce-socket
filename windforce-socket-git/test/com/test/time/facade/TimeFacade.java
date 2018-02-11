package com.test.time.facade;

import java.util.Date;

import org.springframework.stereotype.Component;

import com.test.time.packet.CM_Req_ChangeTime;
import com.test.time.packet.CM_Req_Time;
import com.test.time.packet.TimePacket;
import com.windforce.annotation.SocketClass;
import com.windforce.annotation.SocketMethod;
import com.windforce.core.Wsession;

@Component
@SocketClass
public class TimeFacade {

	@SocketMethod
	public TimePacket getTime(Wsession session, CM_Req_Time cm) {
		TimePacket tp = new TimePacket();
		tp.setTime(new Date().getTime());
		return tp;
	}
	
	@SocketMethod
	public TimePacket changeSystemTime(Wsession session, CM_Req_ChangeTime cm) {
		// TODO 修改系统时间
		
		TimePacket tp = new TimePacket();
		tp.setTime(new Date().getTime());
		return tp;
	}
}
