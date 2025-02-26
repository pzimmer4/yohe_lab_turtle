package com.dupliphy.utils;

import java.util.Iterator;

/**
 * A class to give an alphabetic sequence
 * @author ryanames
 *
 */
public class Sequence implements Iterator<String> {

	private int now;
	private static char[] vs;
	static {
		vs = new char['Z' - 'A' + 1];
		for (char i = 'A'; i <= 'Z'; i++) {
			vs[i - 'A'] = i;
		}
	}
	
	@Override
	public boolean hasNext() {
		return true;
	}
	
	/**
	 * 
	 * @param i - counter
	 * @return a string builder
	 */
	private StringBuilder alpha(int i) {
		assert i > 0;
		char r = vs[--i % vs.length];
		int n = i / vs.length;
		if (n == 0) {
			return new StringBuilder().append(r);
		}
		return alpha(n).append(r);
	}
	
	@Override
	public String next() {
		return alpha(++now).toString();
	}
	
	@Override
	public void remove() {
		
	}
}
