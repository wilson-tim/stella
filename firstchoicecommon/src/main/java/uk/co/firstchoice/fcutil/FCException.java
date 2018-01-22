/*
 * FCException.java
 *
 * Created on 08 May 2003, 15:01
 */

package uk.co.firstchoice.fcutil;

/**
 * 
 * @author Lsvensson
 */
public class FCException extends java.lang.Exception {

	/**
	 * Creates a new instance of <code>FCException</code> without detail
	 * message.
	 */
	public FCException() {
	}

	/**
	 * Constructs an instance of <code>FCException</code> with the specified
	 * detail message.
	 * 
	 * @param msg
	 *            the detail message.
	 */
	public FCException(String msg) {
		super(msg);
	}

	/**
	 * Constructs an instance of <code>FCException</code> with the specified
	 * detail message.
	 * 
	 * @param msg
	 *            the detail message.
	 */
	public FCException(String msg, Throwable e) {
		super(msg, e);
	}

}