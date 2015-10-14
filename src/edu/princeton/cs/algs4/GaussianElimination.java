/******************************************************************************
 *  Compilation:  javac GaussianElimination.java
 *  Execution:    java GaussianElimination
 *  Dependencies: StdOut.java
 *
 *  Gaussian elimination with partial pivoting.
 *
 *  % java GaussianElimination
 *  -1.0
 *  2.0
 *  2.0
 *
 ******************************************************************************/

package edu.princeton.cs.algs4;

public class GaussianElimination
{
	private static final double	EPSILON	= 1e-10;

	// Gaussian elimination with partial pivoting
	public static double[] lsolve(final double[][] A, final double[] b)
	{
		final int N = b.length;

		for (int p = 0; p < N; p++)
		{

			// find pivot row and swap
			int max = p;
			for (int i = p + 1; i < N; i++)
			{
				if (Math.abs(A[i][p]) > Math.abs(A[max][p]))
				{
					max = i;
				}
			}

			swap(A, p, max);
			swap(b, p, max);

			// singular or nearly singular
			if (Math.abs(A[p][p]) <= EPSILON)
			{
				throw new ArithmeticException(
						"Matrix is singular or nearly singular");
			}

			// pivot within A and b
			for (int i = p + 1; i < N; i++)
			{
				final double alpha = A[i][p] / A[p][p];
				b[i] -= alpha * b[p];
				for (int j = p; j < N; j++)
				{
					A[i][j] -= alpha * A[p][j];
				}
			}
		}

		// back substitution
		final double[] x = new double[N];
		for (int i = N - 1; i >= 0; i--)
		{
			double sum = 0.0;
			for (int j = i + 1; j < N; j++)
			{
				sum += A[i][j] * x[j];
			}
			x[i] = (b[i] - sum) / A[i][i];
		}
		return x;
	}

	// sample client
	public static void main(final String[] args)
	{
		final int N = 3;
		final double[][] A = { { 0, 1, 1 }, { 2, 4, -2 }, { 0, 3, 15 } };
		final double[] b = { 4, 2, 36 };
		final double[] x = lsolve(A, b);

		// print results
		for (int i = 0; i < N; i++)
		{
			StdOut.println(x[i]);
		}

	}

	// swap entries a[i] and a[j] in array a[]
	private static void swap(final double[] a, final int i, final int j)
	{
		final double temp = a[i];
		a[i] = a[j];
		a[j] = temp;
	}

	// swap rows A[i][] and A[j][] in 2D array A[][]
	private static void swap(final double[][] A, final int i, final int j)
	{
		final double[] temp = A[i];
		A[i] = A[j];
		A[j] = temp;
	}

	// Do not instantiate.
	private GaussianElimination()
	{
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
