/******************************************************************************
 *  Compilation:  javac Particle.java
 *  Execution:    none
 *  Dependencies: StdDraw.java
 *
 *  A particle moving in the unit box with a given position, velocity,
 *  radius, and mass.
 *
 ******************************************************************************/

package edu.princeton.cs.algs4;

import java.awt.Color;

public class Particle
{
	private static final double	INFINITY	= Double.POSITIVE_INFINITY;

	private final Color			color;									// color
	private int					count;									// number
																		// of
																		// collisions
																		// so
																		// far
	private final double		mass;									// mass
	private final double		radius;								// radius
	private double				rx, ry;								// position
	private double				vx, vy;								// velocity

	// create a random particle in the unit box (overlaps not checked)
	public Particle()
	{
		rx = StdRandom.uniform(0.0, 1.0);
		ry = StdRandom.uniform(0.0, 1.0);
		vx = StdRandom.uniform(-.005, 0.005);
		vy = StdRandom.uniform(-.005, 0.005);
		radius = 0.01;
		mass = 0.5;
		color = Color.BLACK;
	}

	// create a new particle with given parameters
	public Particle(final double rx, final double ry, final double vx,
			final double vy, final double radius, final double mass,
			final Color color)
	{
		this.vx = vx;
		this.vy = vy;
		this.rx = rx;
		this.ry = ry;
		this.radius = radius;
		this.mass = mass;
		this.color = color;
	}

	// update velocities upon collision between this particle and that particle
	public void bounceOff(final Particle that)
	{
		final double dx = that.rx - this.rx;
		final double dy = that.ry - this.ry;
		final double dvx = that.vx - this.vx;
		final double dvy = that.vy - this.vy;
		final double dvdr = dx * dvx + dy * dvy; // dv dot dr
		final double dist = this.radius + that.radius; // distance between
														// particle centers at
														// collison

		// normal force F, and in x and y directions
		final double F = 2 * this.mass * that.mass * dvdr
				/ ((this.mass + that.mass) * dist);
		final double fx = F * dx / dist;
		final double fy = F * dy / dist;

		// update velocities according to normal force
		this.vx += fx / this.mass;
		this.vy += fy / this.mass;
		that.vx -= fx / that.mass;
		that.vy -= fy / that.mass;

		// update collision counts
		this.count++;
		that.count++;
	}

	// update velocity of this particle upon collision with a horizontal wall
	public void bounceOffHorizontalWall()
	{
		vy = -vy;
		count++;
	}

	// update velocity of this particle upon collision with a vertical wall
	public void bounceOffVerticalWall()
	{
		vx = -vx;
		count++;
	}

	// return the number of collisions involving this particle
	public int count()
	{
		return count;
	}

	// draw the particle
	public void draw()
	{
		StdDraw.setPenColor(color);
		StdDraw.filledCircle(rx, ry, radius);
	}

	// return kinetic energy associated with this particle
	public double kineticEnergy()
	{
		return 0.5 * mass * (vx * vx + vy * vy);
	}

	// updates position
	public void move(final double dt)
	{
		rx += vx * dt;
		ry += vy * dt;
	}

	// how long into future until collision between this particle a and b?
	public double timeToHit(final Particle b)
	{
		final Particle a = this;
		if (a == b)
		{
			return INFINITY;
		}
		final double dx = b.rx - a.rx;
		final double dy = b.ry - a.ry;
		final double dvx = b.vx - a.vx;
		final double dvy = b.vy - a.vy;
		final double dvdr = dx * dvx + dy * dvy;
		if (dvdr > 0)
		{
			return INFINITY;
		}
		final double dvdv = dvx * dvx + dvy * dvy;
		final double drdr = dx * dx + dy * dy;
		final double sigma = a.radius + b.radius;
		final double d = dvdr * dvdr - dvdv * (drdr - sigma * sigma);
		// if (drdr < sigma*sigma) StdOut.println("overlapping particles");
		if (d < 0)
		{
			return INFINITY;
		}
		return -(dvdr + Math.sqrt(d)) / dvdv;
	}

	// how long into future until this particle collides with a horizontal wall?
	public double timeToHitHorizontalWall()
	{
		if (vy > 0)
		{
			return (1.0 - ry - radius) / vy;
		}
		else if (vy < 0)
		{
			return (radius - ry) / vy;
		}
		else
		{
			return INFINITY;
		}
	}

	// how long into future until this particle collides with a vertical wall?
	public double timeToHitVerticalWall()
	{
		if (vx > 0)
		{
			return (1.0 - rx - radius) / vx;
		}
		else if (vx < 0)
		{
			return (radius - rx) / vx;
		}
		else
		{
			return INFINITY;
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
