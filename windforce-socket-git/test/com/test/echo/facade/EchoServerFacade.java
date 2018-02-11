package com.test.echo.facade;

import java.util.concurrent.atomic.AtomicLong;

import org.springframework.stereotype.Component;

import com.test.echo.packet.EchoProto.EchoMessage;
import com.windforce.annotation.SocketClass;
import com.windforce.annotation.SocketMethod;
import com.windforce.core.Wsession;

@Component
@SocketClass
public class EchoServerFacade {

	private AtomicLong count = new AtomicLong(0l);

	@SocketMethod()
	public EchoMessage echo(Wsession session, EchoMessage echoMessage) {
		if (count.addAndGet(1l) % 100000 == 0) {
			System.out.println(count.get() + " " + session.getChannel().id().asShortText());
		}
		return echoMessage;
	}

}
