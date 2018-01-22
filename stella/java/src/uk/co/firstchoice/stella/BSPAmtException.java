package uk.co.firstchoice.stella;

/**
 * BSPAmtException Throw when an invalid bsp amt is encountered
 * 
 * @author leigh ashton Created on 3 june 2003 Leigh
 * @version 1.0
 */
public class BSPAmtException extends java.lang.Exception {

	/**
	 * Creates a new instance of <code>BSPAmtException</code> without detail
	 * message.
	 */
	public BSPAmtException() {
	}

	/**
	 * Constructs an instance of <code>BSPAmtException</code> with the
	 * specified detail message.
	 * 
	 * @param msg
	 *            the detail message.
	 */
	public BSPAmtException(String msg) {
		super(msg);
	}
}

