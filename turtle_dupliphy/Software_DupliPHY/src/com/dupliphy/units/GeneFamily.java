package com.dupliphy.units;

import java.util.HashMap;

/**
 * A gene family
 * @author ryanames
 *
 */
public class GeneFamily {

	private HashMap<String, Integer> species;
	
	/**
	 * Constructor
	 */
	public GeneFamily() {
		this.species = new HashMap<String, Integer>();
	}
	
	/**
	 * Overloaded constructor
	 * @param species - the gene family size for each species in the analysis
	 */
	public GeneFamily(HashMap<String, Integer> species) {
		this.species = species;
	}
	
	/**
	 * Add a species to the gene family
	 * @param speciesName - the name of the species
	 * @param size - the size of the species
	 */
	public void addSpecies(String speciesName, Integer size) {
		this.species.put(speciesName, size);
	}
	
	/**
	 * Get the gene family size for a particular species
	 * @param speciesName - the name of the species
	 * @return Integer
	 */
	public Integer getSpeciesSize(String speciesName) {
		if (this.species.containsKey(speciesName)) {
			return this.species.get(speciesName);
		} else {
			return null;
		}
	}
	
	/**
	 * Get the size of the largest family
	 * @return - the size of the largest family
	 */
	public int getLargestSize() {
		int largest = 0;
		for (Integer size : this.species.values()) {
			if (size > largest) {
				largest = size;
			}
		}
		return largest;
	}
}
