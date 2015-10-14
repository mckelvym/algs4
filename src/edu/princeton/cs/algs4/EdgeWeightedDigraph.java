/******************************************************************************
 *  Compilation:  javac EdgeWeightedDigraph.java
 *  Execution:    java EdgeWeightedDigraph V E
 *  Dependencies: Bag.java DirectedEdge.java
 *
 *  An edge-weighted digraph, implemented using adjacency lists.
 *
 ******************************************************************************/

package edu.princeton.cs.algs4;

/**
 * The <tt>EdgeWeightedDigraph</tt> class represents a edge-weighted digraph of
 * vertices named 0 through <em>V</em> - 1, where each directed edge is of type
 * {@link DirectedEdge} and has a real-valued weight. It supports the following
 * two primary operations: add a directed edge to the digraph and iterate over
 * all of edges incident from a given vertex. It also provides methods for
 * returning the number of vertices <em>V</em> and the number of edges
 * <em>E</em>. Parallel edges and self-loops are permitted.
 * <p>
 * This implementation uses an adjacency-lists representation, which is a
 * vertex-indexed array of @link{Bag} objects. All operations take constant time
 * (in the worst case) except iterating over the edges incident from a given
 * vertex, which takes time proportional to the number of such edges.
 * <p>
 * For additional documentation, see <a
 * href="http://algs4.cs.princeton.edu/44sp">Section 4.4</a> of <i>Algorithms,
 * 4th Edition</i> by Robert Sedgewick and Kevin Wayne.
 *
 * @author Robert Sedgewick
 * @author Kevin Wayne
 */
public class EdgeWeightedDigraph
{
	private static final String	NEWLINE	= System.getProperty("line.separator");

	/**
	 * Unit tests the <tt>EdgeWeightedDigraph</tt> data type.
	 */
	public static void main(final String[] args)
	{
		final In in = new In(args[0]);
		final EdgeWeightedDigraph G = new EdgeWeightedDigraph(in);
		StdOut.println(G);
	}

	private final Bag<DirectedEdge>[]	adj;
	private int							E;

	private final int					V;

	/**
	 * Initializes a new edge-weighted digraph that is a deep copy of <tt>G</tt>
	 * .
	 *
	 * @param G
	 *            the edge-weighted digraph to copy
	 */
	public EdgeWeightedDigraph(final EdgeWeightedDigraph G)
	{
		this(G.V());
		this.E = G.E();
		for (int v = 0; v < G.V(); v++)
		{
			// reverse so that adjacency list is in same order as original
			final Stack<DirectedEdge> reverse = new Stack<DirectedEdge>();
			for (final DirectedEdge e : G.adj[v])
			{
				reverse.push(e);
			}
			for (final DirectedEdge e : reverse)
			{
				adj[v].add(e);
			}
		}
	}

	/**
	 * Initializes an edge-weighted digraph from an input stream. The format is
	 * the number of vertices <em>V</em>, followed by the number of edges
	 * <em>E</em>, followed by <em>E</em> pairs of vertices and edge weights,
	 * with each entry separated by whitespace.
	 *
	 * @param in
	 *            the input stream
	 * @throws IndexOutOfBoundsException
	 *             if the endpoints of any edge are not in prescribed range
	 * @throws IllegalArgumentException
	 *             if the number of vertices or edges is negative
	 */
	public EdgeWeightedDigraph(final In in)
	{
		this(in.readInt());
		final int E = in.readInt();
		if (E < 0)
		{
			throw new IllegalArgumentException(
					"Number of edges must be nonnegative");
		}
		for (int i = 0; i < E; i++)
		{
			final int v = in.readInt();
			final int w = in.readInt();
			if (v < 0 || v >= V)
			{
				throw new IndexOutOfBoundsException("vertex " + v
						+ " is not between 0 and " + (V - 1));
			}
			if (w < 0 || w >= V)
			{
				throw new IndexOutOfBoundsException("vertex " + w
						+ " is not between 0 and " + (V - 1));
			}
			final double weight = in.readDouble();
			addEdge(new DirectedEdge(v, w, weight));
		}
	}

	/**
	 * Initializes an empty edge-weighted digraph with <tt>V</tt> vertices and 0
	 * edges.
	 *
	 * @param V
	 *            the number of vertices
	 * @throws IllegalArgumentException
	 *             if <tt>V</tt> < 0
	 */
	public EdgeWeightedDigraph(final int V)
	{
		if (V < 0)
		{
			throw new IllegalArgumentException(
					"Number of vertices in a Digraph must be nonnegative");
		}
		this.V = V;
		this.E = 0;
		adj = new Bag[V];
		for (int v = 0; v < V; v++)
		{
			adj[v] = new Bag<DirectedEdge>();
		}
	}

	/**
	 * Initializes a random edge-weighted digraph with <tt>V</tt> vertices and
	 * <em>E</em> edges.
	 *
	 * @param V
	 *            the number of vertices
	 * @param E
	 *            the number of edges
	 * @throws IllegalArgumentException
	 *             if <tt>V</tt> < 0
	 * @throws IllegalArgumentException
	 *             if <tt>E</tt> < 0
	 */
	public EdgeWeightedDigraph(final int V, final int E)
	{
		this(V);
		if (E < 0)
		{
			throw new IllegalArgumentException(
					"Number of edges in a Digraph must be nonnegative");
		}
		for (int i = 0; i < E; i++)
		{
			final int v = StdRandom.uniform(V);
			final int w = StdRandom.uniform(V);
			final double weight = .01 * StdRandom.uniform(100);
			final DirectedEdge e = new DirectedEdge(v, w, weight);
			addEdge(e);
		}
	}

	/**
	 * Adds the directed edge <tt>e</tt> to this edge-weighted digraph.
	 *
	 * @param e
	 *            the edge
	 * @throws IndexOutOfBoundsException
	 *             unless endpoints of edge are between 0 and V-1
	 */
	public void addEdge(final DirectedEdge e)
	{
		final int v = e.from();
		final int w = e.to();
		validateVertex(v);
		validateVertex(w);
		adj[v].add(e);
		E++;
	}

	/**
	 * Returns the directed edges incident from vertex <tt>v</tt>.
	 *
	 * @param v
	 *            the vertex
	 * @return the directed edges incident from vertex <tt>v</tt> as an Iterable
	 * @throws IndexOutOfBoundsException
	 *             unless 0 <= v < V
	 */
	public Iterable<DirectedEdge> adj(final int v)
	{
		validateVertex(v);
		return adj[v];
	}

	/**
	 * Returns the number of edges in this edge-weighted digraph.
	 *
	 * @return the number of edges in this edge-weighted digraph
	 */
	public int E()
	{
		return E;
	}

	/**
	 * Returns all directed edges in this edge-weighted digraph. To iterate over
	 * the edges in this edge-weighted digraph, use foreach notation:
	 * <tt>for (DirectedEdge e : G.edges())</tt>.
	 *
	 * @return all edges in this edge-weighted digraph, as an iterable
	 */
	public Iterable<DirectedEdge> edges()
	{
		final Bag<DirectedEdge> list = new Bag<DirectedEdge>();
		for (int v = 0; v < V; v++)
		{
			for (final DirectedEdge e : adj(v))
			{
				list.add(e);
			}
		}
		return list;
	}

	/**
	 * Returns the number of directed edges incident from vertex <tt>v</tt>.
	 * This is known as the <em>outdegree</em> of vertex <tt>v</tt>.
	 *
	 * @param v
	 *            the vertex
	 * @return the outdegree of vertex <tt>v</tt>
	 * @throws IndexOutOfBoundsException
	 *             unless 0 <= v < V
	 */
	public int outdegree(final int v)
	{
		validateVertex(v);
		return adj[v].size();
	}

	/**
	 * Returns a string representation of this edge-weighted digraph.
	 *
	 * @return the number of vertices <em>V</em>, followed by the number of
	 *         edges <em>E</em>, followed by the <em>V</em> adjacency lists of
	 *         edges
	 */
	@Override
	public String toString()
	{
		final StringBuilder s = new StringBuilder();
		s.append(V + " " + E + NEWLINE);
		for (int v = 0; v < V; v++)
		{
			s.append(v + ": ");
			for (final DirectedEdge e : adj[v])
			{
				s.append(e + "  ");
			}
			s.append(NEWLINE);
		}
		return s.toString();
	}

	/**
	 * Returns the number of vertices in this edge-weighted digraph.
	 *
	 * @return the number of vertices in this edge-weighted digraph
	 */
	public int V()
	{
		return V;
	}

	// throw an IndexOutOfBoundsException unless 0 <= v < V
	private void validateVertex(final int v)
	{
		if (v < 0 || v >= V)
		{
			throw new IndexOutOfBoundsException("vertex " + v
					+ " is not between 0 and " + (V - 1));
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
