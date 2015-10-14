/******************************************************************************
 *  Compilation: javac MSD.java
 *  Execution:   java MSD < input.txt
 *  Dependencies: StdIn.java StdOut.java
 *
 *  Sort an array of strings or integers using MSD radix sort.
 *
 *  % java MSD < shells.txt
 *  are
 *  by
 *  sea
 *  seashells
 *  seashells
 *  sells
 *  sells
 *  she
 *  she
 *  shells
 *  shore
 *  surely
 *  the
 *  the
 *
 ******************************************************************************/

package edu.princeton.cs.algs4;

/**
 * The <tt>MSD</tt> class provides static methods for sorting an array of
 * extended ASCII strings or integers using MSD radix sort.
 * <p>
 * For additional documentation, see <a
 * href="http://algs4.cs.princeton.edu/51radix">Section 5.1</a> of
 * <i>Algorithms, 4th Edition</i> by Robert Sedgewick and Kevin Wayne.
 *
 * @author Robert Sedgewick
 * @author Kevin Wayne
 */
public class MSD
{
	private static final int	BITS_PER_BYTE	= 8;
	private static final int	BITS_PER_INT	= 32;	// each Java int is 32
														// bits
	private static final int	CUTOFF			= 15;	// cutoff to insertion
														// sort
	private static final int	R				= 256;	// extended ASCII
														// alphabet size

	// return dth character of s, -1 if d = length of string
	private static int charAt(final String s, final int d)
	{
		assert d >= 0 && d <= s.length();
		if (d == s.length())
		{
			return -1;
		}
		return s.charAt(d);
	}

	// exchange a[i] and a[j]
	private static void exch(final int[] a, final int i, final int j)
	{
		final int temp = a[i];
		a[i] = a[j];
		a[j] = temp;
	}

	// exchange a[i] and a[j]
	private static void exch(final String[] a, final int i, final int j)
	{
		final String temp = a[i];
		a[i] = a[j];
		a[j] = temp;
	}

	// insertion sort a[lo..hi], starting at dth character
	private static void insertion(final int[] a, final int lo, final int hi,
			final int d)
	{
		for (int i = lo; i <= hi; i++)
		{
			for (int j = i; j > lo && a[j] < a[j - 1]; j--)
			{
				exch(a, j, j - 1);
			}
		}
	}

	// insertion sort a[lo..hi], starting at dth character
	private static void insertion(final String[] a, final int lo, final int hi,
			final int d)
	{
		for (int i = lo; i <= hi; i++)
		{
			for (int j = i; j > lo && less(a[j], a[j - 1], d); j--)
			{
				exch(a, j, j - 1);
			}
		}
	}

	// is v less than w, starting at character d
	private static boolean less(final String v, final String w, final int d)
	{
		// assert v.substring(0, d).equals(w.substring(0, d));
		for (int i = d; i < Math.min(v.length(), w.length()); i++)
		{
			if (v.charAt(i) < w.charAt(i))
			{
				return true;
			}
			if (v.charAt(i) > w.charAt(i))
			{
				return false;
			}
		}
		return v.length() < w.length();
	}

	/**
	 * Reads in a sequence of extended ASCII strings from standard input; MSD
	 * radix sorts them; and prints them to standard output in ascending order.
	 */
	public static void main(final String[] args)
	{
		final String[] a = StdIn.readAllStrings();
		final int N = a.length;
		sort(a);
		for (int i = 0; i < N; i++)
		{
			StdOut.println(a[i]);
		}
	}

	/**
	 * Rearranges the array of 32-bit integers in ascending order. Currently
	 * assumes that the integers are nonnegative.
	 *
	 * @param a
	 *            the array to be sorted
	 */
	public static void sort(final int[] a)
	{
		final int N = a.length;
		final int[] aux = new int[N];
		sort(a, 0, N - 1, 0, aux);
	}

	// MSD sort from a[lo] to a[hi], starting at the dth byte
	private static void sort(final int[] a, final int lo, final int hi,
			final int d, final int[] aux)
	{

		// cutoff to insertion sort for small subarrays
		if (hi <= lo + CUTOFF)
		{
			insertion(a, lo, hi, d);
			return;
		}

		// compute frequency counts (need R = 256)
		final int[] count = new int[R + 1];
		final int mask = R - 1; // 0xFF;
		final int shift = BITS_PER_INT - BITS_PER_BYTE * d - BITS_PER_BYTE;
		for (int i = lo; i <= hi; i++)
		{
			final int c = a[i] >> shift & mask;
			count[c + 1]++;
		}

		// transform counts to indicies
		for (int r = 0; r < R; r++)
		{
			count[r + 1] += count[r];
		}

		/*************
		 * BUGGGY CODE. // for most significant byte, 0x80-0xFF comes before
		 * 0x00-0x7F if (d == 0) { int shift1 = count[R] - count[R/2]; int
		 * shift2 = count[R/2]; for (int r = 0; r < R/2; r++) count[r] +=
		 * shift1; for (int r = R/2; r < R; r++) count[r] -= shift2; }
		 ************************************/
		// distribute
		for (int i = lo; i <= hi; i++)
		{
			final int c = a[i] >> shift & mask;
			aux[count[c]++] = a[i];
		}

		// copy back
		for (int i = lo; i <= hi; i++)
		{
			a[i] = aux[i - lo];
		}

		// no more bits
		if (d == 4)
		{
			return;
		}

		// recursively sort for each character
		if (count[0] > 0)
		{
			sort(a, lo, lo + count[0] - 1, d + 1, aux);
		}
		for (int r = 0; r < R; r++)
		{
			if (count[r + 1] > count[r])
			{
				sort(a, lo + count[r], lo + count[r + 1] - 1, d + 1, aux);
			}
		}
	}

	/**
	 * Rearranges the array of extended ASCII strings in ascending order.
	 *
	 * @param a
	 *            the array to be sorted
	 */
	public static void sort(final String[] a)
	{
		final int N = a.length;
		final String[] aux = new String[N];
		sort(a, 0, N - 1, 0, aux);
	}

	// sort from a[lo] to a[hi], starting at the dth character
	private static void sort(final String[] a, final int lo, final int hi,
			final int d, final String[] aux)
	{

		// cutoff to insertion sort for small subarrays
		if (hi <= lo + CUTOFF)
		{
			insertion(a, lo, hi, d);
			return;
		}

		// compute frequency counts
		final int[] count = new int[R + 2];
		for (int i = lo; i <= hi; i++)
		{
			final int c = charAt(a[i], d);
			count[c + 2]++;
		}

		// transform counts to indicies
		for (int r = 0; r < R + 1; r++)
		{
			count[r + 1] += count[r];
		}

		// distribute
		for (int i = lo; i <= hi; i++)
		{
			final int c = charAt(a[i], d);
			aux[count[c + 1]++] = a[i];
		}

		// copy back
		for (int i = lo; i <= hi; i++)
		{
			a[i] = aux[i - lo];
		}

		// recursively sort for each character (excludes sentinel -1)
		for (int r = 0; r < R; r++)
		{
			sort(a, lo + count[r], lo + count[r + 1] - 1, d + 1, aux);
		}
	}

	// do not instantiate
	private MSD()
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
