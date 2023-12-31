/******************************************************************************
 *  Compilation:  javac DirectedCycle.java
 *  Execution:    java DirectedCycle input.txt
 *  Dependencies: Digraph.java Stack.java StdOut.java In.java
 *  Data files:   http://algs4.cs.princeton.edu/42digraph/tinyDG.txt
 *                http://algs4.cs.princeton.edu/42digraph/tinyDAG.txt
 *
 *  Finds a directed cycle in a digraph.
 *  Runs in O(E + V) time.
 *
 *  % java DirectedCycle tinyDG.txt
 *  Directed cycle: 3 5 4 3
 *
 *  %  java DirectedCycle tinyDAG.txt
 *  No directed cycle
 *
 ******************************************************************************/

package edu.princeton.cs.algs4;

/**
 * The <tt>DirectedCycle</tt> class represents a data type for determining
 * whether a digraph has a directed cycle. The <em>hasCycle</em> operation
 * determines whether the digraph has a directed cycle and, and of so, the
 * <em>cycle</em> operation returns one.
 * <p>
 * This implementation uses depth-first search. The constructor takes time
 * proportional to <em>V</em> + <em>E</em> (in the worst case), where <em>V</em>
 * is the number of vertices and <em>E</em> is the number of edges. Afterwards,
 * the <em>hasCycle</em> operation takes constant time; the <em>cycle</em>
 * operation takes time proportional to the length of the cycle.
 * <p>
 * See {@link Topological} to compute a topological order if the digraph is
 * acyclic.
 * <p>
 * For additional documentation, see <a
 * href="http://algs4.cs.princeton.edu/42digraph">Section 4.2</a> of
 * <i>Algorithms, 4th Edition</i> by Robert Sedgewick and Kevin Wayne.
 *
 * @author Robert Sedgewick
 * @author Kevin Wayne
 */
public class DirectedCycle
{
	/**
	 * Unit tests the <tt>DirectedCycle</tt> data type.
	 */
	public static void main(final String[] args)
	{
		final In in = new In(args[0]);
		final Digraph G = new Digraph(in);

		final DirectedCycle finder = new DirectedCycle(G);
		if (finder.hasCycle())
		{
			StdOut.print("Directed cycle: ");
			for (final int v : finder.cycle())
			{
				StdOut.print(v + " ");
			}
			StdOut.println();
		}

		else
		{
			StdOut.println("No directed cycle");
		}
		StdOut.println();
	}

	private Stack<Integer>	cycle;		// directed cycle (or null if no such
										// cycle)
	private final int[]		edgeTo;	// edgeTo[v] = previous vertex on path
										// to v
	private final boolean[]	marked;	// marked[v] = has vertex v been marked?

	private final boolean[]	onStack;	// onStack[v] = is vertex on the stack?

	/**
	 * Determines whether the digraph <tt>G</tt> has a directed cycle and, if
	 * so, finds such a cycle.
	 * 
	 * @param G
	 *            the digraph
	 */
	public DirectedCycle(final Digraph G)
	{
		marked = new boolean[G.V()];
		onStack = new boolean[G.V()];
		edgeTo = new int[G.V()];
		for (int v = 0; v < G.V(); v++)
		{
			if (!marked[v] && cycle == null)
			{
				dfs(G, v);
			}
		}
	}

	// certify that digraph has a directed cycle if it reports one
	private boolean check()
	{

		if (hasCycle())
		{
			// verify cycle
			int first = -1, last = -1;
			for (final int v : cycle())
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
	 * Returns a directed cycle if the digraph has a directed cycle, and
	 * <tt>null</tt> otherwise.
	 * 
	 * @return a directed cycle (as an iterable) if the digraph has a directed
	 *         cycle, and <tt>null</tt> otherwise
	 */
	public Iterable<Integer> cycle()
	{
		return cycle;
	}

	// check that algorithm computes either the topological order or finds a
	// directed cycle
	private void dfs(final Digraph G, final int v)
	{
		onStack[v] = true;
		marked[v] = true;
		for (final int w : G.adj(v))
		{

			// short circuit if directed cycle found
			if (cycle != null)
			{
				return;
			}
			else if (!marked[w])
			{
				edgeTo[w] = v;
				dfs(G, w);
			}

			// trace back directed cycle
			else if (onStack[w])
			{
				cycle = new Stack<Integer>();
				for (int x = v; x != w; x = edgeTo[x])
				{
					cycle.push(x);
				}
				cycle.push(w);
				cycle.push(v);
				assert check();
			}
		}
		onStack[v] = false;
	}

	/**
	 * Does the digraph have a directed cycle?
	 * 
	 * @return <tt>true</tt> if the digraph has a directed cycle, <tt>false</tt>
	 *         otherwise
	 */
	public boolean hasCycle()
	{
		return cycle != null;
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
