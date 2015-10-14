package unionfind;

/**
 * Creates new instances of {@link UnionFind}
 *
 * @author mckelvym
 * @since Sep 5, 2015
 *
 */
public enum UnionFindAlg
{
	QUICK_FIND
	{
		@Override
		public UnionFind create(final int p_Size)
		{
			return new QuickFind(p_Size);
		}
	},
	QUICK_UNION
	{
		@Override
		public UnionFind create(final int p_Size)
		{
			return new QuickUnion(p_Size);
		}
	},
	WEIGHTED_QUICK_UNION
	{
		@Override
		public UnionFind create(final int p_Size)
		{
			return new WeightedQuickUnion(p_Size);
		}
	},
	WEIGHTED_QUICK_UNION_PATH_COMPRESSION
	{
		@Override
		public UnionFind create(final int p_Size)
		{
			return new WeightedQuickUnionPathCompression(p_Size);
		}
	},
	;

	/**
	 * create an implementation tuned for the provided size.
	 *
	 * @param p_Size
	 *            the number of elements
	 * @return a new instance of {@link UnionFind}
	 * @since Sep 5, 2015
	 */
	public abstract UnionFind create(int p_Size);
}
