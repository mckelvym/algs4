/******************************************************************************
 *  Compilation:  javac BinaryStdOut.java
 *  Execution:    java BinaryStdOut
 *  Dependencies: none
 *
 *  Write binary data to standard output, either one 1-bit boolean,
 *  one 8-bit char, one 32-bit int, one 64-bit double, one 32-bit float,
 *  or one 64-bit long at a time.
 *
 *  The bytes written are not aligned.
 *
 ******************************************************************************/

package edu.princeton.cs.algs4;

import java.io.BufferedOutputStream;
import java.io.IOException;

/**
 * <i>Binary standard output</i>. This class provides methods for converting
 * primtive type variables (<tt>boolean</tt>, <tt>byte</tt>, <tt>char</tt>,
 * <tt>int</tt>, <tt>long</tt>, <tt>float</tt>, and <tt>double</tt>) to
 * sequences of bits and writing them to standard output. Uses big-endian
 * (most-significant byte first).
 * <p>
 * The client must <tt>flush()</tt> the output stream when finished writing
 * bits.
 * <p>
 * The client should not intermixing calls to <tt>BinaryStdOut</tt> with calls
 * to <tt>StdOut</tt> or <tt>System.out</tt>; otherwise unexpected behavior will
 * result.
 *
 * @author Robert Sedgewick
 * @author Kevin Wayne
 */
public final class BinaryStdOut
{
	private static int					buffer;					// 8-bit
																	// buffer of
																	// bits to
																	// write out

	private static int					n;							// number of
																	// bits
																	// remaining
																	// in buffer
	private static BufferedOutputStream	out	= new BufferedOutputStream(
													System.out);

	// write out any remaining bits in buffer to standard output, padding with
	// 0s
	private static void clearBuffer()
	{
		if (n == 0)
		{
			return;
		}
		if (n > 0)
		{
			buffer <<= 8 - n;
		}
		try
		{
			out.write(buffer);
		}
		catch (final IOException e)
		{
			e.printStackTrace();
		}
		n = 0;
		buffer = 0;
	}

	/**
	 * Flush and close standard output. Once standard output is closed, you can
	 * no longer write bits to it.
	 */
	public static void close()
	{
		flush();
		try
		{
			out.close();
		}
		catch (final IOException e)
		{
			e.printStackTrace();
		}
	}

	/**
	 * Flush standard output, padding 0s if number of bits written so far is not
	 * a multiple of 8.
	 */
	public static void flush()
	{
		clearBuffer();
		try
		{
			out.flush();
		}
		catch (final IOException e)
		{
			e.printStackTrace();
		}
	}

	/**
	 * Test client.
	 */
	public static void main(final String[] args)
	{
		final int T = Integer.parseInt(args[0]);
		// write to standard output
		for (int i = 0; i < T; i++)
		{
			BinaryStdOut.write(i);
		}
		BinaryStdOut.flush();
	}

	/**
	 * Write the specified bit to standard output.
	 * 
	 * @param x
	 *            the <tt>boolean</tt> to write.
	 */
	public static void write(final boolean x)
	{
		writeBit(x);
	}

	/**
	 * Write the 8-bit byte to standard output.
	 * 
	 * @param x
	 *            the <tt>byte</tt> to write.
	 */
	public static void write(final byte x)
	{
		writeByte(x & 0xff);
	}

	/**
	 * Write the 8-bit char to standard output.
	 * 
	 * @param x
	 *            the <tt>char</tt> to write.
	 * @throws IllegalArgumentException
	 *             if <tt>x</tt> is not betwen 0 and 255.
	 */
	public static void write(final char x)
	{
		if (x < 0 || x >= 256)
		{
			throw new IllegalArgumentException("Illegal 8-bit char = " + x);
		}
		writeByte(x);
	}

	/**
	 * Write the r-bit char to standard output.
	 * 
	 * @param x
	 *            the <tt>char</tt> to write.
	 * @param r
	 *            the number of relevant bits in the char.
	 * @throws IllegalArgumentException
	 *             if <tt>r</tt> is not between 1 and 16.
	 * @throws IllegalArgumentException
	 *             if <tt>x</tt> is not between 0 and 2<sup>r</sup> - 1.
	 */
	public static void write(final char x, final int r)
	{
		if (r == 8)
		{
			write(x);
			return;
		}
		if (r < 1 || r > 16)
		{
			throw new IllegalArgumentException("Illegal value for r = " + r);
		}
		if (x >= 1 << r)
		{
			throw new IllegalArgumentException("Illegal " + r + "-bit char = "
					+ x);
		}
		for (int i = 0; i < r; i++)
		{
			final boolean bit = (x >>> r - i - 1 & 1) == 1;
			writeBit(bit);
		}
	}

	/**
	 * Write the 64-bit double to standard output.
	 * 
	 * @param x
	 *            the <tt>double</tt> to write.
	 */
	public static void write(final double x)
	{
		write(Double.doubleToRawLongBits(x));
	}

	/**
	 * Write the 32-bit float to standard output.
	 * 
	 * @param x
	 *            the <tt>float</tt> to write.
	 */
	public static void write(final float x)
	{
		write(Float.floatToRawIntBits(x));
	}

	/**
	 * Write the 32-bit int to standard output.
	 * 
	 * @param x
	 *            the <tt>int</tt> to write.
	 */
	public static void write(final int x)
	{
		writeByte(x >>> 24 & 0xff);
		writeByte(x >>> 16 & 0xff);
		writeByte(x >>> 8 & 0xff);
		writeByte(x >>> 0 & 0xff);
	}

	/**
	 * Write the r-bit int to standard output.
	 * 
	 * @param x
	 *            the <tt>int</tt> to write.
	 * @param r
	 *            the number of relevant bits in the char.
	 * @throws IllegalArgumentException
	 *             if <tt>r</tt> is not between 1 and 32.
	 * @throws IllegalArgumentException
	 *             if <tt>x</tt> is not between 0 and 2<sup>r</sup> - 1.
	 */
	public static void write(final int x, final int r)
	{
		if (r == 32)
		{
			write(x);
			return;
		}
		if (r < 1 || r > 32)
		{
			throw new IllegalArgumentException("Illegal value for r = " + r);
		}
		if (x < 0 || x >= 1 << r)
		{
			throw new IllegalArgumentException("Illegal " + r + "-bit char = "
					+ x);
		}
		for (int i = 0; i < r; i++)
		{
			final boolean bit = (x >>> r - i - 1 & 1) == 1;
			writeBit(bit);
		}
	}

	/**
	 * Write the 64-bit long to standard output.
	 * 
	 * @param x
	 *            the <tt>long</tt> to write.
	 */
	public static void write(final long x)
	{
		writeByte((int) (x >>> 56 & 0xff));
		writeByte((int) (x >>> 48 & 0xff));
		writeByte((int) (x >>> 40 & 0xff));
		writeByte((int) (x >>> 32 & 0xff));
		writeByte((int) (x >>> 24 & 0xff));
		writeByte((int) (x >>> 16 & 0xff));
		writeByte((int) (x >>> 8 & 0xff));
		writeByte((int) (x >>> 0 & 0xff));
	}

	/**
	 * Write the 16-bit int to standard output.
	 * 
	 * @param x
	 *            the <tt>short</tt> to write.
	 */
	public static void write(final short x)
	{
		writeByte(x >>> 8 & 0xff);
		writeByte(x >>> 0 & 0xff);
	}

	/**
	 * Write the string of 8-bit characters to standard output.
	 * 
	 * @param s
	 *            the <tt>String</tt> to write.
	 * @throws IllegalArgumentException
	 *             if any character in the string is not between 0 and 255.
	 */
	public static void write(final String s)
	{
		for (int i = 0; i < s.length(); i++)
		{
			write(s.charAt(i));
		}
	}

	/**
	 * Write the String of r-bit characters to standard output.
	 * 
	 * @param s
	 *            the <tt>String</tt> to write.
	 * @param r
	 *            the number of relevants bits in each character.
	 * @throws IllegalArgumentException
	 *             if r is not between 1 and 16.
	 * @throws IllegalArgumentException
	 *             if any character in the string is not between 0 and
	 *             2<sup>r</sup> - 1.
	 */
	public static void write(final String s, final int r)
	{
		for (int i = 0; i < s.length(); i++)
		{
			write(s.charAt(i), r);
		}
	}

	/**
	 * Write the specified bit to standard output.
	 */
	private static void writeBit(final boolean bit)
	{
		// add bit to buffer
		buffer <<= 1;
		if (bit)
		{
			buffer |= 1;
		}

		// if buffer is full (8 bits), write out as a single byte
		n++;
		if (n == 8)
		{
			clearBuffer();
		}
	}

	/**
	 * Write the 8-bit byte to standard output.
	 */
	private static void writeByte(final int x)
	{
		assert x >= 0 && x < 256;

		// optimized if byte-aligned
		if (n == 0)
		{
			try
			{
				out.write(x);
			}
			catch (final IOException e)
			{
				e.printStackTrace();
			}
			return;
		}

		// otherwise write one bit at a time
		for (int i = 0; i < 8; i++)
		{
			final boolean bit = (x >>> 8 - i - 1 & 1) == 1;
			writeBit(bit);
		}
	}

	// don't instantiate
	private BinaryStdOut()
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
