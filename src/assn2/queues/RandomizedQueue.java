package assn2.queues;

import java.util.Iterator;
import java.util.NoSuchElementException;

import edu.princeton.cs.algs4.StdRandom;

/**
 * Randomized queue. A randomized queue is similar to a stack or queue, except
 * that the item removed is chosen uniformly at random from items in the data
 * structure.
 *
 * @author mckelvym
 * @since Sep 14, 2015
 *
 */
public class RandomizedQueue<Item> implements Iterable<Item>
{
	/**
	 * Require the given object to be non-null.
	 *
	 * @param p_Object
	 *            The object to check
	 * @param p_Message
	 *            the message to display if the object is null
	 * @since Sep 14, 2015
	 */
	private static void require(final Object p_Object, final String p_Message)
	{
		if (p_Object == null)
		{
			throw new NullPointerException(p_Message);
		}
	}

	/**
	 * index of first element of queue
	 *
	 * @since Sep 14, 2015
	 */
	private int		m_First;

	/**
	 * index of next available slot
	 *
	 * @since Sep 14, 2015
	 */
	private int		m_Last;

	/**
	 * queue elements
	 *
	 * @since Sep 14, 2015
	 */
	private Item[]	m_Queue;

	/**
	 * number of elements on queue
	 *
	 * @since Sep 14, 2015
	 */
	private int		m_Size;

	/**
	 * construct an empty randomized queue
	 *
	 * @since Sep 14, 2015
	 */
	@SuppressWarnings("unchecked")
	public RandomizedQueue()
	{
		m_First = 0;
		m_Last = 0;
		m_Size = 0;
		m_Queue = (Item[]) new Object[2];
	}

	/**
	 * remove and return a random item
	 *
	 * @return
	 * @since Sep 14, 2015
	 */
	public Item dequeue()
	{
		if (isEmpty())
		{
			throw new NoSuchElementException("Empty.");
		}
		final int uniform = getRandomIndex();
		final Item item = m_Queue[uniform];
		m_Queue[uniform] = m_Queue[m_First];
		/**
		 * To avoid loitering
		 */
		m_Queue[m_First] = null;
		m_Size--;
		m_First++;
		if (m_First == m_Queue.length)
		{
			/**
			 * wrap-around
			 */
			m_First = 0;
		}
		/**
		 * shrink size of array if necessary
		 */
		if (m_Size > 0 && m_Size == m_Queue.length / 4)
		{
			resize(m_Queue.length / 2);
		}
		return item;
	}

	/**
	 * add the item
	 *
	 * @param item
	 * @since Sep 14, 2015
	 */
	public void enqueue(final Item item)
	{
		require(item, "Item cannot be null.");
		/**
		 * Double size of array if necessary and recopy to front of array
		 */
		if (m_Size == m_Queue.length)
		{
			/**
			 * double size of array if necessary
			 */
			resize(2 * m_Queue.length);
		}
		/**
		 * add item
		 */
		m_Queue[m_Last++] = item;
		if (m_Last == m_Queue.length)
		{
			/**
			 * wrap-around
			 */
			m_Last = 0;
		}
		m_Size++;
	}

	/**
	 * @return a random index into the queue, provided it is non-empty
	 * @since Sep 17, 2015
	 */
	private int getRandomIndex()
	{
		final int uniform = StdRandom.uniform(0, m_Size);
		return (m_First + uniform) % m_Queue.length;
	}

	/**
	 * Is the queue empty?
	 *
	 * @return true if the queue is empty
	 * @since Sep 14, 2015
	 */
	public boolean isEmpty()
	{
		return m_Size == 0;
	}

	/**
	 * return an independent iterator over items in random order
	 */
	@Override
	public Iterator<Item> iterator()
	{
		final int[] shuffle = new int[size()];
		for (int i = 0; i < shuffle.length; i++)
		{
			shuffle[i] = (m_First + i) % m_Queue.length;
		}
		StdRandom.shuffle(shuffle);
		return new Iterator<Item>()
		{
			private int	m_Current	= 0;

			@Override
			public boolean hasNext()
			{
				return m_Current < shuffle.length;
			}

			@Override
			public Item next()
			{
				if (!hasNext())
				{
					throw new NoSuchElementException();
				}
				return m_Queue[shuffle[m_Current++]]; // TODO
			}
		};
	}

	/**
	 * Resize the underlying array
	 *
	 * @param p_MaxSize
	 * @since Sep 14, 2015
	 */
	private void resize(final int p_MaxSize)
	{
		assert p_MaxSize >= m_Size;
		@SuppressWarnings("unchecked")
		final Item[] temp = (Item[]) new Object[p_MaxSize];
		for (int i = 0; i < m_Size; i++)
		{
			temp[i] = m_Queue[(m_First + i) % m_Queue.length];
		}
		m_Queue = temp;
		m_First = 0;
		m_Last = m_Size;
	}

	/**
	 * Return (but do not remove) a random item
	 *
	 * @return a random item
	 * @since Sep 14, 2015
	 */
	public Item sample()
	{
		if (isEmpty())
		{
			throw new NoSuchElementException("Empty.");
		}
		final int uniform = getRandomIndex();
		return m_Queue[uniform];
	}

	/**
	 * Return the number of items on the queue
	 *
	 * @return the number of items on the queue
	 * @since Sep 14, 2015
	 */
	public int size()
	{
		return m_Size;
	}

}
