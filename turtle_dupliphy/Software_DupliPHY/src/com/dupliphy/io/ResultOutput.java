package com.dupliphy.io;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import com.dupliphy.units.BinaryTree;
import com.dupliphy.units.Node;

/**
 * 
 * @author ryanames
 *
 */
public class ResultOutput {
	
	private static String descSuffix = "_desc.txt";
	private static String treeSuffix = "_tree.ph";
	private String descriptionFile;
	private String treeFile;
	
	/**
	 * Constructor creates the file names and deletes the files
	 * if they already exist
	 * @param outputPrefix the prefix for the ouput files
	 */
	public ResultOutput(String outputPrefix) {
		this.descriptionFile = outputPrefix + descSuffix;
		if (fileExists(this.descriptionFile)) {
			deleteFile(this.descriptionFile);
		}
		this.treeFile = outputPrefix + treeSuffix;
		if (fileExists(this.treeFile)) {
			deleteFile(this.treeFile);
		}
	}
	
	/**
	 * Output the results of a single family to a file
	 * @param tree - the binary tree
	 * @param familyID - the family ID
	 * @throws IOException 
	 */
	public void outputResult(BinaryTree tree, String familyID) throws IOException {
		if (!fileExists(this.descriptionFile)) {
			printHeaderLine(tree);
		}
		printFamilyDescription(tree, familyID);
		printFamilyTree(tree, familyID);
	}
	
	/**
	 * Print a family description
	 * @param tree - the binary tree
	 * @param familyID - the family ID
	 * @throws IOException 
	 */
	private void printFamilyDescription(BinaryTree tree, String familyID) throws IOException {
		BufferedWriter writer = new BufferedWriter(new FileWriter(this.descriptionFile, true));
		writer.write(familyID);
		for (Node node : tree.getPreorder(tree.getRoot())) {
			writer.write("\t" + node.getFamilySize());
		}
		writer.write("\n");
		writer.close();
	}
	
	/**
	 * Print the tree with ancestral size information for a single family
	 * @param tree - the binary tree
	 * @param familyID - the family ID
	 * @throws IOException 
	 */
	private void printFamilyTree(BinaryTree tree, String familyID) throws IOException {
		BufferedWriter writer = new BufferedWriter(new FileWriter(this.treeFile, true));
		writer.write(familyID + "\n");
		writer.write(tree.toString() + "\n");
		writer.close();
	}
	
	/**
	 * Print the header line for the description file
	 * @param tree - the binary tree
	 * @throws IOException 
	 */
	private void printHeaderLine(BinaryTree tree) throws IOException {
		BufferedWriter writer = new BufferedWriter(new FileWriter(this.descriptionFile));
		writer.write("FAMILY");
		for (Node node : tree.getPreorder(tree.getRoot())) {
			writer.write("\t" + node.getName());
		}
		writer.write("\n");
		writer.close();
	}
	
	/**
	 * Check whether a file exists
	 * @param fileName - the file
	 * @return boolean
	 */
	private boolean fileExists(String fileName) {
		File file = new File(fileName);
		return file.exists();
	}
	
	/**
	 * Delete a file
	 * @param fileName - the file
	 */
	private void deleteFile(String fileName) {
		File file = new File(fileName);
		file.delete();
	}
}
