package com.test.jprotobuf.extendobject;

import java.io.File;
import java.io.IOException;

import org.junit.Test;

import com.baidu.bjf.remoting.protobuf.Codec;
import com.baidu.bjf.remoting.protobuf.ProtobufProxy;

import junit.framework.TestCase;

public class TestExtendsObject {

	@Test
	public void test() throws IOException {
		Codec<Student> studentCodes = ProtobufProxy.create(Student.class, false, new File("D:/test"));
		Student student = new Student();
		student.setName("a");
		student.setAge(11);
		student.setNo("011");
		byte[] students = studentCodes.encode(student);

		Codec<Person> personCodes = ProtobufProxy.create(Person.class);
		Person newPeron = personCodes.decode(students);
		// PS:你没看错，确实这里no和name相等了。符合PB的读取机制。
		TestCase.assertEquals(student.getNo(), newPeron.getName());

		// 用Codec<Student>来读取
		Student newStudent = studentCodes.decode(students);
		TestCase.assertEquals(student.getName(), newStudent.getName());
		TestCase.assertEquals(student.getNo(), newStudent.getNo());

	}
}
