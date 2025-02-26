package com.dupliphy.processUtils;

import java.io.IOException;

import com.dupliphy.exceptions.MatrixException;
import com.dupliphy.io.ResultOutput;
import com.dupliphy.units.BinaryTree;
import com.dupliphy.units.GeneFamilies;
import com.dupliphy.units.GeneFamily;
import com.dupliphy.units.Matrix;

/**
 * Calculate weighted parsimony scores
 * @author ryanames
 *
 */
public class WeightedParsimony {

	private BinaryTree tree;
	private GeneFamilies families;
	private Matrix matrix;
	private ResultOutput results;
	
	/**
	 * Constructor
	 * @param tree - the binary tree
	 * @param families - the gene families - ordered by ID in a TreeMap
	 * @param matrix - the matirx of weights
	 * @param outputFile - the output file prefix for the results
	 */
	public WeightedParsimony(BinaryTree tree, GeneFamilies families, Matrix matrix, String outputFile) {
		this.tree = tree;
		this.families = families;
		this.matrix = matrix;
		this.results = new ResultOutput(outputFile);
	}
	
	/**
	 * Calculate the parsimony scores
	 * Iterate through all gene families calculating the ancestral family sizes for each family
	 * @throws IOException 
	 * @throws MatrixException 
	 */
	public void calculateParsimony() throws IOException, MatrixException {
		
		for (String familyID : this.families.getFamilies().keySet()) {
			
			GeneFamily geneFamily = this.families.getFamilies().get(familyID);
			
			this.tree.setLeafDuplicates(geneFamily, this.matrix.getLength());
			
			this.tree.setRValues(this.matrix);
			
			this.tree.setFamilySizeTies();
			
			this.tree.setFamilySize(this.matrix);
			
			this.results.outputResult(this.tree, familyID);
			
			tree.clear();
		}
	}
}
