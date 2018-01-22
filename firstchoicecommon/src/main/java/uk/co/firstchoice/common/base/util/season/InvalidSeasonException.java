/*
 * InvalidSeasonException.java
 *
 * Created on 30 March 2004, 16:44
 */

package uk.co.firstchoice.common.base.util.season;

/**
 * 
 * @author Lsvensson
 */
public class InvalidSeasonException extends java.lang.Exception {

	/**
	 * Creates a new instance of <code>InvalidSeasonException</code> without
	 * detail message.
	 */
	public InvalidSeasonException() {
	}

	/**
	 * Constructs an instance of <code>InvalidSeasonException</code> with the
	 * specified detail message.
	 * 
	 * @param msg
	 *            the detail message.
	 */
	public InvalidSeasonException(String msg) {
		super(msg);
	}
}