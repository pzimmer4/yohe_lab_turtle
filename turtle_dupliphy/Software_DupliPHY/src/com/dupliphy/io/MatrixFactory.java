package com.dupliphy.io;

import com.dupliphy.units.Matrix;

/**
 * A matrix factory
 * @author ryanames
 *
 */
public class MatrixFactory {

	/**
	 * Blank constructor
	 */
	public MatrixFactory() {
		
	}
	
	/**
	 * Get the default matrix - gain = loss = the number of events
	 * @param length - the length (width, height) of the matrix
	 * @return a Matrix object
	 */
	public Matrix getDefaultMatrix(int length) {
		double[][] values = new double[length][length];
		for (int i = 0; i < length; i++) {
			for (int j = 0; j < length; j++) {
				double value = (double) j - (double) i;
				if (value < 0) {
					value = value * -1;
				}
				values[i][j] = value;
			}
		}
		Matrix matrix = new Matrix(length, length, values);
		return matrix;
	}
}
