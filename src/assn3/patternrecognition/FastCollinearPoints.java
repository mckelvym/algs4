package assn3.patternrecognition;

import java.util.Arrays;

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.ResizingArrayQueue;
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
public class FastCollinearPoints
{
	/**
	 * Add the secomd if enough collinear points have been collected.
	 *
	 * @param p_Segments
	 *            the destination collection
	 * @param p_CollinearPoints
	 *            the collected points
	 * @since Sep 24, 2015
	 */
	private static void addSegment(
			final ResizingArrayQueue<LineSegment> p_Segments,
			final ResizingArrayQueue<Point> p_CollinearPoints)
	{
		if (p_CollinearPoints.size() >= 4)
		{
			final Point[] subArray = new Point[p_CollinearPoints.size()];
			final int size = p_CollinearPoints.size();
			for (int i = 0; i < size; i++)
			{
				subArray[i] = p_CollinearPoints.dequeue();
			}

			for (int i = 1; i < size; i++)
			{
				if (subArray[0].compareTo(subArray[i]) >= 0)
				{
					return;
				}
			}

			for (int i = 0; i < size - 1; i++)
			{
				if (subArray[size - 1].compareTo(subArray[i]) <= 0)
				{
					return;
				}
			}

			p_Segments.enqueue(new LineSegment(subArray[0],
					subArray[subArray.length - 1]));
		}
	}

	/**
	 * @param args
	 * @since Sep 21, 2015
	 */
	public static void main(final String[] args)
	{
		// if (args.length == 0)
		// {
		// throw new IllegalArgumentException("Please provide the input file.");
		// }
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
		// /**
		// * draw the points
		// */
		// StdDraw.show(0);
		// StdDraw.setYscale(0, 32768);
		// for (final Point p : points)
		// {
		// p.draw();
		// }
		// StdDraw.show();
		/**
		 * print and draw the line segments
		 */
		final FastCollinearPoints collinear = new FastCollinearPoints(points);
		for (final LineSegment segment : collinear.segments())
		{
			StdOut.println(segment);
			// segment.draw();
		}
		StdOut.println(collinear.numberOfSegments());
	}

	private final LineSegment[]	m_Segments;

	/**
	 * Finds all line segments containing 4 or more points
	 *
	 * @param p_Points
	 * @since Sep 21, 2015
	 */
	public FastCollinearPoints(final Point[] p_Points)
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
			final Point[] sortedpoints = points.clone();
			/**
			 * This sort will place p at the beginning of the array since
			 * comparing its slope to itself yields negative infinity.
			 */
			Arrays.sort(sortedpoints, p.slopeOrder());

			double slope = Double.NaN;
			ResizingArrayQueue<Point> collinearPoints = new ResizingArrayQueue<Point>();
			collinearPoints.enqueue(p);
			for (int qIndex = 1; qIndex < sortedpoints.length; qIndex++)
			{
				final Point q = sortedpoints[qIndex];
				if (q.compareTo(p) == 0)
				{
					throw new IllegalArgumentException("Same point.");
				}
				if (Double.isNaN(slope))
				{
					slope = p.slopeTo(q);
					collinearPoints.enqueue(q);
					continue;
				}

				final double thisSlope = p.slopeTo(q);
				final boolean collinear = Double.compare(slope, thisSlope) == 0;
				if (collinear)
				{
					collinearPoints.enqueue(q);
					continue;
				}

				addSegment(segments, collinearPoints);

				collinearPoints = new ResizingArrayQueue<Point>();
				collinearPoints.enqueue(p);
				collinearPoints.enqueue(q);
				slope = thisSlope;
			}
			addSegment(segments, collinearPoints);
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