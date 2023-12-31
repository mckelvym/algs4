/******************************************************************************
 *  Compilation:  javac RabinKarp.java
 *  Execution:    java RabinKarp pat txt
 *  Dependencies: StdOut.java
 *
 *  Reads in two strings, the pattern and the input text, and
 *  searches for the pattern in the input text using the
 *  Las Vegas version of the Rabin-Karp algorithm.
 *
 *  % java RabinKarp abracadabra abacadabrabracabracadabrabrabracad
 *  pattern: abracadabra
 *  text:    abacadabrabracabracadabrabrabracad
 *  match:                 abracadabra
 *
 *  % java RabinKarp rab abacadabrabracabracadabrabrabracad
 *  pattern: rab
 *  text:    abacadabrabracabracadabrabrabracad
 *  match:           rab
 *
 *  % java RabinKarp bcara abacadabrabracabracadabrabrabracad
 *  pattern: bcara
 *  text:         abacadabrabracabracadabrabrabracad
 *
 *  %  java RabinKarp rabrabracad abacadabrabracabracadabrabrabracad
 *  text:    abacadabrabracabracadabrabrabracad
 *  pattern:                        rabrabracad
 *
 *  % java RabinKarp abacad abacadabrabracabracadabrabrabracad
 *  text:    abacadabrabracabracadabrabrabracad
 *  pattern: abacad
 *
 ******************************************************************************/

package edu.princeton.cs.algs4;

import java.math.BigInteger;
import java.util.Random;

/**
 * The <tt>RabinKarp</tt> class finds the first occurrence of a pattern string
 * in a text string.
 * <p>
 * This implementation uses the Rabin-Karp algorithm.
 * <p>
 * For additional documentation, see <a
 * href="http://algs4.cs.princeton.edu/53substring">Section 5.3</a> of
 * <i>Algorithms, 4th Edition</i> by Robert Sedgewick and Kevin Wayne.
 */
public class RabinKarp
{
	// a random 31-bit prime
	private static long longRandomPrime()
	{
		final BigInteger prime = BigInteger.probablePrime(31, new Random());
		return prime.longValue();
	}

	/**
	 * Takes a pattern string and an input string as command-line arguments;
	 * searches for the pattern string in the text string; and prints the first
	 * occurrence of the pattern string in the text string.
	 */
	public static void main(final String[] args)
	{
		final String pat = args[0];
		final String txt = args[1];

		final RabinKarp searcher = new RabinKarp(pat);
		final int offset = searcher.search(txt);

		// print results
		StdOut.println("text:    " + txt);

		// from brute force search method 1
		StdOut.print("pattern: ");
		for (int i = 0; i < offset; i++)
		{
			StdOut.print(" ");
		}
		StdOut.println(pat);
	}

	private int		M;			// pattern length
	private String	pat;		// the pattern // needed only for Las Vegas
	private long	patHash;	// pattern hash value
	private long	Q;			// a large prime, small enough to avoid long
								// overflow

	private int		R;			// radix

	private long	RM;		// R^(M-1) % Q

	/**
	 * Preprocesses the pattern string.
	 *
	 * @param pattern
	 *            the pattern string
	 * @param R
	 *            the alphabet size
	 */
	public RabinKarp(final char[] pattern, final int R)
	{
		throw new UnsupportedOperationException("Operation not supported yet");
	}

	/**
	 * Preprocesses the pattern string.
	 *
	 * @param pat
	 *            the pattern string
	 */
	public RabinKarp(final String pat)
	{
		this.pat = pat; // save pattern (needed only for Las Vegas)
		R = 256;
		M = pat.length();
		Q = longRandomPrime();

		// precompute R^(M-1) % Q for use in removing leading digit
		RM = 1;
		for (int i = 1; i <= M - 1; i++)
		{
			RM = R * RM % Q;
		}
		patHash = hash(pat, M);
	}

	// Monte Carlo version: always return true
	private boolean check(final int i)
	{
		return true;
	}

	// Las Vegas version: does pat[] match txt[i..i-M+1] ?
	private boolean check(final String txt, final int i)
	{
		for (int j = 0; j < M; j++)
		{
			if (pat.charAt(j) != txt.charAt(i + j))
			{
				return false;
			}
		}
		return true;
	}

	// Compute hash for key[0..M-1].
	private long hash(final String key, final int M)
	{
		long h = 0;
		for (int j = 0; j < M; j++)
		{
			h = (R * h + key.charAt(j)) % Q;
		}
		return h;
	}

	/**
	 * Returns the index of the first occurrrence of the pattern string in the
	 * text string.
	 *
	 * @param txt
	 *            the text string
	 * @return the index of the first occurrence of the pattern string in the
	 *         text string; N if no such match
	 */
	public int search(final String txt)
	{
		final int N = txt.length();
		if (N < M)
		{
			return N;
		}
		long txtHash = hash(txt, M);

		// check for match at offset 0
		if (patHash == txtHash && check(txt, 0))
		{
			return 0;
		}

		// check for hash match; if hash match, check for exact match
		for (int i = M; i < N; i++)
		{
			// Remove leading digit, add trailing digit, check for match.
			txtHash = (txtHash + Q - RM * txt.charAt(i - M) % Q) % Q;
			txtHash = (txtHash * R + txt.charAt(i)) % Q;

			// match
			final int offset = i - M + 1;
			if (patHash == txtHash && check(txt, offset))
			{
				return offset;
			}
		}

		// no match
		return N;
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
