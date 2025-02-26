package com.dupliphy.io;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.TreeMap;

import com.dupliphy.exceptions.FamilyException;
import com.dupliphy.units.GeneFamilies;
import com.dupliphy.units.GeneFamily;
import com.dupliphy.utils.AlphanumComparator;

/**
 * The parser for the family information
 * @author ryanames
 *
 */
public class FamilyParser {
	
	private String fileName;
	
	/**
	 * Constructor
	 * @param fileName - the family data file
	 */
	public FamilyParser(String fileName) {
		this.fileName = fileName;
	}
	
	/**
	 * Parse the family data
	 * @return TreeMap<String, GeneFamily>
	 * @throws FamilyException 
	 * @throws IOException 
	 */
	public GeneFamilies parse() throws FamilyException, IOException {
		
		TreeMap<String, GeneFamily> families = new TreeMap<String, GeneFamily>(new AlphanumComparator());
		List<String> speciesNames = new ArrayList<String>();
		
		BufferedReader reader = new BufferedReader(new FileReader(this.fileName));
		String line;
		String[] names = null;
		
		// Read the first line with the species names
		line = reader.readLine();
		names = line.split("\\s+");
		for (int i = 1; i < names.length; i++) {
			speciesNames.add(names[i]);
		}
		
		// Read the rest of the families
		while ((line = reader.readLine()) != null) {
			HashMap<String, Integer> family = new HashMap<String, Integer>();
			String[] split = line.split("\\s+");
			testInput(names.length, split.length);
			for (int i = 1; i < split.length; i++) {
				family.put(names[i], Integer.parseInt(split[i]));
			}
			families.put(split[0], new GeneFamily(family));
		}
		reader.close();
		
		return new GeneFamilies(families, speciesNames);
	}
	
	/**
	 * Test the input for an exception
	 * @param species - the number of species
	 * @param families - the number of families on the line
	 * @throws FamilyException 
	 */
	private void testInput(int species, int families) throws FamilyException {
		if (species != families) {
			throw new FamilyException("\nError: The number of families in the family file header should match the number of gene" 
					+ " families specified on each line.");
		}
	}

}
