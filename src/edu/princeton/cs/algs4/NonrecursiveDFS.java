/******************************************************************************
 *  Compilation:  javac NonrecursiveDFS.java
 *  Execution:    java NonrecursiveDFS graph.txt s
 *  Dependencies: Graph.java Queue.java Stack.java StdOut.java
 *  Data files:   http://algs4.cs.princeton.edu/41graph/tinyCG.txt
 *
 *  Run nonrecurisve depth-first search on an undirected graph.
 *  Runs in O(E + V) time.
 *
 *  Explores the vertices in exactly the same order as DepthFirstSearch.java.
 *
 *  %  java Graph tinyCG.txt
 *  6 8
 *  0: 2 1 5
 *  1: 0 2
 *  2: 0 1 3 4
 *  3: 5 4 2
 *  4: 3 2
 *  5: 3 0
 *
 *  %  java NonrecursiveDFS tinyCG.txt 0
 *  0 to 0 (0):  0
 *  0 to 1 (1):  0-1
 *  0 to 2 (1):  0-2
 *  0 to 3 (2):  0-2-3
 *  0 to 4 (2):  0-2-4
 *  0 to 5 (1):  0-5
 *
 ******************************************************************************/

package edu.princeton.cs.algs4;

import java.util.Iterator;

/**
 * The <tt>NonrecursiveDFS</tt> class represents a data type for finding the
 * vertices connected to a source vertex <em>s</em> in the undirected graph.
 * <p>
 * This implementation uses a nonrecursive version of depth-first search with an
 * explicit stack. The constructor takes time proportional to <em>V</em> +
 * <em>E</em>, where <em>V</em> is the number of vertices and <em>E</em> is the
 * number of edges. It uses extra space (not including the graph) proportional
 * to <em>V</em>.
 * <p>
 * For additional documentation, see <a
 * href="http://algs4.cs.princeton.edu/41graph">Section 4.1</a> of
 * <i>Algorithms, 4th Edition</i> by Robert Sedgewick and Kevin Wayne.
 *
 * @author Robert Sedgewick
 * @author Kevin Wayne
 */
public class NonrecursiveDFS
{
	/**
	 * Unit tests the <tt>NonrecursiveDFS</tt> data type.
	 */
	public static void main(final String[] args)
	{
		final In in = new In(args[0]);
		final Graph G = new Graph(in);
		final int s = Integer.parseInt(args[1]);
		final NonrecursiveDFS dfs = new NonrecursiveDFS(G, s);
		for (int v = 0; v < G.V(); v++)
		{
			if (dfs.marked(v))
			{
				StdOut.print(v + " ");
			}
		}
		StdOut.println();
	}

	private final boolean[]	marked; // marked[v] = is there an s-v path?

	/**
	 * Computes the vertices connected to the source vertex <tt>s</tt> in the
	 * graph <tt>G</tt>.
	 * 
	 * @param G
	 *            the graph
	 * @param s
	 *            the source vertex
	 */
	public NonrecursiveDFS(final Graph G, final int s)
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
	 * Is vertex <tt>v</tt> connected to the source vertex <tt>s</tt>?
	 * 
	 * @param v
	 *            the vertex
	 * @return <tt>true</tt> if vertex <tt>v</tt> is connected to the source
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
