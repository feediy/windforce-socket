package com.test.jprotobuf.simple;

import java.io.IOException;

import org.junit.Test;

import com.baidu.bjf.remoting.protobuf.Codec;
import com.baidu.bjf.remoting.protobuf.FieldType;
import com.baidu.bjf.remoting.protobuf.ProtobufProxy;
import com.baidu.bjf.remoting.protobuf.annotation.Protobuf;

import junit.framework.TestCase;

public class SimpleTypeTest {

	@Protobuf(fieldType = FieldType.STRING, order = 1, required = true)
	private String name;

	@Protobuf(fieldType = FieldType.INT32, order = 2, required = false)
	private int value;

	@Test
	public void simpleTypeTest() throws IOException {
		Codec<SimpleTypeTest> simpleTypeCodec = ProtobufProxy.create(SimpleTypeTest.class);

		SimpleTypeTest stt = new SimpleTypeTest();
		stt.setName("abc");
		stt.setValue(100);
		// 序列化
		byte[] bb = simpleTypeCodec.encode(stt);
		// 反序列化
		SimpleTypeTest newStt = simpleTypeCodec.decode(bb);
		TestCase.assertEquals(newStt.getName(), "abc");
	}

	public int getValue() {
		return value;
	}

	public void setValue(int value) {
		this.value = value;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
