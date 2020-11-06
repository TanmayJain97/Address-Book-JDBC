package com.bridgelabz.addressBook;

import java.util.*;

import com.bridgelabz.addressBook.Exception.AddressBookException;
import com.bridgelabz.addressBook.Exception.AddressBookException.ExceptionType;
import com.bridgelabz.addressBook.Models.Contacts;
import com.bridgelabz.addressBook.io.AddressBookDBIO;

public class AddressBookMain {

	//Declaring global var list of employee data
	public List<Contacts> record;
	private AddressBookDBIO bookDBobj;
	
	public void setContactDataList(List<Contacts> record) {
		this.record = new ArrayList<>(record);
	}
	
	public AddressBookMain() {
		bookDBobj=AddressBookDBIO.getInstance();
	}
	
	public AddressBookMain(List<Contacts> record) {
		this();
		this.record = record;
	}
	
	public List<Contacts> readContactData() {
		record = bookDBobj.readData();
		return record;
	}
	
	public Contacts getRecordDataByName(String firstName) {
		Contacts contactData = this.record.stream()
				.filter(contact->contact.firstName.equals(firstName))
				.findFirst()
				.orElse(null);
		return contactData;
	}
	
	public void updateRecord(String firstName, String address) throws AddressBookException {
		int result = bookDBobj.updateDataUsingPreparedStatement(firstName,address);
		Contacts contactData = null;
		if(result == 0)
			throw new AddressBookException(ExceptionType.UPDATE_FAIL, "Update Failed");
		else 
			contactData = this.getRecordDataByName(firstName);
		if(contactData!=null) {
			contactData.address = address;
		}
	}
	
	public boolean checkAddressBookInSyncWithDB(String FirstName) {
		List<Contacts> checkList = bookDBobj.getRecordDataByName(FirstName);
		return checkList.get(0).equals(getRecordDataByName(FirstName));
	}
	
	public List<Contacts> getRecordAddedInDateRange(String date1, String date2) {
		List<Contacts> record = bookDBobj.getRecordsAddedInGivenDateRange(date1, date2);
		return record;
	}
	
	public List<Contacts> getRecordsByCityOrState(String city, String state) {
		List<Contacts> record = bookDBobj.getRecordsByCityOrState(city, state);
		return record;
	}
	
	public void addContactToRecord(String firstName, String lastName, String address, String city, String state, long zipCode,
			String phoneNo, String email) throws AddressBookException {
		record.add(bookDBobj.addContactToRecord(firstName, lastName, address, city, state, zipCode, phoneNo, email));
	}
	
	public void addMultipleContactsToRecord(List<Contacts> record) {
		bookDBobj.addMultipleContactsToRecord(record);
	}
	
	public void updateRecordInServer(String firstName, String address) throws AddressBookException {
		Contacts contact =  this.getRecordDataByName(firstName);
		if(contact!=null) contact.address=address;
	}

	public void deleteFromServer(String firstName) {
		record.remove(getRecordDataByName(firstName));
	}
}