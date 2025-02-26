package com.dupliphy;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import com.dupliphy.exceptions.ArgumentsException;
import com.dupliphy.exceptions.FamilyException;
import com.dupliphy.io.FamilyParser;
import com.dupliphy.io.MatrixFactory;
import com.dupliphy.io.MatrixParser;
import com.dupliphy.io.TreeParser;
import com.dupliphy.processUtils.WeightedParsimony;
import com.dupliphy.units.BinaryTree;
import com.dupliphy.units.GeneFamilies;
import com.dupliphy.units.Matrix;

/**
 * Runs the analysis given the command line arguments
 * @author ryanames
 *
 */
public class DupliPHY {

	private static final String DATE_FORMAT = "HH:mm:ss dd/MM";
	private static String familyFile = null;
	private static String matrixFile = null;
	private static String treeFile = null;
	private static String outputFile = null;
	
	/**
	 * Main method to run the analysis
	 * @param args - the command line arguments supplied by the user
	 */
	public static void main(String[] args) {
		
		try {
			now("Started analysis at ");
			
			//set the file names from user input
			if (args.length < 3 || args.length > 4) {
				throw new ArgumentsException();
			} else {
				familyFile = args[0];
				treeFile = args[1];
				outputFile = args[2];
			}
			if (args.length == 4) {
				matrixFile = args[3];
			}
		
			//read in the family information
			System.out.println("Reading gene family sizes...");
			FamilyParser family = new FamilyParser(familyFile);
			GeneFamilies families = family.parse();
		
			//read the matrix - or create a default matrix
			System.out.println("Reading the matrix...");
			Matrix matrix = null;
			if (matrixFile != null) {
				MatrixParser matrixP = new MatrixParser(matrixFile);
				matrix = matrixP.parse();
			} else {
				MatrixFactory factory = new MatrixFactory();
				matrix = factory.getDefaultMatrix(families.getLargestFamilySize() + 1);
			}
		
			//create the tree and set its root
			System.out.println("Reading the newick tree...");
			TreeParser newick = new TreeParser(treeFile);
			newick.parse();
			BinaryTree tree = (BinaryTree) newick.getData();
		
			// Test that the leaves of the tree are the same as the species in the family file 
			if (!tree.getLeafNames().containsAll(families.getSpeciesNames())) {
				throw new FamilyException("Error: The species listed in the tree and in the family file are not the same.");
			}
			
			//do weighted parsimony and write output files
			System.out.println("Caluculating ancestral gene family sizes...\nWriting output...");
			WeightedParsimony parsimony = new WeightedParsimony(tree, families, matrix, outputFile);
			parsimony.calculateParsimony();
			
			now("Finished analysis at ");	
		} catch (Exception e) {
			System.out.println("An unexpected error has occurred:");
			System.out.println(e.toString());
			System.exit(1);
		}
		
	}
	
	/**
	 * Get the current time
	 * @param msg - Message to print with the current date and time
	 */
	private static void now(String msg) {
		Calendar cal = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);
		System.out.println(msg + sdf.format(cal.getTime()));
	}
	
}
