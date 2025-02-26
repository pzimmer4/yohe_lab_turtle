package com.dupliphy.units;

import java.util.ArrayList;


/**
 * A node
 * @author ryanames
 *
 */
public  class Node {
	 
	  private ArrayList<Node> descendents;
	  private Node ancestor;
	  private String newick;
	  private String name;
	  private R rValues;
	  private Double distFromParent = null;
	  private boolean root;
	  private int bootval;
	  private int familySize;
	  
	  private ArrayList<Integer> potentialFamilySizes;

	  /**
	   * Constructor
	   * @param name - the name of the node
	   */
	  public Node(String name) {
		  this.setName(name);
		  this.rValues = new R();
		  this.descendents = new ArrayList<Node>();
		  this.potentialFamilySizes = new ArrayList<Integer>();
	  }
	  
	  /**
	   * Constructor
	   * @param branchLength - the branch length associated with the node
	   */
	  public Node(double branchLength) {
		  this.setBranchLength(branchLength);
		  this.rValues = new R();
		  this.descendents = new ArrayList<Node>();
		  this.potentialFamilySizes = new ArrayList<Integer>();
	  }
	  
	  /**
	   * Get the R values associated with a node
	   * @return R
	   */
	  public R getRValues() {
		  return this.rValues;
	  }
	  
	  /**
	   * Clear the R values from the node
	   */
	  public void clearRValues() {
		  this.rValues.clear();
	  }
	  
	  /**
	   * Set the family size at the node
	   * @param familySize - the family size
	   */
	  public void setFamilySize(int familySize) {
		  this.familySize = familySize;
	  }
	  
	  /**
	   * Get the family size at the node
	   * @return int
	   */
	  public int getFamilySize() {
		  return this.familySize;
	  }
	  
	  /**
	   * Set the boostrap value for the node
	   * @param bootval - the bootstrap value
	   */
	  public void setBoot(int bootval) {
		  this.bootval = bootval;
	  }
	  
	  /**
	   * Get the boostrap value for the node
	   * @return int
	   */
	  public int getBoot() {
		  return this.bootval;
	  }
	  
	  /**
	   * Get the potential family sizes for the node
	   * @return ArrayList<Integer>
	   */
	  public ArrayList<Integer> getPotentialFamilySizes() {
		  return this.potentialFamilySizes;
	  }
	  
	  /**
	   * Get a potential family size
	   * @param i - the index of the family size
	   * @return int
	   */
	  public int getPotentialFamilySize(int i) {
		  return this.potentialFamilySizes.get(i);
	  }
	  
	  /**
	   * Set the potential family sizes
	   * @param potentialFamilySizes - a list of family sizes
	   */
	  public void setPotentialFamilySizes(ArrayList<Integer> potentialFamilySizes) {
		  this.potentialFamilySizes = potentialFamilySizes;
	  }
	  
	  /**
	   * Set the copy number at the node
	   * @param size - the size
	   */
	  public void setCopyNumber(int size) {
		  this.potentialFamilySizes.add(size);
	  }
	  
	  /**
	   * Set a specific family size at the node
	   * @param i - the index to add the family size
	   * @param size - the size of the family
	   */
	  public void setPotentialFamilySize(int i, int size) {
		  this.potentialFamilySizes.add(i, size);
	  }
	  
	  /**
	   * Clear the family sizes from the node
	   */
	  public void clearPotentialFamilySizes() {
		  this.potentialFamilySizes.clear();
	  }
	  
	  /**
	   * Get the descendants of the node
	   * @return ArrayList<Node>
	   */
	  public ArrayList<Node> getDescendants() {
		  return descendents;
	  }
	  
	  /**
	   * Set the newick string from this node
	   * @param newick - the newick string
	   */
	  public void setNewick(String newick) {
		  this.newick = newick;
	  }
	  
	  /**
	   * Get the newick string from the node
	   * @return String
	   */
	  public String getNewick() {
		  return newick;
	  }
	  
	  /**
	   * Get the last descendant 
	   * @return Node
	   */
	  public Node getLastDescendant() {
		  return descendents.get(descendents.size() - 1);
	  }
	  
	  /**
	   * Get the ancestor of the node
	   * @return Node
	   */
	  public Node getAncestor() {
		  return ancestor;
	  }
	  
	  /**
	   * Add a descendant to the node
	   * @param node - the descendant
	   */
	  public void addChild(Node node) {
		  descendents.add(node);
	  }
	  
	  /**
	   * Get the name of the node
	   * @return String
	   */
	  public String getName() {
		  return name;
	  }
	  
	  /**
	   * Set the name of the node
	   * @param name - the name of the node
	   */
	  public void setName(String name) {
		  this.name = name;
	  }
	  
	  /**
	   * Set whether the node is the root
	   * @param root - true or false
	   */
	  public void setRoot(boolean root) {
		  this.root = root;
	  }
	  
	  /**
	   * Check whether the node is the root
	   * @return boolean
	   */
	  public boolean isRoot() {
		  return root;
	  }
	  
	  /**
	   * Check whether a node is a leaf
	   * @return boolean
	   */
	  public boolean isLeaf() {
		 if (descendents.isEmpty()) {
			 return true;
		 }
		 return false;
	  }

	  /**
	   * Get the distance from the parent i.e. branch length
	   * @return double
	   */
	  public double getBranchLength() {
		  return distFromParent;
	  }
	  
	  /**
	   * Set the distance from the parent i.e. branch length
	   * @param distFromParent - the branch length
	   */
	  public void setBranchLength(double distFromParent) {
			this.distFromParent = distFromParent;
	  }
	  
	 @Override
	public String toString() {
		StringBuffer sb = new StringBuffer(16 + 16 * this.getDescendants().size());
		if (this.descendents.size() > 0) {
			sb.append("(");
			for (Node child : this.descendents) {
				sb.append(child.toString());
				sb.append(",");
			}
			sb.deleteCharAt(sb.length() - 1);
			sb.append(')');
		}
		if (this.isLeaf()) {
			sb.append(this.name);
		} else {
			sb.append(this.familySize);
		}
		if (this.distFromParent != null) {
			sb.append(":").append(this.distFromParent);
		}
		return sb.toString();
	}
}
