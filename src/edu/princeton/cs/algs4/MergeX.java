/******************************************************************************
 *  Compilation:  javac MergeX.java
 *  Execution:    java MergeX < input.txt
 *  Dependencies: StdOut.java StdIn.java
 *  Data files:   http://algs4.cs.princeton.edu/22mergesort/tiny.txt
 *                http://algs4.cs.princeton.edu/22mergesort/words3.txt
 *
 *  Sorts a sequence of strings from standard input using an
 *  optimized version of mergesort.
 *
 *  % more tiny.txt
 *  S O R T E X A M P L E
 *
 *  % java MergeX < tiny.txt
 *  A E E L M O P R S T X                 [ one string per line ]
 *
 *  % more words3.txt
 *  bed bug dad yes zoo ... all bad yet
 *
 *  % java MergeX < words3.txt
 *  all bad bed bug dad ... yes yet zoo    [ one string per line ]
 *
 ******************************************************************************/

package edu.princeton.cs.algs4;

import java.util.Comparator;

/**
 * The <tt>MergeX</tt> class provides static methods for sorting an array using
 * an optimized version of mergesort.
 * <p>
 * For additional documentation, see <a
 * href="http://algs4.cs.princeton.edu/22mergesort">Section 2.2</a> of
 * <i>Algorithms, 4th Edition</i> by Robert Sedgewick and Kevin Wayne.
 *
 * @author Robert Sedgewick
 * @author Kevin Wayne
 */
public class MergeX
{
	private static final int	CUTOFF	= 7;	// cutoff to insertion sort

	/*******************************************************************
	 * Utility methods.
	 *******************************************************************/

	// exchange a[i] and a[j]
	private static void exch(final Object[] a, final int i, final int j)
	{
		final Object swap = a[i];
		a[i] = a[j];
		a[j] = swap;
	}

	// sort from a[lo] to a[hi] using insertion sort
	private static void insertionSort(final Comparable[] a, final int lo,
			final int hi)
	{
		for (int i = lo; i <= hi; i++)
		{
			for (int j = i; j > lo && less(a[j], a[j - 1]); j--)
			{
				exch(a, j, j - 1);
			}
		}
	}

	// sort from a[lo] to a[hi] using insertion sort
	private static void insertionSort(final Object[] a, final int lo,
			final int hi, final Comparator comparator)
	{
		for (int i = lo; i <= hi; i++)
		{
			for (int j = i; j > lo && less(a[j], a[j - 1], comparator); j--)
			{
				exch(a, j, j - 1);
			}
		}
	}

	/***************************************************************************
	 * Check if array is sorted - useful for debugging.
	 ***************************************************************************/
	private static boolean isSorted(final Comparable[] a)
	{
		return isSorted(a, 0, a.length - 1);
	}

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

	private static boolean isSorted(final Object[] a,
			final Comparator comparator)
	{
		return isSorted(a, 0, a.length - 1, comparator);
	}

	private static boolean isSorted(final Object[] a, final int lo,
			final int hi, final Comparator comparator)
	{
		for (int i = lo + 1; i <= hi; i++)
		{
			if (less(a[i], a[i - 1], comparator))
			{
				return false;
			}
		}
		return true;
	}

	// is a[i] < a[j]?
	private static boolean less(final Comparable a, final Comparable b)
	{
		return a.compareTo(b) < 0;
	}

	/*******************************************************************
	 * Version that takes Comparator as argument.
	 *******************************************************************/

	// is a[i] < a[j]?
	private static boolean less(final Object a, final Object b,
			final Comparator comparator)
	{
		return comparator.compare(a, b) < 0;
	}

	/**
	 * Reads in a sequence of strings from standard input; mergesorts them
	 * (using an optimized version of mergesort); and prints them to standard
	 * output in ascending order.
	 */
	public static void main(final String[] args)
	{
		final String[] a = StdIn.readAllStrings();
		MergeX.sort(a);
		show(a);
	}

	private static void merge(final Comparable[] src, final Comparable[] dst,
			final int lo, final int mid, final int hi)
	{

		// precondition: src[lo .. mid] and src[mid+1 .. hi] are sorted
		// subarrays
		assert isSorted(src, lo, mid);
		assert isSorted(src, mid + 1, hi);

		int i = lo, j = mid + 1;
		for (int k = lo; k <= hi; k++)
		{
			if (i > mid)
			{
				dst[k] = src[j++];
			}
			else if (j > hi)
			{
				dst[k] = src[i++];
			}
			else if (less(src[j], src[i]))
			{
				dst[k] = src[j++]; // to ensure stability
			}
			else
			{
				dst[k] = src[i++];
			}
		}

		// postcondition: dst[lo .. hi] is sorted subarray
		assert isSorted(dst, lo, hi);
	}

	private static void merge(final Object[] src, final Object[] dst,
			final int lo, final int mid, final int hi,
			final Comparator comparator)
	{

		// precondition: src[lo .. mid] and src[mid+1 .. hi] are sorted
		// subarrays
		assert isSorted(src, lo, mid, comparator);
		assert isSorted(src, mid + 1, hi, comparator);

		int i = lo, j = mid + 1;
		for (int k = lo; k <= hi; k++)
		{
			if (i > mid)
			{
				dst[k] = src[j++];
			}
			else if (j > hi)
			{
				dst[k] = src[i++];
			}
			else if (less(src[j], src[i], comparator))
			{
				dst[k] = src[j++];
			}
			else
			{
				dst[k] = src[i++];
			}
		}

		// postcondition: dst[lo .. hi] is sorted subarray
		assert isSorted(dst, lo, hi, comparator);
	}

	// print array to standard output
	private static void show(final Object[] a)
	{
		for (final Object element : a)
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
		final Comparable[] aux = a.clone();
		sort(aux, a, 0, a.length - 1);
		assert isSorted(a);
	}

	private static void sort(final Comparable[] src, final Comparable[] dst,
			final int lo, final int hi)
	{
		// if (hi <= lo) return;
		if (hi <= lo + CUTOFF)
		{
			insertionSort(dst, lo, hi);
			return;
		}
		final int mid = lo + (hi - lo) / 2;
		sort(dst, src, lo, mid);
		sort(dst, src, mid + 1, hi);

		// if (!less(src[mid+1], src[mid])) {
		// for (int i = lo; i <= hi; i++) dst[i] = src[i];
		// return;
		// }

		// using System.arraycopy() is a bit faster than the above loop
		if (!less(src[mid + 1], src[mid]))
		{
			System.arraycopy(src, lo, dst, lo, hi - lo + 1);
			return;
		}

		merge(src, dst, lo, mid, hi);
	}

	/**
	 * Rearranges the array in ascending order, using the provided order.
	 * 
	 * @param a
	 *            the array to be sorted
	 */
	public static void sort(final Object[] a, final Comparator comparator)
	{
		final Object[] aux = a.clone();
		sort(aux, a, 0, a.length - 1, comparator);
		assert isSorted(a, comparator);
	}

	private static void sort(final Object[] src, final Object[] dst,
			final int lo, final int hi, final Comparator comparator)
	{
		// if (hi <= lo) return;
		if (hi <= lo + CUTOFF)
		{
			insertionSort(dst, lo, hi, comparator);
			return;
		}
		final int mid = lo + (hi - lo) / 2;
		sort(dst, src, lo, mid, comparator);
		sort(dst, src, mid + 1, hi, comparator);

		// using System.arraycopy() is a bit faster than the above loop
		if (!less(src[mid + 1], src[mid], comparator))
		{
			System.arraycopy(src, lo, dst, lo, hi - lo + 1);
			return;
		}

		merge(src, dst, lo, mid, hi, comparator);
	}

	// This class should not be instantiated.
	private MergeX()
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
