package com.dupliphy.units;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

import com.dupliphy.utils.AlphanumComparator;

/**
 * Class that stores gene families
 * @author ryanames
 *
 */
public class GeneFamilies {

	private TreeMap<String, GeneFamily> families;
	private List<String> speciesNames;
	
	/**
	 * Constructor
	 */
	public GeneFamilies() {
		this.families = new TreeMap<String, GeneFamily>(new AlphanumComparator());
		this.speciesNames = new ArrayList<String>();
	}
	
	/**
	 * Overloaded constructor
	 * @param families - a map of families
	 */
	public GeneFamilies(TreeMap<String, GeneFamily> families) {
		this.families = families;
		this.speciesNames = new ArrayList<String>();
	}
	
	/**
	 * Overloaded constructor
	 * @param families - the families
	 * @param speciesNames - the list of species names
	 */
	public GeneFamilies(TreeMap<String, GeneFamily> families, List<String> speciesNames) {
		this.families = families;
		this.speciesNames = speciesNames;
	}
	
	/**
	 * Get the map of gene families
	 * @return - a map of gene families
	 */
	public TreeMap<String, GeneFamily> getFamilies() {
		return this.families;
	}
	
	/**
	 * Get the list of species names
	 * @return a list of species names
	 */
	public List<String> getSpeciesNames() {
		return this.speciesNames;
	}
	
	/**
	 * Get the largest family size from the collection of families
	 * @return the largest family size
	 */
	public int getLargestFamilySize() {
		int size = 0;
		for (GeneFamily family : this.families.values()) {
			if (family.getLargestSize() > size) {
				size = family.getLargestSize();
			}
		}
		return size;
	}
}
