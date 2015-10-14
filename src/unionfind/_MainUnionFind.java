package unionfind;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.google.common.base.Splitter;
import com.google.common.collect.ImmutableMap;

/**
 * @author mckelvym
 * @since Sep 5, 2015
 *
 */
public final class _MainUnionFind
{
	/**
	 * @param args
	 * @since Sep 5, 2015
	 */
	public static void main(final String[] args)
	{
		final Map<String, UnionFindAlg> entries = ImmutableMap.of(
				"7-0 3-8 9-2 6-0 2-3 7-4 7-1 7-8 3-5",
				UnionFindAlg.WEIGHTED_QUICK_UNION, "4-7 2-6 9-2 0-8 0-7 5-2",
				UnionFindAlg.QUICK_FIND, "2-5 4-6 1-3 7-8 2-8 3-0 6-3 0-7 6-9",
				UnionFindAlg.WEIGHTED_QUICK_UNION, "2-0 3-9 9-7 7-8 5-1 1-2",
				UnionFindAlg.QUICK_FIND, "4-7 3-7 0-6 9-8 8-2 1-5 8-1 6-7 1-0",
				UnionFindAlg.WEIGHTED_QUICK_UNION);

		for (final Entry<String, UnionFindAlg> entry : entries.entrySet())
		{
			final String input = entry.getKey();
			final UnionFind uf = entry.getValue().create(10);

			final List<String> pairs = Splitter.on(" ").splitToList(input);
			for (final String pair : pairs)
			{
				final List<String> items = Splitter.on("-").splitToList(pair);
				final int p = Integer.parseInt(items.get(0));
				final int q = Integer.parseInt(items.get(1));
				uf.union(p, q);
			}
			System.out.println(input);
			System.out.println(uf);
		}
	}

}
