package com.bridgelabz.addressBook;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import java.util.List;

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
}