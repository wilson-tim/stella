/**
 * File Name : AccrualDAO.java
 * Created on 13-Jan-2005
 * @author Dwood
 *
 * Logger RuntimeException object that accesses log4j property infomation
 * and logger objects
 */
package uk.co.firstchoice.common.base.logging;

/**
 * * Logger RuntimeException object that accesses log4j property infomation and
 * logger objects
 */
public class LogManagerRuntimeException extends RuntimeException {

	/**
	 * Default constructor. Initalises an empty LoggerFactoryRuntimeException
	 * object
	 */
	public LogManagerRuntimeException() {
		super();
	}

	/**
	 * Construct a LoggerFactoryRuntimeException with a specific error message
	 * 
	 * @param msg
	 *            String
	 */
	public LogManagerRuntimeException(String msg) {
		super(msg);
	}

}