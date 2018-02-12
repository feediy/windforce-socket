package com.test.echo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.windforce.common.threadpool.IdentityEventExecutorGroup;
import com.windforce.server.CustomHandlerManager;
import com.windforce.server.Wserver;

public class EchoServer {

	private static final Logger logger = LoggerFactory.getLogger(EchoServer.class);
	/** 默认的上下文配置名 */
	private static final String DEFAULT_APPLICATION_CONTEXT = "applicationContext.xml";

	public static void main(String[] args) throws Exception {
		ClassPathXmlApplicationContext applicationContext = new ClassPathXmlApplicationContext(
				DEFAULT_APPLICATION_CONTEXT);

		CustomHandlerManager customHandlerManager = applicationContext.getBean(CustomHandlerManager.class);
		// customHandlerManager.getHandlers().add(new EchoServerHandler());
		// customHandlerManager.getHandlers().add(new EchoServerCountHandler());

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
