import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import junit.framework.TestCase;

public class SplayTreeTests extends TestCase {
	
	private ThreadedSplayTree<Integer,Integer> intTree;
	private ThreadedSplayTree<String,Integer> stringTree;
	private static final Integer ZERO = new Integer(0);
	private static final int NUMS = 150;

	/**
	 * Set-up the trees that we will use for our tests.
	 */
	@Before
	public void setUp() {
		intTree = new ThreadedSplayTree<Integer,Integer>();
	}

	@Test
	public void easyTest(){
		intTree.insert(new SplayElement<Integer,Integer>(8,8));
		assertFalse("The tree should be empty!", intTree.isEmpty());
		
		intTree.delete(8);
		assertTrue("The tree should be empty!", intTree.isEmpty());
		
		try {
			intTree.delete(-1); // This shouldn't do anything, but it's interesting to investigate if it throws an exception
		} catch(Throwable t) {
			fail("Caught a " + t.getClass().getSimpleName() + " in easyTest(). The message was: " + t.getMessage() + ".");
		}
	}

	/**
	 * Preliminary tests for empty and stub trees. Essentially fiddle with at most
	 * one node.
	 */
	@Test
	public void testEmptyAndStubTree(){
		// Make sure that an empty tree is indeed empty:
		assertTrue("Tree should be empty.", intTree.isEmpty() && stringTree.isEmpty());
		

		// Make a single insertion and check the tree's status.
		intTree.insert(new SplayElement<Integer,Integer>(ZERO,ZERO));
		assertFalse("After a single insertion, the tree should no longer be empty.",
				intTree.isEmpty());
		assertEquals("Incorrect root key.", ZERO, intTree.getRoot());

		// Look up the key through the search() method.
		assertEquals("Looking up the single inserted key in the tree failed.",
				true, intTree.search(ZERO));
		intTree.delete(ZERO);
		// Delete the tree and make it back into a stub.
		assertEquals("Deleting the single key should return the key itself.",
				false, intTree.search(ZERO));
		assertTrue("Tree should now be empty again.", intTree.isEmpty());
	}

	/** Test the "AVL" part of the structure: Does the tree re-balance itself
	 * after insertions (we can figure this out by querying about the height)?
	 *  Is the root the expected root?
	 */
	@Test
	public void testBalancedInsertions(){

		/* First tree will be very simple, yet effective at uncovering
		 * basic errors.
		 */

		intTree.insert(new SplayElement<Integer,Integer>(-1,-1));
		intTree.insert(new SplayElement<Integer,Integer>(-2,-2));

		/* Tree is now like this (we're not drawing the threads):
		 *
		 * 				-1
		 * 				/
		 * 			   /
		 * 			 -2
		 *
		 * Let us first make sure that it is this way, and then insert a new element.
		 */

		
		assertEquals("Incorrect key detected at root: Are you performing " +
				"unnecessary rotations?", new Integer(-1), intTree.getRoot());
		assertEquals("Looking up the key we just inserted should return a reference " +
				"to the key itself.", new Integer(-2), intTree.search(-2));
		assertFalse("The code tells us that a tree where we inserted two keys is empty.",
				intTree.isEmpty());

		/* TODO: Should you expand those tests by making sure the inorder traversal
		 * works as it should?... */

		intTree.insert(new SplayElement<Integer,Integer>(-5,-5));

		/* The tree should now be like this after a right rotation about the root:
		 *
		 *  			-2
		 *  			/ \
		 *  		   /   \
		 *  		 -5	   -1
		 *
		 *  So it should have a height of 1 and the root should be -2!
		 */
		assertEquals("Incorrect key detected at root: are you rotating *right* properly?", new Integer(-2), intTree.getRoot());
		assertEquals("Looking up the key we just inserted should return a reference " +
				"to the key itself.",	true, intTree.search(-5));
		assertFalse("The code tells us that a tree where we inserted three keys is empty.",
				intTree.isEmpty());

		/* We will also make similar tests, for a right-heavy tree this time. */

		
		/* Now we will sequentially insert couple nodes which should not cause
		 * rotations. That is, after any insertion, the tree should be of the same height.
		 *
		 * Recall that the integer tree should look like this now:
		 *
		 * 				-2
		 * 			   /  \
		 * 			  -5  -1
		 *
		 * Inserting another subtree on the left should not cause any rotations.
		 */

		intTree.insert(-10);
		assertEquals("Key at root changed unnecessarily", new Integer(-2), intTree.getRoot());
		// Will no longer test search and empties, that's
		// just unnecessary at this point.

		intTree.insert(-4);
		
		assertEquals("Key at root changed unnecessarily.", new Integer(-2), intTree.getRoot());

		/* If every test passed, the tree should now look like this:
		 *
		 * 				-2
		 * 			   /  \
		 * 			  /    \
		 * 			-5		-1
		 * 		   /  \
		 * 		  /	   \
		 * 		-10     -4
		 *
		 * Inserting -20 should cause an imbalance detected at the root, and corrective action via
		 * a right rotation about the root will need to be taken. Specifically, the tree
		 * should look like this:
		 *
		 * 					-5
		 * 				   /  \
		 * 				  /    \
		 * 				-10    -2
		 * 				/  \   / \
		 * 			  -20	x -4  -1
		 */

		intTree.insert(-20);
		assertEquals("Incorrect root detected: Are you rotating *right* properly?",
				new Integer(-5), intTree.getRoot());


		
		

		/* Finally, we need to check for some cases of LR and RL rotations.
		 * We will check LR on the integer tree and RL on the string tree.
		 *
		 * Recall the current form of the integer tree:
		 *
		 *
		 *  				-5
		 * 				   /  \
		 * 				  /    \
		 * 				-10    -2
		 * 				/  \   / \
		 * 			  -20	x -4  -1
		 *
		 *
		 *	Inserting -15 should cause an imbalance detected at node -10, addressible via a
		 *  LR rotation about it which should modify the tree as follows:
		 *
		 *					-5
		 *				   /   \
		 *				  /     \
		 *				-15		 -2
		 *				/  \	 / \
		 * 			  -20  -10  -4  -1
		 */

		intTree.insert(-15);
		assertEquals("Incorrect root detected after another insertion: are you performing unnecessary rotations?",
				new Integer(-5), intTree.getRoot());
	


		/* For our final tests on insertion, we will see whether the students handle RL and LR rotations
		 * *about the root* properly. We will first begin with a RL rotation:
		 */

		intTree = new ThreadedSplayTree<Integer,Integer>();
		Integer[] keys1 = {ZERO, 5, 3};
		for(Integer k: keys1)
			intTree.insert(k);

		/* Tree should be as follows after an RL rotation about the root triggered
		 * at the insertion of '3':
		 *
		 * 				3
		 * 			   / \
		 * 			  /   \
		 * 			 0	   5
		 */

		assertEquals("Incorrect root detected: check your RL rotations!",
				new Integer(3), intTree.getRoot());

		
	}

	/** Deletion tests. */
	@Test
	public void testBalancedDeletions(){

		/* Our goal will be to check whether students deal correctly with deletions
		 * that should cause rotations as well as deletions that should not cause rotations.
		 * After every deletion, we will also perform a search() operation to make sure the
		 * element cannot be found in the data structure. We are also interested in deleting
		 * both inner nodes as well as leaf nodes (directly).
		 *
		 *  For our first test, let's try a deletion at the leaf level which shouldn't cause any
		 *  rotations to occur.
		 */

		Integer[] keys = {ZERO, 5, 3};
		for(Integer k : keys)
			intTree.insert(k);

		/* Reminder: After all those insertions, the tree should look
		 * like this:
		 *
		 * 				3
		 * 			   / \
		 * 			  /   \
		 * 			0	   5
		 *
		 * Removing 0 should not affect the tree's height:
		 */

		assertEquals("Removing a leaf node should return itself to the caller.", ZERO, intTree.delete(ZERO));
		assertTrue("Once we remove a key, we should no longer be able to find it " + "in the tree.", intTree.search(ZERO) == null);

		/* However, deleting 5 should make it into a stub tree. */

		assertEquals("Removing a leaf node should return itself to the caller.",
				new Integer(5), intTree.delete(5));
		assertTrue("Once we remove a key, we should no longer be able to find it " +
				"in the tree.", intTree.search(5) == null);
		assertEquals("Incorrect tree height detected after deletions.", 0, intTree.height());

		/* After all these deletions, the root should be 3. We will make sure of this. */

		assertEquals("Incorrect root detected after deletions.",
				new Integer(3), intTree.getRoot());

		/* Now that we have a stub tree consisting of just the number 3,
		 * we will need to expand the tree to perform a deletion that will
		 * leave us with an unbalanced tree. We will create the following tree:
		 *
		 *  			3
		 *  		  /   \
		 *  		 /	   \
		 *  		1       5
		 * 					 \
		 *  			  	  6
		 *
		 */

		intTree.insert(1);
		intTree.insert(5);
		intTree.insert(6);

		/* Now, deleting 1 should cause an imbalance detected at the root.
		 * This should be solved via a left rotation of the root, resulting in
		 * the following tree:
		 *
		 * 					5
		 * 				  /  \
		 *			     /	  \
		 *				3	   6
		 */

		intTree.delete(1); // No more need to check return values here.
		assertTrue("Once we remove a key, we should no longer be able to find it " +
				"in the tree.", intTree.search(1) == null);
		assertEquals("Incorrect tree root. Are you rotating *left* correctly " +
				"after deletions?", new Integer(5), intTree.getRoot());
		assertEquals("Incorrect tree height. Are you " +
				"re-balancing correctly after deletions?", 1, intTree.height());

		/* Let's try the same scenario, only this time with a deletion that should trigger
		 * a right root rotation. We will use the string tree to do this.
		 */

		stringTree.insert("Jason");
		stringTree.insert("Matt");
		stringTree.insert("George");
		stringTree.insert("Gene");

		/* This is what the tree should look like right now:
		 *
		 * 					Jason
		 * 					/	\
		 * 				   /	 \
		 * 				George	 Matt
		 * 				 /
		 * 			   Gene
		 *
		 * Removing Matt should trigger a right rotation about Jason, leaving us with the tree:
		 *
		 * 					George
		 * 					/  \
		 * 				   /    \
		 * 				 Gene  Jason
		 */

		stringTree.delete("Matt");
		assertTrue("Once we remove a key, we should no longer be able to find it " +
				"in the tree.", stringTree.search("Matt") == null);
		assertEquals("Incorrect tree root. Are you rotating *right* correctly after deletions?",
				"George", stringTree.getRoot());
		assertEquals("Incorrect tree height. Are you re-balancing correctly after deletions?",
				1, stringTree.height());

		/* The previous cases we examined had one thing in common: After deletions, a simple
		 * left or right rotation of the root took care of the imbalance. We also need to check
		 * whether students correctly take care of LR and RL rotations.
		 *
		 *  Let's check LR first, with the integer tree. We will create the following tree:
		 *
		 *  			10
		 * 			   /  \
		 * 			  /	   \
		 * 			 5	    15
		 * 			  \
		 * 			   7
		 *
		 *   Which, after the deletion of 15, should be re-balanced via an LR rotation about
		 *   the root, yielding the following result:
		 *
		 *   			7
		 *   		   / \
		 *   		  /   \
		 *   		 5	   10
		 */

		intTree = new ThreadedAVLTree<Integer>();
		keys = new Integer[]{10, 5, 15, 7};
		for(Integer k: keys)
			intTree.insert(k);
		intTree.delete(15);
		assertTrue("Once we remove a key, we should no longer be able to find it " +
				"in the tree.", intTree.search(15) == null);
		assertEquals("Incorrect tree root. Are you rotating *LR* correctly after deletions?",
				new Integer(7), intTree.getRoot());
		assertEquals("Incorrect tree height. Are you re-balancing correctly after deletions?",
				1, intTree.height());

		/* We will now use the string tree to test whether students rotate *RL*
		 * correctly after deletions. We will create the following tree:
		 *
		 *  				Karen
		 *  				/	\
		 *  			   /	 \
		 *  			Jake	  Linda
		 * 		`				  /
		 * 						 Lauren
		 *
		 *  Then, removing Jake should trigger an RL rotation about the root, yielding
		 *  the following result:
		 *
		 *
		 * 				Lauren
		 * 			    /	 \
		 * 			   / 	  \
		 * 			Karen     Linda
		 */

		stringTree = new ThreadedAVLTree<String>();
		String[] keys2 = new String[]{"Karen", "Jake", "Linda", "Lauren"};
		for(String k: keys2)
			stringTree.insert(k);
		stringTree.delete("Jake");
		assertTrue("Once we remove a key, we should no longer be able to find it " +
				"in the tree.", stringTree.search("Jake") == null);
		assertEquals("Incorrect tree root. Are you rotating *RL* correctly after deletions?",
				"Lauren", stringTree.getRoot());
		assertEquals("Incorrect tree height. Are you re-balancing correctly after deletions?",
				1, stringTree.height());

		/* We will author two more tests with deletions which impose an RL or LR rotation.
		 * In both of those cases, we have a LR or RL rotation which does *not* propagate to
		 * the root, but rather to one of the root's children. Here's the tree for our first test:
		 *
		 *  				45
		 *  			   /  \
		 *  			  /    \
		 *  			 20	   60
		 *  			/  \   / \
		 *  		   10  30 50  65
		 *  			   /
		 *  			  25
		 *
		 *   Deleting 10 should pose an imbalance at the root's left child (20), triggering
		 *   a RL rotation about it and resulting in the tree:
		 *
		 *    				45
		 *    			   /  \
		 *    			  /    \
		 *    			 25    60
		 *    			/  \   / \
		 *    		   20  30 50  65
		 *
		 *    Let's check whether it does:
		 */

		intTree = new ThreadedAVLTree<Integer>();
		keys = new Integer[]{45, 20, 60, 10, 30, 50, 65, 25}; // Inserting them in this order guarantees the tree above.
		for(Integer k : keys)
			intTree.insert(k);
		intTree.delete(10);
		assertTrue("Once we remove a key, we should no longer be able to find it " +
				"in the tree.", intTree.search(10) == null);
		assertEquals("Tree root should not have changed after the last deletion. " +
						"Are you perhaps rotating too aggressively after deletions?" ,
				new Integer(45), intTree.getRoot());
		assertEquals("Incorrect tree height. Are you re-balancing correctly after deletions?",
				2, intTree.height());

		/* We will use the string tree to impose the symmetric test of the above
		 * and conclude the deletion tests. We will create the following tree:
		 *
		 *   					kal
		 *   					/ \
		 *   				   /   \
		 *   				 bet	rod
		 *   				 / \	/  \
		 *   				ar dog mad zed
		 *   						\
		 *   						nac
		 *
		 *   which, after removing "zed", should look like this:
		 *
		 *   				kal
		 *   			   /    \
		 *   			  /		 \
		 *   			bet	     nac
		 *   			/ \		/	\
		 *   		  ar  dog  mad	rod
		 */

		stringTree = new ThreadedAVLTree<String>();
		keys2 = new String[]{"kal", "bet", "rod", "ar", "dog", "mad", "zed", "nac"};
		for(String k: keys2)
			stringTree.insert(k);
		stringTree.delete("zed");
		assertTrue("Once we remove a key, we should no longer be able to find it " +
				"in the tree.", stringTree.search("zed") == null);
		assertEquals("Tree root should not have changed after this deletion. " +
						"Are you perhaps rotating too aggressively after deletions?" ,
				"kal", stringTree.getRoot());
		assertEquals("Incorrect tree height. Are you re-balancing correctly after deletions?",
				2, intTree.height());

		/* Up until now, we have always deleted keys from the leaf level. We also
		 * need to make sure that students correctly delete keys from inner nodes.
		 *
		 * For that purpose, we will re-create an integer tree from earlier:
		 *
		 * 				    45
		 *  			   /  \
		 *  			  /    \
		 *  			 20	   60
		 *  			/  \   / \
		 *  		   10  30 50  65
		 *  			   /
		 *  			  25
		 */

		intTree = new ThreadedAVLTree<Integer>();
		keys = new Integer[]{45, 20, 60, 10, 30, 50, 65, 25};
		for(Integer k : keys)
			intTree.insert(k);

		/* Now, deleting 20 should promote 25 in its place, yet trigger no rotations:
		 *
		 *     				45
		 *  			   /  \
		 *  			  /    \
		 *  			 25	   60
		 *  			/  \   / \
		 *  		   10  30 50  65
		 */

		intTree.delete(20);
		assertEquals("After a deletion of an inner node, the height of the tree was not " +
				"properly updated.", 2, intTree.height());
		assertEquals("After a deletion of an inner node, the root was found to be needlessly changed.",
				new Integer(45), intTree.getRoot());

		/* Now we will delete the root. This should result in the following tree:
		 *
		 * 					50
		 *  			   /  \
		 *  			  /    \
		 *  			 25	   60
		 *  			/  \     \
		 *  		   10  30     65
		 *
		 *  Again, no rotations occur.
		 */

		intTree.delete(45);
		assertEquals("After root deletion, the height of the tree needlessly changed.",
				2, intTree.height());
		assertEquals("After root deletion, the new root of the tree was incorrect.",
				new Integer(50), intTree.getRoot());

		/* We will perform one last test on deletions, which will be our most complex one.
		 * In this scenario, we delete the tree's root. This results in recursively deleting
		 * its inorder successor from the leaf level. However, that very recursive deletion
		 * triggers an RL rotation of its parent. Here's the tree that we will use (a slight
		 * modification of an earlier integer tree):
		 *
		 * 					45
		 *  			   /  \
		 *  			  /    \
		 *  			 25	   60
		 *  			/  \   / \
		 *  		   10  30 50  65
		 *  					  /
		 *  					 62
		 */

		intTree = new ThreadedAVLTree<Integer>();
		keys = new Integer[]{45, 25, 60, 10, 30, 50, 65, 62};
		for(Integer k: keys)
			intTree.insert(k);


		/* And here's what it should look like after we delete 45, the root:
		 *
		 * 					50
		 *  			   /  \
		 *  			  /    \
		 *  			 25	   62
		 *  			/  \   / \
		 *  		   10  30 60  65
		 */

		// Since this deletion is likely to uncover corner case issues with the code,
		// we will wrap it into a try-catch block and spice up the output to point the
		// students to the right direction.

		try {
			intTree.delete(45);
		}catch(Throwable t){
			fail("In a deletion of the root which should also involve a local rotation, " +
					"the code threw a " + t.getClass() + " with message:" + t.getMessage() + ".");
		}
		assertEquals("After deletion of the root, the new root promoted was incorrect.",
				new Integer(50), intTree.getRoot());
		assertEquals("After deletion of the root, the height of the new tree was incorrect.",
				2, intTree.height());
	}



	/** A stress test meant to catch any and all errors generated by the
	 * students' code.
	 */
	@Test
	public void testManyInsertions(){
		LinkedList<Integer> collector = new LinkedList<Integer>();
		for(int i = 0; i < NUMS; i++)
			collector.add(i);
		Collections.shuffle(collector);

		/* It's not particularly efficient to initiate a try / catch block
		 * within a loop, but it will be helpful for the students so that they know
		 * exactly which iteration failed, and what the key that caused the failure was.
		 */
		for(int i = 0; i < collector.size(); i++){
			try {
				intTree.insert(collector.get(i));
			}catch(Throwable t){
				fail("Caught a " + t.getClass() + " with message: " + t.getMessage() +
						" in iteration " + i + ", when inserting key " + collector.get(i) + ".");
			}
		}
	}


	/** What if I insert a certain collection of numbers and then delete all of them?
	 * Everything ok while doing that?
	 */
	@Test
	public void testManyDeletions(){
		LinkedList<Integer> collector = new LinkedList<Integer>();
		for(int i = 0; i < NUMS; i++)
			collector.add(i);
		Collections.shuffle(collector);

		 /*It's not particularly efficient to initiate a try / catch block
		 * within a loop, but it will be helpful for the students so that they know
		 * exactly which iteration failed, and what the key that caused the failure was.
		 */
		for(int i = 0; i < collector.size(); i++){
			try {
				intTree.insert(collector.get(i));
			}catch(Throwable t){
				fail("Caught a " + t.getClass() + " with message: " + t.getMessage() +
						" in iteration " + i + ", when inserting key " + collector.get(i) + ".");
			}
		}

		// Now, delete all of the keys.
		for(int i = 0; i < collector.size(); i++){
			try {
				Integer retVal = intTree.delete(collector.get(i));
				assertEquals("In iteration " + i +", when attempting to delete key " + collector.get(i) +
								", the return value of delete() did not match the key. Instead, it was: " + retVal + ".",
						collector.get(i), retVal);
				assertTrue("In iteration " + i +", after deleting key " + retVal +
								", search() determined the key to still exist inside the tree.",
						intTree.search(retVal) == null);
			}catch(Throwable t){
				fail("Caught a " + t.getClass() + " with message: " + t.getMessage() +
						" in iteration " + i + ", with the key " + collector.get(i) + ".");
			}
		}
		// After deletion, the tree had better be empty!
		assertTrue("We deleted all items that should be in the tree, yet the code " +
				"believes the tree is not empty.", intTree.isEmpty());
	}


	/**
	 * Make sure the trees generate the proper inorder traversal of nodes. Recall that, when
	 * grading the students, you will need to check the source code to make sure they are actually using
	 * the threads to make the traversal.
	 */
	@Test
	public void testInorderTraversal(){

		/* These tests are fantastically easy to create... All the work for me is to make
		 * sure that the students correctly manage the threads.
		 */

		/* The first thing that we will do is insert a bunch of integers in our int tree,
		 * then generate its inorder traversal and make sure that the integers are spit out
		 * in ascending order.
		 */
		LinkedList<Integer> collector = new LinkedList<Integer>();
		for(int i = 0; i < NUMS; i++)
			collector.add(i);
		Collections.shuffle(collector);
		for(int i = 0; i < collector.size(); i++)
			intTree.insert(new SplayElement(collector.get(i),collector.get(i),null,null));
		

		/* We are now interested in making sure whether deletions affect the inorder traversal
		 * in any way. Students need to be maintaining the threads correctly after both
		 * insertions *and* deletions, after all!
		 *
		 *  The first test on that regard will use the string tree. We will simply insert
		 *  three keys, delete one and check what it does to the inorder traversal.
		 *  We will build the tree:
		 *
		 *   			fad
		 *   			/ \
		 *   		  der  pos
		 *
		 *   and then delete "fad". The inorder traversal should then, naturally, be [der, pos].
		 */

		stringTree.insert("fad");
		stringTree.insert("der");
		stringTree.insert("pos");
		stringTree.delete("fad");
		Iterator<String> inorder2 = stringTree.inorderTraversal();
		assertTrue("After a deletion, we could not generate a single key in the " +
				"inorder traversal of the tree.", inorder2.hasNext());
		assertEquals("After a deletion, the first item of the tree's inorder traversal " +
				"was incorrect.", "der", inorder2.next());
		assertTrue("After a deletion, the inorder traversal of the tree did not generate " +
				"enough elements.", inorder2.hasNext());
		assertEquals("After a deletion, the second item of the tree's inorder traversal " +
				"was incorrect.", "pos", inorder2.next());
		assertFalse("After a deletion, we detected more items generated by the inorder traversal " +
				" than should be in the tree.", inorder2.hasNext());

		/* Our final test consists of deleting every second element of the set {0, 1, ... , NUMS-1}
		 * from the integer tree, and make sure that the inorder traversal of the resulting
		 * tree reflects this new sequence.
		 */

		for(int i = 1; i < NUMS; i+=2)
			intTree.delete(i);
		inorder = intTree.inorderTraversal();
		int iter = 0;
		for(int i = 0; i < NUMS; i+=2){
			assertTrue("After some deletions, the generated inorder traversal unexpectedly ran out of keys + "
					+ "after key #" +	iter + ".", inorder.hasNext());
			assertEquals("Mismatch between expected value and generated value at key #" + iter
					+ ". ", new Integer(i), inorder.next());
			iter++;
		}
		assertFalse("After some deletions, we detected more items generated by the inorder traversal " +
				" than should be in the tree.", inorder.hasNext());
	}

	/** Destroy the trees after every test. We could sequentially delete all nodes, but we will be
	 * testing deletion separately anyway.
	 */
	@After
	public void tearDown() {
		// Allow garbage collection to occur....
		intTree = null;
		stringTree = null;
	}
}
