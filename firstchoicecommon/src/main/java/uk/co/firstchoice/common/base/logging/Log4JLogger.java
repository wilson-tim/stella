package uk.co.firstchoice.common.base.logging;

import org.apache.log4j.Appender;
import org.apache.log4j.ConsoleAppender;
import org.apache.log4j.Hierarchy;
import org.apache.log4j.Level;
import org.apache.log4j.SimpleLayout;
import org.apache.log4j.spi.LoggerRepository;

/**
 * A Log4J implementation of Logger for those applications who use Log4J.
 *  
 */
public class Log4JLogger extends org.apache.log4j.Logger implements
		uk.co.firstchoice.common.base.logging.Logger {

	/**
	 * Log4JLogger()
	 */
	public Log4JLogger() {
		this(null, new ConsoleAppender(new SimpleLayout()));
	}

	/**
	 * @param appender
	 *            Appender
	 */
	public Log4JLogger(Appender appender) {
		this(null, appender);
	}

	/**
	 * @param repository
	 *            LoggerRepository
	 * @param appender
	 *            Appender
	 */
	public Log4JLogger(LoggerRepository repository, Appender appender) {
		super("org.apache.log4j.Logger");

		if (appender == null)
			throw new IllegalArgumentException("Null appender not allowed.");

		if (repository == null) {
			this.repository = new Hierarchy(this);
		} else {
			this.repository = repository;
		}

		this.setLevel(Level.ALL);
		this.addAppender(appender);
	}

	/**
	 * @param level
	 *            int
	 * @param message
	 *            Object
	 */
	public void log(int level, Object message) {
		switch (level) {
		case uk.co.firstchoice.common.base.logging.Logger.DEBUG: {
			this.debug(message);
			break;
		}
		case uk.co.firstchoice.common.base.logging.Logger.FATAL: {
			this.fatal(message);
			break;
		}
		case uk.co.firstchoice.common.base.logging.Logger.INFO: {
			this.info(message);
			break;
		}
		case uk.co.firstchoice.common.base.logging.Logger.WARNING: {
			this.warn(message);
			break;
		}
		default: {
			this.error(message);
			break;
		}
		}
	}

	/**
	 * @param level
	 *            int
	 * @param message
	 *            Object
	 * @param error
	 *            Throwable
	 */
	public void log(int level, Object message, Throwable error) {
		switch (level) {
		case uk.co.firstchoice.common.base.logging.Logger.DEBUG: {
			this.debug(message, error);
			break;
		}
		case uk.co.firstchoice.common.base.logging.Logger.FATAL: {
			this.fatal(message, error);
			break;
		}
		case uk.co.firstchoice.common.base.logging.Logger.INFO: {
			this.info(message, error);
			break;
		}
		case uk.co.firstchoice.common.base.logging.Logger.WARNING: {
			this.warn(message, error);
			break;
		}
		default: {
			this.error(message, error);
			break;
		}
		}
	}

	/**
	 * message Object
	 */
	public void logDebug(Object message) {
		this.debug(message);
	}

	/**
	 * message Object error Throwable
	 */
	public void logDebug(Object message, Throwable error) {
		this.debug(message, error);
	}

	/**
	 * message Object
	 */
	public void logInfo(Object message) {
		this.info(message);
	}

	/**
	 * message Object error Throwable
	 */
	public void logInfo(Object message, Throwable error) {
		this.info(message, error);
	}

	/**
	 * message Object
	 */
	public void logWarning(Object message) {
		this.warn(message);
	}

	/**
	 * message Object error Throwable
	 */
	public void logWarning(Object message, Throwable error) {
		this.warn(message, error);
	}

	/**
	 * message Object
	 */
	public void logError(Object message) {
		this.error(message);
	}

	/**
	 * message Object error Throwable
	 */
	public void logError(Object message, Throwable error) {
		this.error(message, error);
	}

	/**
	 * message Object
	 */
	public void logFatal(Object message) {
		this.fatal(message);
	}

	/**
	 * message Object error Throwable
	 */
	public void logFatal(Object message, Throwable error) {
		this.fatal(message, error);
	}
}