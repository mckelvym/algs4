/******************************************************************************
 *  Compilation:  javac KMP.java
 *  Execution:    java KMP pattern text
 *  Dependencies: StdOut.java
 *
 *  Reads in two strings, the pattern and the input text, and
 *  searches for the pattern in the input text using the
 *  KMP algorithm.
 *
 *  % java KMP abracadabra abacadabrabracabracadabrabrabracad
 *  text:    abacadabrabracabracadabrabrabracad
 *  pattern:               abracadabra
 *
 *  % java KMP rab abacadabrabracabracadabrabrabracad
 *  text:    abacadabrabracabracadabrabrabracad
 *  pattern:         rab
 *
 *  % java KMP bcara abacadabrabracabracadabrabrabracad
 *  text:    abacadabrabracabracadabrabrabracad
 *  pattern:                                   bcara
 *
 *  % java KMP rabrabracad abacadabrabracabracadabrabrabracad
 *  text:    abacadabrabracabracadabrabrabracad
 *  pattern:                        rabrabracad
 *
 *  % java KMP abacad abacadabrabracabracadabrabrabracad
 *  text:    abacadabrabracabracadabrabrabracad
 *  pattern: abacad
 *
 ******************************************************************************/

package edu.princeton.cs.algs4;

/**
 * The <tt>KMP</tt> class finds the first occurrence of a pattern string in a
 * text string.
 * <p>
 * This implementation uses a version of the Knuth-Morris-Pratt substring search
 * algorithm. The version takes time as space proportional to <em>N</em> +
 * <em>M R</em> in the worst case, where <em>N</em> is the length of the text
 * string, <em>M</em> is the length of the pattern, and <em>R</em> is the
 * alphabet size.
 * <p>
 * For additional documentation, see <a
 * href="http://algs4.cs.princeton.edu/53substring">Section 5.3</a> of
 * <i>Algorithms, 4th Edition</i> by Robert Sedgewick and Kevin Wayne.
 */
public class KMP
{
	/**
	 * Takes a pattern string and an input string as command-line arguments;
	 * searches for the pattern string in the text string; and prints the first
	 * occurrence of the pattern string in the text string.
	 */
	public static void main(final String[] args)
	{
		final String pat = args[0];
		final String txt = args[1];
		final char[] pattern = pat.toCharArray();
		final char[] text = txt.toCharArray();

		final KMP kmp1 = new KMP(pat);
		final int offset1 = kmp1.search(txt);

		final KMP kmp2 = new KMP(pattern, 256);
		final int offset2 = kmp2.search(text);

		// print results
		StdOut.println("text:    " + txt);

		StdOut.print("pattern: ");
		for (int i = 0; i < offset1; i++)
		{
			StdOut.print(" ");
		}
		StdOut.println(pat);

		StdOut.print("pattern: ");
		for (int i = 0; i < offset2; i++)
		{
			StdOut.print(" ");
		}
		StdOut.println(pat);
	}

	private final int[][]	dfa;		// the KMP automoton

	private String			pat;		// or the pattern string
	private char[]			pattern;	// either the character array for the
										// pattern

	private final int		R;			// the radix

	/**
	 * Preprocesses the pattern string.
	 *
	 * @param pattern
	 *            the pattern string
	 * @param R
	 *            the alphabet size
	 */
	public KMP(final char[] pattern, final int R)
	{
		this.R = R;
		this.pattern = new char[pattern.length];
		for (int j = 0; j < pattern.length; j++)
		{
			this.pattern[j] = pattern[j];
		}

		// build DFA from pattern
		final int M = pattern.length;
		dfa = new int[R][M];
		dfa[pattern[0]][0] = 1;
		for (int X = 0, j = 1; j < M; j++)
		{
			for (int c = 0; c < R; c++)
			{
				dfa[c][j] = dfa[c][X]; // Copy mismatch cases.
			}
			dfa[pattern[j]][j] = j + 1; // Set match case.
			X = dfa[pattern[j]][X]; // Update restart state.
		}
	}

	/**
	 * Preprocesses the pattern string.
	 *
	 * @param pat
	 *            the pattern string
	 */
	public KMP(final String pat)
	{
		this.R = 256;
		this.pat = pat;

		// build DFA from pattern
		final int M = pat.length();
		dfa = new int[R][M];
		dfa[pat.charAt(0)][0] = 1;
		for (int X = 0, j = 1; j < M; j++)
		{
			for (int c = 0; c < R; c++)
			{
				dfa[c][j] = dfa[c][X]; // Copy mismatch cases.
			}
			dfa[pat.charAt(j)][j] = j + 1; // Set match case.
			X = dfa[pat.charAt(j)][X]; // Update restart state.
		}
	}

	/**
	 * Returns the index of the first occurrrence of the pattern string in the
	 * text string.
	 *
	 * @param text
	 *            the text string
	 * @return the index of the first occurrence of the pattern string in the
	 *         text string; N if no such match
	 */
	public int search(final char[] text)
	{

		// simulate operation of DFA on text
		final int M = pattern.length;
		final int N = text.length;
		int i, j;
		for (i = 0, j = 0; i < N && j < M; i++)
		{
			j = dfa[text[i]][j];
		}
		if (j == M)
		{
			return i - M; // found
		}
		return N; // not found
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

		// simulate operation of DFA on text
		final int M = pat.length();
		final int N = txt.length();
		int i, j;
		for (i = 0, j = 0; i < N && j < M; i++)
		{
			j = dfa[txt.charAt(i)][j];
		}
		if (j == M)
		{
			return i - M; // found
		}
		return N; // not found
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
