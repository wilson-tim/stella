package uk.co.firstchoice.common.base.config.properties;

import java.util.Enumeration;
import java.util.Properties;

import uk.co.firstchoice.common.base.InternalApplicationException;

/**
 * This class provides base functionality for managing configuration properties
 * for applications.
 * 
 * <p>
 * This class assumes that your configuration file is a properties file (see
 * java.util.Properties). If it isn't, you'll have to override the
 * loadProperties method. More detail below.
 * 
 * <p>
 * By default, this class will look for updated properties on the first use
 * after 10 minutes of the last check for updates. You can change this interval
 * by overriding the method getUpdateTimeIntervalInMillis(). An example is
 * provided.
 * 
 * <p>
 * To use this class, do the following:
 * <li>Extend this class in your application.</li>
 * <li>Implement the getConfigurationFileName() method, which returns the name
 * of your configuration file. A sample implementation follows:</li>
 * <blockquote>
 * 
 * <pre>
 * protected String getConfigurationFileName() {
 *     return &quot;myapp.properties&quot;;
 * }
 * </pre>
 * 
 * </blockquote>
 * <li>Declare a singleton instance as follows (SampleEnvironment extends
 * ApplicationEnvironment):</li>
 * <blockquote>
 * 
 * <pre>
 * private static final SampleEnvironment _myEnvironment = new SampleEnvironment();
 * </pre>
 * 
 * </blockquote>
 * <li>Declare an accessor for each of your application properties. An example
 * follows:</li>
 * <blockquote>
 * 
 * <pre>
 * public static String getDatabaseConnectionPoolName() {
 *     return _myEnvironment.getProperty(&quot;db.pool&quot;);
 * }
 * </pre>
 * 
 * </blockquote>
 * <li>If you want to change the default refresh interval of 10 minutes,
 * override method getUpdateTimeIntervalInMillis(). An example follows:</li>
 * <blockquote>
 * 
 * <pre>
 * protected long getUpdateTimeIntervalInMillis() {
 *     return 1200000;
 * }
 * </pre>
 * 
 * </blockquote>
 * <li>If you have a configuration file in another format, you can override the
 * loadProperties() method and set the properties individually with
 * setProperty().</li>
 * 
 *  
 */
public abstract class ApplicationEnvironment {
    protected ApplicationEnvironment() {
    }

    /**
     * Returns the value of a property from the environment.
     * 
     * @param name
     * @return value
     */
    protected String getProperty(String name) {
        return this.getProperty(name, null);
    }

    /**
     * Returns the value of a property from the environment. If the property
     * doesn't exist, the default value is returned.
     * 
     * @param name
     * @param defaultValue
     * @return value
     */
    protected String getProperty(String name, String defaultValue) {
        this.validateProperties();
        return _configProperties.getProperty(name, defaultValue);
    }

    /**
     * Sets a property in the environment. Neither the proporty or its value can
     * be null.
     * 
     * @param property
     * @param value
     */
    protected void setProperty(String property, String value) {
        if (property == null)
            throw new IllegalArgumentException("Null property not allowed.");
        if (value == null)
            throw new IllegalArgumentException("Null value not allowed.");
        if (property.equals(""))
            throw new IllegalArgumentException("Blank property not allowed.");
        _configProperties.setProperty(property, value);
    }

    /*
     * Reloads the properties file if it has changed.
     */
    private void validateProperties() {
        if (_lastFileCheckTimestamp + this.getUpdateTimeIntervalInMillis() < System
                .currentTimeMillis()) {
            this.loadProperties();
        }
    }

    /*
     * Loads the properties file.
     */
    protected void loadProperties() {
        Properties props = new Properties();
        try {
            props.load(ApplicationEnvironment.class.getClassLoader()
                    .getResourceAsStream(this.getConfigurationFileName()));
        } catch (Throwable t) {
            throw new InternalApplicationException("Problem loading "
                    + this.getConfigurationFileName(), t);
        }
        this.loadProperties(props);
        _lastUpdateTimestamp = System.currentTimeMillis();
    }

    /**
     * Appends properties to the application property set.
     * 
     * @param props
     */
    protected void loadProperties(Properties props) {
        Enumeration propNameEnum = props.propertyNames();
        String property, value;

        while (propNameEnum.hasMoreElements()) {
            property = (String) propNameEnum.nextElement();
            value = props.getProperty(property);
            this.setProperty(property, value);
        }
    }

    protected Enumeration getPropertyKeyEnumeration() {
        return _configProperties.keys();
    }

    /**
     * Method to return the applications configuration property filename.
     * 
     * @return fileName
     */
    protected abstract String getConfigurationFileName();

    /**
     * Provides the refresh time interval used to check fo changes in the
     * properties file.
     * 
     * @return timeInMillis
     */
    protected long getUpdateTimeIntervalInMillis() {
        return DEFAULT_UPDATE_TIME_INTERVAL;
    }

    /**
     * The default update time interval for application properties is 10
     * minutes.
     */
    public static final long DEFAULT_UPDATE_TIME_INTERVAL = 600000;

    public static final String DEFAULT_CONFIG_FILE_NAME = "default.properties";

    private long _lastUpdateTimestamp = 0;

    private long _lastFileCheckTimestamp = 0;

    private Properties _configProperties = new Properties();
}