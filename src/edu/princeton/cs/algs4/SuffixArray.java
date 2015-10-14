/******************************************************************************
 *  Compilation:  javac SuffixArray.java
 *  Execution:    java SuffixArray < input.txt
 *  Dependencies: StdIn.java StdOut.java
 *
 *  A data type that computes the suffix array of a string.
 *
 *   % java SuffixArray < abra.txt
 *    i ind lcp rnk  select
 *   ---------------------------
 *    0  11   -   0  "!"
 *    1  10   0   1  "A!"
 *    2   7   1   2  "ABRA!"
 *    3   0   4   3  "ABRACADABRA!"
 *    4   3   1   4  "ACADABRA!"
 *    5   5   1   5  "ADABRA!"
 *    6   8   0   6  "BRA!"
 *    7   1   3   7  "BRACADABRA!"
 *    8   4   0   8  "CADABRA!"
 *    9   6   0   9  "DABRA!"
 *   10   9   0  10  "RA!"
 *   11   2   2  11  "RACADABRA!"
 *
 *  See SuffixArrayX.java for an optimized version that uses 3-way
 *  radix quicksort and does not use the nested class Suffix.
 *
 ******************************************************************************/

package edu.princeton.cs.algs4;

import java.util.Arrays;

/**
 * The <tt>SuffixArray</tt> class represents a suffix array of a string of
 * length <em>N</em>. It supports the <em>selecting</em> the <em>i</em>th
 * smallest suffix, getting the <em>index</em> of the <em>i</em>th smallest
 * suffix, computing the length of the <em>longest common prefix</em> between
 * the <em>i</em>th smallest suffix and the <em>i</em>-1st smallest suffix, and
 * determining the <em>rank</em> of a query string (which is the number of
 * suffixes strictly less than the query string).
 * <p>
 * This implementation uses a nested class <tt>Suffix</tt> to represent a suffix
 * of a string (using constant time and space) and <tt>Arrays.sort()</tt> to
 * sort the array of suffixes. For alternate implementations of the same API,
 * see {@link SuffixArrayX}, which is faster in practice (uses 3-way radix
 * quicksort) and uses less memory (does not create <tt>Suffix</tt> objects).
 * The <em>index</em> and <em>length</em> operations takes constant time in the
 * worst case. The <em>lcp</em> operation takes time proportional to the length
 * of the longest common prefix. The <em>select</em> operation takes time
 * proportional to the length of the suffix and should be used primarily for
 * debugging.
 * <p>
 * For additional documentation, see <a
 * href="http://algs4.cs.princeton.edu/63suffix">Section 6.3</a> of
 * <i>Algorithms, 4th Edition</i> by Robert Sedgewick and Kevin Wayne.
 */
public class SuffixArray
{
	private static class Suffix implements Comparable<Suffix>
	{
		private final int		index;
		private final String	text;

		private Suffix(final String text, final int index)
		{
			this.text = text;
			this.index = index;
		}

		private char charAt(final int i)
		{
			return text.charAt(index + i);
		}

		@Override
		public int compareTo(final Suffix that)
		{
			if (this == that)
			{
				return 0; // optimization
			}
			final int N = Math.min(this.length(), that.length());
			for (int i = 0; i < N; i++)
			{
				if (this.charAt(i) < that.charAt(i))
				{
					return -1;
				}
				if (this.charAt(i) > that.charAt(i))
				{
					return +1;
				}
			}
			return this.length() - that.length();
		}

		private int length()
		{
			return text.length() - index;
		}

		@Override
		public String toString()
		{
			return text.substring(index);
		}
	}

	// compare query string to suffix
	private static int compare(final String query, final Suffix suffix)
	{
		final int N = Math.min(query.length(), suffix.length());
		for (int i = 0; i < N; i++)
		{
			if (query.charAt(i) < suffix.charAt(i))
			{
				return -1;
			}
			if (query.charAt(i) > suffix.charAt(i))
			{
				return +1;
			}
		}
		return query.length() - suffix.length();
	}

	// longest common prefix of s and t
	private static int lcp(final Suffix s, final Suffix t)
	{
		final int N = Math.min(s.length(), t.length());
		for (int i = 0; i < N; i++)
		{
			if (s.charAt(i) != t.charAt(i))
			{
				return i;
			}
		}
		return N;
	}

	/**
	 * Unit tests the <tt>SuffixArray</tt> data type.
	 */
	public static void main(final String[] args)
	{
		final String s = StdIn.readAll().replaceAll("\\s+", " ").trim();
		final SuffixArray suffix = new SuffixArray(s);

		// StdOut.println("rank(" + args[0] + ") = " + suffix.rank(args[0]));

		StdOut.println("  i ind lcp rnk select");
		StdOut.println("---------------------------");

		for (int i = 0; i < s.length(); i++)
		{
			final int index = suffix.index(i);
			final String ith = "\""
					+ s.substring(index, Math.min(index + 50, s.length()))
					+ "\"";
			assert s.substring(index).equals(suffix.select(i));
			final int rank = suffix.rank(s.substring(index));
			if (i == 0)
			{
				StdOut.printf("%3d %3d %3s %3d %s\n", i, index, "-", rank, ith);
			}
			else
			{
				final int lcp = suffix.lcp(i);
				StdOut.printf("%3d %3d %3d %3d %s\n", i, index, lcp, rank, ith);
			}
		}
	}

	private final Suffix[]	suffixes;

	/**
	 * Initializes a suffix array for the given <tt>text</tt> string.
	 * 
	 * @param text
	 *            the input string
	 */
	public SuffixArray(final String text)
	{
		final int N = text.length();
		this.suffixes = new Suffix[N];
		for (int i = 0; i < N; i++)
		{
			suffixes[i] = new Suffix(text, i);
		}
		Arrays.sort(suffixes);
	}

	/**
	 * Returns the index into the original string of the <em>i</em>th smallest
	 * suffix. That is, <tt>text.substring(sa.index(i))</tt> is the <em>i</em>th
	 * smallest suffix.
	 * 
	 * @param i
	 *            an integer between 0 and <em>N</em>-1
	 * @return the index into the original string of the <em>i</em>th smallest
	 *         suffix
	 * @throws java.lang.IndexOutOfBoundsException
	 *             unless 0 &le; <em>i</em> &lt; <Em>N</em>
	 */
	public int index(final int i)
	{
		if (i < 0 || i >= suffixes.length)
		{
			throw new IndexOutOfBoundsException();
		}
		return suffixes[i].index;
	}

	/**
	 * Returns the length of the longest common prefix of the <em>i</em>th
	 * smallest suffix and the <em>i</em>-1st smallest suffix.
	 * 
	 * @param i
	 *            an integer between 1 and <em>N</em>-1
	 * @return the length of the longest common prefix of the <em>i</em>th
	 *         smallest suffix and the <em>i</em>-1st smallest suffix.
	 * @throws java.lang.IndexOutOfBoundsException
	 *             unless 1 &le; <em>i</em> &lt; <em>N</em>
	 */
	public int lcp(final int i)
	{
		if (i < 1 || i >= suffixes.length)
		{
			throw new IndexOutOfBoundsException();
		}
		return lcp(suffixes[i], suffixes[i - 1]);
	}

	/**
	 * Returns the length of the input string.
	 * 
	 * @return the length of the input string
	 */
	public int length()
	{
		return suffixes.length;
	}

	/**
	 * Returns the number of suffixes strictly less than the <tt>query</tt>
	 * string. We note that <tt>rank(select(i))</tt> equals <tt>i</tt> for each
	 * <tt>i</tt> between 0 and <em>N</em>-1.
	 * 
	 * @param query
	 *            the query string
	 * @return the number of suffixes strictly less than <tt>query</tt>
	 */
	public int rank(final String query)
	{
		int lo = 0, hi = suffixes.length - 1;
		while (lo <= hi)
		{
			final int mid = lo + (hi - lo) / 2;
			final int cmp = compare(query, suffixes[mid]);
			if (cmp < 0)
			{
				hi = mid - 1;
			}
			else if (cmp > 0)
			{
				lo = mid + 1;
			}
			else
			{
				return mid;
			}
		}
		return lo;
	}

	/**
	 * Returns the <em>i</em>th smallest suffix as a string.
	 * 
	 * @param i
	 *            the index
	 * @return the <em>i</em> smallest suffix as a string
	 * @throws java.lang.IndexOutOfBoundsException
	 *             unless 0 &le; <em>i</em> &lt; <Em>N</em>
	 */
	public String select(final int i)
	{
		if (i < 0 || i >= suffixes.length)
		{
			throw new IndexOutOfBoundsException();
		}
		return suffixes[i].toString();
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
