package assn4.astar.eightpuzzle;

import java.util.Arrays;

import edu.princeton.cs.algs4.MinPQ;

/**
 * Represents the board and its block positions.
 *
 * @author mckelvym
 * @since Sep 27, 2015
 *
 */
public class Board
{
	/**
	 * unit tests (not graded)
	 *
	 * @param args
	 * @since Sep 27, 2015
	 */
	public static void main(final String[] args)
	{
		final int[][] blocks = new int[127][127];
		int index = 0;
		for (int i = 0; i < 127; i++)
		{
			for (int j = 0; j < 127; j++)
			{
				blocks[i][j] = index++;
			}
		}
		System.out.println(new Board(blocks));
	}

	/**
	 * Swap the {@code i} and {@code j} entries in the array {@code a}
	 *
	 * @param p_Array
	 *            the array
	 * @param p_Index1
	 *            the i entry
	 * @param p_Index2
	 *            the j entry
	 * @since Sep 30, 2015
	 */
	private static void swap(final int[] p_Array, final int p_Index1,
			final int p_Index2)
	{
		final int temp = p_Array[p_Index1];
		p_Array[p_Index1] = p_Array[p_Index2];
		p_Array[p_Index2] = temp;
	}

	private boolean		m_BadApple;
	private int			m_BlankIndex;
	private final int[]	m_Blocks;
	private int			m_Hamming;

	private int			m_Manhattan;

	private final int	m_N;

	private final Board	m_Parent;

	/**
	 * Used by {@link #neighbors()}, simplifies creation of neighbor boards
	 * especially when the blank index is already known
	 *
	 * @param p_Parent
	 *            the parent {@link Board}
	 * @param p_Blocks
	 * @param p_N
	 * @param p_BlankIndex
	 * @since Sep 30, 2015
	 */
	private Board(final Board p_Parent, final int[] p_Blocks, final int p_N,
			final int p_BlankIndex)
	{
		m_Parent = p_Parent;
		m_Hamming = -1;
		m_Manhattan = -1;
		m_N = p_N;
		m_Blocks = p_Blocks.clone();
		m_BlankIndex = p_BlankIndex;
	}

	/**
	 * Used by {@link #twin()}, simplifies creation of twin boards.
	 *
	 * @param p_Blocks
	 * @param p_N
	 * @since Sep 30, 2015
	 */
	private Board(final int[] p_Blocks, final int p_N)
	{
		m_Parent = null;
		m_Hamming = -1;
		m_Manhattan = -1;
		m_N = p_N;
		m_Blocks = new int[p_Blocks.length];
		for (int index = 0; index < p_Blocks.length; index++)
		{
			final int valueIJ = p_Blocks[index];
			if (valueIJ == 0)
			{
				m_BlankIndex = index;
			}
			m_Blocks[index] = valueIJ;
		}
	}

	/**
	 * Construct a board from an N-by-N array of blocks (where blocks[i][j] =
	 * block in row i, column j)
	 *
	 * @param p_Blocks
	 * @since Sep 27, 2015
	 */
	public Board(final int[][] p_Blocks)
	{
		m_Parent = null;
		m_Hamming = -1;
		m_Manhattan = -1;
		m_N = p_Blocks.length;
		final int N2 = m_N * m_N;
		m_Blocks = new int[N2];
		int index = 0;
		for (int i = 0; i < m_N; i++)
		{
			for (int j = 0; j < m_N; j++)
			{
				final int valueIJ = p_Blocks[i][j];
				if (valueIJ == 0)
				{
					m_BlankIndex = index;
				}
				m_Blocks[index++] = valueIJ;
			}
		}
	}

	/**
	 * Board dimension N
	 *
	 * @return Board dimension N
	 * @since Sep 27, 2015
	 */
	public int dimension()
	{
		return m_N;
	}

	// @Override
	// private int hashCode()
	// {
	// return Arrays.hashCode(m_Blocks);
	// }

	@Override
	public boolean equals(final Object p_Obj)
	{
		if (this == p_Obj)
		{
			return true;
		}
		if (!(p_Obj instanceof Board))
		{
			return false;
		}

		final Board o = Board.class.cast(p_Obj);
		return java.util.Objects.equals(dimension(), o.dimension())
				&& Arrays.equals(m_Blocks, o.m_Blocks);
	}

	/**
	 * Get the number of blocks out of place
	 *
	 * @return the number of blocks out of place
	 * @since Sep 27, 2015
	 */
	public int hamming()
	{
		if (m_Hamming < 0)
		{
			int count = 0;
			int position = 1;
			int index = 0;
			for (int i = 0; i < dimension(); i++)
			{
				for (int j = 0; j < dimension(); j++)
				{
					final int valueIJ = m_Blocks[index++];
					if (valueIJ != 0 && valueIJ != position)
					{
						count++;
					}
					position++;
				}
			}
			m_Hamming = count;
		}
		return m_Hamming;
	}

	/**
	 * Is this board the goal board?
	 *
	 * @return
	 * @since Sep 27, 2015
	 */
	public boolean isGoal()
	{
		return manhattan() == 0;
	}

	/**
	 * Sum of Manhattan distances between blocks and goal
	 *
	 * @return
	 * @since Sep 27, 2015
	 */
	public int manhattan()
	{
		if (m_Manhattan < 0)
		{
			int count = 0;
			int index = 0;
			for (int i = 0; i < dimension(); i++)
			{
				for (int j = 0; j < dimension(); j++)
				{
					final int valueIJ = m_Blocks[index++];
					if (valueIJ == 0)
					{
						continue;
					}

					final int[] mapTo2D = mapTo2D(valueIJ - 1);
					final int finalI = mapTo2D[0];
					final int finalJ = mapTo2D[1];
					final int iDiff = Math.abs(finalI - i);
					final int jDiff = Math.abs(finalJ - j);
					final int diff = iDiff + jDiff;
					count += diff;
				}
			}
			m_Manhattan = count;
		}
		return m_Manhattan;
	}

	/**
	 * Map an {@code i} and {@code j} indices to 1D index
	 *
	 * @param p_I
	 * @param p_J
	 * @return
	 * @since Sep 30, 2015
	 */
	private int mapTo1D(final int p_I, final int p_J)
	{
		return p_I * dimension() + p_J;
	}

	/**
	 * Map a 1D index to two dimensional indices
	 *
	 * @param p_Index
	 * @return
	 * @since Sep 30, 2015
	 */
	private int[] mapTo2D(final int p_Index)
	{
		final int i = p_Index / dimension();
		final int j = p_Index % dimension();
		return new int[] { i, j };
	}

	/**
	 * All neighboring boards. Moves the blank position.
	 *
	 * @return
	 * @since Sep 27, 2015
	 */
	public Iterable<Board> neighbors()
	{
		final int N = dimension();
		final MinPQ<Board> boards = new MinPQ<Board>((x, y) ->
		{
			if (x.m_BadApple != y.m_BadApple)
			{
				if (!x.m_BadApple)
				{
					return -1;
				}
				return 1;
			}
			return Integer.compare(x.manhattan(), y.manhattan());
		});
		/**
		 * A critical optimization:
		 *
		 * Best-first search has one annoying feature: search nodes
		 * corresponding to the same board are enqueued on the priority queue
		 * many times. To reduce unnecessary exploration of useless search
		 * nodes, when considering the neighbors of a search node, don't enqueue
		 * a neighbor if its board is the same as the board of the previous
		 * search node.
		 */
		final int[] mapTo2D = mapTo2D(m_BlankIndex);
		final int i = mapTo2D[0];
		final int j = mapTo2D[1];

		/**
		 * Move left
		 */
		if (i - 1 >= 0)
		{
			final int newI = i - 1;
			final int newJ = j;
			prepareNeighbor(boards, N, i, j, newI, newJ);
		}
		/**
		 * Move right
		 */
		if (i + 1 < N)
		{
			final int newI = i + 1;
			final int newJ = j;
			prepareNeighbor(boards, N, i, j, newI, newJ);
		}
		/**
		 * Move up
		 */
		if (j - 1 >= 0)
		{
			final int newI = i;
			final int newJ = j - 1;
			prepareNeighbor(boards, N, i, j, newI, newJ);
		}
		/**
		 * Move down
		 */
		if (j + 1 < N)
		{
			final int newI = i;
			final int newJ = j + 1;
			prepareNeighbor(boards, N, i, j, newI, newJ);
		}
		return boards;
	}

	/**
	 * Prepare the neighbor board and precompute the manhattan cost
	 *
	 * @param p_NeighborBoards
	 * @param p_N
	 * @param p_I
	 * @param p_J
	 * @param p_NewI
	 * @param p_NewJ
	 * @return true if the neighbor should have normal weight.
	 * @since Oct 1, 2015
	 */
	private void prepareNeighbor(final MinPQ<Board> p_NeighborBoards,
			final int p_N, final int p_I, final int p_J, final int p_NewI,
			final int p_NewJ)
	{
		final int newBlankIndex = mapTo1D(p_NewI, p_NewJ);
		boolean makeItCostly = false;
		if (m_Parent != null && newBlankIndex == m_Parent.m_BlankIndex)
		{
			makeItCostly = true;
		}
		final int currentValue = m_Blocks[newBlankIndex];
		final int[] mapTo2d = mapTo2D(currentValue - 1);
		final int finalI = mapTo2d[0];
		final int finalJ = mapTo2d[1];

		final int beforeCost = p_J == p_NewJ ? Math.abs(finalI - p_I) : Math
				.abs(finalJ - p_J);
		final int afterCost = p_J == p_NewJ ? Math.abs(finalI - p_NewI) : +Math
				.abs(finalJ - p_NewJ);

		final Board neighbor = new Board(this, m_Blocks, p_N, newBlankIndex);
		swap(neighbor.m_Blocks, newBlankIndex, m_BlankIndex);

		neighbor.m_Manhattan = manhattan() - afterCost + beforeCost;
		neighbor.m_BadApple = makeItCostly;

		p_NeighborBoards.insert(neighbor);
	}

	/**
	 * String representation of this board (in the output format specified
	 * below)
	 */
	@Override
	public String toString()
	{
		/**
		 * The input and output format for a board is the board dimension N
		 * followed by the N-by-N initial board, using 0 to represent the blank
		 * square. As an example,
		 *
		 * % more puzzle04.txt
		 *
		 * 3
		 *
		 * 0 1 3
		 *
		 * 4 2 5
		 *
		 * 7 8 6
		 */
		final StringBuilder builder = new StringBuilder(dimension() + "\n");
		int index = 0;
		for (int i = 0; i < dimension(); i++)
		{
			for (int j = 0; j < dimension(); j++)
			{
				builder.append(String.format("%5d ", m_Blocks[index++]));
			}
			builder.append("\n");
		}
		return builder.toString();
	}

	/**
	 * A board that is obtained by exchanging any pair of blocks
	 *
	 * @return
	 * @since Sep 27, 2015
	 */
	public Board twin()
	{
		final Board twin = new Board(m_Blocks, dimension());
		int index = 0;
		while (twin.m_Blocks[index] == 0)
		{
			index++;
		}

		int indexprime = index + 1;
		while (twin.m_Blocks[indexprime] == 0)
		{
			indexprime++;
		}

		swap(twin.m_Blocks, index, indexprime);
		return twin;
	}
}
