package com.dupliphy.units;

import java.util.ArrayList;

import junit.framework.Assert;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * Testing the binary tree class
 * @author ryanames
 *
 */
public class BinaryTreeTest {

	private BinaryTree tree;
	private Node root;
	private Node a;
	
	/**
	 * Set up the tests
	 * Create a small binary tree
	 * 			Root
	 * 			 |
	 * 		____________
	 * 		A			B
	 * 		|			|
	 * 	_________	_____
	 * 	C		D	E
	 */
	@Before
	public void setUp() {
		tree = new BinaryTree();
		root = new Node(0.1);
		root.setName("Root");
		
		a = new Node(0.1);
		a.setName("A");
		root.addChild(a);
		
		Node b = new Node(0.1);
		b.setName("B");
		root.addChild(b);
		
		Node c = new Node(0.1);
		c.setName("C");
		a.addChild(c);
		
		Node d = new Node(0.1);
		d.setName("D");
		a.addChild(d);
		
		Node e = new Node(0.1);
		e.setName("E");
		b.addChild(e);
		
		tree.setRoot(root);
	}
	
	/**
	 * Close the tests
	 */
	@After
	public void closeDown() {
		
	}
	
	/**
	 * Test the post order tree traversal
	 * Post order - C, D, A, E, B, Root
	 */
	@Test
	public void testGetPostOrder() {
		ArrayList<Node> postOrder = tree.getPostorder(root);
		Assert.assertEquals("C", postOrder.get(0).getName());
		Assert.assertEquals("D", postOrder.get(1).getName());
		Assert.assertEquals("A", postOrder.get(2).getName());
		Assert.assertEquals("E", postOrder.get(3).getName());
		Assert.assertEquals("B", postOrder.get(4).getName());
		Assert.assertEquals("Root", postOrder.get(5).getName());
	}
	
	/**
	 * Test the pre order tree traversal
	 * Pre order - Root, A, C, D, B, E
	 */
	@Test
	public void testGetPreOrder() {
		ArrayList<Node> preOrder = tree.getPreorder(root);
		Assert.assertEquals("Root", preOrder.get(0).getName());
		Assert.assertEquals("A", preOrder.get(1).getName());
		Assert.assertEquals("C", preOrder.get(2).getName());
		Assert.assertEquals("D", preOrder.get(3).getName());
		Assert.assertEquals("B", preOrder.get(4).getName());
		Assert.assertEquals("E", preOrder.get(5).getName());
	}
	
	/**
	 * Test getting the descendants
	 * Descendants of A are A, C and D
	 */
	@Test
	public void testGetDescendants() {
		ArrayList<Node> descendants = tree.getDescendants(a);
		Assert.assertEquals("A", descendants.get(0).getName());
		Assert.assertEquals("C", descendants.get(1).getName());
		Assert.assertEquals("D", descendants.get(2).getName());
	}
	
	/**
	 * Test getting the leaf nodes
	 * Leaf nodes are C, D and E
	 */
	@Test
	public void testGetLeafNodes() {
		ArrayList<Node> descendants = tree.getLeaves(root);
		Assert.assertEquals("C", descendants.get(0).getName());
		Assert.assertEquals("D", descendants.get(1).getName());
		Assert.assertEquals("E", descendants.get(2).getName());
	}
}
