package assn2.queues;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * Dequeue. A double-ended queue or deque (pronounced "deck") is a
 * generalization of a stack and a queue that supports adding and removing items
 * from either the front or the back of the data structure.
 *
 * @author mckelvym
 * @since Sep 14, 2015
 *
 * @param <Item>
 *            the type parameter
 */
public class Deque<Item> implements Iterable<Item>
{
	/**
	 * A linked node with pointers to a previous and a next node
	 *
	 * @author mckelvym
	 * @since Sep 14, 2015
	 *
	 */
	private class Node
	{
		/**
		 * The data to store
		 *
		 * @since Sep 14, 2015
		 */
		private Item	data;
		/**
		 * The previous node
		 *
		 * @since Sep 14, 2015
		 */
		private Node	next;
		/**
		 * The next node
		 *
		 * @since Sep 14, 2015
		 */
		private Node	prev;
	}

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
	 * @see #addFirst(Object)
	 * @since Sep 14, 2015
	 */
	private Node	m_First;
	/**
	 * @see #addLast(Object)
	 * @since Sep 14, 2015
	 */
	private Node	m_Last;
	/**
	 * @see #size()
	 * @since Sep 14, 2015
	 */
	private int		m_Size;

	/**
	 * Construct an empty deque
	 *
	 * @since Sep 14, 2015
	 */
	public Deque()
	{
		m_First = null;
		m_Last = null;
		m_Size = 0;
	}

	/**
	 * Add the item to the front
	 *
	 * @param item
	 *            the item to add
	 * @since Sep 14, 2015
	 */
	public void addFirst(final Item item)
	{
		require(item, "Item cannot be null.");
		final Node node = new Node();
		node.data = item;
		node.prev = null;
		node.next = m_First;
		if (m_First != null)
		{
			m_First.prev = node;
		}
		m_First = node;
		if (m_Last == null)
		{
			m_Last = node;
		}
		m_Size++;
	}

	/**
	 * Add the item to the end
	 *
	 * @param item
	 *            the item to add
	 * @since Sep 14, 2015
	 */
	public void addLast(final Item item)
	{
		require(item, "Item cannot be null.");
		final Node node = new Node();
		node.data = item;
		node.prev = m_Last;
		node.next = null;
		if (m_Last != null)
		{
			m_Last.next = node;
		}
		m_Last = node;
		if (m_First == null)
		{
			m_First = node;
		}
		m_Size++;
	}

	/**
	 * Is the deque empty?
	 *
	 * @return true if the deque is empty
	 * @since Sep 14, 2015
	 */
	public boolean isEmpty()
	{
		return m_Size == 0;
	}

	/**
	 * Return an iterator over items in order from front to end
	 */
	@Override
	public Iterator<Item> iterator()
	{
		return new Iterator<Item>()
		{
			private Node	m_Current	= m_First;

			@Override
			public boolean hasNext()
			{
				return m_Current != null;
			}

			@Override
			public Item next()
			{
				if (!hasNext())
				{
					throw new NoSuchElementException();
				}
				final Item data = m_Current.data;
				m_Current = m_Current.next;
				return data;
			}
		};
	}

	/**
	 * Remove and return the item from the front
	 *
	 * @return the item from the front
	 * @since Sep 14, 2015
	 */
	public Item removeFirst()
	{
		if (isEmpty())
		{
			throw new NoSuchElementException("Empty.");
		}
		final Node dequeue = m_First;
		m_First = m_First.next;
		final Item data = dequeue.data;
		dequeue.data = null;
		dequeue.prev = null;
		dequeue.next = null;
		if (m_First == null)
		{
			m_Last = null;
		}
		else
		{
			m_First.prev = null;
		}
		m_Size--;
		return data;
	}

	/**
	 * Remove and return the item from the end
	 *
	 * @return the item from the end
	 * @since Sep 14, 2015
	 */
	public Item removeLast()
	{
		if (isEmpty())
		{
			throw new NoSuchElementException("Empty.");
		}
		final Node dequeue = m_Last;
		m_Last = m_Last.prev;
		final Item data = dequeue.data;
		dequeue.data = null;
		dequeue.prev = null;
		dequeue.next = null;
		if (m_Last == null)
		{
			m_First = null;
		}
		else
		{
			m_Last.next = null;
		}
		m_Size--;
		return data;
	}

	/**
	 * Return the number of items on the deque
	 *
	 * @return the number of items on the deque
	 * @since Sep 14, 2015
	 */
	public int size()
	{
		return m_Size;
	}

}
