/******************************************************************************
 *  Compilation:  javac Draw.java
 *  Execution:    java Draw
 *  Dependencies: none
 *
 *  Drawing library. This class provides a basic capability for creating
 *  drawings with your programs. It uses a simple graphics model that
 *  allows you to create drawings consisting of points, lines, and curves
 *  in a window on your computer and to save the drawings to a file.
 *  This is the object-oriented version of standard draw; it supports
 *  multiple indepedent drawing windows.
 *
 *  Todo
 *  ----
 *    -  Add support for gradient fill, etc.
 *
 *  Remarks
 *  -------
 *    -  don't use AffineTransform for rescaling since it inverts
 *       images and strings
 *    -  careful using setFont in inner loop within an animation -
 *       it can cause flicker
 *
 ******************************************************************************/

package edu.princeton.cs.algs4;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.FileDialog;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.MediaTracker;
import java.awt.RenderingHints;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.geom.Arc2D;
import java.awt.geom.Ellipse2D;
import java.awt.geom.GeneralPath;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.awt.image.DirectColorModel;
import java.awt.image.WritableRaster;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.TreeSet;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.KeyStroke;
import javax.swing.WindowConstants;

/**
 * <i>Draw</i>. This class provides a basic capability for creating drawings
 * with your programs. It uses a simple graphics model that allows you to create
 * drawings consisting of points, lines, and curves in a window on your computer
 * and to save the drawings to a file. This is the object-oriented version of
 * standard draw; it supports multiple indepedent drawing windows.
 * <p>
 * For additional documentation, see <a
 * href="http://introcs.cs.princeton.edu/31datatype">Section 3.1</a> of
 * <i>Introduction to Programming in Java: An Interdisciplinary Approach</i> by
 * Robert Sedgewick and Kevin Wayne.
 *
 * @author Robert Sedgewick
 * @author Kevin Wayne
 */
public final class Draw implements ActionListener, MouseListener,
		MouseMotionListener, KeyListener
{

	/**
	 * The color black.
	 */
	public static final Color	BLACK				= Color.BLACK;

	/**
	 * The color blue.
	 */
	public static final Color	BLUE				= Color.BLUE;

	/**
	 * Shade of blue used in Introduction to Programming in Java. It is Pantone
	 * 300U. The RGB values are approximately (9, 90, 166).
	 */
	public static final Color	BOOK_BLUE			= new Color(9, 90, 166);

	/**
	 * Shade of light blue used in Introduction to Programming in Java. The RGB
	 * values are approximately (103, 198, 243).
	 */
	public static final Color	BOOK_LIGHT_BLUE		= new Color(103, 198, 243);

	/**
	 * Shade of red used in Algorithms 4th edition. The RGB values are (173, 32,
	 * 24).
	 */
	public static final Color	BOOK_RED			= new Color(173, 32, 24);

	// boundary of drawing canvas, 0% border
	private static final double	BORDER				= 0.0;

	/**
	 * The color cyan.
	 */
	public static final Color	CYAN				= Color.CYAN;

	/**
	 * The color dark gray.
	 */
	public static final Color	DARK_GRAY			= Color.DARK_GRAY;

	private static final Color	DEFAULT_CLEAR_COLOR	= Color.WHITE;

	// default font
	private static final Font	DEFAULT_FONT		= new Font("SansSerif",
															Font.PLAIN, 16);

	// default colors
	private static final Color	DEFAULT_PEN_COLOR	= BLACK;

	// default pen radius
	private static final double	DEFAULT_PEN_RADIUS	= 0.002;

	// default canvas size is SIZE-by-SIZE
	private static final int	DEFAULT_SIZE		= 512;

	private static final double	DEFAULT_XMAX		= 1.0;

	private static final double	DEFAULT_XMIN		= 0.0;

	private static final double	DEFAULT_YMAX		= 1.0;

	private static final double	DEFAULT_YMIN		= 0.0;
	/**
	 * The color gray.
	 */
	public static final Color	GRAY				= Color.GRAY;

	/**
	 * The color green.
	 */
	public static final Color	GREEN				= Color.GREEN;
	/**
	 * The color light gray.
	 */
	public static final Color	LIGHT_GRAY			= Color.LIGHT_GRAY;
	/**
	 * The color magenta.
	 */
	public static final Color	MAGENTA				= Color.MAGENTA;
	/**
	 * The color orange.
	 */
	public static final Color	ORANGE				= Color.ORANGE;
	/**
	 * The color pink.
	 */
	public static final Color	PINK				= Color.PINK;

	/**
	 * The color red.
	 */
	public static final Color	RED					= Color.RED;

	/**
	 * The color white.
	 */
	public static final Color	WHITE				= Color.WHITE;

	/**
	 * The color yellow.
	 */
	public static final Color	YELLOW				= Color.YELLOW;

	/**
	 * Test client.
	 */
	public static void main(final String[] args)
	{

		// create one drawing window
		final Draw draw1 = new Draw("Test client 1");
		draw1.square(.2, .8, .1);
		draw1.filledSquare(.8, .8, .2);
		draw1.circle(.8, .2, .2);
		draw1.setPenColor(Draw.MAGENTA);
		draw1.setPenRadius(.02);
		draw1.arc(.8, .2, .1, 200, 45);

		// create another one
		final Draw draw2 = new Draw("Test client 2");
		draw2.setCanvasSize(900, 200);
		// draw a blue diamond
		draw2.setPenRadius();
		draw2.setPenColor(Draw.BLUE);
		final double[] x = { .1, .2, .3, .2 };
		final double[] y = { .2, .3, .2, .1 };
		draw2.filledPolygon(x, y);

		// text
		draw2.setPenColor(Draw.BLACK);
		draw2.text(0.2, 0.5, "bdfdfdfdlack text");
		draw2.setPenColor(Draw.WHITE);
		draw2.text(0.8, 0.8, "white text");
	}

	// show we draw immediately or wait until next show?
	private boolean							defer			= false;
	// the JLabel for drawing
	private JLabel							draw;

	// current font
	private Font							font;

	// the frame for drawing to the screen
	private JFrame							frame			= new JFrame();

	private int								height			= DEFAULT_SIZE;

	private final Object					keyLock			= new Object();

	private final TreeSet<Integer>			keysDown		= new TreeSet<Integer>();
	// keyboard state
	private final LinkedList<Character>		keysTyped		= new LinkedList<Character>();

	// event-based listeners
	private final ArrayList<DrawListener>	listeners		= new ArrayList<DrawListener>();

	// for synchronization
	private final Object					mouseLock		= new Object();

	// mouse state
	private boolean							mousePressed	= false;
	private double							mouseX			= 0;

	private double							mouseY			= 0;

	// name of window
	private String							name			= "Draw";
	private Graphics2D						offscreen, onscreen;
	// double buffered graphics
	private BufferedImage					offscreenImage, onscreenImage;

	// current pen color
	private Color							penColor;
	// current pen radius
	private double							penRadius;

	// canvas size
	private int								width			= DEFAULT_SIZE;

	private double							xmin, ymin, xmax, ymax;

	/**
	 * Initializes an empty drawing object.
	 */
	public Draw()
	{
		init();
	}

	/**
	 * Initializes an empty drawing object with the given name.
	 *
	 * @param name
	 *            the title of the drawing window.
	 */
	public Draw(final String name)
	{
		this.name = name;
		init();
	}

	/**
	 * This method cannot be called directly.
	 */
	@Override
	public void actionPerformed(final ActionEvent e)
	{
		final FileDialog chooser = new FileDialog(frame,
				"Use a .png or .jpg extension", FileDialog.SAVE);
		chooser.setVisible(true);
		final String filename = chooser.getFile();
		if (filename != null)
		{
			save(chooser.getDirectory() + File.separator + chooser.getFile());
		}
	}

	/**
	 * Adds a {@link DrawListener} to listen to keyboard and mouse events.
	 *
	 * @param listener
	 *            the {\tt DrawListener} argument
	 */
	public void addListener(final DrawListener listener)
	{
		// ensure there is a window for listenting to events
		show();
		listeners.add(listener);
		frame.addKeyListener(this);
		frame.addMouseListener(this);
		frame.addMouseMotionListener(this);
		frame.setFocusable(true);
	}

	/**
	 * Draws an arc of radius r, centered on (x, y), from angle1 to angle2 (in
	 * degrees).
	 *
	 * @param x
	 *            the x-coordinate of the center of the circle
	 * @param y
	 *            the y-coordinate of the center of the circle
	 * @param r
	 *            the radius of the circle
	 * @param angle1
	 *            the starting angle. 0 would mean an arc beginning at 3
	 *            o'clock.
	 * @param angle2
	 *            the angle at the end of the arc. For example, if you want a 90
	 *            degree arc, then angle2 should be angle1 + 90.
	 * @throws IllegalArgumentException
	 *             if the radius of the circle is negative
	 */
	public void arc(final double x, final double y, final double r,
			final double angle1, double angle2)
	{
		if (r < 0)
		{
			throw new IllegalArgumentException("arc radius can't be negative");
		}
		while (angle2 < angle1)
		{
			angle2 += 360;
		}
		final double xs = scaleX(x);
		final double ys = scaleY(y);
		final double ws = factorX(2 * r);
		final double hs = factorY(2 * r);
		if (ws <= 1 && hs <= 1)
		{
			pixel(x, y);
		}
		else
		{
			offscreen.draw(new Arc2D.Double(xs - ws / 2, ys - hs / 2, ws, hs,
					angle1, angle2 - angle1, Arc2D.OPEN));
		}
		draw();
	}

	/***************************************************************************
	 * User and screen coordinate systems.
	 ***************************************************************************/

	/**
	 * Draws a circle of radius r, centered on (x, y).
	 *
	 * @param x
	 *            the x-coordinate of the center of the circle
	 * @param y
	 *            the y-coordinate of the center of the circle
	 * @param r
	 *            the radius of the circle
	 * @throws IllegalArgumentException
	 *             if the radius of the circle is negative
	 */
	public void circle(final double x, final double y, final double r)
	{
		if (r < 0)
		{
			throw new IllegalArgumentException(
					"circle radius can't be negative");
		}
		final double xs = scaleX(x);
		final double ys = scaleY(y);
		final double ws = factorX(2 * r);
		final double hs = factorY(2 * r);
		if (ws <= 1 && hs <= 1)
		{
			pixel(x, y);
		}
		else
		{
			offscreen.draw(new Ellipse2D.Double(xs - ws / 2, ys - hs / 2, ws,
					hs));
		}
		draw();
	}

	/**
	 * Clears the screen to the default color (white).
	 */
	public void clear()
	{
		clear(DEFAULT_CLEAR_COLOR);
	}

	/**
	 * Clears the screen to the given color.
	 *
	 * @param color
	 *            the color to make the background
	 */
	public void clear(final Color color)
	{
		offscreen.setColor(color);
		offscreen.fillRect(0, 0, width, height);
		offscreen.setColor(penColor);
		draw();
	}

	// create the menu bar (changed to private)
	private JMenuBar createMenuBar()
	{
		final JMenuBar menuBar = new JMenuBar();
		final JMenu menu = new JMenu("File");
		menuBar.add(menu);
		final JMenuItem menuItem1 = new JMenuItem(" Save...   ");
		menuItem1.addActionListener(this);
		menuItem1.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, Toolkit
				.getDefaultToolkit().getMenuShortcutKeyMask()));
		menu.add(menuItem1);
		return menuBar;
	}

	// draw onscreen if defer is false
	private void draw()
	{
		if (defer)
		{
			return;
		}
		onscreen.drawImage(offscreenImage, 0, 0, null);
		frame.repaint();
	}

	/**
	 * Draws an ellipse with given semimajor and semiminor axes, centered on (x,
	 * y).
	 *
	 * @param x
	 *            the x-coordinate of the center of the ellipse
	 * @param y
	 *            the y-coordinate of the center of the ellipse
	 * @param semiMajorAxis
	 *            is the semimajor axis of the ellipse
	 * @param semiMinorAxis
	 *            is the semiminor axis of the ellipse
	 * @throws IllegalArgumentException
	 *             if either of the axes are negative
	 */
	public void ellipse(final double x, final double y,
			final double semiMajorAxis, final double semiMinorAxis)
	{
		if (semiMajorAxis < 0)
		{
			throw new IllegalArgumentException(
					"ellipse semimajor axis can't be negative");
		}
		if (semiMinorAxis < 0)
		{
			throw new IllegalArgumentException(
					"ellipse semiminor axis can't be negative");
		}
		final double xs = scaleX(x);
		final double ys = scaleY(y);
		final double ws = factorX(2 * semiMajorAxis);
		final double hs = factorY(2 * semiMinorAxis);
		if (ws <= 1 && hs <= 1)
		{
			pixel(x, y);
		}
		else
		{
			offscreen.draw(new Ellipse2D.Double(xs - ws / 2, ys - hs / 2, ws,
					hs));
		}
		draw();
	}

	private double factorX(final double w)
	{
		return w * width / Math.abs(xmax - xmin);
	}

	private double factorY(final double h)
	{
		return h * height / Math.abs(ymax - ymin);
	}

	/**
	 * Draws a filled circle of radius r, centered on (x, y).
	 *
	 * @param x
	 *            the x-coordinate of the center of the circle
	 * @param y
	 *            the y-coordinate of the center of the circle
	 * @param r
	 *            the radius of the circle
	 * @throws IllegalArgumentException
	 *             if the radius of the circle is negative
	 */
	public void filledCircle(final double x, final double y, final double r)
	{
		if (r < 0)
		{
			throw new IllegalArgumentException(
					"circle radius can't be negative");
		}
		final double xs = scaleX(x);
		final double ys = scaleY(y);
		final double ws = factorX(2 * r);
		final double hs = factorY(2 * r);
		if (ws <= 1 && hs <= 1)
		{
			pixel(x, y);
		}
		else
		{
			offscreen.fill(new Ellipse2D.Double(xs - ws / 2, ys - hs / 2, ws,
					hs));
		}
		draw();
	}

	/**
	 * Draws an ellipse with given semimajor and semiminor axes, centered on (x,
	 * y).
	 *
	 * @param x
	 *            the x-coordinate of the center of the ellipse
	 * @param y
	 *            the y-coordinate of the center of the ellipse
	 * @param semiMajorAxis
	 *            is the semimajor axis of the ellipse
	 * @param semiMinorAxis
	 *            is the semiminor axis of the ellipse
	 * @throws IllegalArgumentException
	 *             if either of the axes are negative
	 */
	public void filledEllipse(final double x, final double y,
			final double semiMajorAxis, final double semiMinorAxis)
	{
		if (semiMajorAxis < 0)
		{
			throw new IllegalArgumentException(
					"ellipse semimajor axis can't be negative");
		}
		if (semiMinorAxis < 0)
		{
			throw new IllegalArgumentException(
					"ellipse semiminor axis can't be negative");
		}
		final double xs = scaleX(x);
		final double ys = scaleY(y);
		final double ws = factorX(2 * semiMajorAxis);
		final double hs = factorY(2 * semiMinorAxis);
		if (ws <= 1 && hs <= 1)
		{
			pixel(x, y);
		}
		else
		{
			offscreen.fill(new Ellipse2D.Double(xs - ws / 2, ys - hs / 2, ws,
					hs));
		}
		draw();
	}

	/**
	 * Draws a filled polygon with the given (x[i], y[i]) coordinates.
	 *
	 * @param x
	 *            an array of all the x-coordindates of the polygon
	 * @param y
	 *            an array of all the y-coordindates of the polygon
	 */
	public void filledPolygon(final double[] x, final double[] y)
	{
		final int N = x.length;
		final GeneralPath path = new GeneralPath();
		path.moveTo((float) scaleX(x[0]), (float) scaleY(y[0]));
		for (int i = 0; i < N; i++)
		{
			path.lineTo((float) scaleX(x[i]), (float) scaleY(y[i]));
		}
		path.closePath();
		offscreen.fill(path);
		draw();
	}

	/**
	 * Draws a filled rectangle of given half width and half height, centered on
	 * (x, y).
	 *
	 * @param x
	 *            the x-coordinate of the center of the rectangle
	 * @param y
	 *            the y-coordinate of the center of the rectangle
	 * @param halfWidth
	 *            is half the width of the rectangle
	 * @param halfHeight
	 *            is half the height of the rectangle
	 * @throws IllegalArgumentException
	 *             if halfWidth or halfHeight is negative
	 */
	public void filledRectangle(final double x, final double y,
			final double halfWidth, final double halfHeight)
	{
		if (halfWidth < 0)
		{
			throw new IllegalArgumentException("half width can't be negative");
		}
		if (halfHeight < 0)
		{
			throw new IllegalArgumentException("half height can't be negative");
		}
		final double xs = scaleX(x);
		final double ys = scaleY(y);
		final double ws = factorX(2 * halfWidth);
		final double hs = factorY(2 * halfHeight);
		if (ws <= 1 && hs <= 1)
		{
			pixel(x, y);
		}
		else
		{
			offscreen.fill(new Rectangle2D.Double(xs - ws / 2, ys - hs / 2, ws,
					hs));
		}
		draw();
	}

	/**
	 * Draws a filled square of side length 2r, centered on (x, y).
	 *
	 * @param x
	 *            the x-coordinate of the center of the square
	 * @param y
	 *            the y-coordinate of the center of the square
	 * @param r
	 *            radius is half the length of any side of the square
	 * @throws IllegalArgumentException
	 *             if r is negative
	 */
	public void filledSquare(final double x, final double y, final double r)
	{
		if (r < 0)
		{
			throw new IllegalArgumentException(
					"square side length can't be negative");
		}
		final double xs = scaleX(x);
		final double ys = scaleY(y);
		final double ws = factorX(2 * r);
		final double hs = factorY(2 * r);
		if (ws <= 1 && hs <= 1)
		{
			pixel(x, y);
		}
		else
		{
			offscreen.fill(new Rectangle2D.Double(xs - ws / 2, ys - hs / 2, ws,
					hs));
		}
		draw();
	}

	/**
	 * Gets the current font.
	 *
	 * @return the current font
	 */
	public Font getFont()
	{
		return font;
	}

	/***************************************************************************
	 * Drawing images.
	 ***************************************************************************/

	// get an image from the given filename
	private Image getImage(final String filename)
	{

		// to read from file
		ImageIcon icon = new ImageIcon(filename);

		// try to read from URL
		if (icon == null || icon.getImageLoadStatus() != MediaTracker.COMPLETE)
		{
			try
			{
				final URL url = new URL(filename);
				icon = new ImageIcon(url);
			}
			catch (final Exception e)
			{
				/* not a url */
			}
		}

		// in case file is inside a .jar
		if (icon == null || icon.getImageLoadStatus() != MediaTracker.COMPLETE)
		{
			final URL url = Draw.class.getResource(filename);
			if (url == null)
			{
				throw new IllegalArgumentException("image " + filename
						+ " not found");
			}
			icon = new ImageIcon(url);
		}

		return icon.getImage();
	}

	/**
	 * Gets the current <tt>JLabel</tt> for use in some other GUI.
	 *
	 * @return the current <tt>JLabel</tt>
	 */
	public JLabel getJLabel()
	{
		return draw;
	}

	/**
	 * Gets the current pen color.
	 *
	 * @return the current pen color
	 */
	public Color getPenColor()
	{
		return penColor;
	}

	/**
	 * Gets the current pen radius.
	 *
	 * @return the current pen radius
	 */
	public double getPenRadius()
	{
		return penRadius;
	}

	/**
	 * Returns true if the user has typed a key.
	 *
	 * @return <tt>true</tt> if the user has typed a key; <tt>false</tt>
	 *         otherwise
	 */
	public boolean hasNextKeyTyped()
	{
		synchronized (keyLock)
		{
			return !keysTyped.isEmpty();
		}
	}

	private void init()
	{
		if (frame != null)
		{
			frame.setVisible(false);
		}
		frame = new JFrame();
		offscreenImage = new BufferedImage(width, height,
				BufferedImage.TYPE_INT_ARGB);
		onscreenImage = new BufferedImage(width, height,
				BufferedImage.TYPE_INT_ARGB);
		offscreen = offscreenImage.createGraphics();
		onscreen = onscreenImage.createGraphics();
		setXscale();
		setYscale();
		offscreen.setColor(DEFAULT_CLEAR_COLOR);
		offscreen.fillRect(0, 0, width, height);
		setPenColor();
		setPenRadius();
		setFont();
		clear();

		// add antialiasing
		final RenderingHints hints = new RenderingHints(
				RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);
		hints.put(RenderingHints.KEY_RENDERING,
				RenderingHints.VALUE_RENDER_QUALITY);
		offscreen.addRenderingHints(hints);

		// frame stuff
		final ImageIcon icon = new ImageIcon(onscreenImage);
		draw = new JLabel(icon);

		draw.addMouseListener(this);
		draw.addMouseMotionListener(this);

		frame.setContentPane(draw);
		frame.addKeyListener(this); // JLabel cannot get keyboard focus
		frame.setResizable(false);
		// frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // closes all
		// windows
		frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE); // closes
																			// only
																			// current
																			// window
		frame.setTitle(name);
		frame.setJMenuBar(createMenuBar());
		frame.pack();
		frame.requestFocusInWindow();
		frame.setVisible(true);
	}

	/**
	 * Returns true if the keycode is being pressed.
	 * <p>
	 * This method takes as an argument the keycode (corresponding to a physical
	 * key). It can handle action keys (such as F1 and arrow keys) and modifier
	 * keys (such as shift and control). See {@link KeyEvent} for a description
	 * of key codes.
	 *
	 * @param keycode
	 *            the keycode to check
	 * @return <tt>true</tt> if <tt>keycode</tt> is currently being pressed;
	 *         <tt>false</tt> otherwise
	 */
	public boolean isKeyPressed(final int keycode)
	{
		synchronized (keyLock)
		{
			return keysDown.contains(keycode);
		}
	}

	/**
	 * This method cannot be called directly.
	 */
	@Override
	public void keyPressed(final KeyEvent e)
	{
		synchronized (keyLock)
		{
			keysDown.add(e.getKeyCode());
		}
	}

	/**
	 * This method cannot be called directly.
	 */
	@Override
	public void keyReleased(final KeyEvent e)
	{
		synchronized (keyLock)
		{
			keysDown.remove(e.getKeyCode());
		}
	}

	/**
	 * This method cannot be called directly.
	 */
	@Override
	public void keyTyped(final KeyEvent e)
	{
		synchronized (keyLock)
		{
			keysTyped.addFirst(e.getKeyChar());
		}

		// notify all listeners
		for (final DrawListener listener : listeners)
		{
			listener.keyTyped(e.getKeyChar());
		}
	}

	/**
	 * Draws a line from (x0, y0) to (x1, y1).
	 *
	 * @param x0
	 *            the x-coordinate of the starting point
	 * @param y0
	 *            the y-coordinate of the starting point
	 * @param x1
	 *            the x-coordinate of the destination point
	 * @param y1
	 *            the y-coordinate of the destination point
	 */
	public void line(final double x0, final double y0, final double x1,
			final double y1)
	{
		offscreen.draw(new Line2D.Double(scaleX(x0), scaleY(y0), scaleX(x1),
				scaleY(y1)));
		draw();
	}

	/***************************************************************************
	 * Drawing geometric shapes.
	 ***************************************************************************/

	/**
	 * This method cannot be called directly.
	 */
	@Override
	public void mouseClicked(final MouseEvent e)
	{
	}

	/**
	 * This method cannot be called directly.
	 */
	@Override
	public void mouseDragged(final MouseEvent e)
	{
		synchronized (mouseLock)
		{
			mouseX = userX(e.getX());
			mouseY = userY(e.getY());
		}
		// doesn't seem to work if a button is specified
		for (final DrawListener listener : listeners)
		{
			listener.mouseDragged(userX(e.getX()), userY(e.getY()));
		}
	}

	/**
	 * This method cannot be called directly.
	 */
	@Override
	public void mouseEntered(final MouseEvent e)
	{
	}

	/**
	 * This method cannot be called directly.
	 */
	@Override
	public void mouseExited(final MouseEvent e)
	{
	}

	/**
	 * This method cannot be called directly.
	 */
	@Override
	public void mouseMoved(final MouseEvent e)
	{
		synchronized (mouseLock)
		{
			mouseX = userX(e.getX());
			mouseY = userY(e.getY());
		}
	}

	/**
	 * Returns true if the mouse is being pressed.
	 *
	 * @return <tt>true</tt> if the mouse is being pressed; <tt>false</tt>
	 *         otherwise
	 */
	public boolean mousePressed()
	{
		synchronized (mouseLock)
		{
			return mousePressed;
		}
	}

	/**
	 * This method cannot be called directly.
	 */
	@Override
	public void mousePressed(final MouseEvent e)
	{
		synchronized (mouseLock)
		{
			mouseX = userX(e.getX());
			mouseY = userY(e.getY());
			mousePressed = true;
		}
		if (e.getButton() == MouseEvent.BUTTON1)
		{
			for (final DrawListener listener : listeners)
			{
				listener.mousePressed(userX(e.getX()), userY(e.getY()));
			}
		}

	}

	/**
	 * This method cannot be called directly.
	 */
	@Override
	public void mouseReleased(final MouseEvent e)
	{
		synchronized (mouseLock)
		{
			mousePressed = false;
		}
		if (e.getButton() == MouseEvent.BUTTON1)
		{
			for (final DrawListener listener : listeners)
			{
				listener.mouseReleased(userX(e.getX()), userY(e.getY()));
			}
		}
	}

	/**
	 * Returns the x-coordinate of the mouse.
	 *
	 * @return the x-coordinate of the mouse
	 */
	public double mouseX()
	{
		synchronized (mouseLock)
		{
			return mouseX;
		}
	}

	/**
	 * Returns the y-coordinate of the mouse.
	 *
	 * @return the y-coordinate of the mouse
	 */
	public double mouseY()
	{
		synchronized (mouseLock)
		{
			return mouseY;
		}
	}

	/**
	 * The next key typed by the user.
	 *
	 * @return the next key typed by the user
	 */
	public char nextKeyTyped()
	{
		synchronized (keyLock)
		{
			return keysTyped.removeLast();
		}
	}

	/**
	 * Draws picture (gif, jpg, or png) centered on (x, y).
	 *
	 * @param x
	 *            the center x-coordinate of the image
	 * @param y
	 *            the center y-coordinate of the image
	 * @param s
	 *            the name of the image/picture, e.g., "ball.gif"
	 * @throws IllegalArgumentException
	 *             if the image is corrupt
	 */
	public void picture(final double x, final double y, final String s)
	{
		final Image image = getImage(s);
		final double xs = scaleX(x);
		final double ys = scaleY(y);
		final int ws = image.getWidth(null);
		final int hs = image.getHeight(null);
		if (ws < 0 || hs < 0)
		{
			throw new IllegalArgumentException("image " + s + " is corrupt");
		}

		offscreen.drawImage(image, (int) Math.round(xs - ws / 2.0),
				(int) Math.round(ys - hs / 2.0), null);
		draw();
	}

	/**
	 * Draws picture (gif, jpg, or png) centered on (x, y), rotated given number
	 * of degrees.
	 *
	 * @param x
	 *            the center x-coordinate of the image
	 * @param y
	 *            the center y-coordinate of the image
	 * @param s
	 *            the name of the image/picture, e.g., "ball.gif"
	 * @param degrees
	 *            is the number of degrees to rotate counterclockwise
	 * @throws IllegalArgumentException
	 *             if the image is corrupt
	 */
	public void picture(final double x, final double y, final String s,
			final double degrees)
	{
		final Image image = getImage(s);
		final double xs = scaleX(x);
		final double ys = scaleY(y);
		final int ws = image.getWidth(null);
		final int hs = image.getHeight(null);
		if (ws < 0 || hs < 0)
		{
			throw new IllegalArgumentException("image " + s + " is corrupt");
		}

		offscreen.rotate(Math.toRadians(-degrees), xs, ys);
		offscreen.drawImage(image, (int) Math.round(xs - ws / 2.0),
				(int) Math.round(ys - hs / 2.0), null);
		offscreen.rotate(Math.toRadians(+degrees), xs, ys);

		draw();
	}

	/**
	 * Draws picture (gif, jpg, or png) centered on (x, y), rescaled to w-by-h.
	 *
	 * @param x
	 *            the center x coordinate of the image
	 * @param y
	 *            the center y coordinate of the image
	 * @param s
	 *            the name of the image/picture, e.g., "ball.gif"
	 * @param w
	 *            the width of the image
	 * @param h
	 *            the height of the image
	 * @throws IllegalArgumentException
	 *             if the image is corrupt
	 */
	public void picture(final double x, final double y, final String s,
			final double w, final double h)
	{
		final Image image = getImage(s);
		final double xs = scaleX(x);
		final double ys = scaleY(y);
		final double ws = factorX(w);
		final double hs = factorY(h);
		if (ws < 0 || hs < 0)
		{
			throw new IllegalArgumentException("image " + s + " is corrupt");
		}
		if (ws <= 1 && hs <= 1)
		{
			pixel(x, y);
		}
		else
		{
			offscreen.drawImage(image, (int) Math.round(xs - ws / 2.0),
					(int) Math.round(ys - hs / 2.0), (int) Math.round(ws),
					(int) Math.round(hs), null);
		}
		draw();
	}

	/**
	 * Draws picture (gif, jpg, or png) centered on (x, y), rotated given number
	 * of degrees, rescaled to w-by-h.
	 *
	 * @param x
	 *            the center x-coordinate of the image
	 * @param y
	 *            the center y-coordinate of the image
	 * @param s
	 *            the name of the image/picture, e.g., "ball.gif"
	 * @param w
	 *            the width of the image
	 * @param h
	 *            the height of the image
	 * @param degrees
	 *            is the number of degrees to rotate counterclockwise
	 * @throws IllegalArgumentException
	 *             if the image is corrupt
	 */
	public void picture(final double x, final double y, final String s,
			final double w, final double h, final double degrees)
	{
		final Image image = getImage(s);
		final double xs = scaleX(x);
		final double ys = scaleY(y);
		final double ws = factorX(w);
		final double hs = factorY(h);
		if (ws < 0 || hs < 0)
		{
			throw new IllegalArgumentException("image " + s + " is corrupt");
		}
		if (ws <= 1 && hs <= 1)
		{
			pixel(x, y);
		}

		offscreen.rotate(Math.toRadians(-degrees), xs, ys);
		offscreen.drawImage(image, (int) Math.round(xs - ws / 2.0),
				(int) Math.round(ys - hs / 2.0), (int) Math.round(ws),
				(int) Math.round(hs), null);
		offscreen.rotate(Math.toRadians(+degrees), xs, ys);

		draw();
	}

	/**
	 * Draws one pixel at (x, y).
	 *
	 * @param x
	 *            the x-coordinate of the pixel
	 * @param y
	 *            the y-coordinate of the pixel
	 */
	private void pixel(final double x, final double y)
	{
		offscreen.fillRect((int) Math.round(scaleX(x)),
				(int) Math.round(scaleY(y)), 1, 1);
	}

	/**
	 * Draws a point at (x, y).
	 *
	 * @param x
	 *            the x-coordinate of the point
	 * @param y
	 *            the y-coordinate of the point
	 */
	public void point(final double x, final double y)
	{
		final double xs = scaleX(x);
		final double ys = scaleY(y);
		final double r = penRadius;
		// double ws = factorX(2*r);
		// double hs = factorY(2*r);
		// if (ws <= 1 && hs <= 1) pixel(x, y);
		if (r <= 1)
		{
			pixel(x, y);
		}
		else
		{
			offscreen.fill(new Ellipse2D.Double(xs - r / 2, ys - r / 2, r, r));
		}
		draw();
	}

	/**
	 * Draws a polygon with the given (x[i], y[i]) coordinates.
	 *
	 * @param x
	 *            an array of all the x-coordindates of the polygon
	 * @param y
	 *            an array of all the y-coordindates of the polygon
	 */
	public void polygon(final double[] x, final double[] y)
	{
		final int N = x.length;
		final GeneralPath path = new GeneralPath();
		path.moveTo((float) scaleX(x[0]), (float) scaleY(y[0]));
		for (int i = 0; i < N; i++)
		{
			path.lineTo((float) scaleX(x[i]), (float) scaleY(y[i]));
		}
		path.closePath();
		offscreen.draw(path);
		draw();
	}

	/**
	 * Draws a rectangle of given half width and half height, centered on (x,
	 * y).
	 *
	 * @param x
	 *            the x-coordinate of the center of the rectangle
	 * @param y
	 *            the y-coordinate of the center of the rectangle
	 * @param halfWidth
	 *            is half the width of the rectangle
	 * @param halfHeight
	 *            is half the height of the rectangle
	 * @throws IllegalArgumentException
	 *             if halfWidth or halfHeight is negative
	 */
	public void rectangle(final double x, final double y,
			final double halfWidth, final double halfHeight)
	{
		if (halfWidth < 0)
		{
			throw new IllegalArgumentException("half width can't be negative");
		}
		if (halfHeight < 0)
		{
			throw new IllegalArgumentException("half height can't be negative");
		}
		final double xs = scaleX(x);
		final double ys = scaleY(y);
		final double ws = factorX(2 * halfWidth);
		final double hs = factorY(2 * halfHeight);
		if (ws <= 1 && hs <= 1)
		{
			pixel(x, y);
		}
		else
		{
			offscreen.draw(new Rectangle2D.Double(xs - ws / 2, ys - hs / 2, ws,
					hs));
		}
		draw();
	}

	/***************************************************************************
	 * Drawing text.
	 ***************************************************************************/

	/**
	 * Saves this drawing to a file.
	 *
	 * @param filename
	 *            the name of the file (with suffix png, jpg, or gif)
	 */
	public void save(final String filename)
	{
		final File file = new File(filename);
		final String suffix = filename.substring(filename.lastIndexOf('.') + 1);

		// png files
		if (suffix.toLowerCase().equals("png"))
		{
			try
			{
				ImageIO.write(offscreenImage, suffix, file);
			}
			catch (final IOException e)
			{
				e.printStackTrace();
			}
		}

		// need to change from ARGB to RGB for jpeg
		// reference:
		// http://archives.java.sun.com/cgi-bin/wa?A2=ind0404&L=java2d-interest&D=0&P=2727
		else if (suffix.toLowerCase().equals("jpg"))
		{
			final WritableRaster raster = offscreenImage.getRaster();
			WritableRaster newRaster;
			newRaster = raster.createWritableChild(0, 0, width, height, 0, 0,
					new int[] { 0, 1, 2 });
			final DirectColorModel cm = (DirectColorModel) offscreenImage
					.getColorModel();
			final DirectColorModel newCM = new DirectColorModel(
					cm.getPixelSize(), cm.getRedMask(), cm.getGreenMask(),
					cm.getBlueMask());
			final BufferedImage rgbBuffer = new BufferedImage(newCM, newRaster,
					false, null);
			try
			{
				ImageIO.write(rgbBuffer, suffix, file);
			}
			catch (final IOException e)
			{
				e.printStackTrace();
			}
		}

		else
		{
			System.out.println("Invalid image file type: " + suffix);
		}
	}

	// helper functions that scale from user coordinates to screen coordinates
	// and back
	private double scaleX(final double x)
	{
		return width * (x - xmin) / (xmax - xmin);
	}

	private double scaleY(final double y)
	{
		return height * (ymax - y) / (ymax - ymin);
	}

	/**
	 * Sets the window size to w-by-h pixels.
	 *
	 * @param w
	 *            the width as a number of pixels
	 * @param h
	 *            the height as a number of pixels
	 * @throws IllegalArgumentException
	 *             if the width or height is 0 or negative
	 */
	public void setCanvasSize(final int w, final int h)
	{
		if (w < 1 || h < 1)
		{
			throw new IllegalArgumentException(
					"width and height must be positive");
		}
		width = w;
		height = h;
		init();
	}

	/**
	 * Sets the font to the default font (sans serif, 16 point).
	 */
	public void setFont()
	{
		setFont(DEFAULT_FONT);
	}

	/**
	 * Sets the font to the given value.
	 *
	 * @param font
	 *            the font
	 */
	public void setFont(final Font font)
	{
		this.font = font;
	}

	/**
	 * Sets the upper-left hand corner of the drawing window to be (x, y), where
	 * (0, 0) is upper left.
	 *
	 * @param x
	 *            the number of pixels from the left
	 * @param y
	 *            the number of pixels from the top
	 * @throws IllegalArgumentException
	 *             if the width or height is 0 or negative
	 */
	public void setLocationOnScreen(final int x, final int y)
	{
		if (x <= 0 || y <= 0)
		{
			throw new IllegalArgumentException();
		}
		frame.setLocation(x, y);
	}

	/**
	 * Sets the pen color to the default color (black).
	 */
	public void setPenColor()
	{
		setPenColor(DEFAULT_PEN_COLOR);
	}

	/***************************************************************************
	 * Event-based interactions.
	 ***************************************************************************/

	/**
	 * Sets the pen color to the given color.
	 *
	 * @param color
	 *            the color to make the pen
	 */
	public void setPenColor(final Color color)
	{
		penColor = color;
		offscreen.setColor(penColor);
	}

	/***************************************************************************
	 * Mouse interactions.
	 ***************************************************************************/

	/**
	 * Sets the pen color to the given RGB color.
	 *
	 * @param red
	 *            the amount of red (between 0 and 255)
	 * @param green
	 *            the amount of green (between 0 and 255)
	 * @param blue
	 *            the amount of blue (between 0 and 255)
	 * @throws IllegalArgumentException
	 *             if the amount of red, green, or blue are outside prescribed
	 *             range
	 */
	public void setPenColor(final int red, final int green, final int blue)
	{
		if (red < 0 || red >= 256)
		{
			throw new IllegalArgumentException(
					"amount of red must be between 0 and 255");
		}
		if (green < 0 || green >= 256)
		{
			throw new IllegalArgumentException(
					"amount of red must be between 0 and 255");
		}
		if (blue < 0 || blue >= 256)
		{
			throw new IllegalArgumentException(
					"amount of red must be between 0 and 255");
		}
		setPenColor(new Color(red, green, blue));
	}

	/**
	 * Sets the pen size to the default (.002).
	 */
	public void setPenRadius()
	{
		setPenRadius(DEFAULT_PEN_RADIUS);
	}

	/**
	 * Sets the radius of the pen to the given size.
	 *
	 * @param r
	 *            the radius of the pen
	 * @throws IllegalArgumentException
	 *             if r is negative
	 */
	public void setPenRadius(final double r)
	{
		if (r < 0)
		{
			throw new IllegalArgumentException("pen radius must be positive");
		}
		penRadius = r * DEFAULT_SIZE;
		final BasicStroke stroke = new BasicStroke((float) penRadius,
				BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND);
		// BasicStroke stroke = new BasicStroke((float) penRadius);
		offscreen.setStroke(stroke);
	}

	/**
	 * Sets the x-scale to be the default (between 0.0 and 1.0).
	 */
	public void setXscale()
	{
		setXscale(DEFAULT_XMIN, DEFAULT_XMAX);
	}

	/**
	 * Sets the x-scale.
	 *
	 * @param min
	 *            the minimum value of the x-scale
	 * @param max
	 *            the maximum value of the x-scale
	 */
	public void setXscale(final double min, final double max)
	{
		final double size = max - min;
		xmin = min - BORDER * size;
		xmax = max + BORDER * size;
	}

	/**
	 * Sets the y-scale to be the default (between 0.0 and 1.0).
	 */
	public void setYscale()
	{
		setYscale(DEFAULT_YMIN, DEFAULT_YMAX);
	}

	/**
	 * Sets the y-scale.
	 *
	 * @param min
	 *            the minimum value of the y-scale
	 * @param max
	 *            the maximum value of the y-scale
	 */
	public void setYscale(final double min, final double max)
	{
		final double size = max - min;
		ymin = min - BORDER * size;
		ymax = max + BORDER * size;
	}

	/**
	 * Displays on-screen and turn off animation mode. Subsequent calls to
	 * drawing methods such as <tt>line()</tt>, <tt>circle()</tt>, and
	 * <tt>square()</tt> will be displayed on screen when called. This is the
	 * default.
	 */
	public void show()
	{
		defer = false;
		draw();
	}

	/**
	 * Displays on screen, pause for <tt>t</tt> milliseconds, and turn on
	 * <em>animation mode</em>. Subsequent calls to drawing methods such as
	 * <tt>line()</tt>, <tt>circle()</tt>, and <tt>square()</tt> will not be
	 * displayed on screen until the next call to <tt>show()</tt>. This is
	 * useful for producing animations (clear the screen, draw a bunch of
	 * shapes, display on screen for a fixed amount of time, and repeat). It
	 * also speeds up drawing a huge number of shapes (call <tt>show(0)</tt> to
	 * defer drawing on screen, draw the shapes, and call <tt>show(0)</tt> to
	 * display them all on screen at once).
	 *
	 * @param t
	 *            number of milliseconds
	 */
	public void show(final int t)
	{
		defer = false;
		draw();
		try
		{
			Thread.sleep(t);
		}
		catch (final InterruptedException e)
		{
			System.out.println("Error sleeping");
		}
		defer = true;
	}

	/**
	 * Draws a square of side length 2r, centered on (x, y).
	 *
	 * @param x
	 *            the x-coordinate of the center of the square
	 * @param y
	 *            the y-coordinate of the center of the square
	 * @param r
	 *            radius is half the length of any side of the square
	 * @throws IllegalArgumentException
	 *             if r is negative
	 */
	public void square(final double x, final double y, final double r)
	{
		if (r < 0)
		{
			throw new IllegalArgumentException(
					"square side length can't be negative");
		}
		final double xs = scaleX(x);
		final double ys = scaleY(y);
		final double ws = factorX(2 * r);
		final double hs = factorY(2 * r);
		if (ws <= 1 && hs <= 1)
		{
			pixel(x, y);
		}
		else
		{
			offscreen.draw(new Rectangle2D.Double(xs - ws / 2, ys - hs / 2, ws,
					hs));
		}
		draw();
	}

	/***************************************************************************
	 * Keyboard interactions.
	 ***************************************************************************/

	/**
	 * Writes the given text string in the current font, centered on (x, y).
	 *
	 * @param x
	 *            the center x-coordinate of the text
	 * @param y
	 *            the center y-coordinate of the text
	 * @param s
	 *            the text
	 */
	public void text(final double x, final double y, final String s)
	{
		offscreen.setFont(font);
		final FontMetrics metrics = offscreen.getFontMetrics();
		final double xs = scaleX(x);
		final double ys = scaleY(y);
		final int ws = metrics.stringWidth(s);
		final int hs = metrics.getDescent();
		offscreen.drawString(s, (float) (xs - ws / 2.0), (float) (ys + hs));
		draw();
	}

	/**
	 * Writes the given text string in the current font, centered on (x, y) and
	 * rotated by the specified number of degrees.
	 *
	 * @param x
	 *            the center x-coordinate of the text
	 * @param y
	 *            the center y-coordinate of the text
	 * @param s
	 *            the text
	 * @param degrees
	 *            is the number of degrees to rotate counterclockwise
	 */
	public void text(final double x, final double y, final String s,
			final double degrees)
	{
		final double xs = scaleX(x);
		final double ys = scaleY(y);
		offscreen.rotate(Math.toRadians(-degrees), xs, ys);
		text(x, y, s);
		offscreen.rotate(Math.toRadians(+degrees), xs, ys);
	}

	/**
	 * Writes the given text string in the current font, left-aligned at (x, y).
	 *
	 * @param x
	 *            the x-coordinate of the text
	 * @param y
	 *            the y-coordinate of the text
	 * @param s
	 *            the text
	 */
	public void textLeft(final double x, final double y, final String s)
	{
		offscreen.setFont(font);
		final FontMetrics metrics = offscreen.getFontMetrics();
		final double xs = scaleX(x);
		final double ys = scaleY(y);
		// int ws = metrics.stringWidth(s);
		final int hs = metrics.getDescent();
		offscreen.drawString(s, (float) xs, (float) (ys + hs));
		show();
	}

	private double userX(final double x)
	{
		return xmin + x * (xmax - xmin) / width;
	}

	private double userY(final double y)
	{
		return ymax - y * (ymax - ymin) / height;
	}

	/**
	 * Turns off xor mode.
	 */
	public void xorOff()
	{
		offscreen.setPaintMode();
	}

	/**
	 * Turns on xor mode.
	 */
	public void xorOn()
	{
		offscreen.setXORMode(DEFAULT_CLEAR_COLOR);
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
