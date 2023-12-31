/******************************************************************************
 *  Compilation:  javac Bipartite.java
 *  Execution:    java  Bipartite V E F
 *  Dependencies: Graph.java
 *
 *  Given a graph, find either (i) a bipartition or (ii) an odd-length cycle.
 *  Runs in O(E + V) time.
 *
 *
 ******************************************************************************/

package edu.princeton.cs.algs4;

/**
 * The <tt>Bipartite</tt> class represents a data type for determining whether
 * an undirected graph is bipartite or whether it has an odd-length cycle. The
 * <em>isBipartite</em> operation determines whether the graph is bipartite. If
 * so, the <em>color</em> operation determines a bipartition; if not, the
 * <em>oddCycle</em> operation determines a cycle with an odd number of edges.
 * <p>
 * This implementation uses depth-first search. The constructor takes time
 * proportional to <em>V</em> + <em>E</em> (in the worst case), where <em>V</em>
 * is the number of vertices and <em>E</em> is the number of edges. Afterwards,
 * the <em>isBipartite</em> and <em>color</em> operations take constant time;
 * the <em>oddCycle</em> operation takes time proportional to the length of the
 * cycle.
 * <p>
 * For additional documentation, see <a
 * href="http://algs4.cs.princeton.edu/41graph">Section 4.1</a> of
 * <i>Algorithms, 4th Edition</i> by Robert Sedgewick and Kevin Wayne.
 *
 * @author Robert Sedgewick
 * @author Kevin Wayne
 */
public class Bipartite
{
	/**
	 * Unit tests the <tt>Bipartite</tt> data type.
	 */
	public static void main(final String[] args)
	{
		// create random bipartite graph with V vertices and E edges; then add F
		// random edges
		final int V = Integer.parseInt(args[0]);
		final int E = Integer.parseInt(args[1]);
		final int F = Integer.parseInt(args[2]);

		final Graph G = new Graph(V);
		final int[] vertices = new int[V];
		for (int i = 0; i < V; i++)
		{
			vertices[i] = i;
		}
		StdRandom.shuffle(vertices);
		for (int i = 0; i < E; i++)
		{
			final int v = StdRandom.uniform(V / 2);
			final int w = StdRandom.uniform(V / 2);
			G.addEdge(vertices[v], vertices[V / 2 + w]);
		}

		// add F extra edges
		for (int i = 0; i < F; i++)
		{
			final int v = StdRandom.uniform(V);
			final int w = StdRandom.uniform(V);
			G.addEdge(v, w);
		}

		StdOut.println(G);

		final Bipartite b = new Bipartite(G);
		if (b.isBipartite())
		{
			StdOut.println("Graph is bipartite");
			for (int v = 0; v < G.V(); v++)
			{
				StdOut.println(v + ": " + b.color(v));
			}
		}
		else
		{
			StdOut.print("Graph has an odd-length cycle: ");
			for (final int x : b.oddCycle())
			{
				StdOut.print(x + " ");
			}
			StdOut.println();
		}
	}

	private final boolean[]	color;			// color[v] gives vertices on one
											// side of bipartition
	private Stack<Integer>	cycle;			// odd-length cycle
	private final int[]		edgeTo;		// edgeTo[v] = last edge on path to
											// v
	private boolean			isBipartite;	// is the graph bipartite?

	private final boolean[]	marked;		// marked[v] = true if v has been
											// visited in DFS

	/**
	 * Determines whether an undirected graph is bipartite and finds either a
	 * bipartition or an odd-length cycle.
	 * 
	 * @param G
	 *            the graph
	 */
	public Bipartite(final Graph G)
	{
		isBipartite = true;
		color = new boolean[G.V()];
		marked = new boolean[G.V()];
		edgeTo = new int[G.V()];

		for (int v = 0; v < G.V(); v++)
		{
			if (!marked[v])
			{
				dfs(G, v);
			}
		}
		assert check(G);
	}

	private boolean check(final Graph G)
	{
		// graph is bipartite
		if (isBipartite)
		{
			for (int v = 0; v < G.V(); v++)
			{
				for (final int w : G.adj(v))
				{
					if (color[v] == color[w])
					{
						System.err
								.printf("edge %d-%d with %d and %d in same side of bipartition\n",
										v, w, v, w);
						return false;
					}
				}
			}
		}

		// graph has an odd-length cycle
		else
		{
			// verify cycle
			int first = -1, last = -1;
			for (final int v : oddCycle())
			{
				if (first == -1)
				{
					first = v;
				}
				last = v;
			}
			if (first != last)
			{
				System.err.printf("cycle begins with %d and ends with %d\n",
						first, last);
				return false;
			}
		}

		return true;
	}

	/**
	 * Returns the side of the bipartite that vertex <tt>v</tt> is on. param v
	 * the vertex
	 * 
	 * @return the side of the bipartition that vertex <tt>v</tt> is on; two
	 *         vertices are in the same side of the bipartition if and only if
	 *         they have the same color
	 * @throws UnsupportedOperationException
	 *             if this method is called when the graph is not bipartite
	 */
	public boolean color(final int v)
	{
		if (!isBipartite)
		{
			throw new UnsupportedOperationException("Graph is not bipartite");
		}
		return color[v];
	}

	private void dfs(final Graph G, final int v)
	{
		marked[v] = true;
		for (final int w : G.adj(v))
		{

			// short circuit if odd-length cycle found
			if (cycle != null)
			{
				return;
			}

			// found uncolored vertex, so recur
			if (!marked[w])
			{
				edgeTo[w] = v;
				color[w] = !color[v];
				dfs(G, w);
			}

			// if v-w create an odd-length cycle, find it
			else if (color[w] == color[v])
			{
				isBipartite = false;
				cycle = new Stack<Integer>();
				cycle.push(w); // don't need this unless you want to include
								// start vertex twice
				for (int x = v; x != w; x = edgeTo[x])
				{
					cycle.push(x);
				}
				cycle.push(w);
			}
		}
	}

	/**
	 * Is the graph bipartite?
	 * 
	 * @return <tt>true</tt> if the graph is bipartite, <tt>false</tt> otherwise
	 */
	public boolean isBipartite()
	{
		return isBipartite;
	}

	/**
	 * Returns an odd-length cycle if the graph is not bipartite, and
	 * <tt>null</tt> otherwise.
	 * 
	 * @return an odd-length cycle (as an iterable) if the graph is not
	 *         bipartite (and hence has an odd-length cycle), and <tt>null</tt>
	 *         otherwise
	 */
	public Iterable<Integer> oddCycle()
	{
		return cycle;
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
