package assn4.astar.eightpuzzle;

import java.util.Comparator;

import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.Stack;
import edu.princeton.cs.algs4.StdOut;

/**
 * Attempts to solve the 8-puzzle problem
 *
 * @author mckelvym
 * @since Sep 27, 2015
 *
 */
public class Solver
{
	/**
	 * A {@link Board}-based node that also tracks the number of moves.
	 *
	 * @author mckelvym
	 * @since Sep 30, 2015
	 *
	 */
	private static class Node implements Comparable<Node>
	{
		private final Board	m_Board;
		private int			m_Hamming;
		private int			m_Manhattan;
		private final int	m_Moves;
		private final Node	m_Parent;

		/**
		 * Initialize a {@link Board}-based node that also tracks the number of
		 * moves.
		 *
		 * @param p_Parent
		 *            the parent of this node
		 * @param p_Board
		 *            the {@link Board}
		 * @param p_Moves
		 *            the number of moves
		 * @since Sep 30, 2015
		 */
		private Node(final Node p_Parent, final Board p_Board, final int p_Moves)
		{
			m_Parent = p_Parent;
			m_Board = p_Board;
			m_Moves = p_Moves;
			m_Hamming = -1;
			m_Manhattan = -1;
		}

		@Override
		public int compareTo(final Node p_Node)
		{
			final Node x = this;
			final Node y = p_Node;
			final int c1 = Integer.compare(x.manhattanPriority(),
					y.manhattanPriority());
			if (c1 == 0)
			{
				final int c2 = Integer.compare(x.manhattan(), y.manhattan());
				if (c2 == 0)
				{
					final int c3 = Integer.compare(x.hamming(), y.hamming());
					return c3;
				}
				return c2;
			}
			return c1;
		}

		/**
		 * Get the {@link Board}
		 *
		 * @return the {@link Board}
		 * @since Sep 30, 2015
		 */
		private Board getBoard()
		{
			return m_Board;
		}

		/**
		 * Get the number of moves
		 *
		 * @return the number of moves
		 * @since Sep 30, 2015
		 */
		private int getMoves()
		{
			return m_Moves;
		}

		/**
		 * Get the parent {@link Node} or null
		 *
		 * @return the parent {@link Node} or null
		 * @since Sep 30, 2015
		 */
		private Node getParent()
		{
			return m_Parent;
		}

		/**
		 * @return hamming distance without moves
		 * @since Oct 1, 2015
		 */
		private int hamming()
		{
			if (m_Hamming < 0)
			{
				m_Hamming = getBoard().hamming();
			}
			return m_Hamming;
		}

		/**
		 * @return the hamming priority, which includes the number of moves
		 * @since Sep 30, 2015
		 */
		private int hammingPriority()
		{
			return hamming() + getMoves();
		}

		/**
		 * @return manhattan distance without moves
		 * @since Oct 1, 2015
		 */
		private int manhattan()
		{
			if (m_Manhattan < 0)
			{
				m_Manhattan = getBoard().manhattan();
			}
			return m_Manhattan;
		}

		/**
		 * @return the manhattan priority, which includes the number of moves
		 * @since Sep 30, 2015
		 */
		private int manhattanPriority()
		{
			return manhattan() + getMoves();
		}

		@Override
		public String toString()
		{
			final StringBuilder b = new StringBuilder();
			b.append("priority  = " + manhattanPriority() + "\n");
			b.append("moves     = " + getMoves() + "\n");
			b.append("manhattan = " + getBoard().manhattan() + "\n");
			b.append(getBoard());
			return b.toString();
		}
	}

	/**
	 * Get the parent node
	 *
	 * @param p_Node
	 * @return
	 * @since Oct 1, 2015
	 */
	private static Node getRoot(final Node p_Node)
	{
		Node node = p_Node;
		while (node.getParent() != null)
		{
			node = node.getParent();
		}
		return node;
	}

	/**
	 * solve a slider puzzle (given below)
	 *
	 * @param args
	 * @since Sep 27, 2015
	 */
	public static void main(final String[] args)
	{
		// create initial board from file
		// final In in = new In(args[0]);
		final int N = 3;// in.readInt();
		final int[][] blocks = new int[N][N];
		// for (int i = 0; i < N; i++)
		// {
		// for (int j = 0; j < N; j++)
		// {
		// blocks[i][j] = in.readInt();
		// }
		// }
		blocks[0][0] = 0;
		blocks[0][1] = 1;
		blocks[0][2] = 3;
		blocks[1][0] = 4;
		blocks[1][1] = 2;
		blocks[1][2] = 5;
		blocks[2][0] = 7;
		blocks[2][1] = 8;
		blocks[2][2] = 6;
		final Board initial = new Board(blocks);

		// solve the puzzle
		final Solver solver = new Solver(initial);

		// print solution to standard output
		if (!solver.isSolvable())
		{
			StdOut.println("No solution possible");
		}
		else
		{
			StdOut.println("Minimum number of moves = " + solver.moves());
			for (final Board board : solver.solution())
			{
				StdOut.println(board);
			}
		}
	}

	private final Comparator<Node>	HAMMING_PRIORITY;

	private final Comparator<Node>	m_Priority;
	private final MinPQ<Node>		m_PriorityQueue;
	private final MinPQ<Node>		m_PriorityQueue2;
	private Node					m_Solution;
	private final Comparator<Node>	MANHATTAN_PRIORITY;

	/**
	 * Find a solution to the initial board (using the A* algorithm)
	 *
	 * @param p_Initial
	 * @since Sep 27, 2015
	 */
	public Solver(final Board p_Initial)
	{
		final int N = p_Initial.dimension();
		if (N < 2 || N >= 128)
		{
			throw new IllegalArgumentException("Invalid board size: " + N);
		}

		HAMMING_PRIORITY = (x, y) ->
		{
			final int xcost = x.hammingPriority();
			final int ycost = y.hammingPriority();
			if (xcost < ycost)
			{
				return -1;
			}
			if (xcost == ycost)
			{
				final int xmoves = x.getMoves();
				final int ymoves = y.getMoves();
				if (xmoves < ymoves)
				{
					return -1;
				}
				if (xmoves == ymoves)
				{
					return 0;
				}
				return 1;
			}
			return 1;
		};
		MANHATTAN_PRIORITY = (x, y) ->
		{
			final int c1 = Integer.compare(x.manhattanPriority(),
					y.manhattanPriority());
			if (c1 == 0)
			{
				final int c2 = Integer.compare(x.manhattan(), y.manhattan());
				if (c2 == 0)
				{
					final int c3 = Integer.compare(x.hamming(), y.hamming());
					return c3;
				}
				return c2;
			}
			return c1;
		};

		m_Priority = MANHATTAN_PRIORITY;
		m_PriorityQueue = new MinPQ<>(m_Priority);
		m_PriorityQueue2 = new MinPQ<>(m_Priority);
		m_PriorityQueue.insert(new Node(null, p_Initial, 0));
		m_PriorityQueue2.insert(new Node(null, p_Initial.twin(), 0));

		while (true)
		{
			// m_PriorityQueue.forEach(x -> System.out.println(x + ""
			// + (getRoot(x).getBoard() == p_Initial)));

			final Node n = m_PriorityQueue.delMin();
			final boolean goal = n.getBoard().isGoal();
			if (goal)
			{
				m_Solution = n;
				break;
			}

			final Node n2 = m_PriorityQueue2.delMin();
			final boolean goal2 = n2.getBoard().isGoal();
			if (goal2)
			{
				break;
			}

			if (!addNeighbors(m_PriorityQueue, n)
					|| !addNeighbors(m_PriorityQueue2, n2))
			{
				break;
			}
		}
	}

	/**
	 * Fill the provided priority queue the the neighbors of the board within
	 * the provided node.
	 *
	 * @param p_PriorityQueue
	 *            the priority queue
	 * @param p_FromNode
	 *            the {@link Node}
	 * @since Sep 30, 2015
	 */
	private boolean addNeighbors(final MinPQ<Node> p_PriorityQueue,
			final Node p_FromNode)
	{
		final int newMoves = p_FromNode.getMoves() + 1;
		final int N = p_FromNode.getBoard().dimension();
		if (N == 2 && newMoves > 6)
		{
			return false;
		}
		if (N == 3 && newMoves > 31)
		{
			return false;
		}
		if (N == 4 && newMoves > 80)
		{
			return false;
		}
		final int pmanhattan = p_FromNode.manhattan();
		int count = 0;
		for (final Board b : p_FromNode.getBoard().neighbors())
		{
			final Node node = new Node(p_FromNode, b, newMoves);
			if (node.manhattan() == pmanhattan)
			{
				continue;
			}
			else if (count > 0
					&& p_FromNode.getParent() != null
					&& node.getBoard()
							.equals(p_FromNode.getParent().getBoard()))
			{
				continue;
			}
			p_PriorityQueue.insert(node);
			count++;
		}

		return true;
	}

	/**
	 * Is the initial board solvable?
	 *
	 * @return
	 * @since Sep 27, 2015
	 */
	public boolean isSolvable()
	{
		return m_Solution != null;
	}

	/**
	 * Min number of moves to solve initial board; -1 if unsolvable
	 *
	 * @return
	 * @since Sep 27, 2015
	 */
	public int moves()
	{
		return isSolvable() ? m_Solution.getMoves() : -1;
	}

	/**
	 * Sequence of boards in a shortest solution; null if unsolvable
	 *
	 * @return
	 * @since Sep 27, 2015
	 */
	public Iterable<Board> solution()
	{
		if (isSolvable())
		{
			final Stack<Board> boardStack = new Stack<>();
			Node node = m_Solution;
			while (node != null)
			{
				boardStack.push(node.getBoard());
				node = node.getParent();
			}
			return boardStack;
		}
		return null;
	}

}
