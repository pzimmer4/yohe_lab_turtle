package com.dupliphy.units;

import java.util.ArrayList;

/**
 * An R score
 * @author ryanames
 *
 */
public class R {
	
	private ArrayList<Double> r;
	
	/**
	 * Constructor
	 */
	R() {
		this.r = new ArrayList<Double>();
	}
	
	/**
	 * Set the R scores
	 * @param r - the R scores
	 */
	public void setR(ArrayList<Double> r) {
		this.r = r;
	}
	
	/**
	 * Get the R scores
	 * @return ArrayList<Double>
	 */
	public ArrayList<Double> getR() {
		return r;
	}
	
	/**
	 * Set a single R score
	 * @param index - the index of the score
	 * @param rValue - the R score
	 */
	public void setSingleR(int index, double rValue) {
		this.r.add(index, rValue);
	}
	
	/**
	 * Get a single R score
	 * @param index - the index of the score
	 * @return double
	 */
	public double getSingleR(int index) {
		return r.get(index);
	}
	
	/**
	 * Clear the R scores
	 */
	public void clear() {
		this.r.clear();
	}
}
