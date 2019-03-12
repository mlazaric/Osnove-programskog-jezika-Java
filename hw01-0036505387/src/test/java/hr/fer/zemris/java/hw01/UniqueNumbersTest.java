package hr.fer.zemris.java.hw01;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import hr.fer.zemris.java.hw01.UniqueNumbers.TreeNode;

public class UniqueNumbersTest {
	
	@Test
	public void testCaseFromPDF() {
		TreeNode root = null;
		
		root = UniqueNumbers.addNode(root, 42);
		root = UniqueNumbers.addNode(root, 76);
		root = UniqueNumbers.addNode(root, 21);
		root = UniqueNumbers.addNode(root, 76);
		root = UniqueNumbers.addNode(root, 35);
		
		assertEquals(root.value, 42);
		assertEquals(root.left.value, 21);
		assertEquals(root.left.right.value, 35);
		assertEquals(root.right.value, 76);
		
		assertEquals(UniqueNumbers.treeSize(root), 4);
		
		assertTrue(UniqueNumbers.containsValue(root, 76));
		assertFalse(UniqueNumbers.containsValue(root, 3));
		assertFalse(UniqueNumbers.containsValue(root, -100));
	}
	
	@Test
	public void testOrdering() {
		TreeNode root = null;
		
		root = UniqueNumbers.addNode(root, 5);
		root = UniqueNumbers.addNode(root, -2);
		root = UniqueNumbers.addNode(root, 4);
		root = UniqueNumbers.addNode(root, 8);
		root = UniqueNumbers.addNode(root, 15);
		
		assertEquals(root.value, 5);
		assertEquals(root.left.value, -2);
		assertEquals(root.left.right.value, 4);
		assertEquals(root.right.value, 8);
		assertEquals(root.right.right.value, 15);
	}
	
	@Test
	public void testDuplicates() {
		TreeNode root = null;
		
		root = UniqueNumbers.addNode(root, 5);
		root = UniqueNumbers.addNode(root, -2);
		root = UniqueNumbers.addNode(root, 4);
		root = UniqueNumbers.addNode(root, 8);
		root = UniqueNumbers.addNode(root, 15);
		
		// Duplicates
		root = UniqueNumbers.addNode(root, -2);
		root = UniqueNumbers.addNode(root, 5);
		root = UniqueNumbers.addNode(root, 4);
		root = UniqueNumbers.addNode(root, 8);
		root = UniqueNumbers.addNode(root, 15);
		
		assertEquals(UniqueNumbers.treeSize(root), 5);
	}
	
	@Test
	public void testSizeOfEmptyTree() {
		assertEquals(UniqueNumbers.treeSize(null), 0);
	}
	
	@Test
	public void testSizeOfRoot() {
		TreeNode root = new TreeNode();
		
		assertEquals(UniqueNumbers.treeSize(root), 1);
	}
	
	@Test
	public void testSizeOfChain() {
		TreeNode root = new TreeNode();
		
		for (int i = 0; i < 20; ++i) {
			root = UniqueNumbers.addNode(root, i);
		}
		
		assertEquals(UniqueNumbers.treeSize(root), 20);
	}
	
	@Test
	public void testContainsWithEmptyTree() {
		assertEquals(UniqueNumbers.containsValue(null, 5), false);
	}
	
	@Test
	public void testContainsWithChain() {
		TreeNode root = null;
		
		for (int i = 0; i < 20; ++i) {
			assertEquals(UniqueNumbers.containsValue(root, i), false);
		}
		
		for (int i = 0; i < 20; ++i) {
			root = UniqueNumbers.addNode(root, i);
		}
		
		for (int i = 0; i < 20; ++i) {
			assertEquals(UniqueNumbers.containsValue(root, i), true);
		}
	}
	
}
