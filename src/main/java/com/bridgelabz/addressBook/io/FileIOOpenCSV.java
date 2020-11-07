package com.bridgelabz.addressBook.io;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;
import com.opencsv.bean.StatefulBeanToCsv;
import com.opencsv.bean.StatefulBeanToCsvBuilder;

public class FileIOOpenCSV {

	public static final String FILENAME = "./src/Resources/AddressBookRecord.csv";

	public boolean writeData(ArrayList<Contacts> record){
		
		try {
			Writer writer = Files.newBufferedWriter(Paths.get(FILENAME));
			StatefulBeanToCsv<Contacts> beanToCsv = new StatefulBeanToCsvBuilder<Contacts>(writer)
					.withQuotechar(CSVWriter.NO_QUOTE_CHARACTER).build();
			beanToCsv.write(record);
			return true;
		}catch(Exception exception) {
			exception.printStackTrace();
			return false;
		}
	}

	public int countLines() {
		int lines = 0;

		try {
			lines = (int) Files.lines(new File(FILENAME).toPath()).count();
		}catch(IOException exception) {
			exception.printStackTrace();
		}
		return lines;
	}

	public ArrayList<Contacts> readData(){
		ArrayList<Contacts> record=new ArrayList<Contacts>();
		
		try {
			FileReader reader=new FileReader(FILENAME);
			CSVReader csvreader=new CSVReader(reader);
			String[] dataRecord;
			while(csvreader.readNext()!=null) {
				dataRecord=csvreader.readNext();
				Contacts c=new Contacts(dataRecord[0], dataRecord[1],
						dataRecord[2], dataRecord[3], dataRecord[4],
						Long.valueOf(dataRecord[5]), dataRecord[6], dataRecord[7]);
				record.add(c);
			}
			csvreader.close();
			return record;
		}catch(Exception exception) {
			System.out.println("Exception occured");
			exception.printStackTrace();
		}
		return null;
	}
}