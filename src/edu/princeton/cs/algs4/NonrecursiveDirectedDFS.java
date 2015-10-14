/******************************************************************************
 *  Compilation:  javac NonrecursiveDirectedDFS.java
 *  Execution:    java NonrecursiveDirectedDFS digraph.txt s
 *  Dependencies: Digraph.java Queue.java Stack.java StdOut.java
 *  Data files:   http://algs4.cs.princeton.edu/42digraph/tinyDG.txt
 *
 *  Run nonrecurisve depth-first search on an directed graph.
 *  Runs in O(E + V) time.
 *
 *  Explores the vertices in exactly the same order as DirectedDFS.java.
 *
 *
 *  % java NonrecursiveDirectedDFS tinyDG.txt 1
 *  1
 *
 *  % java NonrecursiveDirectedDFS tinyDG.txt 2
 *  0 1 2 3 4 5
 *
 ******************************************************************************/

package edu.princeton.cs.algs4;

import java.util.Iterator;

/**
 * The <tt>NonrecursiveDirectedDFS</tt> class represents a data type for finding
 * the vertices reachable from a source vertex <em>s</em> in the digraph.
 * <p>
 * This implementation uses a nonrecursive version of depth-first search with an
 * explicit stack. The constructor takes time proportional to <em>V</em> +
 * <em>E</em>, where <em>V</em> is the number of vertices and <em>E</em> is the
 * number of edges. It uses extra space (not including the digraph) proportional
 * to <em>V</em>.
 * <p>
 * For additional documentation, see <a
 * href="http://algs4.cs.princeton.edu/42digraph">Section 4.2</a> of
 * <i>Algorithms, 4th Edition</i> by Robert Sedgewick and Kevin Wayne.
 *
 * @author Robert Sedgewick
 * @author Kevin Wayne
 */
public class NonrecursiveDirectedDFS
{
	/**
	 * Unit tests the <tt>NonrecursiveDirectedDFS</tt> data type.
	 */
	public static void main(final String[] args)
	{
		final In in = new In(args[0]);
		final Digraph G = new Digraph(in);
		final int s = Integer.parseInt(args[1]);
		final NonrecursiveDirectedDFS dfs = new NonrecursiveDirectedDFS(G, s);
		for (int v = 0; v < G.V(); v++)
		{
			if (dfs.marked(v))
			{
				StdOut.print(v + " ");
			}
		}
		StdOut.println();
	}

	private final boolean[]	marked; // marked[v] = is there an s->v path?

	/**
	 * Computes the vertices reachable from the source vertex <tt>s</tt> in the
	 * digraph <tt>G</tt>.
	 * 
	 * @param G
	 *            the digraph
	 * @param s
	 *            the source vertex
	 */
	public NonrecursiveDirectedDFS(final Digraph G, final int s)
	{
		marked = new boolean[G.V()];

		// to be able to iterate over each adjacency list, keeping track of
		// which
		// vertex in each adjacency list needs to be explored next
		final Iterator<Integer>[] adj = new Iterator[G.V()];
		for (int v = 0; v < G.V(); v++)
		{
			adj[v] = G.adj(v).iterator();
		}

		// depth-first search using an explicit stack
		final Stack<Integer> stack = new Stack<Integer>();
		marked[s] = true;
		stack.push(s);
		while (!stack.isEmpty())
		{
			final int v = stack.peek();
			if (adj[v].hasNext())
			{
				final int w = adj[v].next();
				// StdOut.printf("check %d\n", w);
				if (!marked[w])
				{
					// discovered vertex w for the first time
					marked[w] = true;
					// edgeTo[w] = v;
					stack.push(w);
					// StdOut.printf("dfs(%d)\n", w);
				}
			}
			else
			{
				// StdOut.printf("%d done\n", v);
				stack.pop();
			}
		}
	}

	/**
	 * Is vertex <tt>v</tt> reachable from the source vertex <tt>s</tt>?
	 * 
	 * @param v
	 *            the vertex
	 * @return <tt>true</tt> if vertex <tt>v</tt> is reachable from the source
	 *         vertex <tt>s</tt>, and <tt>false</tt> otherwise
	 */
	public boolean marked(final int v)
	{
		return marked[v];
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
