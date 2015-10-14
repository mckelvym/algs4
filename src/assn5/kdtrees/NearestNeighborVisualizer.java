package assn5.kdtrees;

/******************************************************************************
 *  Compilation:  javac NearestNeighborVisualizer.java
 *  Execution:    java NearestNeighborVisualizer input.txt
 *  Dependencies: PointSET.java KdTree.java
 *
 *  Read points from a file (specified as a command-line argument) and
 *  draw to standard draw. Highlight the closest point to the mouse.
 *
 *  The nearest neighbor according to the brute-force algorithm is drawn
 *  in red; the nearest neighbor using the kd-tree algorithm is drawn in blue.
 *
 ******************************************************************************/

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.StdDraw;

public class NearestNeighborVisualizer
{

	public static void main(final String[] args)
	{
		final String filename = args[0];
		final In in = new In(filename);

		StdDraw.show(0);

		// initialize the two data structures with point from standard input
		final PointSET brute = new PointSET();
		final KdTree kdtree = new KdTree();
		while (!in.isEmpty())
		{
			final double x = in.readDouble();
			final double y = in.readDouble();
			final Point2D p = new Point2D(x, y);
			kdtree.insert(p);
			brute.insert(p);
		}

		while (true)
		{

			// the location (x, y) of the mouse
			final double x = StdDraw.mouseX();
			final double y = StdDraw.mouseY();
			final Point2D query = new Point2D(x, y);

			// draw all of the points
			StdDraw.clear();
			StdDraw.setPenColor(StdDraw.BLACK);
			StdDraw.setPenRadius(.01);
			brute.draw();

			final Point2D nearestBrute = brute.nearest(query);
			final Point2D nearestKD = kdtree.nearest(query);

			// draw in red the nearest neighbor (using brute-force algorithm)
			StdDraw.setPenRadius(.03);
			if (nearestBrute.equals(nearestKD))
			{
				StdDraw.setPenColor(StdDraw.GREEN);
			}
			else
			{
				StdDraw.setPenColor(StdDraw.RED);
			}
			nearestBrute.draw();

			// draw in blue the nearest neighbor (using kd-tree algorithm)
			StdDraw.setPenRadius(.02);
			StdDraw.setPenColor(StdDraw.BLUE);
			nearestKD.draw();
			StdDraw.show(0);
			StdDraw.show(40);

		}
	}
}
