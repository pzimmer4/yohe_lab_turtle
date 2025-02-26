package com.dupliphy.units;

import junit.framework.Assert;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.dupliphy.exceptions.MatrixException;
import com.dupliphy.io.MatrixFactory;

/**
 * Test the creation of the default matrix
 * @author ryanames
 *
 */
public class DefaultMatrixTest {

private Matrix matrix;
	
	/**
	 * Set up the default matrix
	 * 0 1 2 3
	 * 1 0 1 2
	 * 2 1 0 1
	 * 3 2 1 0
	 */
	@Before
	public void setUp() {
		int length = 4;
		MatrixFactory factory = new MatrixFactory();
		matrix = factory.getDefaultMatrix(length);
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
}
