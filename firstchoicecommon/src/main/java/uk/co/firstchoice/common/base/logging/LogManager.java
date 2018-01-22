/**
 * File Name : LoggerManager.java
 * Created on 16-Feb-2005
 * @author Dwood
 *
 * Logger Management object that accesses log4j property infomation
 * and logger objects
 */
package uk.co.firstchoice.common.base.logging;

import java.io.IOException;

import org.apache.log4j.DailyRollingFileAppender;
import org.apache.log4j.FileAppender;
import org.apache.log4j.Layout;
import org.apache.log4j.PatternLayout;

import uk.co.firstchoice.common.base.config.properties.SystemConfig;

/**
 * Factory class to create Logger objects. Clients will use the Logger object to
 * log tracing messages to the log file. The class initialises the Log4J Logging
 * system as a 'PropertyConfigurator'
 * 
 * @see org.apache.log4j.PropertyConfigurator#configureAndWatch(java.lang.String,
 *      long). It will also setup Log4J to periodically monitor the
 *      configuration property file for changes. Any changes will result in
 *      Log4J being re-configured accordingly.
 * 
 * <p>
 * The class can configure the Log4J system is one of 2 ways:
 * <ol>
 * <li>The user passes a system parameter to the JVM. The system parameter
 * represents the **full path** location of a Log4J properties file. e.g
 * <code>java -DlogConfigFile=C:\temp\myconfig.properties LoggerManager </code>
 * instructs this class to configure Log4J from the file
 * C:\temp\myconfig.properties. The system property represented by the value of
 * the <code>CONFIG_FILENAME_SYSTEM_PROPERTY</code> constant shown below
 * should be used.
 * 
 * <li>If the user does not provide a default file location, then this class
 * looks for a default configuraion filename on the System's classloader. The
 * default filename is represented by the value of the
 * <code>DEFAULT_CONFIG_FILENAME</code> constant below.
 * </ol>
 * Option 1 will always take precedence over option 2.
 * 
 * @see org.apache.log4j;
 */
public final class LogManager {

    /**
     * Hold a reference to a single LogManager object to enforce the singleton
     * pattern
     */
    private static LogManager instance = null;

    /**
     * Hold a reference to a Log4J Logger object for this class. This class logs
     * to the Logger object after it has initialised it.
     */
    private static Logger logManagerLogger = new Log4JLogger();

    /**
     * Holds a reference  of properties file itsystem.properties
     *  
     */
     private SystemConfig systemConfig = null;
    
    /**
     * Return an instance of the LogManager
     * 
     * @return LogManager A logger factory object
     */
    public static LogManager getInstance() {
        if (instance == null) {
            /*
             * This class deliberately does not catch the RuntimeException that
             * could be thrown by the constructor to ensure that the VM
             * terminates. If Log4J cannot be configured properly then the
             * System has not been correctly initialised.
             */
            instance = new LogManager();
        }

        return instance;
    }

    /**
     * Method creates and returns a new Log4J Logger object. The logger is named
     * according to the loggingClass parameter.
     * 
     * @return Logger A Log4J Logger object
     * @see org.apache.log4j.Logger
     */
    public static Logger getLogger() {
        return logManagerLogger;
    }

    /**
     * Sets the current logger. A message the current logger is being replaced
     * will be displayed to the old logger.
     * 
     * @param logger
     *            Logger
     */
    public static void setLogger(Logger logger) {
        if (logger == null) {
            throw new IllegalArgumentException("Null logger not allowed.");
        }
        if (logManagerLogger != null && !logger.equals(logger)) {
            StringBuffer message = new StringBuffer(128);
            message.append("This logger has been replaced by logger: ");
            message.append(logger.getClass().getName());
            logManagerLogger.logInfo(message.toString());
        }

        logManagerLogger = logger;
    }

    /**
     * Private constructor to disallow the construction of an object of this
     * class. Clients create LogManager objects using the getInstance() method.
     */
    private LogManager() {
        // Ititialise the super class
        super();

        systemConfig = SystemConfig.getInstance();
        String logfileName = systemConfig.getParameter(SystemConfig.LOGGER_FILE_NAME);
        String logLayout = systemConfig.getParameter(SystemConfig.LOGGER_LAYOUT);
        String logLayoutPattern = systemConfig.getParameter(SystemConfig.LOGGER_LAYOUT_PATTERN);

        String logAppenderDatePattern = systemConfig.getParameter(SystemConfig.LOGGER_APPENDER_DATEPATTERN);
       
        // check to see if class is avalible on class path
        try {
            Class layoutClass = Class.forName(logLayout);
            Layout layout = (Layout) layoutClass.newInstance();
            FileAppender fileAppender = null;
            DailyRollingFileAppender dayFileAppender = null;
            PatternLayout patternlayout = null;
            if (layout instanceof PatternLayout && logLayoutPattern != null) {
                patternlayout = (PatternLayout) layout;
                patternlayout.setConversionPattern(logLayoutPattern);
            }
            if (logAppenderDatePattern == null) {
                if (patternlayout != null) {
                    fileAppender = new FileAppender(patternlayout, logfileName);
                } else {
                    fileAppender = new FileAppender(layout, logfileName);
                }
                logManagerLogger = new Log4JLogger(fileAppender);
            } else {
                if (patternlayout != null) {
                    dayFileAppender = new DailyRollingFileAppender(patternlayout, logfileName, logAppenderDatePattern);
                } else {
                    dayFileAppender = new DailyRollingFileAppender(layout, logfileName, logAppenderDatePattern);
                }
                logManagerLogger = new Log4JLogger(dayFileAppender);
            }
        } catch (ClassNotFoundException e) {
            System.err.print("Error converting Object to Class Type, class not found: " + e);
        } catch (InstantiationException e) {
        	System.err.print("InstantiationException error for logger: " + e);
        } catch (IllegalAccessException e) {
            System.err.print("IllegalAccessException error for logger: " + e);
        } catch (IOException e) {
            System.out.println("IO error whilst log configuration file: " + e);
        }

        setLogger(logManagerLogger);

    }
	
	/**
	 * @return Returns the systemConfig.
	 */
	public SystemConfig getSystemConfig() {
		return systemConfig;
	}
	/**
	 * @param systemConfig The systemConfig to set.
	 */
	public void setSystemConfig(SystemConfig systemConfig) {
		this.systemConfig = systemConfig;
	}
}