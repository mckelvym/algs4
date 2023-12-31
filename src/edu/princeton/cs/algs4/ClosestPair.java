/******************************************************************************
 *  Compilation:  javac ClosestPair.java
 *  Execution:    java ClosestPair < input.txt
 *  Dependencies: Point2D.java
 *
 *  Given N points in the plane, find the closest pair in N log N time.
 *
 *  Note: could speed it up by comparing square of Euclidean distances
 *  instead of Euclidean distances.
 *
 ******************************************************************************/

package edu.princeton.cs.algs4;

import java.util.Arrays;

public class ClosestPair
{

	// is v < w ?
	private static boolean less(final Comparable v, final Comparable w)
	{
		return v.compareTo(w) < 0;
	}

	public static void main(final String[] args)
	{
		final int N = StdIn.readInt();
		final Point2D[] points = new Point2D[N];
		for (int i = 0; i < N; i++)
		{
			final double x = StdIn.readDouble();
			final double y = StdIn.readDouble();
			points[i] = new Point2D(x, y);
		}
		final ClosestPair closest = new ClosestPair(points);
		StdOut.println(closest.distance() + " from " + closest.either()
				+ " to " + closest.other());
	}

	// stably merge a[lo .. mid] with a[mid+1 ..hi] using aux[lo .. hi]
	// precondition: a[lo .. mid] and a[mid+1 .. hi] are sorted subarrays
	private static void merge(final Comparable[] a, final Comparable[] aux,
			final int lo, final int mid, final int hi)
	{
		// copy to aux[]
		for (int k = lo; k <= hi; k++)
		{
			aux[k] = a[k];
		}

		// merge back to a[]
		int i = lo, j = mid + 1;
		for (int k = lo; k <= hi; k++)
		{
			if (i > mid)
			{
				a[k] = aux[j++];
			}
			else if (j > hi)
			{
				a[k] = aux[i++];
			}
			else if (less(aux[j], aux[i]))
			{
				a[k] = aux[j++];
			}
			else
			{
				a[k] = aux[i++];
			}
		}
	}

	// closest pair of points and their Euclidean distance
	private Point2D	best1, best2;

	private double	bestDistance	= Double.POSITIVE_INFINITY;

	public ClosestPair(final Point2D[] points)
	{
		final int N = points.length;
		if (N <= 1)
		{
			return;
		}

		// sort by x-coordinate (breaking ties by y-coordinate)
		final Point2D[] pointsByX = new Point2D[N];
		for (int i = 0; i < N; i++)
		{
			pointsByX[i] = points[i];
		}
		Arrays.sort(pointsByX, Point2D.X_ORDER);

		// check for coincident points
		for (int i = 0; i < N - 1; i++)
		{
			if (pointsByX[i].equals(pointsByX[i + 1]))
			{
				bestDistance = 0.0;
				best1 = pointsByX[i];
				best2 = pointsByX[i + 1];
				return;
			}
		}

		// sort by y-coordinate (but not yet sorted)
		final Point2D[] pointsByY = new Point2D[N];
		for (int i = 0; i < N; i++)
		{
			pointsByY[i] = pointsByX[i];
		}

		// auxiliary array
		final Point2D[] aux = new Point2D[N];

		closest(pointsByX, pointsByY, aux, 0, N - 1);
	}

	// find closest pair of points in pointsByX[lo..hi]
	// precondition: pointsByX[lo..hi] and pointsByY[lo..hi] are the same
	// sequence of points
	// precondition: pointsByX[lo..hi] sorted by x-coordinate
	// postcondition: pointsByY[lo..hi] sorted by y-coordinate
	private double closest(final Point2D[] pointsByX,
			final Point2D[] pointsByY, final Point2D[] aux, final int lo,
			final int hi)
	{
		if (hi <= lo)
		{
			return Double.POSITIVE_INFINITY;
		}

		final int mid = lo + (hi - lo) / 2;
		final Point2D median = pointsByX[mid];

		// compute closest pair with both endpoints in left subarray or both in
		// right subarray
		final double delta1 = closest(pointsByX, pointsByY, aux, lo, mid);
		final double delta2 = closest(pointsByX, pointsByY, aux, mid + 1, hi);
		double delta = Math.min(delta1, delta2);

		// merge back so that pointsByY[lo..hi] are sorted by y-coordinate
		merge(pointsByY, aux, lo, mid, hi);

		// aux[0..M-1] = sequence of points closer than delta, sorted by
		// y-coordinate
		int M = 0;
		for (int i = lo; i <= hi; i++)
		{
			if (Math.abs(pointsByY[i].x() - median.x()) < delta)
			{
				aux[M++] = pointsByY[i];
			}
		}

		// compare each point to its neighbors with y-coordinate closer than
		// delta
		for (int i = 0; i < M; i++)
		{
			// a geometric packing argument shows that this loop iterates at
			// most 7 times
			for (int j = i + 1; j < M && aux[j].y() - aux[i].y() < delta; j++)
			{
				final double distance = aux[i].distanceTo(aux[j]);
				if (distance < delta)
				{
					delta = distance;
					if (distance < bestDistance)
					{
						bestDistance = delta;
						best1 = aux[i];
						best2 = aux[j];
						// StdOut.println("better distance = " + delta +
						// " from " + best1 + " to " + best2);
					}
				}
			}
		}
		return delta;
	}

	public double distance()
	{
		return bestDistance;
	}

	public Point2D either()
	{
		return best1;
	}

	public Point2D other()
	{
		return best2;
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
