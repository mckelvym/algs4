/******************************************************************************
 *  Compilation:  javac MaxPQ.java
 *  Execution:    java MaxPQ < input.txt
 *  Dependencies: StdIn.java StdOut.java
 *
 *  Generic max priority queue implementation with a binary heap.
 *  Can be used with a comparator instead of the natural order,
 *  but the generic Key type must still be Comparable.
 *
 *  % java MaxPQ < tinyPQ.txt
 *  Q X P (6 left on pq)
 *
 *  We use a one-based array to simplify parent and child calculations.
 *
 *  Can be optimized by replacing full exchanges with half exchanges
 *  (ala insertion sort).
 *
 ******************************************************************************/

package edu.princeton.cs.algs4;

import java.util.Comparator;
import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * The <tt>MaxPQ</tt> class represents a priority queue of generic keys. It
 * supports the usual <em>insert</em> and <em>delete-the-maximum</em>
 * operations, along with methods for peeking at the maximum key, testing if the
 * priority queue is empty, and iterating through the keys.
 * <p>
 * This implementation uses a binary heap. The <em>insert</em> and
 * <em>delete-the-maximum</em> operations take logarithmic amortized time. The
 * <em>max</em>, <em>size</em>, and <em>is-empty</em> operations take constant
 * time. Construction takes time proportional to the specified capacity or the
 * number of items used to initialize the data structure.
 * <p>
 * For additional documentation, see <a
 * href="http://algs4.cs.princeton.edu/24pq">Section 2.4</a> of <i>Algorithms,
 * 4th Edition</i> by Robert Sedgewick and Kevin Wayne.
 *
 * @author Robert Sedgewick
 * @author Kevin Wayne
 *
 * @param <Key>
 *            the generic type of key on this priority queue
 */

public class MaxPQ<Key> implements Iterable<Key>
{
	private class HeapIterator implements Iterator<Key>
	{

		// create a new pq
		private MaxPQ<Key>	copy;

		// add all items to copy of heap
		// takes linear time since already in heap order so no keys move
		public HeapIterator()
		{
			if (comparator == null)
			{
				copy = new MaxPQ<Key>(size());
			}
			else
			{
				copy = new MaxPQ<Key>(size(), comparator);
			}
			for (int i = 1; i <= N; i++)
			{
				copy.insert(pq[i]);
			}
		}

		@Override
		public boolean hasNext()
		{
			return !copy.isEmpty();
		}

		@Override
		public Key next()
		{
			if (!hasNext())
			{
				throw new NoSuchElementException();
			}
			return copy.delMax();
		}

		@Override
		public void remove()
		{
			throw new UnsupportedOperationException();
		}
	}

	/**
	 * Unit tests the <tt>MaxPQ</tt> data type.
	 */
	public static void main(final String[] args)
	{
		final MaxPQ<String> pq = new MaxPQ<String>();
		while (!StdIn.isEmpty())
		{
			final String item = StdIn.readString();
			if (!item.equals("-"))
			{
				pq.insert(item);
			}
			else if (!pq.isEmpty())
			{
				StdOut.print(pq.delMax() + " ");
			}
		}
		StdOut.println("(" + pq.size() + " left on pq)");
	}

	private Comparator<Key>	comparator; // optional Comparator

	private int				N;			// number of items on priority queue

	private Key[]			pq;		// store items at indices 1 to N

	/**
	 * Initializes an empty priority queue.
	 */
	public MaxPQ()
	{
		this(1);
	}

	/**
	 * Initializes an empty priority queue using the given comparator.
	 *
	 * @param comparator
	 *            the order in which to compare the keys
	 */
	public MaxPQ(final Comparator<Key> comparator)
	{
		this(1, comparator);
	}

	/**
	 * Initializes an empty priority queue with the given initial capacity.
	 *
	 * @param initCapacity
	 *            the initial capacity of this priority queue
	 */
	public MaxPQ(final int initCapacity)
	{
		pq = (Key[]) new Object[initCapacity + 1];
		N = 0;
	}

	/**
	 * Initializes an empty priority queue with the given initial capacity,
	 * using the given comparator.
	 *
	 * @param initCapacity
	 *            the initial capacity of this priority queue
	 * @param comparator
	 *            the order in which to compare the keys
	 */
	public MaxPQ(final int initCapacity, final Comparator<Key> comparator)
	{
		this.comparator = comparator;
		pq = (Key[]) new Object[initCapacity + 1];
		N = 0;
	}

	/**
	 * Initializes a priority queue from the array of keys. Takes time
	 * proportional to the number of keys, using sink-based heap construction.
	 *
	 * @param keys
	 *            the array of keys
	 */
	public MaxPQ(final Key[] keys)
	{
		N = keys.length;
		pq = (Key[]) new Object[keys.length + 1];
		for (int i = 0; i < N; i++)
		{
			pq[i + 1] = keys[i];
		}
		for (int k = N / 2; k >= 1; k--)
		{
			sink(k);
		}
		assert isMaxHeap();
	}

	/**
	 * Removes and returns a largest key on this priority queue.
	 *
	 * @return a largest key on this priority queue
	 * @throws NoSuchElementException
	 *             if this priority queue is empty
	 */
	public Key delMax()
	{
		if (isEmpty())
		{
			throw new NoSuchElementException("Priority queue underflow");
		}
		final Key max = pq[1];
		exch(1, N--);
		sink(1);
		pq[N + 1] = null; // to avoid loiterig and help with garbage collection
		if (N > 0 && N == (pq.length - 1) / 4)
		{
			resize(pq.length / 2);
		}
		assert isMaxHeap();
		return max;
	}

	private void exch(final int i, final int j)
	{
		final Key swap = pq[i];
		pq[i] = pq[j];
		pq[j] = swap;
	}

	/**
	 * Adds a new key to this priority queue.
	 *
	 * @param x
	 *            the new key to add to this priority queue
	 */
	public void insert(final Key x)
	{

		// double size of array if necessary
		if (N >= pq.length - 1)
		{
			resize(2 * pq.length);
		}

		// add x, and percolate it up to maintain heap invariant
		pq[++N] = x;
		swim(N);
		assert isMaxHeap();
	}

	/**
	 * Returns true if this priority queue is empty.
	 *
	 * @return <tt>true</tt> if this priority queue is empty; <tt>false</tt>
	 *         otherwise
	 */
	public boolean isEmpty()
	{
		return N == 0;
	}

	// is pq[1..N] a max heap?
	private boolean isMaxHeap()
	{
		return isMaxHeap(1);
	}

	// is subtree of pq[1..N] rooted at k a max heap?
	private boolean isMaxHeap(final int k)
	{
		if (k > N)
		{
			return true;
		}
		final int left = 2 * k, right = 2 * k + 1;
		if (left <= N && less(k, left))
		{
			return false;
		}
		if (right <= N && less(k, right))
		{
			return false;
		}
		return isMaxHeap(left) && isMaxHeap(right);
	}

	/**
	 * Returns an iterator that iterates over the keys on this priority queue in
	 * descending order. The iterator doesn't implement <tt>remove()</tt> since
	 * it's optional.
	 *
	 * @return an iterator that iterates over the keys in descending order
	 */
	@Override
	public Iterator<Key> iterator()
	{
		return new HeapIterator();
	}

	/***************************************************************************
	 * Helper functions for compares and swaps.
	 ***************************************************************************/
	private boolean less(final int i, final int j)
	{
		if (comparator == null)
		{
			return ((Comparable<Key>) pq[i]).compareTo(pq[j]) < 0;
		}
		else
		{
			return comparator.compare(pq[i], pq[j]) < 0;
		}
	}

	/**
	 * Returns a largest key on this priority queue.
	 *
	 * @return a largest key on this priority queue
	 * @throws NoSuchElementException
	 *             if this priority queue is empty
	 */
	public Key max()
	{
		if (isEmpty())
		{
			throw new NoSuchElementException("Priority queue underflow");
		}
		return pq[1];
	}

	// helper function to double the size of the heap array
	private void resize(final int capacity)
	{
		assert capacity > N;
		final Key[] temp = (Key[]) new Object[capacity];
		for (int i = 1; i <= N; i++)
		{
			temp[i] = pq[i];
		}
		pq = temp;
	}

	/***************************************************************************
	 * Iterator.
	 ***************************************************************************/

	private void sink(int k)
	{
		while (2 * k <= N)
		{
			int j = 2 * k;
			if (j < N && less(j, j + 1))
			{
				j++;
			}
			if (!less(k, j))
			{
				break;
			}
			exch(k, j);
			k = j;
		}
	}

	/**
	 * Returns the number of keys on this priority queue.
	 *
	 * @return the number of keys on this priority queue
	 */
	public int size()
	{
		return N;
	}

	/***************************************************************************
	 * Helper functions to restore the heap invariant.
	 ***************************************************************************/

	private void swim(int k)
	{
		while (k > 1 && less(k / 2, k))
		{
			exch(k, k / 2);
			k = k / 2;
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
