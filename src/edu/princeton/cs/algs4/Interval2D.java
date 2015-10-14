/******************************************************************************
 *  Compilation:  javac Interval2D.java
 *  Execution:    java Interval2D
 *  Dependencies: StdOut.java Interval1D.java StdDraw.java
 *
 *  2-dimensional interval data type.
 *
 ******************************************************************************/

package edu.princeton.cs.algs4;

/**
 * The <tt>Interval2D</tt> class represents a closed two-dimensional interval,
 * which represents all points (x, y) with both xleft <= x <= xright and yleft
 * <= y <= right. Two-dimensional intervals are immutable: their values cannot
 * be changed after they are created. The class <code>Interval2D</code> includes
 * methods for checking whether a two-dimensional interval contains a point and
 * determining whether two two-dimensional intervals intersect.
 * <p>
 * For additional documentation, see <a
 * href="http://algs4.cs.princeton.edu/12oop">Section 1.2</a> of <i>Algorithms,
 * 4th Edition</i> by Robert Sedgewick and Kevin Wayne.
 *
 * @author Robert Sedgewick
 * @author Kevin Wayne
 */
public class Interval2D
{
	/**
	 * Unit tests the <tt>Interval2D</tt> data type.
	 */
	public static void main(final String[] args)
	{
		final double xlo = Double.parseDouble(args[0]);
		final double xhi = Double.parseDouble(args[1]);
		final double ylo = Double.parseDouble(args[2]);
		final double yhi = Double.parseDouble(args[3]);
		final int T = Integer.parseInt(args[4]);

		final Interval1D xinterval = new Interval1D(xlo, xhi);
		final Interval1D yinterval = new Interval1D(ylo, yhi);
		final Interval2D box = new Interval2D(xinterval, yinterval);
		box.draw();

		final Counter counter = new Counter("hits");
		for (int t = 0; t < T; t++)
		{
			final double x = StdRandom.uniform(0.0, 1.0);
			final double y = StdRandom.uniform(0.0, 1.0);
			final Point2D p = new Point2D(x, y);

			if (box.contains(p))
			{
				counter.increment();
			}
			else
			{
				p.draw();
			}
		}

		StdOut.println(counter);
		StdOut.printf("box area = %.2f\n", box.area());
	}

	private final Interval1D	x;

	private final Interval1D	y;

	/**
	 * Initializes a two-dimensional interval.
	 * 
	 * @param x
	 *            the one-dimensional interval of x-coordinates
	 * @param y
	 *            the one-dimensional interval of y-coordinates
	 */
	public Interval2D(final Interval1D x, final Interval1D y)
	{
		this.x = x;
		this.y = y;
	}

	/**
	 * Returns the area of this two-dimensional interval.
	 * 
	 * @return the area of this two-dimensional interval
	 */
	public double area()
	{
		return x.length() * y.length();
	}

	/**
	 * Does this two-dimensional interval contain the point p?
	 * 
	 * @param p
	 *            the two-dimensional point
	 * @return true if this two-dimensional interval contains the point p; false
	 *         otherwise
	 */
	public boolean contains(final Point2D p)
	{
		return x.contains(p.x()) && y.contains(p.y());
	}

	/**
	 * Draws this two-dimensional interval to standard draw.
	 */
	public void draw()
	{
		final double xc = (x.left() + x.right()) / 2.0;
		final double yc = (y.left() + y.right()) / 2.0;
		StdDraw.rectangle(xc, yc, x.length() / 2.0, y.length() / 2.0);
	}

	/**
	 * Does this interval equal the other interval?
	 * 
	 * @param other
	 *            the other interval
	 * @return true if this interval equals the other interval; false otherwise
	 */
	@Override
	public boolean equals(final Object other)
	{
		if (other == this)
		{
			return true;
		}
		if (other == null)
		{
			return false;
		}
		if (other.getClass() != this.getClass())
		{
			return false;
		}
		final Interval2D that = (Interval2D) other;
		return this.x.equals(that.x) && this.y.equals(that.y);
	}

	/**
	 * Returns an integer hash code for this interval.
	 * 
	 * @return an integer hash code for this interval
	 */
	@Override
	public int hashCode()
	{
		final int hash1 = x.hashCode();
		final int hash2 = y.hashCode();
		return 31 * hash1 + hash2;
	}

	/**
	 * Does this two-dimensional interval intersect that two-dimensional
	 * interval?
	 * 
	 * @param that
	 *            the other two-dimensional interval
	 * @return true if this two-dimensional interval intersects that
	 *         two-dimensional interval; false otherwise
	 */
	public boolean intersects(final Interval2D that)
	{
		if (!this.x.intersects(that.x))
		{
			return false;
		}
		if (!this.y.intersects(that.y))
		{
			return false;
		}
		return true;
	}

	/**
	 * Returns a string representation of this two-dimensional interval.
	 * 
	 * @return a string representation of this two-dimensional interval in the
	 *         form [xleft, xright] x [yleft, yright]
	 */
	@Override
	public String toString()
	{
		return x + " x " + y;
	}
}

/******************************************************************************
 * Copyright 2002-2015, Robert Sedgewick and Kevin Wayne.
 *
 * This file is part of algs4.jar, which accompanies the textbook
 *
 * Algorithms, 4th edition by Robert Sedgewick and Kevin Wayne, Addison-Wesley
 * Professional, 2011, ISBN 0-321-57351-X. http://algs4.cs.princeton.edu
 *
 *
 * algs4.jar is free software: you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later
 * version.
 *
 * algs4.jar is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR
 * A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with
 * algs4.jar. If not, see http://www.gnu.org/licenses.
 ******************************************************************************/
