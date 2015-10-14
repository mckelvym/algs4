package assn2.queues;

import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;

/**
 * A client program that takes a command-line integer k; reads in a sequence of
 * N strings from standard input using StdIn.readString(); and prints out
 * exactly k of them, uniformly at random. Each item from the sequence can be
 * printed out at most once. Assume that 0 <= k <= N, where N is the number of
 * string on standard input.
 *
 * @author mckelvym
 * @since Sep 14, 2015
 *
 */
public class Subset
{
	/**
	 * Test
	 *
	 * @param args
	 * @since Sep 14, 2015
	 */
	public static void main(final String[] args)
	{
		final int k = Integer.parseInt(args[0]);
		final String[] strings = StdIn.readAllStrings();
		final int[] ks = new int[strings.length];
		for (int i = 0; i < ks.length; i++)
		{
			ks[i] = i;
		}
		StdRandom.shuffle(ks);
		final Deque<String> rq = new Deque<String>();
		for (int i = 0; i < k; i++)
		{
			final String string = strings[ks[i]];
			rq.addLast(string);
		}
		for (int i = 0; i < k; i++)
		{
			StdOut.println(rq.removeFirst());
		}
	}

}
