/******************************************************************************
 *  Compilation:  javac Selection.java
 *  Execution:    java  Selection < input.txt
 *  Dependencies: StdOut.java StdIn.java
 *  Data files:   http://algs4.cs.princeton.edu/21sort/tiny.txt
 *                http://algs4.cs.princeton.edu/21sort/words3.txt
 *
 *  Sorts a sequence of strings from standard input using selection sort.
 *
 *  % more tiny.txt
 *  S O R T E X A M P L E
 *
 *  % java Selection < tiny.txt
 *  A E E L M O P R S T X                 [ one string per line ]
 *
 *  % more words3.txt
 *  bed bug dad yes zoo ... all bad yet
 *
 *  % java Selection < words3.txt
 *  all bad bed bug dad ... yes yet zoo    [ one string per line ]
 *
 ******************************************************************************/

package edu.princeton.cs.algs4;

import java.util.Comparator;

/**
 * The <tt>Selection</tt> class provides static methods for sorting an array
 * using selection sort.
 * <p>
 * For additional documentation, see <a
 * href="http://algs4.cs.princeton.edu/21elementary">Section 2.1</a> of
 * <i>Algorithms, 4th Edition</i> by Robert Sedgewick and Kevin Wayne.
 *
 * @author Robert Sedgewick
 * @author Kevin Wayne
 */
public class Selection
{

	// exchange a[i] and a[j]
	private static void exch(final Object[] a, final int i, final int j)
	{
		final Object swap = a[i];
		a[i] = a[j];
		a[j] = swap;
	}

	/***************************************************************************
	 * Check if array is sorted - useful for debugging.
	 ***************************************************************************/

	// is the array a[] sorted?
	private static boolean isSorted(final Comparable[] a)
	{
		return isSorted(a, 0, a.length - 1);
	}

	// is the array sorted from a[lo] to a[hi]
	private static boolean isSorted(final Comparable[] a, final int lo,
			final int hi)
	{
		for (int i = lo + 1; i <= hi; i++)
		{
			if (less(a[i], a[i - 1]))
			{
				return false;
			}
		}
		return true;
	}

	// is the array a[] sorted?
	private static boolean isSorted(final Object[] a, final Comparator c)
	{
		return isSorted(a, c, 0, a.length - 1);
	}

	// is the array sorted from a[lo] to a[hi]
	private static boolean isSorted(final Object[] a, final Comparator c,
			final int lo, final int hi)
	{
		for (int i = lo + 1; i <= hi; i++)
		{
			if (less(c, a[i], a[i - 1]))
			{
				return false;
			}
		}
		return true;
	}

	/***************************************************************************
	 * Helper sorting functions.
	 ***************************************************************************/

	// is v < w ?
	private static boolean less(final Comparable v, final Comparable w)
	{
		return v.compareTo(w) < 0;
	}

	// is v < w ?
	private static boolean less(final Comparator c, final Object v,
			final Object w)
	{
		return c.compare(v, w) < 0;
	}

	/**
	 * Reads in a sequence of strings from standard input; selection sorts them;
	 * and prints them to standard output in ascending order.
	 */
	public static void main(final String[] args)
	{
		final String[] a = StdIn.readAllStrings();
		Selection.sort(a);
		show(a);
	}

	// print array to standard output
	private static void show(final Comparable[] a)
	{
		for (final Comparable element : a)
		{
			StdOut.println(element);
		}
	}

	/**
	 * Rearranges the array in ascending order, using the natural order.
	 * 
	 * @param a
	 *            the array to be sorted
	 */
	public static void sort(final Comparable[] a)
	{
		final int N = a.length;
		for (int i = 0; i < N; i++)
		{
			int min = i;
			for (int j = i + 1; j < N; j++)
			{
				if (less(a[j], a[min]))
				{
					min = j;
				}
			}
			exch(a, i, min);
			assert isSorted(a, 0, i);
		}
		assert isSorted(a);
	}

	/**
	 * Rearranges the array in ascending order, using a comparator.
	 * 
	 * @param a
	 *            the array
	 * @param c
	 *            the comparator specifying the order
	 */
	public static void sort(final Object[] a, final Comparator c)
	{
		final int N = a.length;
		for (int i = 0; i < N; i++)
		{
			int min = i;
			for (int j = i + 1; j < N; j++)
			{
				if (less(c, a[j], a[min]))
				{
					min = j;
				}
			}
			exch(a, i, min);
			assert isSorted(a, c, 0, i);
		}
		assert isSorted(a, c);
	}

	// This class should not be instantiated.
	private Selection()
	{
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
