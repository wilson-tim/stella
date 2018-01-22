/*
 * PropertyNotFoundException.java
 *
 * Created on 04 November 2002, 15:01
 */

package uk.co.firstchoice.util;

/**
 * PropertyNotFoundException Throw when a property is looked up that does not
 * exist or has no value
 * 
 * @author rbryer
 */
public class PropertyNotFoundException extends java.lang.Exception {

	/**
	 * Creates a new instance of <code>PropertyNotFoundException</code>
	 * without detail message.
	 */
	public PropertyNotFoundException() {
	}

	/**
	 * Constructs an instance of <code>PropertyNotFoundException</code> with
	 * the specified detail message.
	 * 
	 * @param msg
	 *            the detail message.
	 */
	public PropertyNotFoundException(String msg) {
		super(msg);
	}
}