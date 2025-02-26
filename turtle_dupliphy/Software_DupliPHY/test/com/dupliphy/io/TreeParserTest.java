package com.dupliphy.io;

import java.io.IOException;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.dupliphy.exceptions.NewickException;
import com.dupliphy.units.BinaryTree;

/**
 * Test the tree parser
 * @author ryanames
 *
 */
public class TreeParserTest {

	private String file1 = "test_data/trees/tree.ph";
	private String file2 = "test_data/trees/tree1.ph";
	private String file3 = "test_data/trees/tree2.ph";
	private String file4 = "test_data/trees/tree3.ph";
	private String file5 = "test_data/trees/data.tre";

	@Before
	public void setUp() {
		
	}
	
	/**
	 * Test parsing a specific tree
	 * @throws IOException 
	 * @throws NewickException 
	 */
	@Test
	public void test1() throws IOException, NewickException {
		TreeParser parser = new TreeParser(this.file1);
		parser.parse();
		
		BinaryTree tree = (BinaryTree) parser.getData();
		Assert.assertFalse(tree == null);
	}
	
	/**
	 * Test parsing a specific tree
	 * @throws IOException 
	 * @throws NewickException 
	 */
	@Test
	public void test2() throws IOException, NewickException {
		TreeParser parser = new TreeParser(this.file2);
		parser.parse();
		
		BinaryTree tree = (BinaryTree) parser.getData();
		Assert.assertFalse(tree == null);
	}
	
	/**
	 * Test parsing a specific tree
	 * @throws IOException 
	 * @throws NewickException 
	 */
	@Test
	public void test3() throws IOException, NewickException {
		TreeParser parser = new TreeParser(this.file3);
		parser.parse();
		
		BinaryTree tree = (BinaryTree) parser.getData();
		Assert.assertFalse(tree == null);
	}
	
	/**
	 * Test parsing a specific tree
	 * @throws IOException 
	 * @throws NewickException 
	 */
	@Test
	public void test4() throws IOException, NewickException {
		TreeParser parser = new TreeParser(this.file4);
		parser.parse();
		
		BinaryTree tree = (BinaryTree) parser.getData();
		Assert.assertFalse(tree == null);
	}
	
	/**
	 * Test parsing a specific tree
	 * @throws IOException 
	 * @throws NewickException 
	 */
	@Test
	public void test5() throws IOException, NewickException {
		TreeParser parser = new TreeParser(this.file5);
		parser.parse();
		
		BinaryTree tree = (BinaryTree) parser.getData();
		Assert.assertFalse(tree == null);
	}
}

