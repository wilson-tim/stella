/*
 * NoMailRecipientException.java
 *
 * Created on 05 March 2003, 17:48
 */

package uk.co.firstchoice.util.mail;

/**
 * Signals that there aren't any recipients for sending the mail.
 * 
 * @author A.James
 */
public class NoMailRecipientException extends java.lang.Exception {

	/**
	 * Constructs an <CODE>NoMailRecipientException</CODE> with null as its
	 * error detail message.
	 */
	public NoMailRecipientException() {
	}

	/**
	 * Constructs an <CODE>NoMailRecipientException</CODE> with the specified
	 * detail message.
	 * 
	 * @param msg
	 *            the detail message.
	 */
	public NoMailRecipientException(String msg) {
		super(msg);
	}
}