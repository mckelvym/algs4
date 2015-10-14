package assn3.patternrecognition;

import java.util.Arrays;

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.ResizingArrayQueue;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

/**
 * Examines 4 points at a time and checks whether they all lie on the same line
 * segment, returning all such line segments. To check whether the 4 points p,
 * q, r, and s are collinear, check whether the three slopes between p and q,
 * between p and r, and between p and s are all equal.
 *
 * @author mckelvym
 * @since Sep 21, 2015
 *
 */
public class BruteCollinearPoints
{
	/**
	 * @param args
	 * @since Sep 21, 2015
	 */
	public static void main(final String[] args)
	{
		final java.io.File file = new java.io.File(".");
		final String[] list = file.list((x, y) -> y.endsWith("txt"));
		if (list == null || list.length == 0)
		{
			return;
		}
		/**
		 * read the N points from a file
		 */
		final In in = new In(list[0]);
		final int N = in.readInt();
		final Point[] points = new Point[N];
		for (int i = 0; i < N; i++)
		{
			final int x = in.readInt();
			final int y = in.readInt();
			points[i] = new Point(x, y);
		}
		/**
		 * draw the points
		 */
		StdDraw.show(0);
		StdDraw.setYscale(0, 32768);
		for (final Point p : points)
		{
			p.draw();
		}
		StdDraw.show();
		/**
		 * print and draw the line segments
		 */
		final BruteCollinearPoints collinear = new BruteCollinearPoints(points);
		for (final LineSegment segment : collinear.segments())
		{
			StdOut.println(segment);
			segment.draw();
		}
	}

	private final LineSegment[]	m_Segments;

	/**
	 * Finds all line segments containing 4 points
	 *
	 * @param p_Points
	 * @since Sep 21, 2015
	 */
	public BruteCollinearPoints(final Point[] p_Points)
	{
		if (p_Points == null)
		{
			throw new NullPointerException("Invalid argument.");
		}
		if (p_Points.length < 4)
		{
			for (int pIndex = 0; pIndex < p_Points.length; pIndex++)
			{
				if (p_Points[pIndex] == null)
				{
					throw new NullPointerException(
							"Null point in array at index: " + pIndex);
				}
				final Point p = p_Points[pIndex];
				for (int qIndex = pIndex + 1; qIndex < p_Points.length; qIndex++)
				{
					final Point q = p_Points[qIndex];
					if (q.compareTo(p) == 0)
					{
						throw new IllegalArgumentException("Same point.");
					}
				}
			}
			m_Segments = new LineSegment[0];
			return;
		}

		final Point[] points = p_Points.clone();
		Arrays.sort(points);
		final ResizingArrayQueue<LineSegment> segments = new ResizingArrayQueue<LineSegment>();
		for (int pIndex = 0; pIndex < points.length; pIndex++)
		{
			if (points[pIndex] == null)
			{
				throw new NullPointerException("Null point in array at index: "
						+ pIndex);
			}
			final Point p = points[pIndex];
			if (pIndex + 1 < points.length
					&& p.compareTo(points[pIndex + 1]) == 0)
			{
				throw new IllegalArgumentException("Same point.");
			}
			for (int qIndex = pIndex + 1; qIndex < points.length; qIndex++)
			{
				final Point q = points[qIndex];
				for (int rIndex = qIndex + 1; rIndex < points.length; rIndex++)
				{
					final Point r = points[rIndex];
					for (int sIndex = rIndex + 1; sIndex < points.length; sIndex++)
					{
						final Point s = points[sIndex];
						final Point[] subArray = new Point[] { p, q, r, s };

						final double pq = subArray[0].slopeTo(subArray[1]);
						final double pr = subArray[0].slopeTo(subArray[2]);
						boolean collinear = Double.compare(pq, pr) == 0;
						if (!collinear)
						{
							break;
						}
						final double ps = subArray[0].slopeTo(subArray[3]);
						collinear = Double.compare(pq, ps) == 0;
						if (!collinear)
						{
							continue;
						}
						Arrays.sort(subArray);
						subArray[0] = subArray[0];
						subArray[3] = subArray[3];
						segments.enqueue(new LineSegment(subArray[0],
								subArray[3]));
					}
				}
			}
		}

		m_Segments = new LineSegment[segments.size()];
		for (int i = 0; i < m_Segments.length; i++)
		{
			m_Segments[i] = segments.dequeue();
		}
	}

	/**
	 * Get the number of line segments
	 *
	 * @return the number of line segments
	 * @since Sep 21, 2015
	 */
	public int numberOfSegments()
	{
		return m_Segments.length;
	}

	/**
	 * Get the line segments
	 *
	 * @return the line segments
	 * @since Sep 21, 2015
	 */
	public LineSegment[] segments()
	{
		return m_Segments.clone();
	}
}