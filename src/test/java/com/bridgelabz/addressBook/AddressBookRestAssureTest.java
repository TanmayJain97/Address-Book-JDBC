package com.bridgelabz.addressBook;

import static org.junit.Assert.assertEquals;

import java.util.*;
import org.junit.*;

import com.bridgelabz.addressBook.Models.Contacts;
import com.google.gson.Gson;

import io.restassured.RestAssured;
import io.restassured.response.Response;

public class AddressBookRestAssureTest {

	AddressBookMain addressBookFunction;
	Contacts[] contactArr;

	@Before
	public void init() {
		RestAssured.baseURI = "http://localhost";
		RestAssured.port = 3000;
		contactArr = getContactArr();
		addressBookFunction = new AddressBookMain();
		addressBookFunction.setContactDataList(Arrays.asList(contactArr));
	}
	
	public Contacts[] getContactArr() {
		Response response = RestAssured.get("/addressbook");
		Contacts[] contactArr = new Gson().fromJson(response.asString(), Contacts[].class);
		return contactArr;
	}
	
	@Test
	public void givenContactsInJSONServer_WhenRetrieved_ShouldMatchCount() {
		assertEquals(2, contactArr.length);
	}
}