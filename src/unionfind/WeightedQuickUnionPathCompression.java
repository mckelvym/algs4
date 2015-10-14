package unionfind;

import java.util.Arrays;

/**
 * Just after computing the root of p, set the id of each examined node to point
 * to that root.
 *
 * Hopcroft-Ulman, Tarjan
 *
 * Number of array access for read or write.
 *
 * Initialize: N
 *
 * Union: lg* N
 *
 * Find: lg* N
 *
 * @author mckelvym
 * @since Sep 4, 2015
 *
 */
public class WeightedQuickUnionPathCompression implements UnionFind
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
	private final int[]	m_Parent;

	/**
	 * Count the number of objects in the tree rooted at i.
	 *
	 * @since Sep 4, 2015
	 */
	private final int[]	m_Size;

	/**
	 * Create a union-find instance for the provided number of elements
	 *
	 * @param p_Size
	 *            the size
	 * @since Sep 4, 2015
	 */
	public WeightedQuickUnionPathCompression(final int p_Size)
	{
		m_Parent = new int[p_Size];
		m_Size = new int[p_Size];
		for (int i = 0; i < p_Size; i++)
		{
			m_Parent[i] = i;
			m_Size[i] = 1;
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
		while (i != m_Parent[i])
		{

			/**
			 * Two pass variant: add a second loop to root() to set the id[] of
			 * each examined node to the root. But one-pass is almost as good...
			 *
			 * One pass variant: This makes every other node in the path point
			 * to its grandparent, halving path length
			 */
			m_Parent[i] = m_Parent[m_Parent[i]];

			i = m_Parent[i];
		}
		return i;
	}

	@Override
	public String toString()
	{
		return String.format("id: %s; size: %s", Arrays.toString(m_Parent),
				Arrays.toString(m_Size));
	}

	@Override
	public void union(final int p, final int q)
	{
		/**
		 * To merge, set the id of p's root to the id of q's root
		 */
		final int i = find(p);
		final int j = find(q);

		if (i == j)
		{
			return;
		}

		if (m_Size[i] < m_Size[j])
		{
			m_Parent[i] = j;
			m_Size[j] += m_Size[i];
		}
		else
		{
			m_Parent[j] = i;
			m_Size[i] += m_Size[j];
		}
	}
}
