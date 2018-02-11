package com.test.jprotobuf.nestobject;

import java.io.IOException;
import java.util.ArrayList;

import org.junit.BeforeClass;
import org.junit.Test;

import com.baidu.bjf.remoting.protobuf.Codec;
import com.baidu.bjf.remoting.protobuf.ProtobufProxy;

import junit.framework.TestCase;

public class TestNestObject {

	private static Codec<AddressBookProtosPOJO> addressBookProtosCodec;

	@BeforeClass
	public static void before() {
		addressBookProtosCodec = ProtobufProxy.create(AddressBookProtosPOJO.class);
	}

	@Test()
	public void testNestClass() throws IOException {

		Person p1 = new Person();
		p1.setName("a");

		Person p2 = new Person();
		p2.setName("b");

		AddressBookProtosPOJO addBook = new AddressBookProtosPOJO();
		addBook.setPerson(p1);
		addBook.setPersons(new ArrayList<Person>());
		addBook.getPersons().add(p2);

		byte[] addBookBytes = addressBookProtosCodec.encode(addBook);

		AddressBookProtosPOJO newAddBook = addressBookProtosCodec.decode(addBookBytes);

		TestCase.assertEquals(newAddBook.getPerson().getName(), "a");
		TestCase.assertEquals(newAddBook.getPersons().get(0).getName(), "b");

	}

}
