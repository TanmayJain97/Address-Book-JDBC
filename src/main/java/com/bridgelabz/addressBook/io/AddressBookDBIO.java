package com.bridgelabz.addressBook.io;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import com.bridgelabz.addressBook.Models.Contacts;

public class AddressBookDBIO {
	
	private static AddressBookDBIO bookDBobj;
	
	private synchronized Connection getConnection() throws SQLException {
		String jdbcURL = "jdbc:mysql://localhost:3306/address_book_service?useSSL=false";
		String userName = "root";
		String password = "password";
		Connection connection;
		System.out.println("Connecting to database: " + jdbcURL);
		connection = DriverManager.getConnection(jdbcURL, userName, password);
		System.out.println("Connection successful!!" + connection);
		return connection;
	}
	
	public static AddressBookDBIO getInstance() {
		if (bookDBobj == null)
			bookDBobj = new AddressBookDBIO();
		return bookDBobj;
	}
	
	public List<Contacts> readData() {
		String query = "SELECT * FROM address_book;";
		List<Contacts> record = new ArrayList<Contacts>();
		try(Connection connection = this.getConnection();) {
			Statement statement = connection.createStatement();
			ResultSet resultSet = statement.executeQuery(query);
			while(resultSet.next()) {
				String firstName = resultSet.getString("FIRSTNAME");
				String lastName = resultSet.getString("LASTNAME");
				String address = resultSet.getString("ADDRESS");
				String city = resultSet.getString("CITY");
				String state = resultSet.getString("STATE");
				Long zipCode = resultSet.getLong("ZIPCODE");
				String phoneNo = resultSet.getString("PHONE");
				String email = resultSet.getString("EMAIL");
				record.add(new Contacts(firstName, lastName, address,city, state,
						zipCode, phoneNo, email));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return record;
	}
}