/******************************************************************************
 *  Compilation:  javac IndexMaxPQ.java
 *  Execution:    java IndexMaxPQ
 *  Dependencies: StdOut.java
 *
 *  Maximum-oriented indexed PQ implementation using a binary heap.
 *
 ******************************************************************************/

package edu.princeton.cs.algs4;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * The <tt>IndexMaxPQ</tt> class represents an indexed priority queue of generic
 * keys. It supports the usual <em>insert</em> and <em>delete-the-maximum</em>
 * operations, along with <em>delete</em> and <em>change-the-key</em> methods.
 * In order to let the client refer to items on the priority queue, an integer
 * between 0 and maxN-1 is associated with each key&mdash;the client uses this
 * integer to specify which key to delete or change. It also supports methods
 * for peeking at a maximum key, testing if the priority queue is empty, and
 * iterating through the keys.
 * <p>
 * This implementation uses a binary heap along with an array to associate keys
 * with integers in the given range. The <em>insert</em>,
 * <em>delete-the-maximum</em>, <em>delete</em>, <em>change-key</em>,
 * <em>decrease-key</em>, and <em>increase-key</em> operations take logarithmic
 * time. The <em>is-empty</em>, <em>size</em>, <em>max-index</em>,
 * <em>max-key</em>, and <em>key-of</em> operations take constant time.
 * Construction takes time proportional to the specified capacity.
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
public class IndexMaxPQ<Key extends Comparable<Key>> implements
		Iterable<Integer>
{
	private class HeapIterator implements Iterator<Integer>
	{
		// create a new pq
		private final IndexMaxPQ<Key>	copy;

		// add all elements to copy of heap
		// takes linear time since already in heap order so no keys move
		public HeapIterator()
		{
			copy = new IndexMaxPQ<Key>(pq.length - 1);
			for (int i = 1; i <= N; i++)
			{
				copy.insert(pq[i], keys[pq[i]]);
			}
		}

		@Override
		public boolean hasNext()
		{
			return !copy.isEmpty();
		}

		@Override
		public Integer next()
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
	 * Unit tests the <tt>IndexMaxPQ</tt> data type.
	 */
	public static void main(final String[] args)
	{
		// insert a bunch of strings
		final String[] strings = { "it", "was", "the", "best", "of", "times",
				"it", "was", "the", "worst" };

		final IndexMaxPQ<String> pq = new IndexMaxPQ<String>(strings.length);
		for (int i = 0; i < strings.length; i++)
		{
			pq.insert(i, strings[i]);
		}

		// print each key using the iterator
		for (final int i : pq)
		{
			StdOut.println(i + " " + strings[i]);
		}

		StdOut.println();

		// increase or decrease the key
		for (int i = 0; i < strings.length; i++)
		{
			if (StdRandom.uniform() < 0.5)
			{
				pq.increaseKey(i, strings[i] + strings[i]);
			}
			else
			{
				pq.decreaseKey(i, strings[i].substring(0, 1));
			}
		}

		// delete and print each key
		while (!pq.isEmpty())
		{
			final String key = pq.maxKey();
			final int i = pq.delMax();
			StdOut.println(i + " " + key);
		}
		StdOut.println();

		// reinsert the same strings
		for (int i = 0; i < strings.length; i++)
		{
			pq.insert(i, strings[i]);
		}

		// delete them in random order
		final int[] perm = new int[strings.length];
		for (int i = 0; i < strings.length; i++)
		{
			perm[i] = i;
		}
		StdRandom.shuffle(perm);
		for (final int element : perm)
		{
			final String key = pq.keyOf(element);
			pq.delete(element);
			StdOut.println(element + " " + key);
		}

	}

	private final Key[]	keys;	// keys[i] = priority of i
	private int			N;		// number of elements on PQ

	private final int[]	pq;	// binary heap using 1-based indexing

	private final int[]	qp;	// inverse of pq - qp[pq[i]] = pq[qp[i]] = i

	/**
	 * Initializes an empty indexed priority queue with indices between
	 * <tt>0</tt> and <tt>maxN - 1</tt>.
	 *
	 * @param maxN
	 *            the keys on this priority queue are index from <tt>0</tt> to
	 *            <tt>maxN - 1</tt>
	 * @throws IllegalArgumentException
	 *             if maxN < 0
	 */
	public IndexMaxPQ(final int maxN)
	{
		keys = (Key[]) new Comparable[maxN + 1]; // make this of length maxN??
		pq = new int[maxN + 1];
		qp = new int[maxN + 1]; // make this of length maxN??
		for (int i = 0; i <= maxN; i++)
		{
			qp[i] = -1;
		}
	}

	/**
	 * Change the key associated with index <tt>i</tt> to the specified value.
	 *
	 * @param i
	 *            the index of the key to change
	 * @param key
	 *            change the key assocated with index <tt>i</tt> to this key
	 * @throws IndexOutOfBoundsException
	 *             unless 0 &le; <tt>i</tt> &lt; <tt>maxN</tt>
	 * @deprecated Replaced by {@link #changeKey(int, Key)}.
	 */
	@Deprecated
	public void change(final int i, final Key key)
	{
		changeKey(i, key);
	}

	/**
	 * Change the key associated with index <tt>i</tt> to the specified value.
	 *
	 * @param i
	 *            the index of the key to change
	 * @param key
	 *            change the key assocated with index <tt>i</tt> to this key
	 * @throws IndexOutOfBoundsException
	 *             unless 0 &le; i < maxN
	 */
	public void changeKey(final int i, final Key key)
	{
		if (!contains(i))
		{
			throw new NoSuchElementException(
					"index is not in the priority queue");
		}
		keys[i] = key;
		swim(qp[i]);
		sink(qp[i]);
	}

	/**
	 * Is <tt>i</tt> an index on this priority queue?
	 *
	 * @param i
	 *            an index
	 * @return <tt>true</tt> if <tt>i</tt> is an index on this priority queue;
	 *         <tt>false</tt> otherwise
	 * @throws IndexOutOfBoundsException
	 *             unless (0 &le; i < maxN)
	 */
	public boolean contains(final int i)
	{
		return qp[i] != -1;
	}

	/**
	 * Decrease the key associated with index <tt>i</tt> to the specified value.
	 *
	 * @param i
	 *            the index of the key to decrease
	 * @param key
	 *            decrease the key assocated with index <tt>i</tt> to this key
	 * @throws IndexOutOfBoundsException
	 *             unless 0 &le; <tt>i</tt> &lt; <tt>maxN</tt>
	 * @throws IllegalArgumentException
	 *             if key &ge; key associated with index <tt>i</tt>
	 * @throws NoSuchElementException
	 *             no key is associated with index <tt>i</tt>
	 */
	public void decreaseKey(final int i, final Key key)
	{
		if (!contains(i))
		{
			throw new NoSuchElementException(
					"index is not in the priority queue");
		}
		if (keys[i].compareTo(key) <= 0)
		{
			throw new IllegalArgumentException(
					"Calling decreaseKey() with given argument would not strictly decrease the key");
		}

		keys[i] = key;
		sink(qp[i]);
	}

	/**
	 * Remove the key on the priority queue associated with index <tt>i</tt>.
	 *
	 * @param i
	 *            the index of the key to remove
	 * @throws IndexOutOfBoundsException
	 *             unless 0 &le; <tt>i</tt> &lt; <tt>maxN</tt>
	 * @throws NoSuchElementException
	 *             no key is associated with index <tt>i</tt>
	 */
	public void delete(final int i)
	{
		if (!contains(i))
		{
			throw new NoSuchElementException(
					"index is not in the priority queue");
		}
		final int index = qp[i];
		exch(index, N--);
		swim(index);
		sink(index);
		keys[i] = null;
		qp[i] = -1;
	}

	/**
	 * Removes a maximum key and returns its associated index.
	 *
	 * @return an index associated with a maximum key
	 * @throws NoSuchElementException
	 *             if this priority queue is empty
	 */
	public int delMax()
	{
		if (N == 0)
		{
			throw new NoSuchElementException("Priority queue underflow");
		}
		final int min = pq[1];
		exch(1, N--);
		sink(1);
		qp[min] = -1; // delete
		keys[pq[N + 1]] = null; // to help with garbage collection
		pq[N + 1] = -1; // not needed
		return min;
	}

	private void exch(final int i, final int j)
	{
		final int swap = pq[i];
		pq[i] = pq[j];
		pq[j] = swap;
		qp[pq[i]] = i;
		qp[pq[j]] = j;
	}

	/**
	 * Increase the key associated with index <tt>i</tt> to the specified value.
	 *
	 * @param i
	 *            the index of the key to increase
	 * @param key
	 *            increase the key assocated with index <tt>i</tt> to this key
	 * @throws IndexOutOfBoundsException
	 *             unless 0 &le; <tt>i</tt> &lt; <tt>maxN</tt>
	 * @throws IllegalArgumentException
	 *             if key &le; key associated with index <tt>i</tt>
	 * @throws NoSuchElementException
	 *             no key is associated with index <tt>i</tt>
	 */
	public void increaseKey(final int i, final Key key)
	{
		if (!contains(i))
		{
			throw new NoSuchElementException(
					"index is not in the priority queue");
		}
		if (keys[i].compareTo(key) >= 0)
		{
			throw new IllegalArgumentException(
					"Calling increaseKey() with given argument would not strictly increase the key");
		}

		keys[i] = key;
		swim(qp[i]);
	}

	/**
	 * Associate key with index i.
	 *
	 * @param i
	 *            an index
	 * @param key
	 *            the key to associate with index <tt>i</tt>
	 * @throws IndexOutOfBoundsException
	 *             unless 0 &le; <tt>i</tt> &lt; <tt>maxN</tt>
	 * @throws IllegalArgumentException
	 *             if there already is an item associated with index <tt>i</tt>
	 */
	public void insert(final int i, final Key key)
	{
		if (contains(i))
		{
			throw new IllegalArgumentException(
					"index is already in the priority queue");
		}
		N++;
		qp[i] = N;
		pq[N] = i;
		keys[i] = key;
		swim(N);
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

	/**
	 * Returns an iterator that iterates over the keys on the priority queue in
	 * descending order. The iterator doesn't implement <tt>remove()</tt> since
	 * it's optional.
	 *
	 * @return an iterator that iterates over the keys in descending order
	 */
	@Override
	public Iterator<Integer> iterator()
	{
		return new HeapIterator();
	}

	/**
	 * Returns the key associated with index <tt>i</tt>.
	 *
	 * @param i
	 *            the index of the key to return
	 * @return the key associated with index <tt>i</tt>
	 * @throws IndexOutOfBoundsException
	 *             unless 0 &le; <tt>i</tt> &lt; <tt>maxN</tt>
	 * @throws NoSuchElementException
	 *             no key is associated with index <tt>i</tt>
	 */
	public Key keyOf(final int i)
	{
		if (!contains(i))
		{
			throw new NoSuchElementException(
					"index is not in the priority queue");
		}
		else
		{
			return keys[i];
		}
	}

	/***************************************************************************
	 * General helper functions.
	 ***************************************************************************/
	private boolean less(final int i, final int j)
	{
		return keys[pq[i]].compareTo(keys[pq[j]]) < 0;
	}

	/**
	 * Returns an index associated with a maximum key.
	 *
	 * @return an index associated with a maximum key
	 * @throws NoSuchElementException
	 *             if this priority queue is empty
	 */
	public int maxIndex()
	{
		if (N == 0)
		{
			throw new NoSuchElementException("Priority queue underflow");
		}
		return pq[1];
	}

	/**
	 * Returns a maximum key.
	 *
	 * @return a maximum key
	 * @throws NoSuchElementException
	 *             if this priority queue is empty
	 */
	public Key maxKey()
	{
		if (N == 0)
		{
			throw new NoSuchElementException("Priority queue underflow");
		}
		return keys[pq[1]];
	}

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
	 * Heap helper functions.
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
