package com.dupliphy.exceptions;

/**
 * An exception for the arguments passed on the command line
 * @author ryanames
 *
 */
public class ArgumentsException extends Exception {

	private static final long serialVersionUID = 1L;
	
	/**
	 * Constructor
	 */
	public ArgumentsException() {
		
	}
	
	@Override
	public String toString() {
		return "You have specified the wrong number of command line arguments\n"
				+ "Usage:\n"
				+ "java -jar DupliPHY.jar <familyFile> <speciesTree> <resultsPrefix> <matrix>\n"
				+ "familyFile - the tab delimited gene family data\n"
				+ "speciesTree - a newick formatted species tree\n"
				+ "resultsPrefix - a prefix for naming the results files\n"
				+ "matrix (optional) - a square weights matrix for gains and loss events. If this is omitted a default matrix will be used.\n";
	}
}
