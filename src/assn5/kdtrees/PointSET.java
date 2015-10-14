package assn5.kdtrees;

import java.util.TreeSet;

import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.StdDraw;

public class PointSET
{
	/**
	 * Get the distances between the points
	 *
	 * @param p_P1
	 *            the first point or null
	 * @param p_P2
	 *            the second point or null
	 * @return the distances between the points or {@link Double#MAX_VALUE} if
	 *         at least one argument is null
	 * @since Oct 11, 2015
	 */
	private static double distance(final Point2D p_P1, final Point2D p_P2)
	{
		if (p_P1 == null || p_P2 == null)
		{
			return Double.MAX_VALUE;
		}
		return p_P1.distanceSquaredTo(p_P2);
	}

	/**
	 * @param p_Set
	 *            a set sorted by y if p_UseX is false, sorted by x if p_UseX is
	 *            true
	 * @param p_Rectangle
	 *            the filter rectangle
	 * @param p_UseX
	 *            true if the provided set is sorted by x
	 * @return the set filtered by the x or y values of the rectangle, depending
	 *         on p_UseX
	 * @since Oct 11, 2015
	 */
	private static TreeSet<Point2D> filter(final TreeSet<Point2D> p_Set,
			final RectHV p_Rectangle, final boolean p_UseX)
	{
		final TreeSet<Point2D> filtered = new TreeSet<>((p1, p2) ->
		{
			if (p_UseX)
			{
				if (p1.x() < p2.x())
				{
					return -1;
				}
				if (p1.x() > p2.x())
				{
					return +1;
				}
				if (p1.y() < p2.y())
				{
					return -1;
				}
				if (p1.y() > p2.y())
				{
					return +1;
				}
				return 0;
			}
			return p1.compareTo(p2);
		});

		if (p_UseX)
		{
			filtered.addAll(p_Set.headSet(new Point2D(p_Rectangle.xmax(), 1.0),
					true).tailSet(new Point2D(p_Rectangle.xmin(), 0.0), true));
		}
		else
		{
			filtered.addAll(p_Set.headSet(new Point2D(1.0, p_Rectangle.ymax()),
					true).tailSet(new Point2D(0.0, p_Rectangle.ymin()), true));
		}

		return filtered;
	}

	/**
	 * unit testing of the methods (optional)
	 *
	 * @param args
	 * @since Oct 7, 2015
	 */
	public static void main(final String[] args)
	{
		final PointSET pointSET = new PointSET();
		pointSET.insert(new Point2D(0.418638, 0.546882));
		pointSET.insert(new Point2D(0.944237, 0.971456));
		pointSET.insert(new Point2D(0.686983, 0.800207));
		pointSET.insert(new Point2D(0.843254, 0.676690));
		pointSET.insert(new Point2D(0.804746, 0.588051));

		System.out.println(pointSET.nearest(new Point2D(1.0, 1.0)));
	}

	private final TreeSet<Point2D>	m_Points;

	/**
	 * Construct an empty TreeSet of points
	 *
	 * @since Oct 7, 2015
	 */
	public PointSET()
	{
		m_Points = new TreeSet<>();
	}

	/**
	 * Does the set contain the point?
	 *
	 * @param p_Point
	 *            the point to check.
	 * @return true if the set contains the provided point
	 * @since Oct 7, 2015
	 */
	public boolean contains(final Point2D p_Point)
	{
		return m_Points.contains(p_Point);
	}

	/**
	 * Draw all points to standard draw
	 *
	 * @since Oct 7, 2015
	 */
	public void draw()
	{
		m_Points.forEach(node -> StdDraw.point(node.x(), node.y()));
		// m_Points.forEach(node ->
		// {
		// double x = node.x();
		// double y = node.y();
		// final double threshold = 0.08;
		// if (x < threshold)
		// {
		// x += threshold;
		// }
		// else if (x > 1.0 - threshold)
		// {
		// x -= threshold;
		// }
		// if (y < threshold)
		// {
		// y += threshold;
		// }
		// else if (y > 1.0 - threshold)
		// {
		// y -= threshold;
		// }
		// StdDraw.text(x, y, String.format("%s, %s", node.x(), node.y()));
		// });
	}

	/**
	 * Add the point to the set (if it is not already in the set)
	 *
	 * @param p_Point
	 *            the point to check
	 * @since Oct 7, 2015
	 */
	public void insert(final Point2D p_Point)
	{
		m_Points.add(p_Point);
	}

	/**
	 * Es the set empty?
	 *
	 * @return true if the set is empty
	 * @since Oct 7, 2015
	 */
	public boolean isEmpty()
	{
		return m_Points.isEmpty();
	}

	/**
	 * A nearest neighbor in the set to the provided point; null if the set is
	 * empty
	 *
	 * @param p_Point
	 *            the point
	 * @return the nearest neighbor
	 * @since Oct 7, 2015
	 */
	public Point2D nearest(final Point2D p_Point)
	{
		double nearestDistance = Double.MAX_VALUE;
		Point2D nearestPoint = null;
		for (final Point2D p : m_Points)
		{
			final double distance = distance(p, p_Point);
			if (distance < nearestDistance)
			{
				nearestDistance = distance;
				nearestPoint = p;
			}
		}

		return nearestPoint;
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
		// TODO Fix...
		final TreeSet<Point2D> yFiltered = filter(m_Points, p_Rectangle, false);
		final TreeSet<Point2D> xFiltered = filter(yFiltered, p_Rectangle, true);
		final TreeSet<Point2D> filtered = new TreeSet<>();
		for (final Point2D p : xFiltered)
		{
			if (p_Rectangle.contains(p))
			{
				filtered.add(p);
			}
		}
		return filtered;
	}

	/**
	 * The number of points in the set
	 *
	 * @return the number of points in the set
	 * @since Oct 7, 2015
	 */
	public int size()
	{
		return m_Points.size();
	}
}
