package com.dupliphy.units;

import com.dupliphy.exceptions.MatrixException;

/**
 * A matrix
 * @author ryanames
 *
 */
public class Matrix {

	private int width, height;
	private double[][] matrix;
	
	/**
	 * Constructor
	 * @param width - the width of the matrix
	 * @param height - the height of the matrix
	 * @param matrix - the values of the matrix as a 2D array
	 */
	public Matrix(int width, int height, double[][] matrix) {
		this.width = width;
		this.height = height;
		this.matrix = matrix;
	}
	
	/**
	 * Get the width of the matrix
	 * @return int
	 */
	public int getWidth() {
		return width;
	}
	
	/**
	 * Get the height of the matrix
	 * @return int
	 */
	public int getHeight() {
		return height;
	}
	
	/**
	 * Get the length of the matrix
	 * @return int
	 */
	public int getLength() {
		return width;
	}
	
	/**
	 * Get a value from the matrix
	 * @param row - the row number
	 * @param column - the column number
	 * @return double
	 * @throws MatrixException 
	 */
	public double get(int row, int column) throws MatrixException {
		testInput(row, column);
		return matrix[row][column];
	}
	
	/**
	 * Test the selection from the matrix
	 * @param row - the row number
	 * @param column - the column number
	 * @throws MatrixException 
	 */
	private void testInput(int row, int column) throws MatrixException {
		if (row < 0 || row > height - 1 || column < 0 || column > width - 1) {
			throw new MatrixException("\nError: Requesting a row or column not presnt in the matrix\n" 
					+ "Requested row: " + row + ", actual number of rows: " + this.height + "\n" 
					+ "Requested column: " + column + ", actual number of columns: " + this.width + "\n"
					+ "Please check your matrix and try again\n");
		}
	}
}
