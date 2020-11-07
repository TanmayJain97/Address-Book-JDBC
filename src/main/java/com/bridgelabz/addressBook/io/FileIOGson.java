package com.bridgelabz.addressBook.io;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

import com.google.gson.Gson;

public class FileIOGson {

	public static final String FILENAME = "./src/Resources/AddressBookRecord.json";
	
	public boolean writeData(ArrayList<Contacts> record) {
		
		try {
			Writer writer = Files.newBufferedWriter(Paths.get(FILENAME));
			new Gson().toJson(record,writer);
			writer.close();
			return true;
		} catch (IOException exception) {
			exception.printStackTrace();
			return false;
		}
	}
	
	public ArrayList<Contacts> readData(){
		ArrayList<Contacts> record=new ArrayList<Contacts>();
		
		try {
			Reader reader = Files.newBufferedReader(Paths.get(FILENAME));
			record.addAll(Arrays.asList(new Gson().fromJson(reader,Contacts[].class)));
			reader.close();
			return record;
		} catch (IOException exception) {
			exception.printStackTrace();
			return null;
		}
	}
}