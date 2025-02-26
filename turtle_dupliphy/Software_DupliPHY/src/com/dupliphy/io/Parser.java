package com.dupliphy.io;

import java.io.IOException;

import com.dupliphy.exceptions.NewickException;

/**
 * Abstract parser class
 * @author ryanames
 *
 */
public abstract class Parser implements IParser {

	private String fileName;
	
	/**
	 * Constructor
	 * @param fileName - the name of the file to parse
	 */
	public Parser(String fileName) {
		this.fileName = fileName;
	}
	
	/**
	 * Get the file name
	 * @return String
	 */
	public String getFileName() {
		return this.fileName;
	}
	
	/**
	 * Parse the file
	 * @throws IOException 
	 * @throws NewickException 
	 */
	public abstract void parse() throws IOException, NewickException;
	
}
