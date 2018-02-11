package com.test.jprotobuf.pojo2proto;

import com.baidu.bjf.remoting.protobuf.FieldType;
import com.baidu.bjf.remoting.protobuf.annotation.Protobuf;

public class Person {

	@Protobuf(fieldType = FieldType.STRING, order = 1, required = true)
	public String name;
	@Protobuf(fieldType = FieldType.INT32, order = 2, required = true)
	public int id;
	@Protobuf(fieldType = FieldType.STRING, order = 3, required = false)
	public String email;

	@Protobuf(fieldType = FieldType.DOUBLE, order = 4, required = false)
	public Double doubleF;

	@Protobuf(fieldType = FieldType.FLOAT, order = 5, required = false)
	public Float floatF;

	@Protobuf(fieldType = FieldType.BYTES, order = 6, required = false)
	public byte[] bytesF;

	@Protobuf(fieldType = FieldType.BOOL, order = 7, required = false)
	public Boolean boolF;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Double getDoubleF() {
		return doubleF;
	}

	public void setDoubleF(Double doubleF) {
		this.doubleF = doubleF;
	}

	public Float getFloatF() {
		return floatF;
	}

	public void setFloatF(Float floatF) {
		this.floatF = floatF;
	}

	public byte[] getBytesF() {
		return bytesF;
	}

	public void setBytesF(byte[] bytesF) {
		this.bytesF = bytesF;
	}

	public Boolean getBoolF() {
		return boolF;
	}

	public void setBoolF(Boolean boolF) {
		this.boolF = boolF;
	}

}
