package assn3.patternrecognition;


import java.util.Comparator;

import edu.princeton.cs.algs4.StdDraw;

/**
 * An immutable data type Point that represents a point in the plane
 *
 * @author mckelvym
 * @since Sep 21, 2015
 *
 */
public class Point implements Comparable<Point>
{
	private final int	m_X;
	private final int	m_Y;

	/**
	 * Constructs the point (x, y)
	 *
	 * @param p_X
	 *            a value between 0 and 32767
	 * @param p_Y
	 *            a value between 0 and 32767
	 * @since Sep 21, 2015
	 */
	public Point(final int p_X, final int p_Y)
	{
		if (!(p_X >= 0 && p_X <= 32767))
		{
			throw new IllegalArgumentException(
					"X should be between 0 and 32767, but it is: " + p_X);
		}
		if (!(p_Y >= 0 && p_Y <= 32767))
		{
			throw new IllegalArgumentException(
					"Y should be between 0 and 32767, but it is: " + p_Y);
		}

		m_X = p_X;
		m_Y = p_Y;
	}

	/**
	 * Compare two points by y-coordinates, breaking ties by x-coordinate
	 */
	@Override
	public int compareTo(final Point p_Another)
	{
		if (p_Another == null)
		{
			throw new NullPointerException();
		}
		if (m_Y < p_Another.m_Y)
		{
			return -1;
		}
		if (m_Y > p_Another.m_Y)
		{
			return 1;
		}
		if (m_X < p_Another.m_X)
		{
			return -1;
		}
		if (m_X > p_Another.m_X)
		{
			return 1;
		}
		return 0;
	}

	/**
	 * Draws this point
	 *
	 * @since Sep 21, 2015
	 */
	public void draw()
	{
		StdDraw.point(m_X, m_Y);
	}

	/**
	 * Draws the line segment from this point to that point
	 *
	 * @param p_Another
	 * @since Sep 21, 2015
	 */
	public void drawTo(final Point p_Another)
	{
		if (p_Another == null)
		{
			throw new NullPointerException();
		}
		StdDraw.line(m_X, m_Y, p_Another.m_X, p_Another.m_Y);
	}

	/**
	 * Compare two points by slopes they make with this point
	 *
	 * @return a comparator for the slope order
	 * @since Sep 21, 2015
	 */
	public Comparator<Point> slopeOrder()
	{
		return new Comparator<Point>()
		{
			@Override
			public int compare(final Point p_1, final Point p_2)
			{
				if (p_1 == null)
				{
					throw new NullPointerException("p1 is null.");
				}
				if (p_2 == null)
				{
					throw new NullPointerException("p2 is null.");
				}
				final double s1 = slopeTo(p_1);
				final double s2 = slopeTo(p_2);
				return Double.compare(s1, s2);
			}
		};
	}

	/**
	 * The slope between this point and that point
	 *
	 * @param p_Another
	 * @return the slope to another point
	 * @since Sep 21, 2015
	 */
	public double slopeTo(final Point p_Another)
	{
		if (p_Another == null)
		{
			throw new NullPointerException();
		}
		if (m_X == p_Another.m_X && m_Y == p_Another.m_Y)
		{
			return Double.NEGATIVE_INFINITY;
		}
		if (m_Y == p_Another.m_Y)
		{
			return 0;
		}
		if (m_X == p_Another.m_X)
		{
			return Double.POSITIVE_INFINITY;
		}
		final double slope = (double) (p_Another.m_Y - m_Y)
				/ (double) (p_Another.m_X - m_X);
		return slope;
	}

	/**
	 * String representation
	 */
	@Override
	public String toString()
	{
		return String.format("(%s, %s)", m_X, m_Y);
	}
}