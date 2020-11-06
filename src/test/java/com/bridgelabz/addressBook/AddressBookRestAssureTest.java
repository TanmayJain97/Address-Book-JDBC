package com.bridgelabz.addressBook;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import java.time.LocalDate;
import java.util.*;
import org.junit.*;

import com.bridgelabz.addressBook.Exception.AddressBookException;
import com.bridgelabz.addressBook.Models.Contacts;
import com.google.gson.Gson;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

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

	public List<Response> addContactToJsonServer(List<Contacts> record) throws AddressBookException {
		Map<Integer,Boolean> addStatus = new HashMap<>();
		List<Response> responseList = new ArrayList<>();
		for(Contacts contact:record) {
			Runnable task = ()->{
				addStatus.put(contact.hashCode(),false);

				String contactJson = new Gson().toJson(contact);
				//System.out.println(contactJson);
				RequestSpecification request = RestAssured.given();
				request.header("Content-Type", "application/json");
				request.body(contactJson);
				//System.out.println(request.post("/addressbook").getStatusCode());
				responseList.add(request.post("/addressbook"));

				addStatus.put(contact.hashCode(),true);
			};
			Thread thread=new Thread(task,contact.firstName);
			thread.start();
		}
		while(addStatus.containsValue(false)) {
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		return responseList;
	}

	public boolean checkAddressBookInSyncWithServer(String firstName) {
		List<Contacts> checkList = Arrays.asList(getContactArr());
		return (checkList.get(0).firstName).equals(firstName);
	}

	@Test
	public void givenContactsInJSONServer_WhenRetrieved_ShouldMatchCount() {
		assertEquals(2, contactArr.length);
	}

	@Test
	public void givenMultipleContacts_WhenAdded_shouldMatch201ResponseAndCountAndSync() throws AddressBookException {
		Contacts[] contactArr= {
				new Contacts("Akhil", "Shrotriya", "Pingal Marg", "Rohtak",
						"Haryana", 123002, "8465216975", "akshrotriya@gmail.com",LocalDate.now()),
				new Contacts("Donal", "Trump", "White House", "Washington",
						"Washington DC", 100001, "9999999999", "pm@gmai.com",LocalDate.now()),
				new Contacts("Ravi", "Kumar", "JLN Marg", "Sampak",
						"MP", 230056, "9648515621", "rkboi@yahoo.com",LocalDate.now()),
		};
		List<Contacts> record=Arrays.asList(contactArr);
		List<Response> responseList = addContactToJsonServer(record);
		contactArr = getContactArr();

		boolean responseFlag=true;
		for(Response response: responseList) {
			if(response.getStatusCode()!=(201)) {
				responseFlag=false;
			}
		}

		boolean syncFlag=true;
		for(Contacts contact:contactArr) {
			if (!checkAddressBookInSyncWithServer(contact.firstName)) syncFlag=false;
		}

		boolean finalFlag=responseFlag && contactArr.length==5 && syncFlag;
		assertTrue(finalFlag);
	}

	public void givenAddressForContact_WhenUpdated_ShouldReturn200ResponseAndSync() throws AddressBookException {

		addressBookFunction.updateRecordInServer("Tanmay", "Malviya Nagar");
		Contacts contact=addressBookFunction.getRecordDataByName("Tanmay");
		String contactJson = new Gson().toJson(contact);
		System.out.println(contactJson);
		RequestSpecification request = RestAssured.given();
		request.header("Content-Type", "application/json");
		request.body(contactJson);
		Response response = request.put("//addressbook/" + contact.firstName);
		int statusCode = response.getStatusCode();
		assertEquals(200, statusCode);
		assertTrue(checkAddressBookInSyncWithServer("Tanmay"));
	}

	public void givenContactToDlete_WhenDeleted_ShouldReturn200ResponseAndCountAndSync() throws AddressBookException {
		
		Contacts contact=addressBookFunction.getRecordDataByName("Narendra");
		RequestSpecification requestSpecification = RestAssured.given();
		requestSpecification.header("Content-Type","application/json");
		Response response = requestSpecification.delete("//addressbook/"+contact.firstName);
		
		addressBookFunction.deleteFromServer(contact.firstName);
		
		contactArr = getContactArr();
		
		boolean flag=(!checkAddressBookInSyncWithServer("Narendra")) && contactArr.length==4 && response.getStatusCode()==200;
		assertTrue(flag);
	}
}