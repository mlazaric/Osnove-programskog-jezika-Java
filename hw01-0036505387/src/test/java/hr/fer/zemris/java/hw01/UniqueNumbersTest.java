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

		assertEquals(42, root.value);
		assertEquals(21, root.left.value);
		assertEquals(35, root.left.right.value);
		assertEquals(76, root.right.value);

		assertEquals(4, UniqueNumbers.treeSize(root));

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

		assertEquals(5, root.value);
		assertEquals(-2, root.left.value);
		assertEquals(4, root.left.right.value);
		assertEquals(8, root.right.value);
		assertEquals(15, root.right.right.value);
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

		assertEquals(5, UniqueNumbers.treeSize(root));
	}

	@Test
	public void testSizeOfEmptyTree() {
		assertEquals(0, UniqueNumbers.treeSize(null));
	}

	@Test
	public void testSizeOfRoot() {
		TreeNode root = new TreeNode();

		assertEquals(1, UniqueNumbers.treeSize(root));
	}

	@Test
	public void testSizeOfChain() {
		TreeNode root = new TreeNode();

		for (int i = 0; i < 20; ++i) {
			root = UniqueNumbers.addNode(root, i);
		}

		assertEquals(20, UniqueNumbers.treeSize(root));
	}

	@Test
	public void testContainsWithEmptyTree() {
		assertFalse(UniqueNumbers.containsValue(null, 5));
	}

	@Test
	public void testContainsWithChain() {
		TreeNode root = null;

		for (int i = 0; i < 20; ++i) {
			assertFalse(UniqueNumbers.containsValue(root, i));
		}

		for (int i = 0; i < 20; ++i) {
			root = UniqueNumbers.addNode(root, i);
		}

		for (int i = 0; i < 20; ++i) {
			assertTrue(UniqueNumbers.containsValue(root, i));
		}
	}

}
