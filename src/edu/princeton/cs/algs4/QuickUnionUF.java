/******************************************************************************
 *  Compilation:  javac QuickUnionUF.java
 *  Execution:  java QuickUnionUF < input.txt
 *  Dependencies: StdIn.java StdOut.java
 *
 *  Quick-union algorithm.
 *
 ******************************************************************************/

package edu.princeton.cs.algs4;

/**
 * The <tt>QuickUnionUF</tt> class represents a union-find data structure. It
 * supports the <em>union</em> and <em>find</em> operations, along with methods
 * for determinig whether two objects are in the same component and the total
 * number of components.
 * <p>
 * This implementation uses quick union. Initializing a data structure with
 * <em>N</em> objects takes linear time. Afterwards, <em>union</em>,
 * <em>find</em>, and <em>connected</em> take time linear time (in the worst
 * case) and <em>count</em> takes constant time.
 * <p>
 * For additional documentation, see <a
 * href="http://algs4.cs.princeton.edu/15uf">Section 1.5</a> of <i>Algorithms,
 * 4th Edition</i> by Robert Sedgewick and Kevin Wayne.
 * 
 * @author Robert Sedgewick
 * @author Kevin Wayne
 */
public class QuickUnionUF
{
	/**
	 * Reads in a sequence of pairs of integers (between 0 and N-1) from
	 * standard input, where each integer represents some object; if the objects
	 * are in different components, merge the two components and print the pair
	 * to standard output.
	 */
	public static void main(final String[] args)
	{
		final int N = StdIn.readInt();
		final QuickUnionUF uf = new QuickUnionUF(N);
		while (!StdIn.isEmpty())
		{
			final int p = StdIn.readInt();
			final int q = StdIn.readInt();
			if (uf.connected(p, q))
			{
				continue;
			}
			uf.union(p, q);
			StdOut.println(p + " " + q);
		}
		StdOut.println(uf.count() + " components");
	}

	private int			count;	// number of components

	private final int[]	parent; // parent[i] = parent of i

	/**
	 * Initializes an empty union-find data structure with N isolated components
	 * 0 through N-1.
	 * 
	 * @param N
	 *            the number of objects
	 * @throws java.lang.IllegalArgumentException
	 *             if N < 0
	 */
	public QuickUnionUF(final int N)
	{
		parent = new int[N];
		count = N;
		for (int i = 0; i < N; i++)
		{
			parent[i] = i;
		}
	}

	/**
	 * Are the two sites <tt>p</tt> and <tt>q</tt> in the same component?
	 * 
	 * @param p
	 *            the integer representing one site
	 * @param q
	 *            the integer representing the other site
	 * @return <tt>true</tt> if the sites <tt>p</tt> and <tt>q</tt> are in the
	 *         same component, and <tt>false</tt> otherwise
	 * @throws java.lang.IndexOutOfBoundsException
	 *             unless both 0 <= p < N and 0 <= q < N
	 */
	public boolean connected(final int p, final int q)
	{
		return find(p) == find(q);
	}

	/**
	 * Returns the number of components.
	 * 
	 * @return the number of components (between 1 and N)
	 */
	public int count()
	{
		return count;
	}

	/**
	 * Returns the component identifier for the component containing site
	 * <tt>p</tt>.
	 * 
	 * @param p
	 *            the integer representing one site
	 * @return the component identifier for the component containing site
	 *         <tt>p</tt>
	 * @throws java.lang.IndexOutOfBoundsException
	 *             unless 0 <= p < N
	 */
	public int find(int p)
	{
		validate(p);
		while (p != parent[p])
		{
			p = parent[p];
		}
		return p;
	}

	/**
	 * Merges the component containing site<tt>p</tt> with the component
	 * containing site <tt>q</tt>.
	 * 
	 * @param p
	 *            the integer representing one site
	 * @param q
	 *            the integer representing the other site
	 * @throws java.lang.IndexOutOfBoundsException
	 *             unless both 0 <= p < N and 0 <= q < N
	 */
	public void union(final int p, final int q)
	{
		final int rootP = find(p);
		final int rootQ = find(q);
		if (rootP == rootQ)
		{
			return;
		}
		parent[rootP] = rootQ;
		count--;
	}

	// validate that p is a valid index
	private void validate(final int p)
	{
		final int N = parent.length;
		if (p < 0 || p >= N)
		{
			throw new IndexOutOfBoundsException("index " + p
					+ " is not between 0 and " + N);
		}
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
