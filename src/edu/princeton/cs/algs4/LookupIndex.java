/******************************************************************************
 *  Compilation:  javac LookupIndex.java
 *  Execution:    java LookupIndex movies.txt "/"
 *  Dependencies: ST.java Queue.java In.java StdIn.java StdOut.java
 *  Data files:   http://algs4.cs.princeton.edu/35applications/aminoI.csv
 *                http://algs4.cs.princeton.edu/35applications/movies.txt
 *
 *  % java LookupIndex aminoI.csv ","
 *  Serine
 *    TCT
 *    TCA
 *    TCG
 *    AGT
 *    AGC
 *  TCG
 *    Serine
 *
 *  % java LookupIndex movies.txt "/"
 *  Bacon, Kevin
 *    Animal House (1978)
 *    Apollo 13 (1995)
 *    Beauty Shop (2005)
 *    Diner (1982)
 *    Few Good Men, A (1992)
 *    Flatliners (1990)
 *    Footloose (1984)
 *    Friday the 13th (1980)
 *    ...
 *  Tin Men (1987)
 *    DeBoy, David
 *    Blumenfeld, Alan
 *    ...
 *
 ******************************************************************************/

package edu.princeton.cs.algs4;

/**
 * The <tt>LookupIndex</tt> class provides a data-driven client for reading in a
 * key-value pairs from a file; then, printing the values corresponding to the
 * keys found on standard input. Keys are strings; values are lists of strings.
 * The separating delimiter is taken as a command-line argument. This client is
 * sometimes known as an <em>inverted index</em>.
 * <p>
 * For additional documentation, see <a
 * href="http://algs4.cs.princeton.edu/35applications">Section 3.5</a> of
 * <i>Algorithms, 4th Edition</i> by Robert Sedgewick and Kevin Wayne.
 * 
 * @author Robert Sedgewick
 * @author Kevin Wayne
 */
public class LookupIndex
{

	public static void main(final String[] args)
	{
		final String filename = args[0];
		final String separator = args[1];
		final In in = new In(filename);

		final ST<String, Queue<String>> st = new ST<String, Queue<String>>();
		final ST<String, Queue<String>> ts = new ST<String, Queue<String>>();

		while (in.hasNextLine())
		{
			final String line = in.readLine();
			final String[] fields = line.split(separator);
			final String key = fields[0];
			for (int i = 1; i < fields.length; i++)
			{
				final String val = fields[i];
				if (!st.contains(key))
				{
					st.put(key, new Queue<String>());
				}
				if (!ts.contains(val))
				{
					ts.put(val, new Queue<String>());
				}
				st.get(key).enqueue(val);
				ts.get(val).enqueue(key);
			}
		}

		StdOut.println("Done indexing");

		// read queries from standard input, one per line
		while (!StdIn.isEmpty())
		{
			final String query = StdIn.readLine();
			if (st.contains(query))
			{
				for (final String vals : st.get(query))
				{
					StdOut.println("  " + vals);
				}
			}
			if (ts.contains(query))
			{
				for (final String keys : ts.get(query))
				{
					StdOut.println("  " + keys);
				}
			}
		}

	}

	// Do not instantiate.
	private LookupIndex()
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
