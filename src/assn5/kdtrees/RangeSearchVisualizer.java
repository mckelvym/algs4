package assn5.kdtrees;

/******************************************************************************
 *  Compilation:  javac RangeSearchVisualizer.java
 *  Execution:    java RangeSearchVisualizer input.txt
 *  Dependencies: PointSET.java KdTree.java
 *
 *  Read points from a file (specified as a command-line arugment) and
 *  draw to standard draw. Also draw all of the points in the rectangle
 *  the user selects by dragging the mouse.
 *
 *  The range search results using the brute-force algorithm are drawn
 *  in red; the results using the kd-tree algorithms are drawn in blue.
 *
 ******************************************************************************/

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.StdDraw;

public class RangeSearchVisualizer
{

	public static void main(final String[] args)
	{

		final String filename = args[0];
		final In in = new In(filename);

		StdDraw.show(0);

		// initialize the data structures with N points from standard input
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

		double x0 = 0.0, y0 = 0.0; // initial endpoint of rectangle
		double x1 = 0.0, y1 = 0.0; // current location of mouse
		boolean isDragging = false; // is the user dragging a rectangle

		// draw the points
		StdDraw.clear();
		StdDraw.setPenColor(StdDraw.BLACK);
		StdDraw.setPenRadius(.01);
		brute.draw();

		while (true)
		{
			StdDraw.show(40);

			// user starts to drag a rectangle
			if (StdDraw.mousePressed() && !isDragging)
			{
				x0 = StdDraw.mouseX();
				y0 = StdDraw.mouseY();
				isDragging = true;
				continue;
			}

			// user is dragging a rectangle
			else if (StdDraw.mousePressed() && isDragging)
			{
				x1 = StdDraw.mouseX();
				y1 = StdDraw.mouseY();
				continue;
			}

			// mouse no longer pressed
			else if (!StdDraw.mousePressed() && isDragging)
			{
				isDragging = false;
			}

			final RectHV rect = new RectHV(Math.min(x0, x1), Math.min(y0, y1),
					Math.max(x0, x1), Math.max(y0, y1));
			// draw the points
			StdDraw.clear();
			StdDraw.setPenColor(StdDraw.BLACK);
			StdDraw.setPenRadius(.01);
			brute.draw();

			// draw the rectangle
			StdDraw.setPenColor(StdDraw.BLACK);
			StdDraw.setPenRadius();
			rect.draw();

			// draw the range search results for brute-force data structure in
			// red
			StdDraw.setPenRadius(.01);
			StdDraw.setPenColor(StdDraw.RED);
			for (final Point2D p : brute.range(rect))
			{
				p.draw();
			}

			// draw the range search results for kd-tree in blue
			StdDraw.setPenRadius(.01);
			StdDraw.setPenColor(StdDraw.BLUE);
			for (final Point2D p : kdtree.range(rect))
			{
				p.draw();
			}

			StdDraw.show(40);
		}
	}
}
