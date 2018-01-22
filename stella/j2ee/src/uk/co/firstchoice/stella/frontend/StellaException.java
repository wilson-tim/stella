/*
 * StellaException.java
 *
 * Created on 10 February 2003, 12:41
 */

package uk.co.firstchoice.stella.frontend;

/**
 * 
 * @author Lsvensson
 */
public class StellaException extends Exception {

	/**
	 * Creates a new instance of <code>StellaException</code> without detail
	 * message.
	 */
	public StellaException() {
	}

	/**
	 * Constructs an instance of <code>StellaException</code> with the
	 * specified detail message.
	 * 
	 * @param msg
	 *            the detail message.
	 */
	//	public StellaException(String msg) {
	//		super(msg);
	//	}
	/**
	 * Constructs an instance of <code>StellaException</code> with the
	 * specified detail message and exception.
	 * 
	 * @param msg
	 *            the detail message.
	 * @param e
	 *            the causing exception.
	 */
	public StellaException(String msg, Throwable e) {
		super(msg, e);
	}

}