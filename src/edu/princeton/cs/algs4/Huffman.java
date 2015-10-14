/******************************************************************************
 *  Compilation:  javac Huffman.java
 *  Execution:    java Huffman - < input.txt   (compress)
 *  Execution:    java Huffman + < input.txt   (expand)
 *  Dependencies: BinaryIn.java BinaryOut.java
 *  Data files:   http://algs4.cs.princeton.edu/55compression/abra.txt
 *                http://algs4.cs.princeton.edu/55compression/tinytinyTale.txt
 *
 *  Compress or expand a binary input stream using the Huffman algorithm.
 *
 *  % java Huffman - < abra.txt | java BinaryDump 60
 *  010100000100101000100010010000110100001101010100101010000100
 *  000000000000000000000000000110001111100101101000111110010100
 *  120 bits
 *
 *  % java Huffman - < abra.txt | java Huffman +
 *  ABRACADABRA!
 *
 ******************************************************************************/

package edu.princeton.cs.algs4;

public class Huffman
{

	// Huffman trie node
	private static class Node implements Comparable<Node>
	{
		private final char	ch;
		private final int	freq;
		private final Node	left, right;

		Node(final char ch, final int freq, final Node left, final Node right)
		{
			this.ch = ch;
			this.freq = freq;
			this.left = left;
			this.right = right;
		}

		// compare, based on frequency
		@Override
		public int compareTo(final Node that)
		{
			return this.freq - that.freq;
		}

		// is the node a leaf node?
		private boolean isLeaf()
		{
			assert left == null && right == null || left != null
					&& right != null;
			return left == null && right == null;
		}
	}

	// alphabet size of extended ASCII
	private static final int	R	= 256;

	// make a lookup table from symbols and their encodings
	private static void buildCode(final String[] st, final Node x,
			final String s)
	{
		if (!x.isLeaf())
		{
			buildCode(st, x.left, s + '0');
			buildCode(st, x.right, s + '1');
		}
		else
		{
			st[x.ch] = s;
		}
	}

	// build the Huffman trie given frequencies
	private static Node buildTrie(final int[] freq)
	{

		// initialze priority queue with singleton trees
		final MinPQ<Node> pq = new MinPQ<Node>();
		for (char i = 0; i < R; i++)
		{
			if (freq[i] > 0)
			{
				pq.insert(new Node(i, freq[i], null, null));
			}
		}

		// special case in case there is only one character with a nonzero
		// frequency
		if (pq.size() == 1)
		{
			if (freq['\0'] == 0)
			{
				pq.insert(new Node('\0', 0, null, null));
			}
			else
			{
				pq.insert(new Node('\1', 0, null, null));
			}
		}

		// merge two smallest trees
		while (pq.size() > 1)
		{
			final Node left = pq.delMin();
			final Node right = pq.delMin();
			final Node parent = new Node('\0', left.freq + right.freq, left,
					right);
			pq.insert(parent);
		}
		return pq.delMin();
	}

	// compress bytes from standard input and write to standard output
	public static void compress()
	{
		// read the input
		final String s = BinaryStdIn.readString();
		final char[] input = s.toCharArray();

		// tabulate frequency counts
		final int[] freq = new int[R];
		for (int i = 0; i < input.length; i++)
		{
			freq[input[i]]++;
		}

		// build Huffman trie
		final Node root = buildTrie(freq);

		// build code table
		final String[] st = new String[R];
		buildCode(st, root, "");

		// print trie for decoder
		writeTrie(root);

		// print number of bytes in original uncompressed message
		BinaryStdOut.write(input.length);

		// use Huffman code to encode input
		for (final char element : input)
		{
			final String code = st[element];
			for (int j = 0; j < code.length(); j++)
			{
				if (code.charAt(j) == '0')
				{
					BinaryStdOut.write(false);
				}
				else if (code.charAt(j) == '1')
				{
					BinaryStdOut.write(true);
				}
				else
				{
					throw new IllegalStateException("Illegal state");
				}
			}
		}

		// close output stream
		BinaryStdOut.close();
	}

	// expand Huffman-encoded input from standard input and write to standard
	// output
	public static void expand()
	{

		// read in Huffman trie from input stream
		final Node root = readTrie();

		// number of bytes to write
		final int length = BinaryStdIn.readInt();

		// decode using the Huffman trie
		for (int i = 0; i < length; i++)
		{
			Node x = root;
			while (!x.isLeaf())
			{
				final boolean bit = BinaryStdIn.readBoolean();
				if (bit)
				{
					x = x.right;
				}
				else
				{
					x = x.left;
				}
			}
			BinaryStdOut.write(x.ch, 8);
		}
		BinaryStdOut.close();
	}

	public static void main(final String[] args)
	{
		if (args[0].equals("-"))
		{
			compress();
		}
		else if (args[0].equals("+"))
		{
			expand();
		}
		else
		{
			throw new IllegalArgumentException("Illegal command line argument");
		}
	}

	private static Node readTrie()
	{
		final boolean isLeaf = BinaryStdIn.readBoolean();
		if (isLeaf)
		{
			return new Node(BinaryStdIn.readChar(), -1, null, null);
		}
		else
		{
			return new Node('\0', -1, readTrie(), readTrie());
		}
	}

	// write bitstring-encoded trie to standard output
	private static void writeTrie(final Node x)
	{
		if (x.isLeaf())
		{
			BinaryStdOut.write(true);
			BinaryStdOut.write(x.ch, 8);
			return;
		}
		BinaryStdOut.write(false);
		writeTrie(x.left);
		writeTrie(x.right);
	}

	// Do not instantiate.
	private Huffman()
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
