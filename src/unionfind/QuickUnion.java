package unionfind;

import java.util.Arrays;

/**
 * Quick-union is too slow.
 *
 * Number of array access for read or write.
 *
 * Initialize: N
 *
 * Union: N
 *
 * Find: N
 *
 * Defect: tree can get too tall, making find too expensive.
 *
 * @author mckelvym
 * @since Sep 4, 2015
 *
 */
public class QuickUnion implements UnionFind
{
	/**
	 * The lookup structure.
	 *
	 * id[i] is parent of i.
	 *
	 * Root of i is id[id[id[...id[i]...]]]
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
	public QuickUnion(final int p_Size)
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
		 * Do they have the same root?
		 */
		return find(p) == find(q);
	}

	/**
	 * Find the root of <code>i</code>
	 *
	 * @param i
	 * @return
	 * @since Sep 4, 2015
	 */
	private int find(int i)
	{
		while (i != m_ID[i])
		{
			i = m_ID[i];
		}
		return i;
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
		 * To merge, set the id of p's root to the id of q's root
		 */
		final int i = find(p);
		final int j = find(q);
		m_ID[i] = j;
	}

}
