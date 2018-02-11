package com.test.jprotobuf.enumpojo;

import java.io.IOException;

import org.junit.Test;

import com.baidu.bjf.remoting.protobuf.Codec;
import com.baidu.bjf.remoting.protobuf.ProtobufProxy;
import com.baidu.bjf.remoting.protobuf.annotation.Protobuf;

import junit.framework.TestCase;

public class EnumPojo {

	@Protobuf
	private SimpleEnum simpleEnum;

	public SimpleEnum getSimpleEnum() {
		return simpleEnum;
	}

	public void setSimpleEnum(SimpleEnum simpleEnum) {
		this.simpleEnum = simpleEnum;
	}

	@Test
	public void test() throws IOException {
		EnumPojo ep = new EnumPojo();
		ep.simpleEnum = SimpleEnum.A;

		Codec<EnumPojo> epCodec = ProtobufProxy.create(EnumPojo.class);
		byte[] epbytes = epCodec.encode(ep);
		EnumPojo newEp = epCodec.decode(epbytes);

		TestCase.assertEquals(newEp.getSimpleEnum(), SimpleEnum.A);
	}
}
