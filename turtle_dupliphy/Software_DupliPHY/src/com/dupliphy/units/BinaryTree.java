package com.dupliphy.units;

import java.util.ArrayList;

import com.dupliphy.exceptions.MatrixException;

/**
 * A binary tree
 * @author ryanames
 *
 */
public class BinaryTree {
	  
	  /**
	   * Binary Tree class - includes methods for tree traversal and processing nodes
	   */
	  private Node root;
	  private String newick;
	  private ArrayList<Node> postNodes;
	  private ArrayList<Node> leaves;
	  private ArrayList<String> leafNames;
	  private ArrayList<Node> preNodes;
	  private ArrayList<Node> nodeDescendents;
	  
	  /**
	   * Constructor
	   * @param root - the root of the tree
	   */
	  public BinaryTree(Node root) {
	    setRoot(root);
	    this.postNodes = new ArrayList<Node>();
		this.leaves = new ArrayList<Node>();
		this.leafNames = new ArrayList<String>();
		this.preNodes = new ArrayList<Node>();
		this.nodeDescendents = new ArrayList<Node>();
	  }
	  
	  /**
	   * Constructor
	   */
	  public BinaryTree() {
	    setRoot(null);
	    this.postNodes = new ArrayList<Node>();
		this.leaves = new ArrayList<Node>();
		this.preNodes = new ArrayList<Node>();
		this.nodeDescendents = new ArrayList<Node>();
	  }

	  /**
	   * Set the root
	   * @param root - the root of the tree
	   */
	  public void setRoot(Node root) {
		  this.root = root;
	  }
	  
	  /**
	   * Get the root node of the tree
	   * @return Node
	   */
	  public Node getRoot() {
		  return this.root;
	  }
	  
	  /**
	   * Set the newick tree string
	   * @param newick - the newick tree sting
	   */
	  public void setNewick(String newick) {
		  this.newick = newick;
	  }
	  
	  /**
	   * Get the newick tree string
	   * @return String
	   */
	  public String getNewick() {
		  return this.newick;
	  }
	  
	 /**
	  * Gets the nodes of the tree in postorder
	  * i.e. leaves to root
	  * @param node a node object
	  * @return an ArrayList of node objects in postorder
	  */
	  public ArrayList<Node> getPostorder(Node node) {
		 postNodes.clear();
		 processPostorder(node);
		 return postNodes;
	  }

	  /**
	   * Process the nodes in post order
	   * @param node - the node to process
	   */
	  private void processPostorder(Node node) {
		ArrayList<Node> descendents = node.getDescendants();		  
		for (Node n : descendents) {
			processPostorder(n);
		}
		postNodes.add(node);
		 
	  }

	  /**
	   * Get the nodes of the tree in preorder 
	   * i.e. root to leaves
	   * @param node a node object
	   * @return an ArrayList of node objects in preorder
	   */
	  public ArrayList<Node> getPreorder(Node node) {
			 preNodes.clear();
			 processPreorder(node);
			 return preNodes;
	  }

	  /**
	   * Process the nodes of the tree in pre order
	   * @param node - the node to process
	   */
	  private void processPreorder(Node node) {
		  preNodes.add(node);
		  
		  ArrayList<Node> descendents = node.getDescendants();
		  for (Node n : descendents) {
			  processPreorder(n);
		  }
	  }
	  
	  /**
	   * Gets all the descendants of a node
	   * @param node the node to retrieve the descendants
	   * @return an ArrayList of node objects
	   */
	  public ArrayList<Node> getDescendants(Node node) {
		  nodeDescendents.clear();
		  processDescendents(node);
		  return nodeDescendents;
	  }
	  /**
	   * process the tree by recursion to get the descendants
	   * @param node a node object
	   */
	  private void processDescendents(Node node) {
		  nodeDescendents.add(node);
		  
		  ArrayList<Node> descendents = node.getDescendants();
		  for (Node n : descendents) {
			  processDescendents(n);
		  }
	  }
	  
	  /**
	   * Gets the leaves of the tree
	   * @param node - a node of the tree
	   * @return ArrayList<Node>
	   */
	  public ArrayList<Node> getLeaves(Node node) {
		  leaves.clear();
		  processLeaves(node);
		  return leaves;
	  }
	  
	  /**
	   * processes the leaves of the tree by recursion
	   * add those nodes with no descenedants (leaves) to the leaves ArrayList
	   * the leaves ArrayList is returned by getLeaves()
	   * 
	   * @param node the node of the tree
	   */
	 private void processLeaves(Node node) {
		ArrayList<Node> descendents = node.getDescendants();
		for (Node n : descendents) {
			processLeaves(n);
		}
		if (node.isLeaf()) {
		  leaves.add(node);
		}
	  }
	 
	 /**
	   * Gets the leaves of the tree
	   * @return ArrayList<String>
	   */
	  public ArrayList<String> getLeafNames() {
		  leafNames.clear();
		  processLeafNames(this.getRoot());
		  return leafNames;
	  }
	  
	  /**
	   * processes the leaves of the tree by recursion
	   * add those nodes with no descendants (leaves) to the leaves ArrayList
	   * the leaves ArrayList is returned by getLeaves()
	   * 
	   * @param node the node of the tree
	   */
	 private void processLeafNames(Node node) {
		ArrayList<Node> descendents = node.getDescendants();
		for (Node n : descendents) {
			processLeafNames(n);
		}
		if (node.isLeaf()) {
		  leafNames.add(node.getName());
		}
	  }
	 
	 /**
	  * Sets the gene family sizes at the leaves of the tree
	  * The R value for the actually gene family size is 0
	  * and infinity (Double.MAX_VALUE) for all other gene
	  * family sizes
	  * 
	  * @param family the GeneFamily object of the gene faily we are analysing
	  * @param length the length of the matrix of weights
	  */
	 public void setLeafDuplicates(GeneFamily family, int length) {
		  for (Node leaf : this.getLeaves(this.getRoot())) {
			  int size = family.getSpeciesSize(leaf.getName());
			  for (int i = 0; i < length; i++) {
				  if (i == size) {
					  leaf.getRValues().setSingleR(size, 0);
					  leaf.setCopyNumber(size);
				  } else {
					  leaf.getRValues().setSingleR(i, Double.MAX_VALUE); //MAX_VALUE used for infinity
				  }
			  }
		  }
	  }
	  
	  /**
	   * Set the R values for the tree by processing the tree in 
	   * postorder
	   * 
	   * @param matrix the matrix of weights for gene gain and loss
	 * @throws MatrixException 
	   */
	  public void setRValues(Matrix matrix) throws MatrixException {
		  for (Node node : this.getPostorder(this.getRoot())) {
			  if (!node.isLeaf()) {
				  calcRNonBinary(node, matrix);
			  }
		  }
	  }
	  
	  /**
	   * Calculates R values according to weighted parsimony
	   * for every possible gene family size based on the length of the
	   * imported matrix of weights
	   * 
	   * @param node a node of the tree
	   * @param matrix the matrix of weights for gene gain and loss
	 * @throws MatrixException 
	   */
	  private void calcRNonBinary(Node node, Matrix matrix) throws MatrixException {
		  ArrayList<Node> descendants = node.getDescendants();
		  for (int i = 0; i < matrix.getLength(); i++) {
			double rati = 0;
			for (Node n : descendants) {
				 double[] values = new double[matrix.getLength()]; 
				 ArrayList<Double> rValues = n.getRValues().getR();
				 for (int j = 0; j < rValues.size(); j++) {
					 double r = rValues.get(j) + matrix.get(i, j); 
					 values[j] = r;
				 }
				 double min = getMin(values);
				 rati += min; 
			 }
			node.getRValues().setSingleR(i, rati);
		  }
	  }
	  
	  /**
	   * Get the min value in an array
	   * @param array - the array of doubles
	   * @return double
	   */
	  private double getMin(double[] array) {
		  double value = 10000;
		  for (int i = 0; i < array.length; i++) {
			  if (array[i] < value) {
				  value = array[i];
			  }
		  }
		  return value;
	  }
	 
	 /**
	  * Processes the tree in preorder and finds nodes where multiple 
	  * family sizes have the same R score
	  */
	 public void setFamilySizeTies() {
		 for (Node node : this.getPreorder(this.getRoot())) {
			if (!node.isLeaf()) {
				ArrayList<Integer> dupNums = this.getTies(node.getRValues().getR());
				for (Integer dupNum : dupNums) {
					if (dupNum.intValue() != Double.MAX_VALUE) {
						node.setCopyNumber(dupNum); 
					}
				}
			}
		}
	 }

	 /**
	  * Get an tied scores
	  * @param array - the scores
	  * @return ArrayList<Integer>
	  */
	 private ArrayList<Integer> getTies(ArrayList<Double> array) {
		 ArrayList<Integer> ties = new ArrayList<Integer>();
		 double value = Double.MAX_VALUE;
		 for (int i = 0; i < array.size(); i++) {
			 if (array.get(i).doubleValue() == value) {
				 ties.add(i);
				 value = array.get(i);
			 }
			 if (array.get(i).doubleValue() < value) {
				 ties.clear();
				 ties.add(i);
				 value = array.get(i);
			 }
		 }
		 return ties;
	 }
	 
	 /**
	  * Processes the tree in preorder and sets the ancestral
	  * gene family size based on the minimum cost change
	  * between a node and its descendant
	  * 
	  * @param matrix the matrix of weights from gene gain and loss
	 * @throws MatrixException 
	  */
	 public void setFamilySize(Matrix matrix) throws MatrixException {
		 for (Node node : this.getPreorder(this.getRoot())) {
			 ArrayList<Integer> currentSizes = node.getPotentialFamilySizes();
			 if (node.isRoot()) {
				 currentSizes = getMinCopyNumber(currentSizes);
				 node.setFamilySize(currentSizes.get(0));
			 } else {
				 node.setFamilySize(currentSizes.get(0));
			 }
			 node.setPotentialFamilySizes(currentSizes);
			 ArrayList<Node> descendants = node.getDescendants();
			 for (Node desc : descendants) {
				 if (!node.isLeaf()) {
					 ArrayList<Integer> descSizes = desc.getPotentialFamilySizes();
					 ArrayList<Integer> shared = this.getIntersection(currentSizes, descSizes);
					 if (!shared.isEmpty()) {
						 desc.setPotentialFamilySizes(shared);
					 } else {
						 ArrayList<Integer> minCostChanges = this.getMinCostChange(currentSizes, descSizes, matrix);
						 desc.setPotentialFamilySizes(minCostChanges);
					 }
				 }
			 }
		 }
	}
	 
	 /**
	  * Get the minimum gene family size in the case of ties
	  * @param copyNumbers - a list of gene family sizes
	  * @return ArrayList<Integer>
	  */
	 private ArrayList<Integer> getMinCopyNumber(ArrayList<Integer> copyNumbers) {
		 ArrayList<Integer> min = new ArrayList<Integer>(1);
		 Integer minimum = copyNumbers.get(0);
		 for (int i = 1; i < copyNumbers.size(); i++) {
			 if (copyNumbers.get(i) < minimum) {
				 minimum = copyNumbers.get(i);
			 }
		 }
		 min.add(minimum);
		 return min;
	 }
	 
	 /**
	  * Get the intersection of two lists
	  * @param currentDups - The list of possible gene family sizes at the current node
	  * @param descDups - The list of possible gene family sizes at the ancestor node
	  * @return ArrayList<Integer>
	  */
	 private ArrayList<Integer> getIntersection(ArrayList<Integer> currentDups, ArrayList<Integer> descDups) {
		 ArrayList<Integer> intersection = new ArrayList<Integer>();
		 for (Integer i : currentDups) {
			 if (descDups.contains(i)) {
				 intersection.add(i);
			 }
		 }
		 return intersection;
	 }
	 
	 /**
	  * Get the minimum cost change between a parent and child node
	  * @param currentDups - the possible gene family sizes at the current node
	  * @param descDups - the possible gene family sizes at the parent node
	  * @param matrix - the matrix of weights for the changes
	  * @return ArrayList<Integer>
	 * @throws MatrixException 
	  */
	 private ArrayList<Integer> getMinCostChange(ArrayList<Integer> currentDups, ArrayList<Integer> descDups, Matrix matrix) throws MatrixException {
		ArrayList<Integer> minCosts = new ArrayList<Integer>();
		for (Integer i : currentDups) {
			Double matrixScore = null;
			Double minCost = null;
			for (Integer d : descDups) {
				double score = matrix.get(i, d);
				if (matrixScore == null || score < matrixScore.doubleValue()) {
					minCost = new Double(d.doubleValue());
					matrixScore = new Double(score);
				}
			}
			if (minCost != null) {
				minCosts.add(minCost.intValue());
			}
		}
		return minCosts;
	 }
	 
	 /**
	  * Clears the stored family sizes and R values
	  * between processing each geneFamily
	  */
	 public void clear() {
		 for (Node node : this.getPreorder(this.getRoot())) {
			 node.clearPotentialFamilySizes();
			 node.clearRValues();
		 }
	 }
	 
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder(this.root.toString());
		sb.append(";");
		return sb.toString();
	}
}
