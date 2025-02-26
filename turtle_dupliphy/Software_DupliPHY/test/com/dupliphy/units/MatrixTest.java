package com.dupliphy.units;

import junit.framework.Assert;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.dupliphy.exceptions.MatrixException;

/**
 * Test the matrix class
 * @author ryanames
 *
 */
public class MatrixTest {

	private Matrix matrix;
	
	/**
	 * Set up the matrix
	 * 0 1 2 3
	 * 1 0 1 2
	 * 2 1 0 1
	 * 3 2 1 0
	 */
	@Before
	public void setUp() {
		int width = 4;
		int height = 4;
		double[][] values = new double[width][height];
		for (int i = 0; i < width; i++) {
			for (int j = 0; j < height; j++) {
				double value = (double) j - (double) i;
				if (value < 0) {
					value = value * -1;
				}
				values[i][j] = value;
			}
		}
		
		matrix = new Matrix(width, height, values);
	}
	
	/**
	 * Close down the tests
	 */
	@After
	public void closeDown() {
		
	}
	
	/**
	 * Test getting values back from the array
	 * @throws MatrixException 
	 */
	@Test
	public void testGet() throws MatrixException {
		Assert.assertEquals(1.0, matrix.get(0, 1));
		Assert.assertEquals(0.0, matrix.get(2, 2));
	}
	
	/**
	 * Test the exception thrown by the Matrix class
	 */
	@Test
	public void testException() {
		try {
			matrix.get(10, 10);
		} catch (MatrixException e) {
			Assert.assertTrue(e.toString().contains("10"));
		}
	}
}
