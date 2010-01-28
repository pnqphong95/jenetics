/*
 * Java Genetic Algorithm Library (@!identifier!@).
 * Copyright (c) @!year!@ Franz Wilhelmstötter
 *  
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 * 
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA
 *
 * Author:
 *     Franz Wilhelmstötter (franz.wilhelmstoetter@gmx.at)
 *     
 */
package org.jenetics.util;

import static org.jenetics.util.Validator.nonNull;

import java.io.Serializable;

import javax.measure.Measurable;
import javax.measure.Measure;
import javax.measure.quantity.Duration;
import javax.measure.unit.SI;

import javolution.lang.Reusable;

/**
 * Timer for measure the performance of the GA. The timer uses nano second
 * precision (by using {@link System#nanoTime()}). This timer is not synchronized.
 * It's up to the user to ensure thread safety.
 * 
 * @author <a href="mailto:franz.wilhelmstoetter@gmx.at">Franz Wilhelmstötter</a>
 * @version $Id: Timer.java,v 1.9 2010-01-28 19:34:14 fwilhelm Exp $
 */
public class Timer implements Comparable<Timer>, Reusable, Serializable, Cloneable {
	private static final long serialVersionUID = -4564917943200602352L;
	private static final String DEFAULT_LABEL = "Timer";
	
	private String _label;
	private long _start = 0;
	private long _stop = 0;
	private long _sum = 0;
	
	/**
	 * Create a new time with the given label. The label is use in the 
	 * {@link #toString()} method.
	 * 
	 * @param label the timer label.
	 * @throws NullPointerException if the {@code label} is {@code null}.
	 */
	public Timer(final String label) {
		_label = nonNull(label, "Time label");
	}
	
	/**
	 * Create a new Timer object.
	 */
	public Timer() {
		this(DEFAULT_LABEL);
	}
	
	/**
	 * Start the timer.
	 */
	public void start() {
		_start = System.nanoTime();
	}
	
	/**
	 * Stop the timer.
	 */
	public void stop() {
		_stop = System.nanoTime();
		_sum += _stop - _start;
	}
	
	/**
	 * Reset the timer.
	 */
	@Override
	public void reset() {
		_sum = 0;
		_start = 0;
		_stop = 0;
		_label = DEFAULT_LABEL;
	}
	
	/**
	 * Return the overall time of this timer. The following code snippet would
	 * return a measured time of 10 s (theoretically).
	 * [code]
	 *     final Timer timer = new Timer();
	 *     for (int i = 0; i < 10) {
	 *         timer.start();
	 *         Thread.sleep(1000);
	 *         timer.stop();
	 *     }
	 * [/code]
	 * 
	 * @return the measured time so far.
	 */
	public Measurable<Duration> getTime() {
		return Measure.valueOf(_sum, SI.NANO(SI.SECOND));
	}
	
	/**
	 * Return the time between two successive calls of {@link #start()} and
	 * {@link #stop()}.
	 * 
	 * @return the interim time measured.
	 */
	public Measurable<Duration> getInterimTime() {
		return Measure.valueOf(_stop - _start, SI.NANO(SI.SECOND));
	}
	
	/**
	 * Return the timer label.
	 * 
	 * @return the timer label.
	 */
	public String getLabel() {
		return _label;
	}
	
	/**
	 * Set the timer label.
	 * 
	 * @param label the new timer label
	 */
	public void setLabel(final String label) {
		_label = nonNull(label, "Timer label");
	}
	
	@Override
	public int compareTo(final Timer timer) {
		nonNull(timer, "Timer");
		
		long diff = _sum - timer._sum;
		int comp = 0;
		if (diff < 0) {
			comp = -1;
		} else if (diff > 0) {
			comp = 1;
		}
		return comp;
	}
	
	@Override
	public int hashCode() {
		int hash = 17;
		
		hash += 37*_label.hashCode() + 17;
		hash += 37*_start + 17;
		hash += 37*_stop + 17;
		hash += 37*_sum + 17;
		
		return hash;
	}
	
	@Override
	public boolean equals(final Object object) {
		if (object == this) {
			return true;
		}
		if (!(object instanceof Timer)) {
			return false;
		}
		
		final Timer timer = (Timer)object;
		return _start == timer._start &&
				_stop == timer._stop &&
				_sum == timer._sum &&
				_label.equals(timer._label);
	}
	
	@Override
	public Timer clone() {
		try {
			return (Timer)super.clone();
		} catch (CloneNotSupportedException e) {
			throw new AssertionError(e);
		}
	}
	
	@Override
	public String toString() {
		return String.format("%s: %11.11f s", _label, getTime().doubleValue(SI.SECOND));
	}
	
}



