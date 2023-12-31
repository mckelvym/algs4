/******************************************************************************
 *  Compilation:  javac Cycle.java
 *  Execution:    java  Cycle filename.txt
 *  Dependencies: Graph.java Stack.java In.java StdOut.java
 *
 *  Identifies a cycle.
 *  Runs in O(E + V) time.
 *
 *  % java Cycle tinyG.txt
 *  3 4 5 3
 *
 *  % java Cycle mediumG.txt
 *  15 0 225 15
 *
 *  % java Cycle largeG.txt
 *  996673 762 840164 4619 785187 194717 996673
 *
 ******************************************************************************/

package edu.princeton.cs.algs4;

/**
 * The <tt>Cycle</tt> class represents a data type for determining whether an
 * undirected graph has a cycle. The <em>hasCycle</em> operation determines
 * whether the graph has a cycle and, if so, the <em>cycle</em> operation
 * returns one.
 * <p>
 * This implementation uses depth-first search. The constructor takes time
 * proportional to <em>V</em> + <em>E</em> (in the worst case), where <em>V</em>
 * is the number of vertices and <em>E</em> is the number of edges. Afterwards,
 * the <em>hasCycle</em> operation takes constant time; the <em>cycle</em>
 * operation takes time proportional to the length of the cycle.
 * <p>
 * For additional documentation, see <a
 * href="http://algs4.cs.princeton.edu/41graph">Section 4.1</a> of
 * <i>Algorithms, 4th Edition</i> by Robert Sedgewick and Kevin Wayne.
 *
 * @author Robert Sedgewick
 * @author Kevin Wayne
 */
public class Cycle
{
	/**
	 * Unit tests the <tt>Cycle</tt> data type.
	 */
	public static void main(final String[] args)
	{
		final In in = new In(args[0]);
		final Graph G = new Graph(in);
		final Cycle finder = new Cycle(G);
		if (finder.hasCycle())
		{
			for (final int v : finder.cycle())
			{
				StdOut.print(v + " ");
			}
			StdOut.println();
		}
		else
		{
			StdOut.println("Graph is acyclic");
		}
	}

	private Stack<Integer>	cycle;
	private int[]			edgeTo;

	private boolean[]		marked;

	/**
	 * Determines whether the undirected graph <tt>G</tt> has a cycle and, if
	 * so, finds such a cycle.
	 *
	 * @param G
	 *            the undirected graph
	 */
	public Cycle(final Graph G)
	{
		if (hasSelfLoop(G))
		{
			return;
		}
		if (hasParallelEdges(G))
		{
			return;
		}
		marked = new boolean[G.V()];
		edgeTo = new int[G.V()];
		for (int v = 0; v < G.V(); v++)
		{
			if (!marked[v])
			{
				dfs(G, -1, v);
			}
		}
	}

	/**
	 * Returns a cycle in the graph <tt>G</tt>.
	 * 
	 * @return a cycle if the graph <tt>G</tt> has a cycle, and <tt>null</tt>
	 *         otherwise
	 */
	public Iterable<Integer> cycle()
	{
		return cycle;
	}

	private void dfs(final Graph G, final int u, final int v)
	{
		marked[v] = true;
		for (final int w : G.adj(v))
		{

			// short circuit if cycle already found
			if (cycle != null)
			{
				return;
			}

			if (!marked[w])
			{
				edgeTo[w] = v;
				dfs(G, v, w);
			}

			// check for cycle (but disregard reverse of edge leading to v)
			else if (w != u)
			{
				cycle = new Stack<Integer>();
				for (int x = v; x != w; x = edgeTo[x])
				{
					cycle.push(x);
				}
				cycle.push(w);
				cycle.push(v);
			}
		}
	}

	/**
	 * Returns true if the graph <tt>G</tt> has a cycle.
	 *
	 * @return <tt>true</tt> if the graph has a cycle; <tt>false</tt> otherwise
	 */
	public boolean hasCycle()
	{
		return cycle != null;
	}

	// does this graph have two parallel edges?
	// side effect: initialize cycle to be two parallel edges
	private boolean hasParallelEdges(final Graph G)
	{
		marked = new boolean[G.V()];

		for (int v = 0; v < G.V(); v++)
		{

			// check for parallel edges incident to v
			for (final int w : G.adj(v))
			{
				if (marked[w])
				{
					cycle = new Stack<Integer>();
					cycle.push(v);
					cycle.push(w);
					cycle.push(v);
					return true;
				}
				marked[w] = true;
			}

			// reset so marked[v] = false for all v
			for (final int w : G.adj(v))
			{
				marked[w] = false;
			}
		}
		return false;
	}

	// does this graph have a self loop?
	// side effect: initialize cycle to be self loop
	private boolean hasSelfLoop(final Graph G)
	{
		for (int v = 0; v < G.V(); v++)
		{
			for (final int w : G.adj(v))
			{
				if (v == w)
				{
					cycle = new Stack<Integer>();
					cycle.push(v);
					cycle.push(v);
					return true;
				}
			}
		}
		return false;
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
