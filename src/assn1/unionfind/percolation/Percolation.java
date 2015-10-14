package assn1.unionfind.percolation;

import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.WeightedQuickUnionUF;

/**
 * Percolation data type used to model a percolation system. It is backed by a
 * weighted union-find algorithm.
 *
 * @author mckelvym
 * @since Sep 6, 2015
 *
 */
public class Percolation
{
	/**
	 * Confirm that the two arguments are equal
	 *
	 * @param p_Expected
	 *            the expected value
	 * @param p_Actual
	 *            the actual value
	 * @since Sep 10, 2015
	 */
	private static void assertEqual(final Object p_Expected,
			final Object p_Actual)
	{
		if (p_Expected != p_Actual)
		{
			throw new IllegalStateException(String.format(
					"Expected %s, but got %s", p_Expected, p_Actual));
		}
	}

	/**
	 * Test client (optional)
	 *
	 * @param args
	 *            ignored
	 * @since Sep 6, 2015
	 */
	public static void main(final String[] args)
	{
		final int n = 5;
		final Percolation percolation = new Percolation(n);
		int cellSite = 0;
		int cellUF = 1;
		for (int row = 1; row <= n; row++)
		{
			for (int col = 1; col <= n; col++)
			{
				assertEqual(cellSite++, percolation.getCellInSites(row, col));
				assertEqual(cellUF++, percolation.getCellInUF(row, col));
				assertEqual(false, percolation.isOpen(row, col));
				assertEqual(false, percolation.isFull(row, col));
			}
		}
		assertEqual(false, percolation.percolates());

		StdOut.println("Done");
	}

	/**
	 * Index for the bottom virtual site. Typically = grid size + 1
	 *
	 * @since Sep 10, 2015
	 */
	private final int					m_BottomVirtualUFIndex;
	/**
	 * The square root of the grid size. The 'N' for the N-by-N grid.
	 *
	 * @since Sep 10, 2015
	 */
	private final int					m_N;
	/**
	 * The array of open sites. Does not include the virtual sites.
	 *
	 * @since Sep 10, 2015
	 */
	private final boolean[]				m_OpenSites;
	/**
	 * Index for the top bottom virtual site. Typically = 0
	 *
	 * @since Sep 10, 2015
	 */
	private final int					m_TopVirtualUFIndex;
	/**
	 * The union-find backing structure.
	 *
	 * @since Sep 10, 2015
	 */
	private final WeightedQuickUnionUF	m_UF;

	/**
	 * Do not use.
	 *
	 * @since Sep 6, 2015
	 */
	@SuppressWarnings("unused")
	private Percolation()
	{
		throw new IllegalStateException(
				"Default constructor should not be used.");
	}

	/**
	 * Create N-by-N grid, with all sites blocked
	 *
	 * @param p_N
	 *            the value of N for creating a NxN grid
	 * @since Sep 6, 2015
	 */
	public Percolation(final int p_N)
	{
		if (p_N <= 0)
		{
			throw new IllegalArgumentException("The value of 'N' must be > 0");
		}
		m_N = p_N;
		final int gridSize = p_N * p_N;
		m_OpenSites = new boolean[gridSize];
		for (int i = 0; i < gridSize; i++)
		{
			m_OpenSites[i] = false;
		}
		m_UF = new WeightedQuickUnionUF(gridSize + 2);
		/**
		 * Top and bottom virtual sites
		 */
		m_TopVirtualUFIndex = 0;
		m_BottomVirtualUFIndex = gridSize + 1;
		/**
		 * Preconnect the top and bottom virtual sites.
		 */
		for (int i = 1; i <= m_N; i++)
		{
			m_UF.union(m_TopVirtualUFIndex, i);
		}
		for (int i = gridSize; i > gridSize - m_N; i--)
		{
			m_UF.union(m_BottomVirtualUFIndex, i);
		}
	}

	/**
	 * Ensure that the provided positions are valid.
	 *
	 * @param p_Row
	 *            the 1-to-N row value
	 * @param p_Column
	 *            the 1-to-N column value
	 * @since Sep 6, 2015
	 * @throws IllegalArgumentException
	 *             if the positions are invalid.
	 */
	private void checkPositions(final int p_Row, final int p_Column)
	{
		if (p_Row < 1 || p_Row > m_N)
		{
			throw new IndexOutOfBoundsException(String.format(
					"The row %s is outside of the range 1-%s", p_Row, m_N));
		}
		if (p_Column < 1 || p_Column > m_N)
		{
			throw new IndexOutOfBoundsException(
					String.format("The column %s is outside of the range 1-%s",
							p_Column, m_N));
		}
	}

	/**
	 * Transform the given row, column positions to site index, which does not
	 * take into account the existence of the virtual sites.
	 *
	 * @param p_Row
	 *            the 1-to-N row value
	 * @param p_Column
	 *            the 1-to-N column value
	 * @return the cell ID in the sites structure
	 * @since Sep 7, 2015
	 */
	private int getCellInSites(final int p_Row, final int p_Column)
	{
		checkPositions(p_Row, p_Column);
		/**
		 * Transform 1-based row to 0-based
		 */
		final int row = p_Row - 1;
		/**
		 * Transform 1-based col to 0-based
		 */
		final int col = p_Column - 1;
		/**
		 * Transform 0-based row and column to 0-based site index, which
		 * excludes the virtual sites
		 */
		final int cellIDWithoutVirtualSites = row * m_N + col;
		return cellIDWithoutVirtualSites;
	}

	/**
	 * Transform the given row, column positions to UF index, which takes into
	 * account the existence of the virtual sites.
	 *
	 * @param p_Row
	 *            the 1-to-N row value
	 * @param p_Column
	 *            the 1-to-N column value
	 * @return the cell ID in the union-find structure
	 * @since Sep 7, 2015
	 */
	private int getCellInUF(final int p_Row, final int p_Column)
	{
		checkPositions(p_Row, p_Column);
		/**
		 * Transform 1-based row to 0-based
		 */
		final int row = p_Row - 1;
		/**
		 * Transform 1-based col to 0-based
		 */
		final int col = p_Column - 1;
		/**
		 * Transform 0-based row and column to 0-based UF index, which excludes
		 * the virtual sites
		 */
		final int cellIDWithoutVirtualSites = row * m_N + col;
		/**
		 * Account for the top virtual site, which shifts the index by +1
		 */
		return cellIDWithoutVirtualSites + 1;
	}

	/**
	 * Is site (row i, column j) full?
	 *
	 * @param p_Row
	 *            the 1-to-N row value
	 * @param p_Column
	 *            the 1-to-N column value
	 * @return true if the site is full
	 * @since Sep 6, 2015
	 */
	public boolean isFull(final int p_Row, final int p_Column)
	{
		checkPositions(p_Row, p_Column);
		final int cellIndexUF = getCellInUF(p_Row, p_Column);
		return isOpen(p_Row, p_Column)
				&& m_UF.connected(m_TopVirtualUFIndex, cellIndexUF);
	}

	/**
	 * Is site (row i, column j) open?
	 *
	 * @param p_Row
	 *            the 1-to-N row value
	 * @param p_Column
	 *            the 1-to-N column value
	 * @return true if the site is open
	 * @since Sep 6, 2015
	 */
	public boolean isOpen(final int p_Row, final int p_Column)
	{
		checkPositions(p_Row, p_Column);
		final int cellIndex = getCellInSites(p_Row, p_Column);
		return m_OpenSites[cellIndex];
	}

	/**
	 * Open site (row i, column j) if it is not open already
	 *
	 * @param p_Row
	 *            the 1-to-N row value
	 * @param p_Column
	 *            the 1-to-N column value
	 * @since Sep 6, 2015
	 */
	public void open(final int p_Row, final int p_Column)
	{
		checkPositions(p_Row, p_Column);
		final int cellIndex = getCellInSites(p_Row, p_Column);
		m_OpenSites[cellIndex] = true;

		final int p = getCellInUF(p_Row, p_Column);
		final int[] neighbors = new int[] { p_Row - 1, p_Column, p_Row,
				p_Column - 1, p_Row, p_Column + 1, p_Row + 1, p_Column };
		for (int i = 0; i < neighbors.length; i += 2)
		{
			final int row = neighbors[i];
			final int col = neighbors[i + 1];
			try
			{
				checkPositions(row, col);
			}
			catch (final Exception e)
			{
				continue;
			}
			if (isOpen(row, col))
			{
				final int q = getCellInUF(row, col);
				m_UF.union(p, q);
			}
		}
	}

	/**
	 * Does the system percolate?
	 *
	 * @return true if the system percolates
	 * @since Sep 6, 2015
	 */
	public boolean percolates()
	{
		if (m_OpenSites.length == 1 && !m_OpenSites[0])
		{
			return false;
		}
		return m_UF.connected(m_TopVirtualUFIndex, m_BottomVirtualUFIndex);
	}
}
