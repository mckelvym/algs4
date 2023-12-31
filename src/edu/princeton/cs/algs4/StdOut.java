/******************************************************************************
 *  Compilation:  javac StdOut.java
 *  Execution:    java StdOut
 *  Dependencies: none
 *
 *  Writes data of various types to standard output.
 *
 ******************************************************************************/

package edu.princeton.cs.algs4;

import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.Locale;

/**
 * <i>Standard output</i>. This class provides methods for writing strings and
 * numbers to standard output.
 * <p>
 * For additional documentation, see <a
 * href="http://introcs.cs.princeton.edu/15inout">Section 1.5</a> of
 * <i>Introduction to Programming in Java: An Interdisciplinary Approach</i> by
 * Robert Sedgewick and Kevin Wayne.
 *
 * @author Robert Sedgewick
 * @author Kevin Wayne
 */
public final class StdOut
{

	// force Unicode UTF-8 encoding; otherwise it's system dependent
	private static final String	CHARSET_NAME	= "UTF-8";

	// assume language = English, country = US for consistency with StdIn
	private static final Locale	LOCALE			= Locale.US;

	// send output here
	private static PrintWriter	out;

	// this is called before invoking any methods
	static
	{
		try
		{
			out = new PrintWriter(new OutputStreamWriter(System.out,
					CHARSET_NAME), true);
		}
		catch (final UnsupportedEncodingException e)
		{
			System.out.println(e);
		}
	}

	/**
	 * Closes standard output.
	 */
	public static void close()
	{
		out.close();
	}

	/**
	 * Simple unit test for <tt>StdOut</tt>.
	 */
	public static void main(final String[] args)
	{

		// write to stdout
		StdOut.println("Test");
		StdOut.println(17);
		StdOut.println(true);
		StdOut.printf("%.6f\n", 1.0 / 7.0);
	}

	/**
	 * Flushes standard output.
	 */
	public static void print()
	{
		out.flush();
	}

	/**
	 * Prints a boolean to standard output and flushes standard output.
	 * 
	 * @param x
	 *            the boolean to print
	 */
	public static void print(final boolean x)
	{
		out.print(x);
		out.flush();
	}

	/**
	 * Prints a byte to standard output and flushes standard output.
	 *
	 * @param x
	 *            the byte to print
	 */
	public static void print(final byte x)
	{
		out.print(x);
		out.flush();
	}

	/**
	 * Prints a character to standard output and flushes standard output.
	 * 
	 * @param x
	 *            the character to print
	 */
	public static void print(final char x)
	{
		out.print(x);
		out.flush();
	}

	/**
	 * Prints a double to standard output and flushes standard output.
	 * 
	 * @param x
	 *            the double to print
	 */
	public static void print(final double x)
	{
		out.print(x);
		out.flush();
	}

	/**
	 * Prints a float to standard output and flushes standard output.
	 * 
	 * @param x
	 *            the float to print
	 */
	public static void print(final float x)
	{
		out.print(x);
		out.flush();
	}

	/**
	 * Prints an integer to standard output and flushes standard output.
	 * 
	 * @param x
	 *            the integer to print
	 */
	public static void print(final int x)
	{
		out.print(x);
		out.flush();
	}

	/**
	 * Prints a long integer to standard output and flushes standard output.
	 * 
	 * @param x
	 *            the long integer to print
	 */
	public static void print(final long x)
	{
		out.print(x);
		out.flush();
	}

	/**
	 * Prints an object to standard output and flushes standard output.
	 * 
	 * @param x
	 *            the object to print
	 */
	public static void print(final Object x)
	{
		out.print(x);
		out.flush();
	}

	/**
	 * Prints a short integer to standard output and flushes standard output.
	 * 
	 * @param x
	 *            the short integer to print
	 */
	public static void print(final short x)
	{
		out.print(x);
		out.flush();
	}

	/**
	 * Prints a formatted string to standard output, using the locale and the
	 * specified format string and arguments; then flushes standard output.
	 *
	 * @param locale
	 *            the locale
	 * @param format
	 *            the format string
	 * @param args
	 *            the arguments accompanying the format string
	 */
	public static void printf(final Locale locale, final String format,
			final Object... args)
	{
		out.printf(locale, format, args);
		out.flush();
	}

	/**
	 * Prints a formatted string to standard output, using the specified format
	 * string and arguments, and then flushes standard output.
	 *
	 * @param format
	 *            the format string
	 * @param args
	 *            the arguments accompanying the format string
	 */
	public static void printf(final String format, final Object... args)
	{
		out.printf(LOCALE, format, args);
		out.flush();
	}

	/**
	 * Terminates the current line by printing the line-separator string.
	 */
	public static void println()
	{
		out.println();
	}

	/**
	 * Prints a boolean to standard output and then terminates the line.
	 *
	 * @param x
	 *            the boolean to print
	 */
	public static void println(final boolean x)
	{
		out.println(x);
	}

	/**
	 * Prints a byte to standard output and then terminates the line.
	 * <p>
	 * To write binary data, see {@link BinaryStdOut}.
	 *
	 * @param x
	 *            the byte to print
	 */
	public static void println(final byte x)
	{
		out.println(x);
	}

	/**
	 * Prints a character to standard output and then terminates the line.
	 *
	 * @param x
	 *            the character to print
	 */
	public static void println(final char x)
	{
		out.println(x);
	}

	/**
	 * Prints a double to standard output and then terminates the line.
	 *
	 * @param x
	 *            the double to print
	 */
	public static void println(final double x)
	{
		out.println(x);
	}

	/**
	 * Prints an integer to standard output and then terminates the line.
	 *
	 * @param x
	 *            the integer to print
	 */
	public static void println(final float x)
	{
		out.println(x);
	}

	/**
	 * Prints an integer to standard output and then terminates the line.
	 *
	 * @param x
	 *            the integer to print
	 */
	public static void println(final int x)
	{
		out.println(x);
	}

	/**
	 * Prints a long to standard output and then terminates the line.
	 *
	 * @param x
	 *            the long to print
	 */
	public static void println(final long x)
	{
		out.println(x);
	}

	/**
	 * Prints an object to this output stream and then terminates the line.
	 *
	 * @param x
	 *            the object to print
	 */
	public static void println(final Object x)
	{
		out.println(x);
	}

	/**
	 * Prints a short integer to standard output and then terminates the line.
	 *
	 * @param x
	 *            the short to print
	 */
	public static void println(final short x)
	{
		out.println(x);
	}

	// don't instantiate
	private StdOut()
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
