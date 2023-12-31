/******************************************************************************
 *  Compilation:  javac CC.java
 *  Execution:    java CC filename.txt
 *  Dependencies: Graph.java StdOut.java Queue.java
 *  Data files:   http://algs4.cs.princeton.edu/41graph/tinyG.txt
 *
 *  Compute connected components using depth first search.
 *  Runs in O(E + V) time.
 *
 *  % java CC tinyG.txt
 *  3 components
 *  0 1 2 3 4 5 6
 *  7 8
 *  9 10 11 12
 *
 *  % java CC mediumG.txt
 *  1 components
 *  0 1 2 3 4 5 6 7 8 9 10 ...
 *
 *  % java -Xss50m CC largeG.txt
 *  1 components
 *  0 1 2 3 4 5 6 7 8 9 10 ...
 *
 ******************************************************************************/

package edu.princeton.cs.algs4;

/**
 * The <tt>CC</tt> class represents a data type for determining the connected
 * components in an undirected graph. The <em>id</em> operation determines in
 * which connected component a given vertex lies; the <em>connected</em>
 * operation determines whether two vertices are in the same connected
 * component; the <em>count</em> operation determines the number of connected
 * components; and the <em>size</em> operation determines the number of vertices
 * in the connect component containing a given vertex.
 * 
 * The <em>component identifier</em> of a connected component is one of the
 * vertices in the connected component: two vertices have the same component
 * identifier if and only if they are in the same connected component.
 * 
 * <p>
 * This implementation uses depth-first search. The constructor takes time
 * proportional to <em>V</em> + <em>E</em> (in the worst case), where <em>V</em>
 * is the number of vertices and <em>E</em> is the number of edges. Afterwards,
 * the <em>id</em>, <em>count</em>, <em>connected</em>, and <em>size</em>
 * operations take constant time.
 * <p>
 * For additional documentation, see <a
 * href="http://algs4.cs.princeton.edu/41graph">Section 4.1</a> of
 * <i>Algorithms, 4th Edition</i> by Robert Sedgewick and Kevin Wayne.
 *
 * @author Robert Sedgewick
 * @author Kevin Wayne
 */
public class CC
{
	/**
	 * Unit tests the <tt>CC</tt> data type.
	 */
	public static void main(final String[] args)
	{
		final In in = new In(args[0]);
		final Graph G = new Graph(in);
		final CC cc = new CC(G);

		// number of connected components
		final int M = cc.count();
		StdOut.println(M + " components");

		// compute list of vertices in each connected component
		final Queue<Integer>[] components = new Queue[M];
		for (int i = 0; i < M; i++)
		{
			components[i] = new Queue<Integer>();
		}
		for (int v = 0; v < G.V(); v++)
		{
			components[cc.id(v)].enqueue(v);
		}

		// print results
		for (int i = 0; i < M; i++)
		{
			for (final int v : components[i])
			{
				StdOut.print(v + " ");
			}
			StdOut.println();
		}
	}

	private int				count;	// number of connected components
	private final int[]		id;	// id[v] = id of connected component
									// containing v
	private final boolean[]	marked; // marked[v] = has vertex v been marked?

	private final int[]		size;	// size[id] = number of vertices in given
									// component

	/**
	 * Computes the connected components of the undirected graph <tt>G</tt>.
	 *
	 * @param G
	 *            the undirected graph
	 */
	public CC(final Graph G)
	{
		marked = new boolean[G.V()];
		id = new int[G.V()];
		size = new int[G.V()];
		for (int v = 0; v < G.V(); v++)
		{
			if (!marked[v])
			{
				dfs(G, v);
				count++;
			}
		}
	}

	/**
	 * Returns true if vertices <tt>v</tt> and <tt>w</tt> are in the same
	 * connected component.
	 *
	 * @param v
	 *            one vertex
	 * @param w
	 *            the other vertex
	 * @return <tt>true</tt> if vertices <tt>v</tt> and <tt>w</tt> are in the
	 *         same connected component; <tt>false</tt> otherwise
	 * @deprecated Replaced by {@link #connected(int, int)}.
	 */
	@Deprecated
	public boolean areConnected(final int v, final int w)
	{
		return id(v) == id(w);
	}

	/**
	 * Returns true if vertices <tt>v</tt> and <tt>w</tt> are in the same
	 * connected component.
	 *
	 * @param v
	 *            one vertex
	 * @param w
	 *            the other vertex
	 * @return <tt>true</tt> if vertices <tt>v</tt> and <tt>w</tt> are in the
	 *         same connected component; <tt>false</tt> otherwise
	 */
	public boolean connected(final int v, final int w)
	{
		return id(v) == id(w);
	}

	/**
	 * Returns the number of connected components in the graph <tt>G</tt>.
	 *
	 * @return the number of connected components in the graph <tt>G</tt>
	 */
	public int count()
	{
		return count;
	}

	// depth-first search
	private void dfs(final Graph G, final int v)
	{
		marked[v] = true;
		id[v] = count;
		size[count]++;
		for (final int w : G.adj(v))
		{
			if (!marked[w])
			{
				dfs(G, w);
			}
		}
	}

	/**
	 * Returns the component id of the connected component containing vertex
	 * <tt>v</tt>.
	 *
	 * @param v
	 *            the vertex
	 * @return the component id of the connected component containing vertex
	 *         <tt>v</tt>
	 */
	public int id(final int v)
	{
		return id[v];
	}

	/**
	 * Returns the number of vertices in the connected component containing
	 * vertex <tt>v</tt>.
	 *
	 * @param v
	 *            the vertex
	 * @return the number of vertices in the connected component containing
	 *         vertex <tt>v</tt>
	 */
	public int size(final int v)
	{
		return size[id[v]];
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
