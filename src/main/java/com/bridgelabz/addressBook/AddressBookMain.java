package com.bridgelabz.addressBook;

import java.util.List;

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
}