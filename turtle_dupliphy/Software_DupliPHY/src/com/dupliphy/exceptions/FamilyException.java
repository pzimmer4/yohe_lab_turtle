package com.dupliphy.exceptions;

/**
 * Exception for the input family data
 * @author ryanames
 *
 */
public class FamilyException extends Exception {

	private static final long serialVersionUID = 1L;
	private String message;
	
	/**
	 * Constructor for the exception
	 * @param message - the error message
	 */
	public FamilyException(String message) {
		this.message = message;
	}
	
	@Override
	public String toString() {
		return this.message + "\nPlease check the gene family file and try again\n";
	}
}
