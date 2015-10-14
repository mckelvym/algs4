/******************************************************************************
 *  Compilation: javac IndexFibonacciMinPQ.java
 *  Execution:
 *
 *  An index Fibonacci heap.
 *
 ******************************************************************************/

package edu.princeton.cs.algs4;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.NoSuchElementException;

/*
 *  The IndexFibonacciMinPQ class represents an indexed priority queue of generic keys.
 *  It supports the usual insert and delete-the-minimum operations,
 *  along with delete and change-the-key methods.
 *  In order to let the client refer to keys on the priority queue,
 *  an integer between 0 and N-1 is associated with each key ; the client
 *  uses this integer to specify which key to delete or change.
 *  It also supports methods for peeking at the minimum key,
 *  testing if the priority queue is empty, and iterating through
 *  the keys.
 *
 *  This implementation uses a Fibonacci heap along with an array to associate
 *  keys with integers in the given range.
 *  The insert, size, is-empty, contains, minimum-index, minimum-key
 *  and key-of take constant time.
 *  The decrease-key operation takes amortized constant time.
 *  The delete, increase-key, delete-the-minimum, change-key take amortized logarithmic time.
 *  Construction takes time proportional to the specified capacity
 *
 *  @author Tristan Claverie
 */
public class IndexFibonacciMinPQ<Key> implements Iterable<Integer>
{
	/***************************
	 * Comparator
	 **************************/

	// default Comparator
	private class MyComparator implements Comparator<Key>
	{
		@Override
		public int compare(final Key key1, final Key key2)
		{
			return ((Comparable<Key>) key1).compareTo(key2);
		}
	}

	private class MyIterator implements Iterator<Integer>
	{
		private final IndexFibonacciMinPQ<Key>	copy;

		// Constructor takes linear time
		public MyIterator()
		{
			copy = new IndexFibonacciMinPQ<Key>(comp, n);
			for (final Node<Key> x : nodes)
			{
				if (x != null)
				{
					copy.insert(x.index, x.key);
				}
			}
		}

		@Override
		public boolean hasNext()
		{
			return !copy.isEmpty();
		}

		// Takes amortized logarithmic time
		@Override
		public Integer next()
		{
			if (!hasNext())
			{
				throw new NoSuchElementException();
			}
			return copy.delMin();
		}

		@Override
		public void remove()
		{
			throw new UnsupportedOperationException();
		}
	}

	// Represents a Node of a tree
	private class Node<Key>
	{
		int		index;	// Index associated with the key
		Key		key;	// Key of the Node
		boolean	mark;	// Indicates if this Node already lost a child
		int		order;	// The order of the tree rooted by this Node
		Node<Key>	parent, child;	// parent and child of this Node
		Node<Key>	prev, next;	// siblings of the Node
	}

	private final Comparator<Key>				comp;											// Comparator
																								// over
																								// the
																								// keys
	private Node<Key>							head;											// Head
																								// of
																								// the
																								// circular
																								// root
																								// list
	private Node<Key>							min;											// Minimum
																								// Node
																								// in
																								// the
																								// heap
	private final int							n;												// Maximum
																								// number
																								// of
																								// elements
																								// in
																								// the
																								// heap

	private final Node<Key>[]					nodes;											// Array
																								// of
																								// Nodes
																								// in
																								// the
																								// heap

	private int									size;											// Number
																								// of
																								// keys
																								// in
																								// the
																								// heap

	private final HashMap<Integer, Node<Key>>	table	= new HashMap<Integer, Node<Key>>();	// Used
																								// for
																								// the
																								// consolidate
																								// operation

	/**
	 * Initializes an empty indexed priority queue with indices between 0 and
	 * N-1 Worst case is O(n)
	 * 
	 * @param N
	 *            number of keys in the priority queue, index from 0 to N-1
	 * @param C
	 *            a Comparator over the keys
	 * @throws java.lang.IllegalArgumentException
	 *             if N < 0
	 */
	public IndexFibonacciMinPQ(final Comparator<Key> C, final int N)
	{
		if (N < 0)
		{
			throw new IllegalArgumentException(
					"Cannot create a priority queue of negative size");
		}
		n = N;
		nodes = new Node[n];
		comp = C;
	}

	/**
	 * Initializes an empty indexed priority queue with indices between 0 and
	 * N-1 Worst case is O(n)
	 * 
	 * @param N
	 *            number of keys in the priority queue, index from 0 to N-1
	 * @throws java.lang.IllegalArgumentException
	 *             if N < 0
	 */
	public IndexFibonacciMinPQ(final int N)
	{
		if (N < 0)
		{
			throw new IllegalArgumentException(
					"Cannot create a priority queue of negative size");
		}
		n = N;
		nodes = new Node[n];
		comp = new MyComparator();
	}

	/**
	 * Changes the key associated with index i to the given key If the given key
	 * is greater, Worst case is O(log(n)) If the given key is lower, Worst case
	 * is O(1) (amortized)
	 * 
	 * @param i
	 *            an index
	 * @param key
	 *            the key to associate with i
	 * @throws java.lang.IndexOutOfBoundsException
	 *             if the specified index is invalid
	 * @throws java.util.NoSuchElementException
	 *             if the index has no key associated with
	 */

	public void changeKey(final int i, final Key key)
	{
		if (i < 0 || i >= n)
		{
			throw new IndexOutOfBoundsException();
		}
		if (!contains(i))
		{
			throw new NoSuchElementException(
					"Specified index is not in the queue");
		}
		if (greater(key, nodes[i].key))
		{
			increaseKey(i, key);
		}
		else
		{
			decreaseKey(i, key);
		}
	}

	/*************************************
	 * Function for consolidating all trees in the root list
	 ************************************/

	// Coalesces the roots, thus reshapes the heap
	// Caching a HashMap improves greatly performances
	private void consolidate()
	{
		table.clear();
		Node<Key> x = head;
		int maxOrder = 0;
		min = head;
		Node<Key> y = null, z = null;
		do
		{
			y = x;
			x = x.next;
			z = table.get(y.order);
			while (z != null)
			{
				table.remove(y.order);
				if (greater(y.key, z.key))
				{
					link(y, z);
					y = z;
				}
				else
				{
					link(z, y);
				}
				z = table.get(y.order);
			}
			table.put(y.order, y);
			if (y.order > maxOrder)
			{
				maxOrder = y.order;
			}
		}
		while (x != head);
		head = null;
		for (final Node<Key> n : table.values())
		{
			min = greater(min.key, n.key) ? n : min;
			head = insert(n, head);
		}
	}

	/**
	 * Does the priority queue contains the index i ? Worst case is O(1)
	 * 
	 * @param i
	 *            an index
	 * @throws java.lang.IndexOutOfBoundsException
	 *             if the specified index is invalid
	 * @return true if i is on the priority queue, false if not
	 */

	public boolean contains(final int i)
	{
		if (i < 0 || i >= n)
		{
			throw new IndexOutOfBoundsException();
		}
		else
		{
			return nodes[i] != null;
		}
	}

	/*************************************
	 * Function for decreasing a key
	 ************************************/

	// Removes a Node from its parent's child list and insert it in the root
	// list
	// If the parent Node already lost a child, reshapes the heap accordingly
	private void cut(final int i)
	{
		final Node<Key> x = nodes[i];
		final Node<Key> parent = x.parent;
		parent.child = cut(x, parent.child);
		x.parent = null;
		parent.order--;
		head = insert(x, head);
		parent.mark = !parent.mark;
		if (!parent.mark && parent.parent != null)
		{
			cut(parent.index);
		}
	}

	// Removes a tree from the list defined by the head pointer
	private Node<Key> cut(final Node<Key> x, final Node<Key> head)
	{
		if (x.next == x)
		{
			x.next = null;
			x.prev = null;
			return null;
		}
		else
		{
			x.next.prev = x.prev;
			x.prev.next = x.next;
			final Node<Key> res = x.next;
			x.next = null;
			x.prev = null;
			if (head == x)
			{
				return res;
			}
			else
			{
				return head;
			}
		}
	}

	/**
	 * Decreases the key associated with index i to the given key Worst case is
	 * O(1) (amortized).
	 * 
	 * @param i
	 *            an index
	 * @param key
	 *            the key to associate with i
	 * @throws java.lang.IndexOutOfBoundsException
	 *             if the specified index is invalid
	 * @throws java.util.NoSuchElementException
	 *             if the index has no key associated with
	 * @throws java.util.IllegalArgumentException
	 *             if the given key is greater than the current key
	 */

	public void decreaseKey(final int i, final Key key)
	{
		if (i < 0 || i >= n)
		{
			throw new IndexOutOfBoundsException();
		}
		if (!contains(i))
		{
			throw new NoSuchElementException(
					"Specified index is not in the queue");
		}
		if (greater(key, nodes[i].key))
		{
			throw new IllegalArgumentException(
					"Calling with this argument would not decrease the key");
		}
		final Node<Key> x = nodes[i];
		x.key = key;
		if (greater(min.key, key))
		{
			min = x;
		}
		if (x.parent != null && greater(x.parent.key, key))
		{
			cut(i);
		}
	}

	/**
	 * Deletes the key associated the given index Worst case is O(log(n))
	 * (amortized)
	 * 
	 * @param i
	 *            an index
	 * @throws java.lang.IndexOutOfBoundsException
	 *             if the specified index is invalid
	 * @throws java.util.NoSuchElementException
	 *             if the given index has no key associated with
	 */

	public void delete(final int i)
	{
		if (i < 0 || i >= n)
		{
			throw new IndexOutOfBoundsException();
		}
		if (!contains(i))
		{
			throw new NoSuchElementException(
					"Specified index is not in the queue");
		}
		Node<Key> x = nodes[i];
		x.key = null; // For garbage collection
		if (x.parent != null)
		{
			cut(i);
		}
		head = cut(x, head);
		if (x.child != null)
		{
			Node<Key> child = x.child;
			x.child = null; // For garbage collection
			x = child;
			do
			{
				child.parent = null;
				child = child.next;
			}
			while (child != x);
			head = meld(head, child);
		}
		if (!isEmpty())
		{
			consolidate();
		}
		else
		{
			min = null;
		}
		nodes[i] = null;
		size--;
	}

	/**
	 * Delete the minimum key Worst case is O(log(n)) (amortized)
	 * 
	 * @throws java.util.NoSuchElementException
	 *             if the priority queue is empty
	 * @return the index associated with the minimum key
	 */

	public int delMin()
	{
		if (isEmpty())
		{
			throw new NoSuchElementException("Priority queue is empty");
		}
		head = cut(min, head);
		Node<Key> x = min.child;
		final int index = min.index;
		min.key = null; // For garbage collection
		if (x != null)
		{
			do
			{
				x.parent = null;
				x = x.next;
			}
			while (x != min.child);
			head = meld(head, x);
			min.child = null; // For garbage collection
		}
		size--;
		if (!isEmpty())
		{
			consolidate();
		}
		else
		{
			min = null;
		}
		nodes[index] = null;
		return index;
	}

	/*************************************
	 * General helper functions
	 ************************************/

	// Compares two keys
	private boolean greater(final Key n, final Key m)
	{
		if (n == null)
		{
			return false;
		}
		if (m == null)
		{
			return true;
		}
		return comp.compare(n, m) > 0;
	}

	/**
	 * Increases the key associated with index i to the given key Worst case is
	 * O(log(n))
	 * 
	 * @param i
	 *            an index
	 * @param key
	 *            the key to associate with i
	 * @throws java.lang.IndexOutOfBoundsException
	 *             if the specified index is invalid
	 * @throws java.util.NoSuchElementException
	 *             if the index has no key associated with
	 * @throws java.util.IllegalArgumentException
	 *             if the given key is lower than the current key
	 */

	public void increaseKey(final int i, final Key key)
	{
		if (i < 0 || i >= n)
		{
			throw new IndexOutOfBoundsException();
		}
		if (!contains(i))
		{
			throw new NoSuchElementException(
					"Specified index is not in the queue");
		}
		if (greater(nodes[i].key, key))
		{
			throw new IllegalArgumentException(
					"Calling with this argument would not increase the key");
		}
		delete(i);
		insert(i, key);
	}

	/**
	 * Associates a key with an index Worst case is O(1)
	 * 
	 * @param i
	 *            an index
	 * @param key
	 *            a Key associated with i
	 * @throws java.lang.IndexOutOfBoundsException
	 *             if the specified index is invalid
	 * @throws java.util.IllegalArgumentException
	 *             if the index is already in the queue
	 */

	public void insert(final int i, final Key key)
	{
		if (i < 0 || i >= n)
		{
			throw new IndexOutOfBoundsException();
		}
		if (contains(i))
		{
			throw new IllegalArgumentException(
					"Specified index is already in the queue");
		}
		final Node<Key> x = new Node<Key>();
		x.key = key;
		x.index = i;
		nodes[i] = x;
		size++;
		head = insert(x, head);
		if (min == null)
		{
			min = head;
		}
		else
		{
			min = greater(min.key, key) ? head : min;
		}
	}

	/*************************************
	 * General helper functions for manipulating circular lists
	 ************************************/

	// Inserts a Node in a circular list containing head, returns a new head
	private Node<Key> insert(final Node<Key> x, final Node<Key> head)
	{
		if (head == null)
		{
			x.prev = x;
			x.next = x;
		}
		else
		{
			head.prev.next = x;
			x.next = head;
			x.prev = head.prev;
			head.prev = x;
		}
		return x;
	}

	/**
	 * Whether the priority queue is empty Worst case is O(1)
	 * 
	 * @return true if the priority queue is empty, false if not
	 */

	public boolean isEmpty()
	{
		return size == 0;
	}

	/**
	 * Get an Iterator over the indexes in the priority queue in ascending order
	 * The Iterator does not implement the remove() method iterator() : Worst
	 * case is O(n) next() : Worst case is O(log(n)) (amortized) hasNext() :
	 * Worst case is O(1)
	 * 
	 * @return an Iterator over the indexes in the priority queue in ascending
	 *         order
	 */

	@Override
	public Iterator<Integer> iterator()
	{
		return new MyIterator();
	}

	/**
	 * Get the key associated with index i Worst case is O(1)
	 * 
	 * @param i
	 *            an index
	 * @throws java.lang.IndexOutOfBoundsException
	 *             if the specified index is invalid
	 * @throws java.util.NoSuchElementException
	 *             if the index is not in the queue
	 * @return the key associated with index i
	 */

	public Key keyOf(final int i)
	{
		if (i < 0 || i >= n)
		{
			throw new IndexOutOfBoundsException();
		}
		if (!contains(i))
		{
			throw new NoSuchElementException(
					"Specified index is not in the queue");
		}
		return nodes[i].key;
	}

	// Assuming root1 holds a greater key than root2, root2 becomes the new root
	private void link(final Node<Key> root1, final Node<Key> root2)
	{
		root1.parent = root2;
		root2.child = insert(root1, root2.child);
		root2.order++;
	}

	// Merges two lists together.
	private Node<Key> meld(final Node<Key> x, final Node<Key> y)
	{
		if (x == null)
		{
			return y;
		}
		if (y == null)
		{
			return x;
		}
		x.prev.next = y.next;
		y.next.prev = x.prev;
		x.prev = y;
		y.next = x;
		return x;
	}

	/*************************************
	 * Iterator
	 ************************************/

	/**
	 * Get the index associated with the minimum key Worst case is O(1)
	 * 
	 * @throws java.util.NoSuchElementException
	 *             if the priority queue is empty
	 * @return the index associated with the minimum key
	 */

	public int minIndex()
	{
		if (isEmpty())
		{
			throw new NoSuchElementException("Priority queue is empty");
		}
		return min.index;
	}

	/**
	 * Get the minimum key currently in the queue Worst case is O(1)
	 * 
	 * @throws java.util.NoSuchElementException
	 *             if the priority queue is empty
	 * @return the minimum key currently in the priority queue
	 */

	public Key minKey()
	{
		if (isEmpty())
		{
			throw new NoSuchElementException("Priority queue is empty");
		}
		return min.key;
	}

	/**
	 * Number of elements currently on the priority queue Worst case is O(1)
	 * 
	 * @return the number of elements on the priority queue
	 */

	public int size()
	{
		return size;
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
