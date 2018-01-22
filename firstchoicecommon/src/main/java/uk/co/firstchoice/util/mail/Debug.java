
/*
 This class reads outputs a message to System output if debug mode has been set on.
 
 Author: A.James
 Created: 12/02/03
 */

package uk.co.firstchoice.util.mail;

/**
 * This class implements a debug object. By setting up such an object, an
 * application can control whether or not messages can be output to help debug
 * the application.
 * 
 * @author A.James
 */
public class Debug {

	boolean debugOn;

	/**
	 * Creates a new debug object and sets debug on or off.
	 * 
	 * @param debug
	 *            A value of <CODE>false</CODE> sets debug off and so no debug
	 *            messages will be produced. A value of <CODE>true</CODE>
	 *            turns debug on and will produce debug messages.
	 */
	public Debug(boolean debug) {
		debugOn = debug;
	}

	/**
	 * Outputs the message if debug is on. If debug is not on then no message
	 * will be produced.
	 * 
	 * @param msg
	 *            Debug message to be output.
	 */
	public void handleDebugMessage(String msg) {
		if (debugOn) {
			System.out.println("DEBUG: " + msg);
		}
	}
}