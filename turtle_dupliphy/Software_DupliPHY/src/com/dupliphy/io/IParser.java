package com.dupliphy.io;

import java.io.IOException;

import com.dupliphy.exceptions.NewickException;

/**
 * Interface for parser objects
 * @author ryanames
 *
 */
public interface IParser {

	/**
	 * Get the fileName
	 * @return String
	 */
	String getFileName();
	
	/**
	 * Parse the file
	 * @throws IOException 
	 * @throws NewickException 
	 */
	void parse() throws IOException, NewickException;
	
	/**
	 * Get the data after the parse
	 * @return Object
	 */
	Object getData();
}
