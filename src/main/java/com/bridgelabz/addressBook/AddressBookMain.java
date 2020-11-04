package com.bridgelabz.addressBook;

import java.util.List;

import com.bridgelabz.addressBook.Exception.AddressBookException;
import com.bridgelabz.addressBook.Exception.AddressBookException.ExceptionType;
import com.bridgelabz.addressBook.Models.Contacts;
import com.bridgelabz.addressBook.io.AddressBookDBIO;

public class AddressBookMain {

	//Declaring global var list of employee data
	public List<Contacts> record;
	private AddressBookDBIO bookDBobj;
	
	
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
	
	private Contacts getRecordDataByName(String firstName) {
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
}