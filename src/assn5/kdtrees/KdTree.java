package assn5.kdtrees;

import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.StdDraw;

/**
 * 2-d tree implementation
 *
 * @author mckelvym
 * @since Oct 12, 2015
 *
 */
public class KdTree
{
	/**
	 * A tree node
	 *
	 * @author mckelvym
	 * @since Oct 8, 2015
	 *
	 */
	private static class Node
	{
		/**
		 * the point
		 *
		 * @since Oct 8, 2015
		 */
		private final Point2D	m_Key;
		/**
		 * the left/bottom subtree
		 *
		 * @since Oct 8, 2015
		 */
		private Node			m_Left;

		/**
		 * the right/top subtree
		 *
		 * @since Oct 8, 2015
		 */
		private Node			m_Right;

		/**
		 * Construct a new node that references the provided key.
		 *
		 * @param p_Key
		 *            the key
		 * @since Oct 13, 2015
		 */
		private Node(final Point2D p_Key)
		{
			m_Key = p_Key;
			m_Left = null;
			m_Right = null;
		}

		@Override
		public boolean equals(final Object p_Obj)
		{
			if (this == p_Obj)
			{
				return true;
			}
			if (!(p_Obj instanceof KdTree.Node))
			{
				return false;
			}

			final KdTree.Node o = KdTree.Node.class.cast(p_Obj);
			return java.util.Objects.equals(m_Key, o.m_Key);
		}

		@Override
		public int hashCode()
		{
			return java.util.Objects.hash(m_Key);
		}

		@Override
		public String toString()
		{
			return m_Key.toString();
		}
	}

	/**
	 * The 'd' in KD-Tree. Though, much more would need to be changed if this
	 * number were changed.
	 *
	 * @since Oct 13, 2015
	 */
	private static int	D	= 2;

	/**
	 * Get the distances between the nodes
	 *
	 * @param p_Node1
	 *            the first node or null
	 * @param p_Node2
	 *            the second node or null
	 * @return the distances between the nodes or {@link Double#MAX_VALUE} if at
	 *         least one argument is null
	 * @since Oct 11, 2015
	 */
	private static double distance(final Node p_Node1, final Node p_Node2)
	{
		if (p_Node1 == null || p_Node2 == null)
		{
			return Double.MAX_VALUE;
		}
		final Point2D key1 = p_Node1.m_Key;
		final Point2D key2 = p_Node2.m_Key;
		return key1.distanceSquaredTo(key2);
	}

	/**
	 * Get the value from the node for the provided k-index
	 *
	 * @param p_Node
	 *            the node
	 * @param p_KIndex
	 *            the k-index to get the value for
	 * @return the value
	 * @since Oct 11, 2015
	 */
	private static double getKValue(final Node p_Node, final int p_KIndex)
	{
		return getKValue(p_Node.m_Key, p_KIndex);
	}

	/**
	 * Get the value from the key for the provided k-index
	 *
	 * @param p_Key
	 *            the key
	 * @param p_KIndex
	 *            the k-index to get the value for
	 * @return the value
	 * @since Oct 11, 2015
	 */
	private static double getKValue(final Point2D p_Key, final int p_KIndex)
	{
		if (p_KIndex == 0)
		{
			return p_Key.x();
		}
		return p_Key.y();
	}

	/**
	 * Get the max value of the rectangle for the provided k-index
	 *
	 * @param p_Rectangle
	 *            the rectangle
	 * @param p_KIndex
	 *            the k-index
	 * @return the max value
	 * @since Oct 11, 2015
	 */
	private static double getMax(final RectHV p_Rectangle, final int p_KIndex)
	{
		if (p_KIndex == 0)
		{
			return p_Rectangle.xmax();
		}
		return p_Rectangle.ymax();
	}

	/**
	 * Get the min value of the rectangle for the provided k-index
	 *
	 * @param p_Rectangle
	 *            the rectangle
	 * @param p_KIndex
	 *            the k-index
	 * @return the min value
	 * @since Oct 11, 2015
	 */
	private static double getMin(final RectHV p_Rectangle, final int p_KIndex)
	{
		if (p_KIndex == 0)
		{
			return p_Rectangle.xmin();
		}
		return p_Rectangle.ymin();
	}

	/**
	 * unit testing of the methods (optional)
	 *
	 * @param args
	 * @since Oct 7, 2015
	 */
	public static void main(final String[] args)
	{
		final KdTree tree = new KdTree();
		tree.insert(new Point2D(0.418638, 0.546882));
		tree.insert(new Point2D(0.944237, 0.971456));
		tree.insert(new Point2D(0.686983, 0.800207));
		tree.insert(new Point2D(0.843254, 0.676690));
		tree.insert(new Point2D(0.804746, 0.588051));

		System.out.println(toString(tree));
	}

	/**
	 * String representation
	 *
	 * @param p_Tree
	 * @return
	 * @since Oct 12, 2015
	 */
	private static String toString(final KdTree p_Tree)
	{
		final StringBuilder builder = new StringBuilder();
		p_Tree.levelOrder().forEach(builder::append);
		return builder.toString();
	}

	/**
	 * Count of nodes inserted
	 *
	 * @since Oct 13, 2015
	 */
	private int		m_Count;

	/**
	 * Root node
	 *
	 * @since Oct 13, 2015
	 */
	private Node	m_Root;

	/**
	 * construct an empty set of points
	 *
	 * @since Oct 7, 2015
	 */
	public KdTree()
	{
		m_Root = null;
		m_Count = 0;
	}

	/**
	 * Does the set contain the point?
	 *
	 * @param p_Key
	 *            the point to check.
	 * @return true if the set contains the provided point
	 * @since Oct 7, 2015
	 */
	public boolean contains(final Point2D p_Key)
	{
		final Node nearest = nearestSearch(m_Root, new Node(p_Key), 0);
		if (nearest == null)
		{
			return false;
		}
		return nearest.m_Key.equals(p_Key);
	}

	/**
	 * Draw all points to standard draw
	 *
	 * @since Oct 7, 2015
	 */
	public void draw()
	{
		if (m_Root == null)
		{
			return;
		}
		draw(m_Root, 0, new RectHV(0.0, 0.0, 1.0, 1.0));
	}

	/**
	 * Draw the nodes and the divider lines.
	 *
	 * @param p_Node
	 *            the {@link Node} to draw
	 * @param p_KIndex
	 *            the k-level
	 * @param p_Rect
	 *            the bounding rectangle for the level.
	 * @since Oct 12, 2015
	 */
	private void draw(final Node p_Node, final int p_KIndex, final RectHV p_Rect)
	{
		if (p_Node == null)
		{
			return;
		}

		StdDraw.setPenRadius(.01);
		StdDraw.setPenColor(StdDraw.BLACK);
		final double x = p_Node.m_Key.x();
		final double y = p_Node.m_Key.y();
		StdDraw.point(x, y);

		if (p_KIndex % D == 0)
		{
			StdDraw.setPenRadius(.001);
			StdDraw.setPenColor(StdDraw.RED);
			StdDraw.line(x, p_Rect.ymin(), x, p_Rect.ymax());
		}
		else
		{
			StdDraw.setPenRadius(.001);
			StdDraw.setPenColor(StdDraw.BLUE);
			StdDraw.line(p_Rect.xmin(), y, p_Rect.xmax(), y);
		}

		if (p_Node.m_Left != null)
		{
			if (p_KIndex % D == 0)
			{
				final RectHV rect = new RectHV(p_Rect.xmin(), p_Rect.ymin(),
						p_Node.m_Key.x(), p_Rect.ymax());
				draw(p_Node.m_Left, (p_KIndex + 1) % D, rect);
			}
			else
			{
				final RectHV rect = new RectHV(p_Rect.xmin(), p_Rect.ymin(),
						p_Rect.xmax(), p_Node.m_Key.y());
				draw(p_Node.m_Left, (p_KIndex + 1) % D, rect);
			}
		}

		if (p_Node.m_Right != null)
		{
			if (p_KIndex % D == 0)
			{
				final RectHV rect = new RectHV(p_Node.m_Key.x(), p_Rect.ymin(),
						p_Rect.xmax(), p_Rect.ymax());
				draw(p_Node.m_Right, (p_KIndex + 1) % D, rect);
			}
			else
			{
				final RectHV rect = new RectHV(p_Rect.xmin(), p_Node.m_Key.y(),
						p_Rect.xmax(), p_Rect.ymax());
				draw(p_Node.m_Right, (p_KIndex + 1) % D, rect);
			}
		}
	}

	/**
	 * Insert a node into the tree.
	 *
	 * @param p_RootNode
	 *            the node to insert from
	 * @param p_Key
	 *            the key to insert
	 * @param p_KIndex
	 *            the k-level
	 * @return true if a node was inserted
	 * @since Oct 12, 2015
	 */
	private boolean insert(final Node p_RootNode, final Point2D p_Key,
			final int p_KIndex)
	{
		final Point2D key = p_RootNode.m_Key;
		final double lValue = getKValue(p_Key, p_KIndex);
		final double rValue = getKValue(key, p_KIndex);
		final int cmp = Double.compare(lValue, rValue);
		final int kIndexNext = (p_KIndex + 1) % D;
		if (cmp < 0)
		{
			if (p_RootNode.m_Left == null)
			{
				p_RootNode.m_Left = new Node(p_Key);
				return true;
			}
			return insert(p_RootNode.m_Left, p_Key, kIndexNext);
		}
		else if (cmp == 0 && p_Key.equals(key))
		{
			return false;
		}
		else
		{
			if (p_RootNode.m_Right == null)
			{
				p_RootNode.m_Right = new Node(p_Key);
				return true;
			}
			return insert(p_RootNode.m_Right, p_Key, kIndexNext);
		}
	}

	/**
	 * Add the point to the set (if it is not already in the set)
	 *
	 * @param p_Key
	 *            the point to check
	 * @since Oct 7, 2015
	 */
	public void insert(final Point2D p_Key)
	{
		if (m_Root == null)
		{
			m_Root = new Node(p_Key);
			m_Count++;
		}
		else
		{
			final boolean inserted = insert(m_Root, p_Key, 0);
			if (inserted)
			{
				m_Count++;
			}
		}
	}

	/**
	 * Es the set empty?
	 *
	 * @return true if the set is empty
	 * @since Oct 7, 2015
	 */
	public boolean isEmpty()
	{
		return size() == 0;
	}

	/**
	 * Returns the keys in the BST in level order (for debugging).
	 *
	 * @return the keys in the BST in level order traversal
	 */
	private Iterable<Point2D> levelOrder()
	{
		final Queue<Point2D> keys = new Queue<Point2D>();
		final Queue<Node> queue = new Queue<Node>();
		queue.enqueue(m_Root);
		while (!queue.isEmpty())
		{
			final Node x = queue.dequeue();
			if (x == null)
			{
				continue;
			}
			keys.enqueue(x.m_Key);
			queue.enqueue(x.m_Left);
			queue.enqueue(x.m_Right);
		}
		return keys;
	}

	/**
	 * A nearest neighbor in the set to the provided point; null if the set is
	 * empty
	 *
	 * @param p_SearchKey
	 *            the point
	 * @return the nearest neighbor
	 * @since Oct 7, 2015
	 */
	public Point2D nearest(final Point2D p_SearchKey)
	{
		final Node searchNode = new Node(p_SearchKey);
		final Node nearestSearch = nearestSearch(m_Root, searchNode, 0);
		if (nearestSearch == null)
		{
			return null;
		}
		return nearestSearch.m_Key;
	}

	/**
	 * Search from the provided root node to find the closest node to the
	 * provided search node
	 *
	 * @param p_RootNode
	 *            the node to search from
	 * @param p_SearchNode
	 *            the node to search for closest distance
	 * @param p_KIndex
	 *            the k-level
	 * @return the nearest node from the provided root
	 * @since Oct 12, 2015
	 */
	private Node nearestSearch(final Node p_RootNode, final Node p_SearchNode,
			final int p_KIndex)
	{
		if (p_RootNode == null)
		{
			return null;
		}

		final double lvalue = getKValue(p_SearchNode, p_KIndex);
		final double rvalue = getKValue(p_RootNode, p_KIndex);
		final int kIndexNext = (p_KIndex + 1) % D;
		final Node left = p_RootNode.m_Left;
		final Node right = p_RootNode.m_Right;

		Node nearChildNode;
		Node farChildNode;

		/**
		 * Proceed in the direction of the nearest child first
		 */
		if (Double.compare(lvalue, rvalue) < 0)
		{
			nearChildNode = left;
			farChildNode = right;
		}
		else
		{
			nearChildNode = right;
			farChildNode = left;
		}

		/**
		 * Current champion is root.
		 */
		Node nearestNode = p_RootNode;
		final double distanceRoot = distance(p_RootNode, p_SearchNode);

		/**
		 * If the child in the nearest direction isn't null, recursively search
		 * the subtree.
		 */
		if (nearChildNode != null)
		{
			/**
			 * Nearest node is now the closest from the subtree.
			 */
			nearestNode = nearestSearch(nearChildNode, p_SearchNode, kIndexNext);
		}

		double distanceNearestNode = nearestNode != p_RootNode ? distance(
				nearestNode, p_SearchNode) : distanceRoot;
		/**
		 * Root is closer than nearest from subtree
		 */
		if (distanceRoot < distanceNearestNode)
		{
			nearestNode = p_RootNode;
			distanceNearestNode = distanceRoot;
		}

		/**
		 * Now backtrack to make sure the far child is not closer
		 */
		if (farChildNode != null
				&& Math.pow(lvalue - rvalue, 2) < distanceNearestNode)
		{
			final Node farNode = nearestSearch(farChildNode, p_SearchNode,
					kIndexNext);
			final double distanceFarNode = distance(farNode, p_SearchNode);
			if (distanceFarNode < distanceNearestNode)
			{
				nearestNode = farNode;
			}
		}
		return nearestNode;
	}

	/**
	 * All points that are inside the rectangle
	 *
	 * @param p_Rectangle
	 *            the filter rectangle
	 * @return all points that are inside the rectangle
	 * @since Oct 7, 2015
	 */
	public Iterable<Point2D> range(final RectHV p_Rectangle)
	{
		final Queue<Point2D> q = new Queue<>();
		rangeFill(q, m_Root, p_Rectangle, 0);
		return q;
	}

	/**
	 * Fill the collection with the values within the range of the rectangle.
	 *
	 * @param p_Collection
	 * @param p_RootNode
	 * @param p_Rectangle
	 * @param p_KIndex
	 * @since Oct 11, 2015
	 */
	private void rangeFill(final Queue<Point2D> p_Collection,
			final Node p_RootNode, final RectHV p_Rectangle, final int p_KIndex)
	{
		if (p_RootNode == null)
		{
			return;
		}

		if (p_Rectangle.contains(p_RootNode.m_Key))
		{
			p_Collection.enqueue(p_RootNode.m_Key);
		}
		if (getMin(p_Rectangle, p_KIndex) <= getKValue(p_RootNode.m_Key,
				p_KIndex))
		{
			rangeFill(p_Collection, p_RootNode.m_Left, p_Rectangle,
					(p_KIndex + 1) % D);
		}
		if (getMax(p_Rectangle, p_KIndex) >= getKValue(p_RootNode.m_Key,
				p_KIndex))
		{
			rangeFill(p_Collection, p_RootNode.m_Right, p_Rectangle,
					(p_KIndex + 1) % D);
		}
	}

	/**
	 * The number of points in the set
	 *
	 * @return the number of points in the set
	 * @since Oct 7, 2015
	 */
	public int size()
	{
		return m_Count;
	}
}
