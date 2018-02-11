package com.windforce.dispatcher;

import io.netty.util.concurrent.DefaultThreadFactory;

/**
 * wsocket.任务调度器
 * 
 * @author Kuang Hao
 * @since v1.0 2018年2月3日
 *
 */
public class IdentityEventExecutorGroup {

	final private EventExecutor[] children;

	public IdentityEventExecutorGroup(int nThreads) {
		children = new EventExecutor[nThreads];
		for (int i = 0; i < nThreads; i++) {
			children[i] = new EventExecutor(null, new DefaultThreadFactory("Identity-dispatcher"), true);
		}
	}

	public void addTask(AbstractDispatcherHashCodeRunable dispatcherHashCode) {
		EventExecutor eventExecutor = children[dispatcherHashCode.getDispatcherHashCode() % children.length];
		dispatcherHashCode.setEventExecutor(eventExecutor);
		eventExecutor.addTask(dispatcherHashCode);
	}
}
