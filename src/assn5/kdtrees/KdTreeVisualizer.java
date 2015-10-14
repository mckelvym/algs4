package assn5.kdtrees;

/******************************************************************************
 *  Compilation:  javac KdTreeVisualizer.java
 *  Execution:    java KdTreeVisualizer
 *  Dependencies: KdTree.java
 *
 *  Add the points that the user clicks in the standard draw window
 *  to a kd-tree and draw the resulting kd-tree.
 *
 ******************************************************************************/

import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

public class KdTreeVisualizer
{

	public static void main(final String[] args)
	{
		final RectHV rect = new RectHV(0.0, 0.0, 1.0, 1.0);
		StdDraw.show(0);
		final KdTree kdtree = new KdTree();
		StdDraw.setPenRadius(.01);
		while (true)
		{
			if (StdDraw.mousePressed())
			{
				final double x = StdDraw.mouseX();
				final double y = StdDraw.mouseY();
				StdOut.printf("%8.6f %8.6f\n", x, y);
				final Point2D p = new Point2D(x, y);
				if (rect.contains(p))
				{
					StdOut.printf("%8.6f %8.6f\n", x, y);
					kdtree.insert(p);
					StdDraw.clear();
					kdtree.draw();
				}
			}
			StdDraw.show(50);
		}

	}
}
