/******************************************************************************
 *  Compilation:  javac RandomSeq.java
 *  Execution:    java RandomSeq N lo hi
 *  Dependencies: StdOut.java
 *
 *  Prints N numbers between lo and hi.
 *
 *  % java RandomSeq 5 100.0 200.0
 *  123.43
 *  153.13
 *  144.38
 *  155.18
 *  104.02
 *
 ******************************************************************************/

package edu.princeton.cs.algs4;

/**
 * The <tt>RandomSeq</tt> class is a client that prints out a pseudorandom
 * sequence of real numbers in a given range.
 * <p>
 * For additional documentation, see <a
 * href="http://algs4.cs.princeton.edu/11model">Section 1.1</a> of
 * <i>Algorithms, 4th Edition</i> by Robert Sedgewick and Kevin Wayne.
 *
 * @author Robert Sedgewick
 * @author Kevin Wayne
 */
public class RandomSeq
{

	/**
	 * Reads in two command-line arguments lo and hi and prints N uniformly
	 * random real numbers in [lo, hi) to standard output.
	 */
	public static void main(final String[] args)
	{

		// command-line arguments
		final int N = Integer.parseInt(args[0]);

		// for backward compatibility with Intro to Programming in Java version
		// of RandomSeq
		if (args.length == 1)
		{
			// generate and print N numbers between 0.0 and 1.0
			for (int i = 0; i < N; i++)
			{
				final double x = StdRandom.uniform();
				StdOut.println(x);
			}
		}

		else if (args.length == 3)
		{
			final double lo = Double.parseDouble(args[1]);
			final double hi = Double.parseDouble(args[2]);

			// generate and print N numbers between lo and hi
			for (int i = 0; i < N; i++)
			{
				final double x = StdRandom.uniform(lo, hi);
				StdOut.printf("%.2f\n", x);
			}
		}

		else
		{
			throw new IllegalArgumentException("Invalid number of arguments");
		}
	}

	// this class should not be instantiated
	private RandomSeq()
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
