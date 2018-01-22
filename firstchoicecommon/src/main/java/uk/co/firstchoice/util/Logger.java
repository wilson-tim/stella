/*
 * Logger.java
 *
 * Created on 06 November 2002, 10:55
 * amended Leigh jan 03 - added stuff to make closer to 1.4 implementation level name with level integer value pair
 * and added code to prevent logging if level set below threshold
 */

package uk.co.firstchoice.util;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Logger This class provides basic logging methods. It is used
 * 
 * Note this can be replaced by the java.util.logging.Logger class if Java 1.4
 * or above is used. The function names defined here are the same as those
 * defined in the above class to ease the move.
 * 
 * @author rbryer
 */
public class Logger {

	private String name;

	private BufferedWriter os = null;

	private volatile int levelValue; // current effective level value

	private volatile boolean sysOutRequired = true;

	private static final int offValue = Level.OFF.intValue();

	/**
	 * Creates a new instance of Logger Protected method, loggers can only be
	 * create by a call to the factory method getLogger()
	 */
	protected Logger(String name) {
		this.name = name;
		levelValue = Level.INFO.intValue(); // default to info level
	}

	/**
	 * getLogger() factory method to get a suitable logger.
	 *  
	 */
	public static Logger getLogger(String name) {

		// currently just returns a new logger. This function is more
		// sophisticated
		// in the 1.4 JDK implementation

		Logger logger = new Logger(name);
		return logger;
	}

	/**
	 * setLoggerFile set the file that the logging information should be logged
	 * to. If this is null the the log will be output to screen only.
	 */
	public boolean setLoggerFile(File file) {
		try {
			os = new BufferedWriter(new FileWriter(file));
		} catch (IOException ex) {
			System.out.println("Warning : The logger file "
					+ file.getAbsolutePath()
					+ " can't be opened, logging to screen");
			return false;
		}
		return true;
	}

	/**
	 * close Closes down the logger and returns any resources
	 */
	public void close() {
		try {
			os.close();
		} catch (Exception ex) {
			// null
		}
	}

	/*
	 * Logging methods. These log to the file system and should dynamically
	 * redirect them self to either the local file system or server file server
	 * dependant on if the Application is being run as a pure Java application
	 * or as an Oracle Java Stored procedure. If being run within the database
	 * then the appropriate file permission must be granted with a call to
	 * dbms_java.grant_permission() on the database.
	 */

	// Change these methods to logger methods if using 1.4 JDK
	public void severe(String msg) {
		logMessage(Level.SEVERE, msg);
	}

	public void warning(String msg) {
		logMessage(Level.WARNING, msg);
	}

	public void info(String msg) {
		logMessage(Level.INFO, msg);
	}

	public void config(String msg) {
		logMessage(Level.CONFIG, msg);
	}

	public void fine(String msg) {
		logMessage(Level.FINE, msg);
	}

	public void finer(String msg) {
		logMessage(Level.FINER, msg);
	}

	public void finest(String msg) {
		logMessage(Level.FINEST, msg);
	}

	public void setLevel(int newLevel) {

		levelValue = newLevel;
	}

	public void setLogSysOut(boolean newLevel) {

		sysOutRequired = newLevel;
	}

	/*
	 * @return the level of messages being logged.
	 */
	public int getLevel() {
		return levelValue;
	}

	public boolean getLogSysOut() {
		return sysOutRequired;
	}

	public void logMessage(Level level, String msg) {
		String message;

		if (level.intValue() < levelValue || levelValue == offValue) {
			return;
		}

		message = level.getName() + " :" + msg;
		if (sysOutRequired) {
			System.out.println(message);
		}

		// if a file has been defined then log to file
		if (os != null) {
			try {
				os.write(message + "\r\n");
				os.flush();
			} catch (IOException ex) {
				// do nothing as the message has been writtern to sout
			}

		}
	}

}