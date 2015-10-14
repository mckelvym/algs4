package unionfind;

import java.util.Arrays;

/**
 * Quick-find is too slow.
 *
 * Number of array access for read or write.
 *
 * Initialize: N
 *
 * Union: N
 *
 * Find: 1
 *
 * it takes N^2 array accesses to process N objects.
 *
 * @author mckelvym
 * @since Sep 4, 2015
 *
 */
public class QuickFind implements UnionFind
{
	/**
	 * The lookup structure.
	 *
	 * @since Sep 4, 2015
	 */
	private final int[]	m_ID;

	/**
	 * Create a union-find instance for the provided number of elements
	 *
	 * @param p_Size
	 *            the size
	 * @since Sep 4, 2015
	 */
	public QuickFind(final int p_Size)
	{
		m_ID = new int[p_Size];
		for (int i = 0; i < p_Size; i++)
		{
			m_ID[i] = i;
		}
	}

	@Override
	public boolean connected(final int p, final int q)
	{
		/**
		 * Entries are connected if they have the same ID.
		 */
		return m_ID[p] == m_ID[q];
	}

	@Override
	public String toString()
	{
		return Arrays.toString(m_ID);
	}

	@Override
	public void union(final int p, final int q)
	{
		/**
		 * Union may change up to N-1 entries to the same value.
		 */
		final int pid = m_ID[p];
		final int qid = m_ID[q];
		for (int i = 0; i < m_ID.length; i++)
		{
			if (m_ID[i] == pid)
			{
				m_ID[i] = qid;
			}
		}
	}
}
