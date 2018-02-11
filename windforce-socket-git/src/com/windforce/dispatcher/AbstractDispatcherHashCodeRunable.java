package com.windforce.dispatcher;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.windforce.dispatcher.EventExecutor.RunableStatistics;

/**
 * 任务分发标记器
 * 
 * @author Kuang Hao
 * @since v1.0 2018年2月3日
 *
 */
public abstract class AbstractDispatcherHashCodeRunable implements Runnable {

	private static final Logger logger = LoggerFactory.getLogger(AbstractDispatcherHashCodeRunable.class);

	/**
	 * 用于分发的编号
	 * 
	 * @return
	 */
	abstract int getDispatcherHashCode();

	/**
	 * 任务类型,同一种类型任务添加一种任务即可.
	 * 
	 * @return
	 */
	abstract String name();

	/**
	 * 该任务纳秒时间
	 * 
	 * @return
	 */
	abstract long timeoutNanoTime();

	/**
	 * 执行的任务
	 */
	abstract protected void doRun();

	private EventExecutor eventExecutor;

	@Override
	final public void run() {
		RunableStatistics rs = eventExecutor.getRunableStatistics().get(name());
		long start = System.nanoTime();
		try {
			doRun();
		} catch (Throwable e) {
			logger.error("任务执行错误!", e);
		} finally {
			long cost = System.nanoTime() - start;
			if (cost >= timeoutNanoTime()) {
				rs.getTimeOutCount().incrementAndGet();
			}
			rs.getCurrentCount().decrementAndGet();
			rs.getExeTotalTime().addAndGet(cost);
		}

	}

	public void setEventExecutor(EventExecutor eventExecutor) {
		this.eventExecutor = eventExecutor;
	}

}
