package com.dupliphy.exceptions;

/**
 * Exception for the newick tree input
 * @author ryanames
 *
 */
public class NewickException extends Exception {
	
	private static final long serialVersionUID = 1L;
	private String message;
	
	/**
	 * Constructor for the exception
	 * @param message - the error message
	 */
	public NewickException(String message) {
		this.message = message;
	}
	
	@Override
	public String toString() {
		return message + "\nPlease check the newick format input tree and try again.";
	}

}
