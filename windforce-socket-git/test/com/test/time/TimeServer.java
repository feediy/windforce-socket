package com.test.time;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.windforce.dispatcher.IdentityEventExecutorGroup;
import com.windforce.server.Wserver;

public class TimeServer {

	private static final Logger logger = LoggerFactory.getLogger(TimeServer.class);
	/** 默认的上下文配置名 */
	private static final String DEFAULT_APPLICATION_CONTEXT = "com\\test\\time\\TimeContext.xml";

	public static void main(String[] args) throws Exception {
		ClassPathXmlApplicationContext applicationContext = new ClassPathXmlApplicationContext(
				DEFAULT_APPLICATION_CONTEXT);

		Wserver wserver = applicationContext.getBean(Wserver.class);
		IdentityEventExecutorGroup identityEventExecutorGroup = new IdentityEventExecutorGroup(8);
		wserver.bind(identityEventExecutorGroup);

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
