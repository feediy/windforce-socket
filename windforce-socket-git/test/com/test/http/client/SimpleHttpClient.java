package com.test.http.client;

import org.junit.Test;

import com.windforce.client.WsynHttpClient;

import junit.framework.TestCase;

public class SimpleHttpClient {

	@Test
	public void test() {
		WsynHttpClient client = new WsynHttpClient("http://127.0.0.1:8082");
		try {
			client.execute(true);
		} catch (Exception e) {
			e.printStackTrace();
			TestCase.fail();
		}

		try {
			Thread.sleep(100000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
