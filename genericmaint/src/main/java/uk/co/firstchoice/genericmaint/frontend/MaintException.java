/*
 * StellaException.java
 *
 * Created on 16 May 2003, 16:30
 */

package uk.co.firstchoice.genericmaint.frontend;

/**
 *
 * @author  Lsvensson
 */
public class MaintException extends Exception {
	
	/**
	 * Creates a new instance of <code>StellaException</code> without detail message.
	 */
	public MaintException() {
	}
	
	
	/**
	 * Constructs an instance of <code>StellaException</code> with the specified detail message.
	 * @param msg the detail message.
	 */
//	public StellaException(String msg) {
//		super(msg);
//	}
	
	
	/**
	 * Constructs an instance of <code>StellaException</code> with the specified detail message and exception.
	 * @param msg the detail message.
	 * @param e the causing exception.
	 */
	public MaintException(String msg, Throwable e) {
		super(msg, e);
	}
	
}
