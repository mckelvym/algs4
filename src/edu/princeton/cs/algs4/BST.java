/******************************************************************************
 *  Compilation:  javac BST.java
 *  Execution:    java BST
 *  Dependencies: StdIn.java StdOut.java Queue.java
 *  Data files:   http://algs4.cs.princeton.edu/32bst/tinyST.txt
 *
 *  A symbol table implemented with a binary search tree.
 *
 *  % more tinyST.txt
 *  S E A R C H E X A M P L E
 *
 *  % java BST < tinyST.txt
 *  A 8
 *  C 4
 *  E 12
 *  H 5
 *  L 11
 *  M 9
 *  P 10
 *  R 3
 *  S 0
 *  X 7
 *
 ******************************************************************************/

package edu.princeton.cs.algs4;

import java.util.NoSuchElementException;

/**
 * The <tt>BST</tt> class represents an ordered symbol table of generic
 * key-value pairs. It supports the usual <em>put</em>, <em>get</em>,
 * <em>contains</em>, <em>delete</em>, <em>size</em>, and <em>is-empty</em>
 * methods. It also provides ordered methods for finding the <em>minimum</em>,
 * <em>maximum</em>, <em>floor</em>, <em>select</em>, <em>ceiling</em>. It also
 * provides a <em>keys</em> method for iterating over all of the keys. A symbol
 * table implements the <em>associative array</em> abstraction: when associating
 * a value with a key that is already in the symbol table, the convention is to
 * replace the old value with the new value. Unlike {@link java.util.Map}, this
 * class uses the convention that values cannot be <tt>null</tt>&mdash;setting
 * the value associated with a key to <tt>null</tt> is equivalent to deleting
 * the key from the symbol table.
 * <p>
 * This implementation uses an (unbalanced) binary search tree. It requires that
 * the key type implements the <tt>Comparable</tt> interface and calls the
 * <tt>compareTo()</tt> and method to compare two keys. It does not call either
 * <tt>equals()</tt> or <tt>hashCode()</tt>. The <em>put</em>, <em>contains</em>, <em>remove</em>, <em>minimum</em>, <em>maximum</em>, <em>ceiling</em>,
 * <em>floor</em>, <em>select</em>, and <em>rank</em> operations each take
 * linear time in the worst case, if the tree becomes unbalanced. The
 * <em>size</em>, and <em>is-empty</em> operations take constant time.
 * Construction takes constant time.
 * <p>
 * For additional documentation, see <a
 * href="http://algs4.cs.princeton.edu/32bst">Section 3.2</a> of <i>Algorithms,
 * 4th Edition</i> by Robert Sedgewick and Kevin Wayne. For other
 * implementations, see {@link ST}, {@link BinarySearchST},
 * {@link SequentialSearchST}, {@link RedBlackBST},
 * {@link SeparateChainingHashST}, and {@link LinearProbingHashST},
 * <i>Algorithms, 4th Edition</i> by Robert Sedgewick and Kevin Wayne.
 */
public class BST<Key extends Comparable<Key>, Value>
{
	private class Node
	{
		private final Key	key;	// sorted by key
		private Node		left, right;	// left and right subtrees
		private int			N;				// number of nodes in subtree
		private Value		val;			// associated data

		public Node(final Key key, final Value val, final int N)
		{
			this.key = key;
			this.val = val;
			this.N = N;
		}
	}

	/**
	 * Unit tests the <tt>BST</tt> data type.
	 */
	public static void main(final String[] args)
	{
		final BST<String, Integer> st = new BST<String, Integer>();
		for (int i = 0; !StdIn.isEmpty(); i++)
		{
			final String key = StdIn.readString();
			st.put(key, i);
		}

		for (final String s : st.levelOrder())
		{
			StdOut.println(s + " " + st.get(s));
		}

		StdOut.println();

		for (final String s : st.keys())
		{
			StdOut.println(s + " " + st.get(s));
		}
	}

	private Node	root;	// root of BST

	/**
	 * Initializes an empty symbol table.
	 */
	public BST()
	{
	}

	/**
	 * Returns the smallest key in the symbol table greater than or equal to
	 * <tt>key</tt>.
	 *
	 * @param key
	 *            the key
	 * @return the smallest key in the symbol table greater than or equal to
	 *         <tt>key</tt>
	 * @throws NoSuchElementException
	 *             if there is no such key
	 * @throws NullPointerException
	 *             if <tt>key</tt> is <tt>null</tt>
	 */
	public Key ceiling(final Key key)
	{
		if (isEmpty())
		{
			throw new NoSuchElementException(
					"called ceiling() with empty symbol table");
		}
		final Node x = ceiling(root, key);
		if (x == null)
		{
			return null;
		}
		else
		{
			return x.key;
		}
	}

	private Node ceiling(final Node x, final Key key)
	{
		if (x == null)
		{
			return null;
		}
		final int cmp = key.compareTo(x.key);
		if (cmp == 0)
		{
			return x;
		}
		if (cmp < 0)
		{
			final Node t = ceiling(x.left, key);
			if (t != null)
			{
				return t;
			}
			else
			{
				return x;
			}
		}
		return ceiling(x.right, key);
	}

	/*************************************************************************
	 * Check integrity of BST data structure.
	 ***************************************************************************/
	private boolean check()
	{
		if (!isBST())
		{
			StdOut.println("Not in symmetric order");
		}
		if (!isSizeConsistent())
		{
			StdOut.println("Subtree counts not consistent");
		}
		if (!isRankConsistent())
		{
			StdOut.println("Ranks not consistent");
		}
		return isBST() && isSizeConsistent() && isRankConsistent();
	}

	/**
	 * Does this symbol table contain the given key?
	 *
	 * @param key
	 *            the key
	 * @return <tt>true</tt> if this symbol table contains <tt>key</tt> and
	 *         <tt>false</tt> otherwise
	 * @throws NullPointerException
	 *             if <tt>key</tt> is <tt>null</tt>
	 */
	public boolean contains(final Key key)
	{
		return get(key) != null;
	}

	/**
	 * Removes the key and associated value from the symbol table (if the key is
	 * in the symbol table).
	 *
	 * @param key
	 *            the key
	 * @throws NullPointerException
	 *             if <tt>key</tt> is <tt>null</tt>
	 */
	public void delete(final Key key)
	{
		root = delete(root, key);
		assert check();
	}

	private Node delete(Node x, final Key key)
	{
		if (x == null)
		{
			return null;
		}
		final int cmp = key.compareTo(x.key);
		if (cmp < 0)
		{
			x.left = delete(x.left, key);
		}
		else if (cmp > 0)
		{
			x.right = delete(x.right, key);
		}
		else
		{
			if (x.right == null)
			{
				return x.left;
			}
			if (x.left == null)
			{
				return x.right;
			}
			final Node t = x;
			x = min(t.right);
			x.right = deleteMin(t.right);
			x.left = t.left;
		}
		x.N = size(x.left) + size(x.right) + 1;
		return x;
	}

	/**
	 * Removes the largest key and associated value from the symbol table.
	 *
	 * @throws NoSuchElementException
	 *             if the symbol table is empty
	 */
	public void deleteMax()
	{
		if (isEmpty())
		{
			throw new NoSuchElementException("Symbol table underflow");
		}
		root = deleteMax(root);
		assert check();
	}

	private Node deleteMax(final Node x)
	{
		if (x.right == null)
		{
			return x.left;
		}
		x.right = deleteMax(x.right);
		x.N = size(x.left) + size(x.right) + 1;
		return x;
	}

	/**
	 * Removes the smallest key and associated value from the symbol table.
	 *
	 * @throws NoSuchElementException
	 *             if the symbol table is empty
	 */
	public void deleteMin()
	{
		if (isEmpty())
		{
			throw new NoSuchElementException("Symbol table underflow");
		}
		root = deleteMin(root);
		assert check();
	}

	private Node deleteMin(final Node x)
	{
		if (x.left == null)
		{
			return x.right;
		}
		x.left = deleteMin(x.left);
		x.N = size(x.left) + size(x.right) + 1;
		return x;
	}

	/**
	 * Returns the largest key in the symbol table less than or equal to
	 * <tt>key</tt>.
	 *
	 * @param key
	 *            the key
	 * @return the largest key in the symbol table less than or equal to
	 *         <tt>key</tt>
	 * @throws NoSuchElementException
	 *             if there is no such key
	 * @throws NullPointerException
	 *             if <tt>key</tt> is <tt>null</tt>
	 */
	public Key floor(final Key key)
	{
		if (isEmpty())
		{
			throw new NoSuchElementException(
					"called floor() with empty symbol table");
		}
		final Node x = floor(root, key);
		if (x == null)
		{
			return null;
		}
		else
		{
			return x.key;
		}
	}

	private Node floor(final Node x, final Key key)
	{
		if (x == null)
		{
			return null;
		}
		final int cmp = key.compareTo(x.key);
		if (cmp == 0)
		{
			return x;
		}
		if (cmp < 0)
		{
			return floor(x.left, key);
		}
		final Node t = floor(x.right, key);
		if (t != null)
		{
			return t;
		}
		else
		{
			return x;
		}
	}

	/**
	 * Returns the value associated with the given key.
	 *
	 * @param key
	 *            the key
	 * @return the value associated with the given key if the key is in the
	 *         symbol table and <tt>null</tt> if the key is not in the symbol
	 *         table
	 * @throws NullPointerException
	 *             if <tt>key</tt> is <tt>null</tt>
	 */
	public Value get(final Key key)
	{
		return get(root, key);
	}

	private Value get(final Node x, final Key key)
	{
		if (x == null)
		{
			return null;
		}
		final int cmp = key.compareTo(x.key);
		if (cmp < 0)
		{
			return get(x.left, key);
		}
		else if (cmp > 0)
		{
			return get(x.right, key);
		}
		else
		{
			return x.val;
		}
	}

	/**
	 * Returns the height of the BST (for debugging).
	 *
	 * @return the height of the BST (a 1-node tree has height 0)
	 */
	public int height()
	{
		return height(root);
	}

	private int height(final Node x)
	{
		if (x == null)
		{
			return -1;
		}
		return 1 + Math.max(height(x.left), height(x.right));
	}

	// does this binary tree satisfy symmetric order?
	// Note: this test also ensures that data structure is a binary tree since
	// order is strict
	private boolean isBST()
	{
		return isBST(root, null, null);
	}

	// is the tree rooted at x a BST with all keys strictly between min and max
	// (if min or max is null, treat as empty constraint)
	// Credit: Bob Dondero's elegant solution
	private boolean isBST(final Node x, final Key min, final Key max)
	{
		if (x == null)
		{
			return true;
		}
		if (min != null && x.key.compareTo(min) <= 0)
		{
			return false;
		}
		if (max != null && x.key.compareTo(max) >= 0)
		{
			return false;
		}
		return isBST(x.left, min, x.key) && isBST(x.right, x.key, max);
	}

	/**
	 * Returns true if this symbol table is empty.
	 * 
	 * @return <tt>true</tt> if this symbol table is empty; <tt>false</tt>
	 *         otherwise
	 */
	public boolean isEmpty()
	{
		return size() == 0;
	}

	// check that ranks are consistent
	private boolean isRankConsistent()
	{
		for (int i = 0; i < size(); i++)
		{
			if (i != rank(select(i)))
			{
				return false;
			}
		}
		for (final Key key : keys())
		{
			if (key.compareTo(select(rank(key))) != 0)
			{
				return false;
			}
		}
		return true;
	}

	// are the size fields correct?
	private boolean isSizeConsistent()
	{
		return isSizeConsistent(root);
	}

	private boolean isSizeConsistent(final Node x)
	{
		if (x == null)
		{
			return true;
		}
		if (x.N != size(x.left) + size(x.right) + 1)
		{
			return false;
		}
		return isSizeConsistent(x.left) && isSizeConsistent(x.right);
	}

	/**
	 * Returns all keys in the symbol table as an <tt>Iterable</tt>. To iterate
	 * over all of the keys in the symbol table named <tt>st</tt>, use the
	 * foreach notation: <tt>for (Key key : st.keys())</tt>.
	 *
	 * @return all keys in the symbol table
	 */
	public Iterable<Key> keys()
	{
		return keys(min(), max());
	}

	/**
	 * Returns all keys in the symbol table in the given range, as an
	 * <tt>Iterable</tt>.
	 *
	 * @return all keys in the sybol table between <tt>lo</tt> (inclusive) and
	 *         <tt>hi</tt> (exclusive)
	 * @throws NullPointerException
	 *             if either <tt>lo</tt> or <tt>hi</tt> is <tt>null</tt>
	 */
	public Iterable<Key> keys(final Key lo, final Key hi)
	{
		final Queue<Key> queue = new Queue<Key>();
		keys(root, queue, lo, hi);
		return queue;
	}

	private void keys(final Node x, final Queue<Key> queue, final Key lo,
			final Key hi)
	{
		if (x == null)
		{
			return;
		}
		final int cmplo = lo.compareTo(x.key);
		final int cmphi = hi.compareTo(x.key);
		if (cmplo < 0)
		{
			keys(x.left, queue, lo, hi);
		}
		if (cmplo <= 0 && cmphi >= 0)
		{
			queue.enqueue(x.key);
		}
		if (cmphi > 0)
		{
			keys(x.right, queue, lo, hi);
		}
	}

	/**
	 * Returns the keys in the BST in level order (for debugging).
	 *
	 * @return the keys in the BST in level order traversal
	 */
	public Iterable<Key> levelOrder()
	{
		final Queue<Key> keys = new Queue<Key>();
		final Queue<Node> queue = new Queue<Node>();
		queue.enqueue(root);
		while (!queue.isEmpty())
		{
			final Node x = queue.dequeue();
			if (x == null)
			{
				continue;
			}
			keys.enqueue(x.key);
			queue.enqueue(x.left);
			queue.enqueue(x.right);
		}
		return keys;
	}

	/**
	 * Returns the largest key in the symbol table.
	 *
	 * @return the largest key in the symbol table
	 * @throws NoSuchElementException
	 *             if the symbol table is empty
	 */
	public Key max()
	{
		if (isEmpty())
		{
			throw new NoSuchElementException(
					"called max() with empty symbol table");
		}
		return max(root).key;
	}

	private Node max(final Node x)
	{
		if (x.right == null)
		{
			return x;
		}
		else
		{
			return max(x.right);
		}
	}

	/**
	 * Returns the smallest key in the symbol table.
	 *
	 * @return the smallest key in the symbol table
	 * @throws NoSuchElementException
	 *             if the symbol table is empty
	 */
	public Key min()
	{
		if (isEmpty())
		{
			throw new NoSuchElementException(
					"called min() with empty symbol table");
		}
		return min(root).key;
	}

	private Node min(final Node x)
	{
		if (x.left == null)
		{
			return x;
		}
		else
		{
			return min(x.left);
		}
	}

	/**
	 * Inserts the key-value pair into the symbol table, overwriting the old
	 * value with the new value if the key is already in the symbol table. If
	 * the value is <tt>null</tt>, this effectively deletes the key from the
	 * symbol table.
	 *
	 * @param key
	 *            the key
	 * @param val
	 *            the value
	 * @throws NullPointerException
	 *             if <tt>key</tt> is <tt>null</tt>
	 */
	public void put(final Key key, final Value val)
	{
		if (val == null)
		{
			delete(key);
			return;
		}
		root = put(root, key, val);
		assert check();
	}

	private Node put(final Node x, final Key key, final Value val)
	{
		if (x == null)
		{
			return new Node(key, val, 1);
		}
		final int cmp = key.compareTo(x.key);
		if (cmp < 0)
		{
			x.left = put(x.left, key, val);
		}
		else if (cmp > 0)
		{
			x.right = put(x.right, key, val);
		}
		else
		{
			x.val = val;
		}
		x.N = 1 + size(x.left) + size(x.right);
		return x;
	}

	/**
	 * Return the number of keys in the symbol table strictly less than
	 * <tt>key</tt>.
	 *
	 * @param key
	 *            the key
	 * @return the number of keys in the symbol table strictly less than
	 *         <tt>key</tt>
	 * @throws NullPointerException
	 *             if <tt>key</tt> is <tt>null</tt>
	 */
	public int rank(final Key key)
	{
		return rank(key, root);
	}

	// Number of keys in the subtree less than key.
	private int rank(final Key key, final Node x)
	{
		if (x == null)
		{
			return 0;
		}
		final int cmp = key.compareTo(x.key);
		if (cmp < 0)
		{
			return rank(key, x.left);
		}
		else if (cmp > 0)
		{
			return 1 + size(x.left) + rank(key, x.right);
		}
		else
		{
			return size(x.left);
		}
	}

	/**
	 * Return the kth smallest key in the symbol table.
	 *
	 * @param k
	 *            the order statistic
	 * @return the kth smallest key in the symbol table
	 * @throws IllegalArgumentException
	 *             unless <tt>k</tt> is between 0 and <em>N</em> &minus; 1
	 */
	public Key select(final int k)
	{
		if (k < 0 || k >= size())
		{
			throw new IllegalArgumentException();
		}
		final Node x = select(root, k);
		return x.key;
	}

	// Return key of rank k.
	private Node select(final Node x, final int k)
	{
		if (x == null)
		{
			return null;
		}
		final int t = size(x.left);
		if (t > k)
		{
			return select(x.left, k);
		}
		else if (t < k)
		{
			return select(x.right, k - t - 1);
		}
		else
		{
			return x;
		}
	}

	/**
	 * Returns the number of key-value pairs in this symbol table.
	 * 
	 * @return the number of key-value pairs in this symbol table
	 */
	public int size()
	{
		return size(root);
	}

	/**
	 * Returns the number of keys in the symbol table in the given range.
	 *
	 * @return the number of keys in the sybol table between <tt>lo</tt>
	 *         (inclusive) and <tt>hi</tt> (exclusive)
	 * @throws NullPointerException
	 *             if either <tt>lo</tt> or <tt>hi</tt> is <tt>null</tt>
	 */
	public int size(final Key lo, final Key hi)
	{
		if (lo.compareTo(hi) > 0)
		{
			return 0;
		}
		if (contains(hi))
		{
			return rank(hi) - rank(lo) + 1;
		}
		else
		{
			return rank(hi) - rank(lo);
		}
	}

	// return number of key-value pairs in BST rooted at x
	private int size(final Node x)
	{
		if (x == null)
		{
			return 0;
		}
		else
		{
			return x.N;
		}
	}
}

/******************************************************************************
 * Copyright 2002-2015, Robert Sedgewick and Kevin Wayne.
 *
 * This file is part of algs4.jar, which accompanies the textbook
 *
 * Algorithms, 4th edition by Robert Sedgewick and Kevin Wayne, Addison-Wesley
 * Professional, 2011, ISBN 0-321-57351-X. http://algs4.cs.princeton.edu
 *
 *
 * algs4.jar is free software: you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later
 * version.
 *
 * algs4.jar is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR
 * A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with
 * algs4.jar. If not, see http://www.gnu.org/licenses.
 ******************************************************************************/
