package com.bridgelabz.addressBook;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import java.util.*;

import com.bridgelabz.addressBook.Exception.AddressBookException;
import com.bridgelabz.addressBook.Models.Contacts;

public class AddressBookTest {
	
	AddressBookMain addressBookFunction;
	List<Contacts> record;

	@Before
	public void init() {
		addressBookFunction=new AddressBookMain();
		record=addressBookFunction.readContactData();
	}
	
	@Test
	public void givenAddressBook_WhenRetrieved_ShouldMatchCount() {
		assertEquals(2, record.size());
	}
	
	@Test
	public void givenNewAddressForRecord_WhenUpdated_ShouldSyncWithDatabase() throws AddressBookException {
		addressBookFunction.updateRecord("Tanmay","Malviya Nagar");
		assertTrue(addressBookFunction.checkAddressBookInSyncWithDB("Tanmay"));
	}
	
	@Test
	public void givenDateRangeForRecord_WhenRetrieved_ShouldReturnProperData() throws AddressBookException {
		List<Contacts> recordDataInGivenDateRange = addressBookFunction.getRecordAddedInDateRange("2018-01-01","2020-11-30");
		assertEquals(2, recordDataInGivenDateRange.size());
	}
	
	@Test
	public void givenCityOrState_WhenRetrieved_ShouldReturnProperData() throws AddressBookException {
		List<Contacts> recordDataByCityState = addressBookFunction.getRecordsByCityOrState("Jaipur", "Rajasthan");
		assertEquals(2, recordDataByCityState.size());
	}
	
	@Test
	public void givenNewContact_WhenAdded_ShouldSyncWithDB() throws AddressBookException {
		addressBookFunction.addContactToRecord("Avnish", "Prabhakar", "Mall Road",
				"Gurugram", "Haryana", 245694, "8456215596", "av.prabhakar@hotmail.com");
		assertTrue(addressBookFunction.checkAddressBookInSyncWithDB("Avnish"));
		
		record=addressBookFunction.readContactData();
		assertEquals(3, record.size());
	}
	
	@Test
	public void givenMultipleContact_WhenAdded_ShouldSyncWithDB() throws AddressBookException {
		Contacts[] contactArr= {
				new Contacts("Akhil", "Shrotriya", "Pingal Marg", "Rohtak",
						"Haryana", 123002, "8465216975", "akshrotriya@gmail.com"),
				new Contacts("Donal", "Trump", "White House", "Washington",
						"Washington DC", 100001, "9999999999", "pm@gmai.com"),
				new Contacts("Ravi", "Kumar", "JLN Marg", "Sampak",
						"MP", 230056, "9648515621", "rkboi@yahoo.com"),
		};
		List<Contacts> record=Arrays.asList(contactArr);
		addressBookFunction.addMultipleContactsToRecord(record);
		
		assertTrue(addressBookFunction.checkAddressBookInSyncWithDB("Akhil"));
		assertTrue(addressBookFunction.checkAddressBookInSyncWithDB("Donal"));
		assertTrue(addressBookFunction.checkAddressBookInSyncWithDB("Ravi"));
		
		record=addressBookFunction.readContactData();
		assertEquals(6, record.size());
	}
}