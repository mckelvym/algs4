/******************************************************************************
 *  Compilation:  javac Merge.java
 *  Execution:    java Merge < input.txt
 *  Dependencies: StdOut.java StdIn.java
 *  Data files:   http://algs4.cs.princeton.edu/22mergesort/tiny.txt
 *                http://algs4.cs.princeton.edu/22mergesort/words3.txt
 *
 *  Sorts a sequence of strings from standard input using mergesort.
 *
 *  % more tiny.txt
 *  S O R T E X A M P L E
 *
 *  % java Merge < tiny.txt
 *  A E E L M O P R S T X                 [ one string per line ]
 *
 *  % more words3.txt
 *  bed bug dad yes zoo ... all bad yet
 *
 *  % java Merge < words3.txt
 *  all bad bed bug dad ... yes yet zoo    [ one string per line ]
 *
 ******************************************************************************/

package edu.princeton.cs.algs4;

/**
 * The <tt>Merge</tt> class provides static methods for sorting an array using
 * mergesort.
 * <p>
 * For additional documentation, see <a
 * href="http://algs4.cs.princeton.edu/22mergesort">Section 2.2</a> of
 * <i>Algorithms, 4th Edition</i> by Robert Sedgewick and Kevin Wayne. For an
 * optimized version, see {@link MergeX}.
 *
 * @author Robert Sedgewick
 * @author Kevin Wayne
 */
public class Merge
{

	// exchange a[i] and a[j]
	private static void exch(final Object[] a, final int i, final int j)
	{
		final Object swap = a[i];
		a[i] = a[j];
		a[j] = swap;
	}

	/**
	 * Returns a permutation that gives the elements in the array in ascending
	 * order.
	 * 
	 * @param a
	 *            the array
	 * @return a permutation <tt>p[]</tt> such that <tt>a[p[0]]</tt>,
	 *         <tt>a[p[1]]</tt>, ..., <tt>a[p[N-1]]</tt> are in ascending order
	 */
	public static int[] indexSort(final Comparable[] a)
	{
		final int N = a.length;
		final int[] index = new int[N];
		for (int i = 0; i < N; i++)
		{
			index[i] = i;
		}

		final int[] aux = new int[N];
		sort(a, index, aux, 0, N - 1);
		return index;
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

	/***************************************************************************
	 * Helper sorting functions.
	 ***************************************************************************/

	// is v < w ?
	private static boolean less(final Comparable v, final Comparable w)
	{
		return v.compareTo(w) < 0;
	}

	/**
	 * Reads in a sequence of strings from standard input; mergesorts them; and
	 * prints them to standard output in ascending order.
	 */
	public static void main(final String[] args)
	{
		final String[] a = StdIn.readAllStrings();
		Merge.sort(a);
		show(a);
	}

	// stably merge a[lo .. mid] with a[mid+1 ..hi] using aux[lo .. hi]
	private static void merge(final Comparable[] a, final Comparable[] aux,
			final int lo, final int mid, final int hi)
	{
		// precondition: a[lo .. mid] and a[mid+1 .. hi] are sorted subarrays
		assert isSorted(a, lo, mid);
		assert isSorted(a, mid + 1, hi);

		// copy to aux[]
		for (int k = lo; k <= hi; k++)
		{
			aux[k] = a[k];
		}

		// merge back to a[]
		int i = lo, j = mid + 1;
		for (int k = lo; k <= hi; k++)
		{
			if (i > mid)
			{
				a[k] = aux[j++]; // this copying is unnecessary
			}
			else if (j > hi)
			{
				a[k] = aux[i++];
			}
			else if (less(aux[j], aux[i]))
			{
				a[k] = aux[j++];
			}
			else
			{
				a[k] = aux[i++];
			}
		}

		// postcondition: a[lo .. hi] is sorted
		assert isSorted(a, lo, hi);
	}

	/***************************************************************************
	 * Index mergesort.
	 ***************************************************************************/
	// stably merge a[lo .. mid] with a[mid+1 .. hi] using aux[lo .. hi]
	private static void merge(final Comparable[] a, final int[] index,
			final int[] aux, final int lo, final int mid, final int hi)
	{

		// copy to aux[]
		for (int k = lo; k <= hi; k++)
		{
			aux[k] = index[k];
		}

		// merge back to a[]
		int i = lo, j = mid + 1;
		for (int k = lo; k <= hi; k++)
		{
			if (i > mid)
			{
				index[k] = aux[j++];
			}
			else if (j > hi)
			{
				index[k] = aux[i++];
			}
			else if (less(a[aux[j]], a[aux[i]]))
			{
				index[k] = aux[j++];
			}
			else
			{
				index[k] = aux[i++];
			}
		}
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
		final Comparable[] aux = new Comparable[a.length];
		sort(a, aux, 0, a.length - 1);
		assert isSorted(a);
	}

	// mergesort a[lo..hi] using auxiliary array aux[lo..hi]
	private static void sort(final Comparable[] a, final Comparable[] aux,
			final int lo, final int hi)
	{
		if (hi <= lo)
		{
			return;
		}
		final int mid = lo + (hi - lo) / 2;
		sort(a, aux, lo, mid);
		sort(a, aux, mid + 1, hi);
		merge(a, aux, lo, mid, hi);
	}

	// mergesort a[lo..hi] using auxiliary array aux[lo..hi]
	private static void sort(final Comparable[] a, final int[] index,
			final int[] aux, final int lo, final int hi)
	{
		if (hi <= lo)
		{
			return;
		}
		final int mid = lo + (hi - lo) / 2;
		sort(a, index, aux, lo, mid);
		sort(a, index, aux, mid + 1, hi);
		merge(a, index, aux, lo, mid, hi);
	}

	// This class should not be instantiated.
	private Merge()
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
