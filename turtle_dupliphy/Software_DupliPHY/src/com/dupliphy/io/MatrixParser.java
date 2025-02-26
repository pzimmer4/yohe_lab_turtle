package com.dupliphy.io;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.dupliphy.exceptions.MatrixException;
import com.dupliphy.units.Matrix;

/**
 * Parse the matrix input file
 * @author ryanames
 *
 */
public class MatrixParser {

	private String fileName;
	
	/**
	 * Constructor
	 * @param fileName - the file containing the weights matrix
	 */
	public MatrixParser(String fileName) {
		this.fileName = fileName;
	}
	
	/**
	 * Parse the matrix
	 * @return Matrix
	 * @throws MatrixException 
	 * @throws IOException 
	 */
	public Matrix parse() throws MatrixException, IOException {
		BufferedReader reader = new BufferedReader(new FileReader(this.fileName));
		
		List<Double> values = new ArrayList<Double>();
		int columns = 0;
		int rows = 0;
		
		String line = null;
		while ((line = reader.readLine()) != null) {
			String[] split = line.split("\\s");
			columns = split.length;
			for (int i = 0; i < split.length; i++) {
				values.add(Double.parseDouble(split[i]));
			}
			rows++;
		}
		testInput(rows, columns);
		
		double[][] matrixValues = new double[rows][columns];
		int index = 0;
		for (int i = 0; i < rows; i++) {
			for (int j = 0; j < columns; j++) {
				matrixValues[i][j] = values.get(index);
				index++;
			}
		}
		
		Matrix matrix = new Matrix(columns, rows, matrixValues);
		return matrix;
	}
	
	/**
	 * Test the user provided input
	 * @param rows - the number of rows in the matrix
	 * @param columns - the number of columns in the matrix
	 * @throws MatrixException 
	 */
	private void testInput(int rows, int columns) throws MatrixException {
		if (rows != columns) {
			throw new MatrixException("\nError: The matrix has an incorrect number of columns or rows\n"
					+ "The number of rows must equal the number of columns \n" 
					+ "Rows: " + rows + " Columns: " + columns 
					+ "Please check your matrix and try again\n");
		}
	}
}
