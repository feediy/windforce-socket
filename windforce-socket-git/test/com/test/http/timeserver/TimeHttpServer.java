package com.test.http.timeserver;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.test.time.TimeServer;
import com.windforce.server.WhttpServer;

public class TimeHttpServer {

	private static final Logger logger = LoggerFactory.getLogger(TimeServer.class);
	/** 默认的上下文配置名 */
	private static final String DEFAULT_APPLICATION_CONTEXT = "com\\test\\http\\timeserver\\TimeContext.xml";

	public static void main(String[] args) throws InterruptedException {
		ClassPathXmlApplicationContext applicationContext = new ClassPathXmlApplicationContext(
				DEFAULT_APPLICATION_CONTEXT);

		WhttpServer wHttpServer = applicationContext.getBean(WhttpServer.class);
		wHttpServer.getChanneHandles().add(new TimeHttpServerChannel());
		wHttpServer.bind(8082);

		while (applicationContext.isActive()) {
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				if (logger.isDebugEnabled()) {
					logger.debug("服务器主线程被非法打断", e);
				}
			}
		}
	}

}
