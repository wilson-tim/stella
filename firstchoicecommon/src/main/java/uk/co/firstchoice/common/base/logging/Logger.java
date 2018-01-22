/**
 * File Name : Logger.java
 * Created on 16-Feb-2005
 * @author Dwood
 *
 * Interface for all logger objects
 */
package uk.co.firstchoice.common.base.logging;

/**
 * 
 * Interface for all logger objects
 */
public interface Logger {

	public void log(int level, Object message);

	public void log(int level, Object message, Throwable error);

	public void logDebug(Object message);

	public void logInfo(Object message);

	public void logWarning(Object message);

	public void logError(Object message);

	public void logFatal(Object message);

	public void logDebug(Object message, Throwable error);

	public void logInfo(Object message, Throwable error);

	public void logWarning(Object message, Throwable error);

	public void logError(Object message, Throwable error);

	public void logFatal(Object message, Throwable error);

	public static final int DEBUG = 0;

	public static final int INFO = 1;

	public static final int WARNING = 2;

	public static final int ERROR = 3;

	public static final int FATAL = 4;

}