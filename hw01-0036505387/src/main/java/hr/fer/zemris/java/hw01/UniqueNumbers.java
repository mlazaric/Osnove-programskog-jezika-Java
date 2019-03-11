package hr.fer.zemris.java.hw01;

import java.util.Scanner;

/**
 * A program for storing an ordered binary tree.
 * 
 * @author Marko Lazarić
 *
 */
public class UniqueNumbers {
	
	/**
	 *   Continually asks the user for integers and adds them to the ordered
	 * binary tree. If the user enters an invalid integer or a duplicate, a 
	 * relevant error message is printed to the console. If the user enters
	 * "kraj", it prints all the valid integers the user has entered (without
	 * duplicates, in ascending and descending order. 
	 * 
	 * @param args the arguments passed to the program are ignored
	 */
	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);
		TreeNode root = null;
		
		while (true) {
			System.out.print("Unesite broj > ");
			
			if (sc.hasNextInt()) {
				int value = sc.nextInt();
				
				if (containsValue(root, value)) {
					System.out.println("Broj već postoji. Preskačem.");
				}
				else {
					System.out.println("Dodano.");
					root = addNode(root, value);
				}
				
				continue;
			}
			
			String token = sc.next();
			
			if (token.equals("kraj")) {
				System.out.print("Ispis od najmanjeg: ");
				printTreeAscending(root);
				System.out.format("%nIspis od najvećeg: ");
				printTreeDescending(root);
				System.out.println();
				
				break;
			}
			
			System.out.format("'%s' nije cijeli broj.%n", token);
		}
		
		sc.close();
	}
	
	/**
	 * Prints the elements of the ordered binary tree in ascending order.
	 * 
	 * @param root the root of the current subtree
	 */
	public static void printTreeAscending(TreeNode root) {
		if (root == null) {
			return;
		}
		
		printTreeAscending(root.left);
		System.out.print(root.value + " ");
		printTreeAscending(root.right);
	}
	
	/**
	 * Prints the elements of the ordered binary tree in descending order.
	 * 
	 * @param root the root of the current subtree
	 */
	public static void printTreeDescending(TreeNode root) {
		if (root == null) {
			return;
		}
		
		printTreeDescending(root.right);
		System.out.print(root.value + " ");
		printTreeDescending(root.left);
	}
	
	/**
	 * Adds the value to the ordered binary tree if it does not already exist.
	 * 
	 * @param root the root of the ordered binary tree
	 * @param value the value to be added
	 * @return the new root of the ordered binary tree
	 */
	public static TreeNode addNode(TreeNode root, int value) {
		if (root == null) {
			TreeNode node = new TreeNode();
			
			node.value = value;
			
			return node;
		}
		
		TreeNode current, previous;
		
		current = root;
		previous = null;
		
		while (current != null) {
			previous = current;
			
			if (current.value == value) {
				return root;
			}
			else if (current.value < value) {
				current = current.right;
			}
			else {
				current = current.left;
			}
		}
		
		if (previous.value < value) {
			previous.right = new TreeNode();
			previous.right.value = value;
		}
		else {
			previous.left = new TreeNode();
			previous.left.value = value;
		}
		
		return root;
	}
	
	/**
	 * Recursively calculates the size of the ordered binary tree.
	 * 
	 * @param root the root of the current subtree it is calculating the size of 
	 * @return the number of elements in the tree
	 */
	public static int treeSize(TreeNode root) {
		if (root == null) {
			return 0;
		}
		
		return 1 + treeSize(root.left) +
				   treeSize(root.right);
	}
	
	/**
	 * Recursively searches for the value in the ordered binary tree.
	 * 
	 * @param root the current node it is recursively checking
	 * @param value the value it is checking for
	 * @return true if the value was found, false otherwise
	 */
	public static boolean containsValue(TreeNode root, int value) {
		if (root == null) {
			return false;
		}
		
		if (root.value == value) {
			return true;
		}
		else if (root.value < value) {
			return containsValue(root.right, value);
		}
		else {
			return containsValue(root.left, value);
		}
	}
	
	/**
	 * Represents a node in the ordered binary tree.
	 * 
	 * @author Marko Lazarić
	 *
	 */
	public static class TreeNode {
		/**
		 *   The left and right children of this node. The value of the right child
		 * (if it exists) will be greater than the value of its parent, while the
		 * value of the left child (if it exists) will be lesser than the value of
		 * its parent.
		 */
		public TreeNode left, right;
		
		
		/**
		 * The value stored in this node. 
		 */
		public int value;
	}

}
