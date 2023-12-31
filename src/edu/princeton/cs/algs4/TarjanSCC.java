/******************************************************************************
 *  Compilation:  javac TarjanSCC.java
 *  Execution:    Java TarjanSCC V E
 *  Dependencies: Digraph.java Stack.java TransitiveClosure.java StdOut.java
 *
 *  Compute the strongly-connected components of a digraph using
 *  Tarjan's algorithm.
 *
 *  Runs in O(E + V) time.
 *
 *  % java TarjanSCC tinyDG.txt
 *  5 components
 *  1
 *  0 2 3 4 5
 *  9 10 11 12
 *  6 8
 *  7
 *
 ******************************************************************************/

package edu.princeton.cs.algs4;

/**
 * The <tt>TarjanSCC</tt> class represents a data type for determining the
 * strong components in a digraph. The <em>id</em> operation determines in which
 * strong component a given vertex lies; the <em>areStronglyConnected</em>
 * operation determines whether two vertices are in the same strong component;
 * and the <em>count</em> operation determines the number of strong components.
 * 
 * The <em>component identifier</em> of a component is one of the vertices in
 * the strong component: two vertices have the same component identifier if and
 * only if they are in the same strong component.
 * 
 * <p>
 * This implementation uses Tarjan's algorithm. The constructor takes time
 * proportional to <em>V</em> + <em>E</em> (in the worst case), where <em>V</em>
 * is the number of vertices and <em>E</em> is the number of edges. Afterwards,
 * the <em>id</em>, <em>count</em>, and <em>areStronglyConnected</em> operations
 * take constant time. For alternate implementations of the same API, see
 * {@link KosarajuSharirSCC} and {@link GabowSCC}.
 * <p>
 * For additional documentation, see <a
 * href="http://algs4.cs.princeton.edu/42digraph">Section 4.2</a> of
 * <i>Algorithms, 4th Edition</i> by Robert Sedgewick and Kevin Wayne.
 *
 * @author Robert Sedgewick
 * @author Kevin Wayne
 */
public class TarjanSCC
{

	/**
	 * Unit tests the <tt>TarjanSCC</tt> data type.
	 */
	public static void main(final String[] args)
	{
		final In in = new In(args[0]);
		final Digraph G = new Digraph(in);
		final TarjanSCC scc = new TarjanSCC(G);

		// number of connected components
		final int M = scc.count();
		StdOut.println(M + " components");

		// compute list of vertices in each strong component
		final Queue<Integer>[] components = new Queue[M];
		for (int i = 0; i < M; i++)
		{
			components[i] = new Queue<Integer>();
		}
		for (int v = 0; v < G.V(); v++)
		{
			components[scc.id(v)].enqueue(v);
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

	private int						count;	// number of strongly-connected
											// components
	private final int[]				id;	// id[v] = id of strong component
											// containing v
	private final int[]				low;	// low[v] = low number of v
	private final boolean[]			marked; // marked[v] = has v been visited?
	private int						pre;	// preorder number counter

	private final Stack<Integer>	stack;

	/**
	 * Computes the strong components of the digraph <tt>G</tt>.
	 * 
	 * @param G
	 *            the digraph
	 */
	public TarjanSCC(final Digraph G)
	{
		marked = new boolean[G.V()];
		stack = new Stack<Integer>();
		id = new int[G.V()];
		low = new int[G.V()];
		for (int v = 0; v < G.V(); v++)
		{
			if (!marked[v])
			{
				dfs(G, v);
			}
		}

		// check that id[] gives strong components
		assert check(G);
	}

	// does the id[] array contain the strongly connected components?
	private boolean check(final Digraph G)
	{
		final TransitiveClosure tc = new TransitiveClosure(G);
		for (int v = 0; v < G.V(); v++)
		{
			for (int w = 0; w < G.V(); w++)
			{
				if (stronglyConnected(v, w) != (tc.reachable(v, w) && tc
						.reachable(w, v)))
				{
					return false;
				}
			}
		}
		return true;
	}

	/**
	 * Returns the number of strong components.
	 * 
	 * @return the number of strong components
	 */
	public int count()
	{
		return count;
	}

	private void dfs(final Digraph G, final int v)
	{
		marked[v] = true;
		low[v] = pre++;
		int min = low[v];
		stack.push(v);
		for (final int w : G.adj(v))
		{
			if (!marked[w])
			{
				dfs(G, w);
			}
			if (low[w] < min)
			{
				min = low[w];
			}
		}
		if (min < low[v])
		{
			low[v] = min;
			return;
		}
		int w;
		do
		{
			w = stack.pop();
			id[w] = count;
			low[w] = G.V();
		}
		while (w != v);
		count++;
	}

	/**
	 * Returns the component id of the strong component containing vertex
	 * <tt>v</tt>.
	 * 
	 * @param v
	 *            the vertex
	 * @return the component id of the strong component containing vertex
	 *         <tt>v</tt>
	 */
	public int id(final int v)
	{
		return id[v];
	}

	/**
	 * Are vertices <tt>v</tt> and <tt>w</tt> in the same strong component?
	 * 
	 * @param v
	 *            one vertex
	 * @param w
	 *            the other vertex
	 * @return <tt>true</tt> if vertices <tt>v</tt> and <tt>w</tt> are in the
	 *         same strong component, and <tt>false</tt> otherwise
	 */
	public boolean stronglyConnected(final int v, final int w)
	{
		return id[v] == id[w];
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
