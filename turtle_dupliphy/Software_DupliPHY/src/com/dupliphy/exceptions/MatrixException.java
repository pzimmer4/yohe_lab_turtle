package com.dupliphy.exceptions;

/**
 * Exception for the matrix input
 * @author ryanames
 *
 */
public class MatrixException extends Exception {
	
	private static final long serialVersionUID = 1L;
	private String message;
	
	/**
	 * Constructor for the exception
	 * @param message - the error message
	 */
	public MatrixException(String message) {
		this.message = message;
	}
	
	@Override
	public String toString() {
		return this.message;
	}
}
