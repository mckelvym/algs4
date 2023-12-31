/******************************************************************************
 *  Compilation:  javac LinkedQueue.java
 *  Execution:    java LinkedQueue < input.txt
 *  Dependencies: StdIn.java StdOut.java
 *  Data files:   http://algs4.cs.princeton.edu/13stacks/tobe.txt
 *
 *  A generic queue, implemented using a singly-linked list.
 *
 *  % java Queue < tobe.txt
 *  to be or not to be (2 left on queue)
 *
 ******************************************************************************/

package edu.princeton.cs.algs4;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * The <tt>LinkedQueue</tt> class represents a first-in-first-out (FIFO) queue
 * of generic items. It supports the usual <em>enqueue</em> and <em>dequeue</em>
 * operations, along with methods for peeking at the first item, testing if the
 * queue is empty, and iterating through the items in FIFO order.
 * <p>
 * This implementation uses a singly-linked list with a non-static nested class
 * for linked-list nodes. See {@link Queue} for a version that uses a static
 * nested class. The <em>enqueue</em>, <em>dequeue</em>, <em>peek</em>,
 * <em>size</em>, and <em>is-empty</em> operations all take constant time in the
 * worst case.
 * <p>
 * For additional documentation, see <a
 * href="http://algs4.cs.princeton.edu/13stacks">Section 1.3</a> of
 * <i>Algorithms, 4th Edition</i> by Robert Sedgewick and Kevin Wayne.
 *
 * @author Robert Sedgewick
 * @author Kevin Wayne
 */
public class LinkedQueue<Item> implements Iterable<Item>
{
	// an iterator, doesn't implement remove() since it's optional
	private class ListIterator implements Iterator<Item>
	{
		private Node	current	= first;

		@Override
		public boolean hasNext()
		{
			return current != null;
		}

		@Override
		public Item next()
		{
			if (!hasNext())
			{
				throw new NoSuchElementException();
			}
			final Item item = current.item;
			current = current.next;
			return item;
		}

		@Override
		public void remove()
		{
			throw new UnsupportedOperationException();
		}
	}

	// helper linked list class
	private class Node
	{
		private Item	item;
		private Node	next;
	}

	/**
	 * Unit tests the <tt>LinkedQueue</tt> data type.
	 */
	public static void main(final String[] args)
	{
		final LinkedQueue<String> q = new LinkedQueue<String>();
		while (!StdIn.isEmpty())
		{
			final String item = StdIn.readString();
			if (!item.equals("-"))
			{
				q.enqueue(item);
			}
			else if (!q.isEmpty())
			{
				StdOut.print(q.dequeue() + " ");
			}
		}
		StdOut.println("(" + q.size() + " left on queue)");
	}

	private Node	first;	// beginning of queue

	private Node	last;	// end of queue

	private int		N;		// number of elements on queue

	/**
	 * Initializes an empty queue.
	 */
	public LinkedQueue()
	{
		first = null;
		last = null;
		N = 0;
		assert check();
	}

	// check internal invariants
	private boolean check()
	{
		if (N == 0)
		{
			if (first != null)
			{
				return false;
			}
			if (last != null)
			{
				return false;
			}
		}
		else if (N == 1)
		{
			if (first == null || last == null)
			{
				return false;
			}
			if (first != last)
			{
				return false;
			}
			if (first.next != null)
			{
				return false;
			}
		}
		else
		{
			if (first == last)
			{
				return false;
			}
			if (first.next == null)
			{
				return false;
			}
			if (last.next != null)
			{
				return false;
			}

			// check internal consistency of instance variable N
			int numberOfNodes = 0;
			for (Node x = first; x != null; x = x.next)
			{
				numberOfNodes++;
			}
			if (numberOfNodes != N)
			{
				return false;
			}

			// check internal consistency of instance variable last
			Node lastNode = first;
			while (lastNode.next != null)
			{
				lastNode = lastNode.next;
			}
			if (last != lastNode)
			{
				return false;
			}
		}

		return true;
	}

	/**
	 * Removes and returns the item on this queue that was least recently added.
	 * 
	 * @return the item on this queue that was least recently added
	 * @throws java.util.NoSuchElementException
	 *             if this queue is empty
	 */
	public Item dequeue()
	{
		if (isEmpty())
		{
			throw new NoSuchElementException("Queue underflow");
		}
		final Item item = first.item;
		first = first.next;
		N--;
		if (isEmpty())
		{
			last = null; // to avoid loitering
		}
		assert check();
		return item;
	}

	/**
	 * Adds the item to this queue.
	 * 
	 * @param item
	 *            the item to add
	 */
	public void enqueue(final Item item)
	{
		final Node oldlast = last;
		last = new Node();
		last.item = item;
		last.next = null;
		if (isEmpty())
		{
			first = last;
		}
		else
		{
			oldlast.next = last;
		}
		N++;
		assert check();
	}

	/**
	 * Is this queue empty?
	 * 
	 * @return true if this queue is empty; false otherwise
	 */
	public boolean isEmpty()
	{
		return first == null;
	}

	/**
	 * Returns an iterator that iterates over the items in this queue in FIFO
	 * order.
	 * 
	 * @return an iterator that iterates over the items in this queue in FIFO
	 *         order
	 */
	@Override
	public Iterator<Item> iterator()
	{
		return new ListIterator();
	}

	/**
	 * Returns the item least recently added to this queue.
	 * 
	 * @return the item least recently added to this queue
	 * @throws java.util.NoSuchElementException
	 *             if this queue is empty
	 */
	public Item peek()
	{
		if (isEmpty())
		{
			throw new NoSuchElementException("Queue underflow");
		}
		return first.item;
	}

	/**
	 * Returns the number of items in this queue.
	 * 
	 * @return the number of items in this queue
	 */
	public int size()
	{
		return N;
	}

	/**
	 * Returns a string representation of this queue.
	 * 
	 * @return the sequence of items in FIFO order, separated by spaces
	 */
	@Override
	public String toString()
	{
		final StringBuilder s = new StringBuilder();
		for (final Item item : this)
		{
			s.append(item + " ");
		}
		return s.toString();
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
