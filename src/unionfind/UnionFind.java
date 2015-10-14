package unionfind;
/**
 * General interface for joining and determining if elements are joined.
 *
 * @author mckelvym
 * @since Sep 4, 2015
 *
 */
public interface UnionFind
{
	/**
	 * Are elements <code>p</code> and <code>q</code> connected?
	 *
	 * @param p
	 *            the first element
	 * @param q
	 *            the second element
	 * @return true if connected.
	 * @since Sep 4, 2015
	 */
	boolean connected(int p, int q);

	/**
	 * Join the two elements <code>p</code> and <code>q</code>
	 *
	 * @param p
	 *            the first element
	 * @param q
	 *            the second element
	 * @since Sep 4, 2015
	 */
	void union(int p, int q);

}