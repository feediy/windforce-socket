package com.test.jprotobuf.nestobject;

import java.util.List;

import com.baidu.bjf.remoting.protobuf.FieldType;
import com.baidu.bjf.remoting.protobuf.annotation.Protobuf;

public class AddressBookProtosPOJO {
	@Protobuf(fieldType = FieldType.OBJECT, order = 1, required = false)
	public Person person;

	@Protobuf(fieldType = FieldType.OBJECT, order = 2, required = false)
	public List<Person> persons;

	@Protobuf(fieldType = FieldType.STRING, order = 3, required = false)
	public List<String> stringList;

	@Protobuf(fieldType = FieldType.INT32, order = 4, required = false)
	public List<Integer> intList;

	public Person getPerson() {
		return person;
	}

	public void setPerson(Person person) {
		this.person = person;
	}

	public List<Person> getPersons() {
		return persons;
	}

	public void setPersons(List<Person> persons) {
		this.persons = persons;
	}

	public List<String> getStringList() {
		return stringList;
	}

	public void setStringList(List<String> stringList) {
		this.stringList = stringList;
	}

	public List<Integer> getIntList() {
		return intList;
	}

	public void setIntList(List<Integer> intList) {
		this.intList = intList;
	}

}
