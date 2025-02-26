package com.dupliphy.io;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.dupliphy.exceptions.NewickException;
import com.dupliphy.units.BinaryTree;
import com.dupliphy.units.Node;
import com.dupliphy.utils.Sequence;

/**
 * Parse a tree from a file containing a newick string
 * @author ryanames
 *
 */
public class TreeParser extends Parser {

	private BinaryTree tree;
	
	/**
	 * Constructor
	 * @param fileName - the file to parse
	 */
	public TreeParser(String fileName) {
		super(fileName);
	}

	@Override
	public Object getData() {
		return this.tree;
	}

	@Override
	public void parse() throws IOException, NewickException {
		BufferedReader reader = new BufferedReader(new FileReader(this.getFileName()));
		String newick = reader.readLine();
		
		// Make sure all internal nodes are named and add branch lengths where necessary
		newick = newick.replaceAll("\\s", "");
		Sequence counter = new Sequence();
		if (!newick.contains(":")) {
			newick = newick.replaceAll(",", ":1.0,");
			newick = newick.replaceAll("\\)", ":1.0)");
		}
		while (newick.contains("):")) {
			 newick = newick.replaceFirst("\\):", ")" + counter.next() + ":");
		}
		while (newick.contains("),")) {
			 newick = newick.replaceFirst("\\),", ")" + counter.next() + ":1.0,");
		}
		while (newick.contains("))")) {
			 newick = newick.replaceFirst("\\)\\)", ")" + counter.next() + ":1.0)");
		}
		if (newick.contains(");")) {
			 newick = newick.replaceFirst("\\);", ")" + counter.next() + ":1.0;");
		}
		
		// Parse the newick string
		newick = newick.replaceAll(";", "");
		Node root = parseNewick(newick);
		this.tree = new BinaryTree(root);
	}
	
	/**
	 * Parse the newick tree string to extract the nodes
	 * @param newick - the newick tree string
	 * @return Node
	 * @throws NewickException 
	 */
	private Node parseNewick(String newick) throws NewickException {
		List<String> knownNodes = new ArrayList<String>();
		int x = newick.lastIndexOf(':');
		String name = parseName(newick, x);
		Node node = new Node(name);
		node.setBranchLength(Double.parseDouble(newick.substring(x + 1)));
		knownNodes.add(name);
		return build(newick, node, 0, x, knownNodes);
	}
	
	/**
	 * Build a binary tree from the newick tree string using recursion
	 * @param s - the current tree string
	 * @param parent - the most recent parent node
	 * @param from - position in the string to parse from
	 * @param to - position in the string to parse to
	 * @param knownNodes - a lits of already parsed nodes
	 * @return Node
	 * @throws NewickException 
	 */
	public Node build(String s, Node parent, int from, int to, List<String> knownNodes) throws NewickException {
		
		if (s.charAt(from) != '(') {
			return parent;
		}
 
		int b = 0; // bracket counter
		int colon = 0; // colon marker
		int x = from; // position marker
 
		for (int i = from; i < to; i++) {
			char c = s.charAt(i);
 
			if (c == '(') {
				b++;
			} else if (c == ')') {
				b--;
			} else if (c == ':') {
				colon = i;
			}
 
			if (b == 0 || b == 1 && c == ',') {
				if (s.charAt(i - 1) != ')' && (colon + 1) < i) {
					String branchLength = s.substring(colon + 1, i);
					String[] split = branchLength.split("\\)");
					branchLength = split[0];
					String nodeName = parseName(s, colon);
					if (!knownNodes.contains(nodeName)) {
						Node node = new Node(nodeName);
						try {
							node.setBranchLength(Double.parseDouble(branchLength));
						} catch (NumberFormatException e) {
							throw new NewickException("Error: Branch lengths in tree are not numeric.");
						}
						
						parent.addChild(build(s, node, x + 1, colon, knownNodes));
						x = i;
						knownNodes.add(nodeName);
					}
				}
			}
		}
 
		return parent;
	}
	
	/**
	 * Parse the name of a node from the tree string
	 * @param s - the current tree string
	 * @param end - the end position of the name
	 * @return String
	 * @throws NewickException 
	 */
	private String parseName(String s, int end) throws NewickException {
		String name = null;
		for (int i = end; i >= 0; i--) {
			if (s.charAt(i) == ')' || s.charAt(i) == '(' || s.charAt(i) == ',') {
				if ((i + 1) < end) {
					name = s.substring(i + 1, end);
					String[] split = name.split("\\:");
					if (split[0].matches("[;\\(\\),]")) {
						throw new NewickException("Error: Tree names do not appear to be formatted correctly.");
					}
					return split[0];
				}
			}
		}
		return name;
	}

}
