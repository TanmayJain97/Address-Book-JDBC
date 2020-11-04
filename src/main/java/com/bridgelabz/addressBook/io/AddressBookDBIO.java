package com.bridgelabz.addressBook.io;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import com.bridgelabz.addressBook.Models.Contacts;

public class AddressBookDBIO {
	
	private static AddressBookDBIO bookDBobj;
	private PreparedStatement recordDataStatement;
	
	public enum StatementType 
		{PREPARED_STATEMENT, STATEMENT}	
	
	private void preparedStatementForRecord() {
		try {
			Connection connection = this.getConnection();
			String query = "SELECT * FROM address_book WHERE FIRSTNAME = ?";
			recordDataStatement = connection.prepareStatement(query);
		} catch (SQLException exception) {
			exception.printStackTrace();
		}
	}
	
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
		return getAddressBookData(query);
	}
	
	public List<Contacts> getAddressBookData(String query){
		List<Contacts> record = new ArrayList<Contacts>();
		try(Connection connection = this.getConnection()) {
			Statement statement = connection.createStatement();
			ResultSet resultSet = statement.executeQuery(query);
			record = this.getAddressBookData(resultSet);
		} catch (SQLException exception) {
			exception.printStackTrace();
		}
		return record;
	}
	
	private List<Contacts> getAddressBookData(ResultSet resultSet) {
		List<Contacts> contactList = new ArrayList<>();
		try {
			while (resultSet.next()) {
				String firstName = resultSet.getString("FIRSTNAME");
				String lastName = resultSet.getString("LASTNAME");
				String address = resultSet.getString("ADDRESS");
				String city = resultSet.getString("CITY");
				String state = resultSet.getString("STATE");
				Long zipCode = resultSet.getLong("ZIPCODE");
				String phoneNumber = resultSet.getString("PHONE");
				String emailId = resultSet.getString("EMAIL");
				contactList.add(new Contacts(firstName, lastName, address, city, state, zipCode, phoneNumber, emailId));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return contactList;
	}
	
	public List<Contacts> getRecordDataByName(String firstName) {
		List<Contacts> record = null;
		if (this.recordDataStatement == null) this.preparedStatementForRecord();
		try {
			recordDataStatement.setString(1, firstName);
			ResultSet resultSet = recordDataStatement.executeQuery();
			record = this.getAddressBookData(resultSet);
		} catch (SQLException exception) {
			exception.printStackTrace();
		}
		return record;
	}
	
	public int updateDataUsingPreparedStatement(String firstName, String address) {
		String query = "UPDATE address_book SET ADDRESS = ? WHERE FIRSTNAME = ?";
		try (Connection connection = this.getConnection();) {
			PreparedStatement preparedStatementUpdate = connection.prepareStatement(query);
			preparedStatementUpdate.setString(1, address);
			preparedStatementUpdate.setString(2, firstName);
			return preparedStatementUpdate.executeUpdate();
		} catch (SQLException exception) {
			exception.printStackTrace();
		}
		return 0;
	}
}