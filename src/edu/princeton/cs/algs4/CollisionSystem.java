/******************************************************************************
 *  Compilation:  javac CollisionSystem.java
 *  Execution:    java CollisionSystem N               (N random particles)
 *                java CollisionSystem < input.txt     (from a file)
 *  Dependencies: StdDraw.java Particle.java MinPQ.java
 *
 *  Creates N random particles and simulates their motion according
 *  to the laws of elastic collisions.
 *
 ******************************************************************************/

package edu.princeton.cs.algs4;

import java.awt.Color;

public class CollisionSystem
{
	/***************************************************************************
	 * An event during a particle collision simulation. Each event contains the
	 * time at which it will occur (assuming no supervening actions) and the
	 * particles a and b involved.
	 *
	 * - a and b both null: redraw event - a null, b not null: collision with
	 * vertical wall - a not null, b null: collision with horizontal wall - a
	 * and b both not null: binary collision between a and b
	 *
	 ***************************************************************************/
	private static class Event implements Comparable<Event>
	{
		private final Particle	a, b;	// particles involved in event, possibly
										// null
		private final int		countA, countB; // collision counts at event
												// creation
		private final double	time;			// time that event is scheduled
												// to occur

		// create a new event to occur at time t involving a and b
		public Event(final double t, final Particle a, final Particle b)
		{
			this.time = t;
			this.a = a;
			this.b = b;
			if (a != null)
			{
				countA = a.count();
			}
			else
			{
				countA = -1;
			}
			if (b != null)
			{
				countB = b.count();
			}
			else
			{
				countB = -1;
			}
		}

		// compare times when two events will occur
		@Override
		public int compareTo(final Event that)
		{
			if (this.time < that.time)
			{
				return -1;
			}
			else if (this.time > that.time)
			{
				return +1;
			}
			else
			{
				return 0;
			}
		}

		// has any collision occurred between when event was created and now?
		public boolean isValid()
		{
			if (a != null && a.count() != countA)
			{
				return false;
			}
			if (b != null && b.count() != countB)
			{
				return false;
			}
			return true;
		}

	}

	/***************************************************************************
	 * Sample client.
	 ***************************************************************************/
	public static void main(final String[] args)
	{

		StdDraw.setCanvasSize(800, 800);

		// remove the border
		// StdDraw.setXscale(1.0/22.0, 21.0/22.0);
		// StdDraw.setYscale(1.0/22.0, 21.0/22.0);

		// turn on animation mode
		StdDraw.show(0);

		// the array of particles
		Particle[] particles;

		// create N random particles
		if (args.length == 1)
		{
			final int N = Integer.parseInt(args[0]);
			particles = new Particle[N];
			for (int i = 0; i < N; i++)
			{
				particles[i] = new Particle();
			}
		}

		// or read from standard input
		else
		{
			final int N = StdIn.readInt();
			particles = new Particle[N];
			for (int i = 0; i < N; i++)
			{
				final double rx = StdIn.readDouble();
				final double ry = StdIn.readDouble();
				final double vx = StdIn.readDouble();
				final double vy = StdIn.readDouble();
				final double radius = StdIn.readDouble();
				final double mass = StdIn.readDouble();
				final int r = StdIn.readInt();
				final int g = StdIn.readInt();
				final int b = StdIn.readInt();
				final Color color = new Color(r, g, b);
				particles[i] = new Particle(rx, ry, vx, vy, radius, mass, color);
			}
		}

		// create collision system and simulate
		final CollisionSystem system = new CollisionSystem(particles);
		system.simulate(10000);
	}

	private final double		hz	= 0.5;	// number of redraw events per clock
											// tick
	private final Particle[]	particles;	// the array of particles

	private MinPQ<Event>		pq;		// the priority queue

	private double				t	= 0.0;	// simulation clock time

	// create a new collision system with the given set of particles
	public CollisionSystem(final Particle[] particles)
	{
		this.particles = particles.clone(); // defensive copy
	}

	// updates priority queue with all new events for particle a
	private void predict(final Particle a, final double limit)
	{
		if (a == null)
		{
			return;
		}

		// particle-particle collisions
		for (final Particle particle : particles)
		{
			final double dt = a.timeToHit(particle);
			if (t + dt <= limit)
			{
				pq.insert(new Event(t + dt, a, particle));
			}
		}

		// particle-wall collisions
		final double dtX = a.timeToHitVerticalWall();
		final double dtY = a.timeToHitHorizontalWall();
		if (t + dtX <= limit)
		{
			pq.insert(new Event(t + dtX, a, null));
		}
		if (t + dtY <= limit)
		{
			pq.insert(new Event(t + dtY, null, a));
		}
	}

	// redraw all particles
	private void redraw(final double limit)
	{
		StdDraw.clear();
		for (final Particle particle : particles)
		{
			particle.draw();
		}
		StdDraw.show(20);
		if (t < limit)
		{
			pq.insert(new Event(t + 1.0 / hz, null, null));
		}
	}

	/***************************************************************************
	 * Event based simulation for limit seconds.
	 ***************************************************************************/
	public void simulate(final double limit)
	{

		// initialize PQ with collision events and redraw event
		pq = new MinPQ<Event>();
		for (final Particle particle : particles)
		{
			predict(particle, limit);
		}
		pq.insert(new Event(0, null, null)); // redraw event

		// the main event-driven simulation loop
		while (!pq.isEmpty())
		{

			// get impending event, discard if invalidated
			final Event e = pq.delMin();
			if (!e.isValid())
			{
				continue;
			}
			final Particle a = e.a;
			final Particle b = e.b;

			// physical collision, so update positions, and then simulation
			// clock
			for (final Particle particle : particles)
			{
				particle.move(e.time - t);
			}
			t = e.time;

			// process event
			if (a != null && b != null)
			{
				a.bounceOff(b); // particle-particle collision
			}
			else if (a != null && b == null)
			{
				a.bounceOffVerticalWall(); // particle-wall collision
			}
			else if (a == null && b != null)
			{
				b.bounceOffHorizontalWall(); // particle-wall collision
			}
			else if (a == null && b == null)
			{
				redraw(limit); // redraw event
			}

			// update the priority queue with new collisions involving a or b
			predict(a, limit);
			predict(b, limit);
		}
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
