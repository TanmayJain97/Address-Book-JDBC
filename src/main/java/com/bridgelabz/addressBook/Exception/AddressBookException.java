package com.bridgelabz.addressBook.Exception;

public class AddressBookException extends Exception{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -7064709631081304104L;

	public enum ExceptionType
	{UPDATE_FAIL}

	public ExceptionType type;

	public AddressBookException(ExceptionType type,String message) {
		super(message);
		this.type = type;
	}
}