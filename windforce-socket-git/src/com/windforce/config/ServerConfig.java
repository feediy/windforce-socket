package com.windforce.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * wnet.properties 管理
 * 
 * @author Kuang Hao
 * @since v1.0 2018年1月31日
 *
 */
@Component
public class ServerConfig {

	private static final Logger logger = LoggerFactory.getLogger(ServerConfig.class);
	private Properties prop = new Properties();

	@PostConstruct
	public void initProperties() throws IOException {
		String fileName = "wnet.properties";

		InputStream inputStream = ServerConfig.class.getClassLoader().getResourceAsStream(fileName);
		if (inputStream == null) {
			logger.error(String.format("没有找到wnet.properties文件!"));
			throw new RuntimeException("没有找到wnet.properties文件!");
		}
		prop.load(inputStream);
	}

	public String getProp(String key) {
		return prop.getProperty(key);
	}
}
