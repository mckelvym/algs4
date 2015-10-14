/******************************************************************************
 *  Compilation:  javac BreadthFirstDirectedPaths.java
 *  Execution:    java BreadthFirstDirectedPaths V E
 *  Dependencies: Digraph.java Queue.java Stack.java
 *
 *  Run breadth first search on a digraph.
 *  Runs in O(E + V) time.
 *
 *  % java BreadthFirstDirectedPaths tinyDG.txt 3
 *  3 to 0 (2):  3->2->0
 *  3 to 1 (3):  3->2->0->1
 *  3 to 2 (1):  3->2
 *  3 to 3 (0):  3
 *  3 to 4 (2):  3->5->4
 *  3 to 5 (1):  3->5
 *  3 to 6 (-):  not connected
 *  3 to 7 (-):  not connected
 *  3 to 8 (-):  not connected
 *  3 to 9 (-):  not connected
 *  3 to 10 (-):  not connected
 *  3 to 11 (-):  not connected
 *  3 to 12 (-):  not connected
 *
 ******************************************************************************/

package edu.princeton.cs.algs4;

/**
 * The <tt>BreadthDirectedFirstPaths</tt> class represents a data type for
 * finding shortest paths (number of edges) from a source vertex <em>s</em> (or
 * set of source vertices) to every other vertex in the digraph.
 * <p>
 * This implementation uses breadth-first search. The constructor takes time
 * proportional to <em>V</em> + <em>E</em>, where <em>V</em> is the number of
 * vertices and <em>E</em> is the number of edges. It uses extra space (not
 * including the digraph) proportional to <em>V</em>.
 * <p>
 * For additional documentation, see <a
 * href="http://algs4.cs.princeton.edu/42digraph">Section 4.2</a> of
 * <i>Algorithms, 4th Edition</i> by Robert Sedgewick and Kevin Wayne.
 *
 * @author Robert Sedgewick
 * @author Kevin Wayne
 */
public class BreadthFirstDirectedPaths
{
	private static final int	INFINITY	= Integer.MAX_VALUE;

	/**
	 * Unit tests the <tt>BreadthFirstDirectedPaths</tt> data type.
	 */
	public static void main(final String[] args)
	{
		final In in = new In(args[0]);
		final Digraph G = new Digraph(in);
		// StdOut.println(G);

		final int s = Integer.parseInt(args[1]);
		final BreadthFirstDirectedPaths bfs = new BreadthFirstDirectedPaths(G,
				s);

		for (int v = 0; v < G.V(); v++)
		{
			if (bfs.hasPathTo(v))
			{
				StdOut.printf("%d to %d (%d):  ", s, v, bfs.distTo(v));
				for (final int x : bfs.pathTo(v))
				{
					if (x == s)
					{
						StdOut.print(x);
					}
					else
					{
						StdOut.print("->" + x);
					}
				}
				StdOut.println();
			}

			else
			{
				StdOut.printf("%d to %d (-):  not connected\n", s, v);
			}

		}
	}

	private final int[]		distTo; // distTo[v] = length of shortest s->v path
	private final int[]		edgeTo; // edgeTo[v] = last edge on shortest s->v
									// path

	private final boolean[]	marked; // marked[v] = is there an s->v path?

	/**
	 * Computes the shortest path from <tt>s</tt> and every other vertex in
	 * graph <tt>G</tt>.
	 * 
	 * @param G
	 *            the digraph
	 * @param s
	 *            the source vertex
	 */
	public BreadthFirstDirectedPaths(final Digraph G, final int s)
	{
		marked = new boolean[G.V()];
		distTo = new int[G.V()];
		edgeTo = new int[G.V()];
		for (int v = 0; v < G.V(); v++)
		{
			distTo[v] = INFINITY;
		}
		bfs(G, s);
	}

	/**
	 * Computes the shortest path from any one of the source vertices in
	 * <tt>sources</tt> to every other vertex in graph <tt>G</tt>.
	 * 
	 * @param G
	 *            the digraph
	 * @param sources
	 *            the source vertices
	 */
	public BreadthFirstDirectedPaths(final Digraph G,
			final Iterable<Integer> sources)
	{
		marked = new boolean[G.V()];
		distTo = new int[G.V()];
		edgeTo = new int[G.V()];
		for (int v = 0; v < G.V(); v++)
		{
			distTo[v] = INFINITY;
		}
		bfs(G, sources);
	}

	// BFS from single source
	private void bfs(final Digraph G, final int s)
	{
		final Queue<Integer> q = new Queue<Integer>();
		marked[s] = true;
		distTo[s] = 0;
		q.enqueue(s);
		while (!q.isEmpty())
		{
			final int v = q.dequeue();
			for (final int w : G.adj(v))
			{
				if (!marked[w])
				{
					edgeTo[w] = v;
					distTo[w] = distTo[v] + 1;
					marked[w] = true;
					q.enqueue(w);
				}
			}
		}
	}

	// BFS from multiple sources
	private void bfs(final Digraph G, final Iterable<Integer> sources)
	{
		final Queue<Integer> q = new Queue<Integer>();
		for (final int s : sources)
		{
			marked[s] = true;
			distTo[s] = 0;
			q.enqueue(s);
		}
		while (!q.isEmpty())
		{
			final int v = q.dequeue();
			for (final int w : G.adj(v))
			{
				if (!marked[w])
				{
					edgeTo[w] = v;
					distTo[w] = distTo[v] + 1;
					marked[w] = true;
					q.enqueue(w);
				}
			}
		}
	}

	/**
	 * Returns the number of edges in a shortest path from the source <tt>s</tt>
	 * (or sources) to vertex <tt>v</tt>?
	 * 
	 * @param v
	 *            the vertex
	 * @return the number of edges in a shortest path
	 */
	public int distTo(final int v)
	{
		return distTo[v];
	}

	/**
	 * Is there a directed path from the source <tt>s</tt> (or sources) to
	 * vertex <tt>v</tt>?
	 * 
	 * @param v
	 *            the vertex
	 * @return <tt>true</tt> if there is a directed path, <tt>false</tt>
	 *         otherwise
	 */
	public boolean hasPathTo(final int v)
	{
		return marked[v];
	}

	/**
	 * Returns a shortest path from <tt>s</tt> (or sources) to <tt>v</tt>, or
	 * <tt>null</tt> if no such path.
	 * 
	 * @param v
	 *            the vertex
	 * @return the sequence of vertices on a shortest path, as an Iterable
	 */
	public Iterable<Integer> pathTo(final int v)
	{
		if (!hasPathTo(v))
		{
			return null;
		}
		final Stack<Integer> path = new Stack<Integer>();
		int x;
		for (x = v; distTo[x] != 0; x = edgeTo[x])
		{
			path.push(x);
		}
		path.push(x);
		return path;
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
