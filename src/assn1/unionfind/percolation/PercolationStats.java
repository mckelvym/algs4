package assn1.unionfind.percolation;

import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;

/**
 * Performs a series of computational experiments and stores the results.
 *
 * @author mckelvym
 * @since Sep 6, 2015
 *
 */
public class PercolationStats
{
	/**
	 * Test client
	 *
	 * @param args
	 *            expected length of 2 with: N, T
	 * @since Sep 6, 2015
	 */
	public static void main(final String[] args)
	{
		if (args.length != 2)
		{
			StdOut.println("Please provide N, T for the N-by-N grid with T Monte Carlo simulations.");
			System.exit(1);
		}

		int n = 5;
		int t = 1;

		try
		{
			n = Integer.parseInt(args[0]);
		}
		catch (final NumberFormatException e)
		{
			StdOut.printf(
					"Unable to get the value of 'N' from '%s'. Using '%s' instead.%n",
					args[0], n);
		}

		try
		{
			t = Integer.parseInt(args[1]);
		}
		catch (final NumberFormatException e)
		{
			StdOut.printf(
					"Unable to get the value of 'T' from '%s'. Using '%s' instead.%n",
					args[1], t);
		}

		final PercolationStats percolationStats = new PercolationStats(n, t);
		StdOut.printf("mean\t\t\t= %s%n", percolationStats.mean());
		StdOut.printf("stddev\t\t\t= %s%n", percolationStats.stddev());
		StdOut.printf("95%% confidence interval = %s, %s%n",
				percolationStats.confidenceLo(),
				percolationStats.confidenceHi());
	}

	/**
	 * The results between 0 and 1 for each trial.
	 *
	 * @since Sep 10, 2015
	 */
	private double[]	m_Results;

	/**
	 * Do not use.
	 *
	 * @since Sep 6, 2015
	 */
	public PercolationStats()
	{
		throw new IllegalStateException(
				"Default constructor should not be used.");
	}

	/**
	 * Perform <code>p_NumExperiments</code> independent experiments on an
	 * N-by-N grid
	 *
	 * @param p_N
	 *            the square root of the number of sites
	 * @param p_NumExperiments
	 *            the number of trials to run
	 * @since Sep 7, 2015
	 */
	public PercolationStats(final int p_N, final int p_NumExperiments)
	{
		if (p_N <= 0)
		{
			throw new IllegalArgumentException("The value of 'N' must be > 0");
		}
		if (p_NumExperiments <= 0)
		{
			throw new IllegalArgumentException(
					"The number of experiments must be > 0");
		}
		m_Results = new double[p_NumExperiments];

		for (Integer experimentIndex = 0; experimentIndex < p_NumExperiments; experimentIndex++)
		{
			final Integer gridSize = p_N * p_N;
			final Percolation p = new Percolation(p_N);

			final int[] cellsToOpen = new int[gridSize];
			for (int cellIndex = 0; cellIndex < gridSize; cellIndex++)
			{
				cellsToOpen[cellIndex] = cellIndex;
			}

			StdRandom.shuffle(cellsToOpen);

			boolean percolated = false;
			for (Integer cellCount = 0; cellCount < gridSize; cellCount++)
			{
				if (p.percolates())
				{
					m_Results[experimentIndex] = cellCount.doubleValue()
							/ gridSize.doubleValue();
					percolated = true;
					break;
				}

				final int cell = cellsToOpen[cellCount];
				final int row = cell / p_N + 1;
				final int col = cell % p_N + 1;

				p.open(row, col);
			}

			if (!percolated && p.percolates())
			{
				m_Results[experimentIndex] = 1.0;
			}
		}
	}

	/**
	 * High endpoint of 95% confidence interval
	 *
	 * @return the high endpoint of 95% confidence interval
	 * @since Sep 7, 2015
	 */
	public double confidenceHi()
	{
		return mean() + 1.96 * stddev() / Math.sqrt(m_Results.length);
	}

	/**
	 * Low endpoint of 95% confidence interval
	 *
	 * @return the low endpoint of 95% confidence interval
	 * @since Sep 7, 2015
	 */
	public double confidenceLo()
	{
		return mean() - 1.96 * stddev() / Math.sqrt(m_Results.length);
	}

	/**
	 * Sample mean of percolation threshold
	 *
	 * @return the sample mean of percolation threshold
	 * @since Sep 7, 2015
	 */
	public double mean()
	{
		return StdStats.mean(m_Results);
	}

	/**
	 * Sample standard deviation of percolation threshold
	 *
	 * @return the sample standard deviation of percolation threshold
	 * @since Sep 7, 2015
	 */
	public double stddev()
	{
		return StdStats.stddev(m_Results);
	}
}
