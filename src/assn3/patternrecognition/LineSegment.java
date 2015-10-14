package assn3.patternrecognition;

/**
 * Represents line segments in the plane
 *
 * @author mckelvym
 * @since Sep 21, 2015
 *
 */
public class LineSegment
{
	private final Point	m_P1;
	private final Point	m_P2;

	/**
	 * Constructs the line segment between points p and q
	 *
	 * @param p_FirstPoint
	 * @param p_SecondPoint
	 * @since Sep 21, 2015
	 */
	public LineSegment(final Point p_FirstPoint, final Point p_SecondPoint)
	{
		if (p_FirstPoint == null || p_SecondPoint == null)
		{
			throw new NullPointerException();
		}
		m_P1 = p_FirstPoint;
		m_P2 = p_SecondPoint;
	}

	/**
	 * Draws this line segment
	 *
	 * @since Sep 21, 2015
	 */
	public void draw()
	{
		m_P1.drawTo(m_P2);
	}

	/**
	 * String representation
	 */
	@Override
	public String toString()
	{
		return String.format("%s - %s", m_P1, m_P2);
	}
}