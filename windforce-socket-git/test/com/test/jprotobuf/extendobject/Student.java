package com.test.jprotobuf.extendobject;

import com.baidu.bjf.remoting.protobuf.annotation.Protobuf;

public class Student extends Person {
	@Protobuf
	private String no;

	public String getNo() {
		return no;
	}

	public void setNo(String no) {
		this.no = no;
	}

}
