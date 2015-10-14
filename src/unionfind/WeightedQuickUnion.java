package unionfind;

import java.util.Arrays;

/**
 * Number of array access for read or write.
 *
 * Initialize: N
 *
 * Union: lg N
 *
 * Find: lg N
 *
 * Here is the id[] array after each union operation:
 *
 * ___: 0 1 2 3 4 5 6 7 8 9
 *
 * 7-0: 7 1 2 3 4 5 6 7 8 9
 *
 * 3-8: 7 1 2 3 4 5 6 7 3 9
 *
 * 9-2: 7 1 9 3 4 5 6 7 3 9
 *
 * 6-0: 7 1 9 3 4 5 7 7 3 9
 *
 * 2-3: 7 1 9 9 4 5 7 7 3 9
 *
 * 7-4: 7 1 9 9 7 5 7 7 3 9
 *
 * 7-1: 7 7 9 9 7 5 7 7 3 9
 *
 * 7-8: 7 7 9 9 7 5 7 7 3 7
 *
 * 3-5: 7 7 9 9 7 7 7 7 3 7
 *
 * To check arrays that might result:
 *
 * 1. check for a cycle (should be no cycle). 3 7 1 2 3 3 7 3 3 3: The id[]
 * array contains a cycle: 2->1->7->3->2
 *
 * 2. Height of forest = 4 > lg N = lg(10) should be satisfied. This example
 * fails: 6 1 7 7 6 8 6 5 6 1
 *
 * 3. Size of tree rooted at parent of 9 < twice the size of tree rooted at 9.
 * This example fails: 3 1 9 9 9 2 1 1 3 1
 *
 * @author mckelvym
 * @since Sep 4, 2015
 *
 */
public class WeightedQuickUnion implements UnionFind
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
	public WeightedQuickUnion(final int p_Size)
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
