package uk.co.firstchoice.common.base.util;

import java.io.ByteArrayOutputStream;
import java.io.PrintWriter;
import java.util.Collection;
import java.util.Hashtable;

/**
 * A collection of exception oriented utilities.
 * 
 * <p>.
 */
public class ExceptionUtility {

	// Prevent instantiation
	protected ExceptionUtility() {
	}

	/**
	 * Convenience one-liner to convert a Throwable stack trace to a string.
	 * 
	 * @param throwable
	 * @return trace content as string
	 *  
	 */
	public static final String getStackTraceAsString(Throwable e) {
		if (e == null)
			throw new IllegalArgumentException("Null exception not allowed.");
		ByteArrayOutputStream byteStream = null;
		PrintWriter printWriter = null;
		String stackTrace = null;

		byteStream = new ByteArrayOutputStream();
		printWriter = new PrintWriter(byteStream, true);

		e.printStackTrace(printWriter);
		printWriter.flush();
		stackTrace = byteStream.toString();
		printWriter.close();

		return stackTrace;
	}

	private static String produceExceptionMessage(String type, String label) {
		StringBuffer message = new StringBuffer(48);
		message.append(type);
		message.append(" ");
		if (label != null)
			message.append(label);
		else
			message.append("argument");
		message.append(" is not allowed.");

		return message.toString();
	}

	/**
	 * Throws an IllegalArgumentException for null objects. This gets a typical
	 * null value check down to a one-line call.
	 * 
	 * @param value
	 *            to be verified
	 * @param label
	 *            used in exception if necessary
	 */
	public static void exceptForNulls(Object value, String label) {
		if (value == null) {
			throw new IllegalArgumentException(ExceptionUtility
					.produceExceptionMessage("Null", label));
		}
	}

	/**
	 * Throws an IllegalArgumentException for null and blank Strings. This gets
	 * a typical null value check down to a one-line call.
	 * 
	 * @param value
	 *            to be verified
	 * @param label
	 *            used in exception if necessary
	 */
	public static void exceptForNullsAndBlanks(Object value, String label) {
		ExceptionUtility.exceptForNulls(value, label);

		if (value instanceof Hashtable) {
			ExceptionUtility.pExceptForBlanks((Hashtable) value, label);
		} else if (value instanceof Collection) {
			ExceptionUtility.pExceptForBlanks((Collection) value, label);
		}
	}

	/**
	 * Throws an IllegalArgumentException for null and blank Strings. This gets
	 * a typical null value check down to a one-line call.
	 * 
	 * @param value
	 *            to be verified
	 * @param label
	 *            used in exception if necessary
	 */
	public static void exceptForNullsAndBlanks(String value, String label) {
		ExceptionUtility.exceptForNulls(value, label);

		if (value.equals("")) {
			throw new IllegalArgumentException(ExceptionUtility
					.produceExceptionMessage("Blank", label));
		}
	}

	/**
	 * Throws an IllegalArgumentException for blank Hashtables. This gets a
	 * typical null value check down to a one-line call.
	 * 
	 * @param value
	 *            to be verified
	 * @param label
	 *            used in exception if necessary
	 */
	private static void pExceptForBlanks(Hashtable value, String label) {
		if (value.size() == 0) {
			throw new IllegalArgumentException(ExceptionUtility
					.produceExceptionMessage("Zero-sized ", label));
		}
	}

	/**
	 * Throws an IllegalArgumentException for blank Collections. This gets a
	 * typical null value check down to a one-line call.
	 * 
	 * @param value
	 *            to be verified
	 * @param label
	 *            used in exception if necessary
	 */
	private static void pExceptForBlanks(Collection value, String label) {
		if (value.size() == 0) {
			throw new IllegalArgumentException(ExceptionUtility
					.produceExceptionMessage("Zero-sized ", label));
		}
	}
}