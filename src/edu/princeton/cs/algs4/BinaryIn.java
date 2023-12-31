/******************************************************************************
 *  Compilation:  javac BinaryIn.java
 *  Execution:    java BinaryIn input output
 *  Dependencies: none
 *
 *  This library is for reading binary data from an input stream.
 *
 *  % java BinaryIn http://introcs.cs.princeton.edu/cover.jpg output.jpg
 *
 ******************************************************************************/

package edu.princeton.cs.algs4;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.net.URL;
import java.net.URLConnection;

/**
 * <i>Binary input</i>. This class provides methods for reading in bits from a
 * binary input stream, either one bit at a time (as a <tt>boolean</tt>), 8 bits
 * at a time (as a <tt>byte</tt> or <tt>char</tt>), 16 bits at a time (as a
 * <tt>short</tt>), 32 bits at a time (as an <tt>int</tt> or <tt>float</tt>), or
 * 64 bits at a time (as a <tt>double</tt> or <tt>long</tt>).
 * <p>
 * The binary input stream can be from standard input, a filename, a URL name, a
 * Socket, or an InputStream.
 * <p>
 * All primitive types are assumed to be represented using their standard Java
 * representations, in big-endian (most significant byte first) order.
 * <p>
 * The client should not intermix calls to <tt>BinaryIn</tt> with calls to
 * <tt>In</tt>; otherwise unexpected behavior will result.
 *
 * @author Robert Sedgewick
 * @author Kevin Wayne
 */
public final class BinaryIn
{
	private static final int	EOF	= -1;	// end of file

	/**
	 * Unit tests the <tt>BinaryIn</tt> data type. Reads the name of a file or
	 * URL (first command-line argument) and writes it to a file (second
	 * command-line argument).
	 */
	public static void main(final String[] args)
	{
		final BinaryIn in = new BinaryIn(args[0]);
		final BinaryOut out = new BinaryOut(args[1]);

		// read one 8-bit char at a time
		while (!in.isEmpty())
		{
			final char c = in.readChar();
			out.write(c);
		}
		out.flush();
	}

	private int					buffer; // one character buffer
	private BufferedInputStream	in;	// the input stream

	private int					n;		// number of bits left in buffer

	/**
	 * Initializes a binary input stream from standard input.
	 */
	public BinaryIn()
	{
		in = new BufferedInputStream(System.in);
		fillBuffer();
	}

	/**
	 * Initializes a binary input stream from an <tt>InputStream</tt>.
	 *
	 * @param is
	 *            the <tt>InputStream</tt> object
	 */
	public BinaryIn(final InputStream is)
	{
		in = new BufferedInputStream(is);
		fillBuffer();
	}

	/**
	 * Initializes a binary input stream from a socket.
	 *
	 * @param socket
	 *            the socket
	 */
	public BinaryIn(final Socket socket)
	{
		try
		{
			final InputStream is = socket.getInputStream();
			in = new BufferedInputStream(is);
			fillBuffer();
		}
		catch (final IOException ioe)
		{
			System.err.println("Could not open " + socket);
		}
	}

	/**
	 * Initializes a binary input stream from a filename or URL name.
	 *
	 * @param name
	 *            the name of the file or URL
	 */
	public BinaryIn(final String name)
	{

		try
		{
			// first try to read file from local file system
			final File file = new File(name);
			if (file.exists())
			{
				final FileInputStream fis = new FileInputStream(file);
				in = new BufferedInputStream(fis);
				fillBuffer();
				return;
			}

			// next try for files included in jar
			URL url = getClass().getResource(name);

			// or URL from web
			if (url == null)
			{
				url = new URL(name);
			}

			final URLConnection site = url.openConnection();
			final InputStream is = site.getInputStream();
			in = new BufferedInputStream(is);
			fillBuffer();
		}
		catch (final IOException ioe)
		{
			System.err.println("Could not open " + name);
		}
	}

	/**
	 * Initializes a binary input stream from a URL.
	 *
	 * @param url
	 *            the URL
	 */
	public BinaryIn(final URL url)
	{
		try
		{
			final URLConnection site = url.openConnection();
			final InputStream is = site.getInputStream();
			in = new BufferedInputStream(is);
			fillBuffer();
		}
		catch (final IOException ioe)
		{
			System.err.println("Could not open " + url);
		}
	}

	/**
	 * Returns true if this binary input stream exists.
	 *
	 * @return <tt>true</tt> if this binary input stream exists; <tt>false</tt>
	 *         otherwise
	 */
	public boolean exists()
	{
		return in != null;
	}

	private void fillBuffer()
	{
		try
		{
			buffer = in.read();
			n = 8;
		}
		catch (final IOException e)
		{
			System.err.println("EOF");
			buffer = EOF;
			n = -1;
		}
	}

	/**
	 * Returns true if this binary input stream is empty.
	 *
	 * @return <tt>true</tt> if this binary input stream is empty;
	 *         <tt>false</tt> otherwise
	 */
	public boolean isEmpty()
	{
		return buffer == EOF;
	}

	/**
	 * Reads the next bit of data from this binary input stream and return as a
	 * boolean.
	 *
	 * @return the next bit of data from this binary input stream as a
	 *         <tt>boolean</tt>
	 * @throws RuntimeException
	 *             if this binary input stream is empty
	 */
	public boolean readBoolean()
	{
		if (isEmpty())
		{
			throw new RuntimeException("Reading from empty input stream");
		}
		n--;
		final boolean bit = (buffer >> n & 1) == 1;
		if (n == 0)
		{
			fillBuffer();
		}
		return bit;
	}

	/**
	 * Reads the next 8 bits from this binary input stream and return as an
	 * 8-bit byte.
	 *
	 * @return the next 8 bits of data from this binary input stream as a
	 *         <tt>byte</tt>
	 * @throws RuntimeException
	 *             if there are fewer than 8 bits available
	 */
	public byte readByte()
	{
		final char c = readChar();
		final byte x = (byte) (c & 0xff);
		return x;
	}

	/**
	 * Reads the next 8 bits from this binary input stream and return as an
	 * 8-bit char.
	 *
	 * @return the next 8 bits of data from this binary input stream as a
	 *         <tt>char</tt>
	 * @throws RuntimeException
	 *             if there are fewer than 8 bits available
	 */
	public char readChar()
	{
		if (isEmpty())
		{
			throw new RuntimeException("Reading from empty input stream");
		}

		// special case when aligned byte
		if (n == 8)
		{
			final int x = buffer;
			fillBuffer();
			return (char) (x & 0xff);
		}

		// combine last N bits of current buffer with first 8-N bits of new
		// buffer
		int x = buffer;
		x <<= 8 - n;
		final int oldN = n;
		fillBuffer();
		if (isEmpty())
		{
			throw new RuntimeException("Reading from empty input stream");
		}
		n = oldN;
		x |= buffer >>> n;
		return (char) (x & 0xff);
		// the above code doesn't quite work for the last character if N = 8
		// because buffer will be -1
	}

	/**
	 * Reads the next r bits from this binary input stream and return as an
	 * r-bit character.
	 *
	 * @param r
	 *            number of bits to read
	 * @return the next r bits of data from this binary input streamt as a
	 *         <tt>char</tt>
	 * @throws RuntimeException
	 *             if there are fewer than r bits available
	 */
	public char readChar(final int r)
	{
		if (r < 1 || r > 16)
		{
			throw new RuntimeException("Illegal value of r = " + r);
		}

		// optimize r = 8 case
		if (r == 8)
		{
			return readChar();
		}

		char x = 0;
		for (int i = 0; i < r; i++)
		{
			x <<= 1;
			final boolean bit = readBoolean();
			if (bit)
			{
				x |= 1;
			}
		}
		return x;
	}

	/**
	 * Reads the next 64 bits from this binary input stream and return as a
	 * 64-bit double.
	 *
	 * @return the next 64 bits of data from this binary input stream as a
	 *         <tt>double</tt>
	 * @throws RuntimeException
	 *             if there are fewer than 64 bits available
	 */
	public double readDouble()
	{
		return Double.longBitsToDouble(readLong());
	}

	/**
	 * Reads the next 32 bits from standard input and return as a 32-bit float.
	 *
	 * @return the next 32 bits of data from standard input as a <tt>float</tt>
	 * @throws RuntimeException
	 *             if there are fewer than 32 bits available on standard input
	 */
	public float readFloat()
	{
		return Float.intBitsToFloat(readInt());
	}

	/**
	 * Reads the next 32 bits from this binary input stream and return as a
	 * 32-bit int.
	 *
	 * @return the next 32 bits of data from this binary input stream as a
	 *         <tt>int</tt>
	 * @throws RuntimeException
	 *             if there are fewer than 32 bits available
	 */
	public int readInt()
	{
		int x = 0;
		for (int i = 0; i < 4; i++)
		{
			final char c = readChar();
			x <<= 8;
			x |= c;
		}
		return x;
	}

	/**
	 * Reads the next r bits from this binary input stream return as an r-bit
	 * int.
	 *
	 * @param r
	 *            number of bits to read
	 * @return the next r bits of data from this binary input stream as a
	 *         <tt>int</tt>
	 * @throws RuntimeException
	 *             if there are fewer than r bits available on standard input
	 */
	public int readInt(final int r)
	{
		if (r < 1 || r > 32)
		{
			throw new RuntimeException("Illegal value of r = " + r);
		}

		// optimize r = 32 case
		if (r == 32)
		{
			return readInt();
		}

		int x = 0;
		for (int i = 0; i < r; i++)
		{
			x <<= 1;
			final boolean bit = readBoolean();
			if (bit)
			{
				x |= 1;
			}
		}
		return x;
	}

	/**
	 * Reads the next 64 bits from this binary input stream and return as a
	 * 64-bit long.
	 *
	 * @return the next 64 bits of data from this binary input stream as a
	 *         <tt>long</tt>
	 * @throws RuntimeException
	 *             if there are fewer than 64 bits available
	 */
	public long readLong()
	{
		long x = 0;
		for (int i = 0; i < 8; i++)
		{
			final char c = readChar();
			x <<= 8;
			x |= c;
		}
		return x;
	}

	/**
	 * Reads the next 16 bits from this binary input stream and return as a
	 * 16-bit short.
	 *
	 * @return the next 16 bits of data from this binary standard input as a
	 *         <tt>short</tt>
	 * @throws RuntimeException
	 *             if there are fewer than 16 bits available
	 */
	public short readShort()
	{
		short x = 0;
		for (int i = 0; i < 2; i++)
		{
			final char c = readChar();
			x <<= 8;
			x |= c;
		}
		return x;
	}

	/**
	 * Reads the remaining bytes of data from this binary input stream and
	 * return as a string.
	 *
	 * @return the remaining bytes of data from this binary input stream as a
	 *         <tt>String</tt>
	 * @throws RuntimeException
	 *             if this binary input stream is empty or if the number of bits
	 *             available is not a multiple of 8 (byte-aligned)
	 */
	public String readString()
	{
		if (isEmpty())
		{
			throw new RuntimeException("Reading from empty input stream");
		}

		final StringBuilder sb = new StringBuilder();
		while (!isEmpty())
		{
			final char c = readChar();
			sb.append(c);
		}
		return sb.toString();
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
