package hr.fer.zemris.java.hw01;

import java.util.Scanner;

public class UniqueNumbers {
	
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
	
	public static void printTreeAscending(TreeNode root) {
		if (root == null) {
			return;
		}
		
		printTreeAscending(root.left);
		System.out.print(root.value + " ");
		printTreeAscending(root.right);
	}
	
	public static void printTreeDescending(TreeNode root) {
		if (root == null) {
			return;
		}
		
		printTreeDescending(root.right);
		System.out.print(root.value + " ");
		printTreeDescending(root.left);
	}
	
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
	
	public static int treeSize(TreeNode root) {
		if (root == null) {
			return 0;
		}
		
		return 1 + treeSize(root.left) +
				   treeSize(root.right);
	}
	
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
	
	public static class TreeNode {
		public TreeNode left, right;
		public int value;
	}

}
