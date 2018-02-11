package com.test.jprotobuf.enumpojo;

import com.baidu.bjf.remoting.protobuf.EnumReadable;

public enum SimpleEnum implements EnumReadable {
	A(1, "a"), B(2, "b");

	private final int value;

	private final String name;

	SimpleEnum(int value, String name) {
		this.value = value;
		this.name = name;
	}

	@Override
	public int value() {
		return this.value;
	}

	public String getName() {
		return name;
	}

}
