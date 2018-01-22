/**
 * File Name : SystemConfigRuntimeException.java
 * Created on 14-Jan-2005 @author
 * Dwood
 *
 */
package uk.co.firstchoice.common.base.config.properties;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Properties;

import javax.naming.NamingException;

import uk.co.firstchoice.common.base.util.J2EEUtility;

/**
 * 
 * Singleton class to handle all system configuration (e.g. IP address)
 * information.
 * <p>
 * The class reads system configuration properties from the filename passed as
 * as JNDI ref, a system property 'systemConfigFile', or as a file on the
 * classpath.
 * <p>
 * e.g. java -DsystemConfigFile="C:\temp\system.props" reads the system
 * configuration properties from the file C:\temp\system.props.
 * <p>
 * Note: This class represents system properties that are changeable at system
 * deployment time but not changeable at runtime. e.g. IP Addresses, JNDI Names
 * etc. Once the class is constructed the system configuration properties cannot
 * be changed. They are read from the properties file at creation time, not
 * runtime to avoid the runtime I/O overhead.
 * 
 * <li>If the user does not provide a default file location, then this class
 * looks for a default configuraion filename on the System's classloader. The
 * default filename is represented by the value of the
 * <code>DEFAULT_CONFIG_FILENAME</code> constant below.
 * </ol>
 * 
 * Option 1 will always take precedence over option 2.
 */
public class SystemConfig implements SystemProperties {

    /**
     * Name of the system property that defines type server id being run on
     */
    public static final Parameter SERVER_ID = createParameter("serverId");

    /**
     * Name of the system property that defines type of database id being run on
     */
    public static final Parameter DATABASE_ID = createParameter("databaseId");

    /**
     * Name of the system property that defines type of database id being run on
     */
    public static final Parameter VERSION_NO = createParameter("versionNumber");

    /** log file related parameters * */

    /**
     * log4j file name/location
     */
    public static final Parameter LOGGER_FILE_NAME = createParameter("logger.filename");

    /**
     * log4j file format
     */
    public static final Parameter LOGGER_LAYOUT = createParameter("logger.layout");

    /**
     * log4j file pattern
     */
    public static final Parameter LOGGER_LAYOUT_PATTERN = createParameter("logger.layout.pattern");

    /**
     * log4j Date file pattern
     */
    public static final Parameter LOGGER_APPENDER_DATEPATTERN = createParameter("logger.appender.DatePattern");

    /** log file related parameters end * */

    /** email file related parameters * */

    public static final Parameter MAIL_PROTOCOL = createParameter("mail.transport.protocol");

    public static final Parameter MAIL_HOST = createParameter("mail.host");

    public static final Parameter MAIL_SMTP_HOST = createParameter("mail.smtp.host");

    /** email file related parameters end* */

    /**
     * Name of the system property jndi referance that defines the exact
     * location of the file that stores the system configuration information
     */
    public static final String CONFIG_FILE_JNDI = "jndi.system.properties";

    /**
     * Name of the system property that defines the exact location of the file
     * that stores the system configuration information
     */
    public static final String CONFIG_FILENAME_SYSTEM_PROPERTY = "systemConfigFile";

    /**
     * Name of the system property that defines the exact location of the file
     * that stores the local system configuration information (i.e. the
     * configuration parameters that need to have a differenet value on each
     * server)
     */
    public static final String LOCAL_CONFIG_FILENAME_SYSTEM_PROPERTY = "localSystemConfigFile";

    /**
     * Default configuration filename that this class searches the classpath for
     * to configure system properties
     */
    public static String CONFIG_FILE_NAME = "system.properties";

    /**
     * Default configuration filename that this class searches the classpath for
     * to configure system properties
     */
    public static String LOCAL_CONFIG_FILE_NAME = "system.properties.local";

    /**
     * Stores a reference to the system config properties file. The properties
     * are read once at class initialisation.
     */
    private ReferencedProperties systemConfigProperties = new ReferencedProperties();

    /**
     * Stores a reference to the singleton SystemConfig object
     */
    protected static SystemConfig instance = null;

    /**
     * Load the system configuration properties from the external properties
     * file. The location of the file is passed as the system property
     * 'systemConfigFile'. The method throws a runtime exception if the file is
     * missing - this indicates incorrect system setup.
     */
    protected void loadSystemProperties() {
        loadProperties(CONFIG_FILE_JNDI, CONFIG_FILENAME_SYSTEM_PROPERTY,
                CONFIG_FILE_NAME, true); // load standard system.properties
        loadProperties(null, LOCAL_CONFIG_FILENAME_SYSTEM_PROPERTY,
                LOCAL_CONFIG_FILE_NAME, false); // load system.properties.local

        systemConfigProperties.expandAllValues();
    }

    /**
     * constructor that overrides the default jndi value
     * @param jndiValue
     */
    protected SystemConfig(final String jndiValue) {
        loadProperties(jndiValue, CONFIG_FILENAME_SYSTEM_PROPERTY,CONFIG_FILE_NAME, true); // load standard system.properties
        
        systemConfigProperties.expandAllValues();
    }
    

    /**
     * Creates a singleton SystemConfig object and returns it to the caller
     * 
     * @return The singleton SystemConfig object
     */
    public static SystemConfig getInstance(final String jndiValue) {
        if (instance == null) {
            instance = new SystemConfig(jndiValue);
        }

        return instance;
    }
    
    /**
     * Protected constructor. Forces caller to construct the object using the
     * getInstance() method.
     */
    protected SystemConfig() {
        // Read properties file
        loadSystemProperties();
    }

    /**
     * Creates a singleton SystemConfig object and returns it to the caller
     * 
     * @return The singleton SystemConfig object
     */
    public static SystemConfig getInstance() {
        if (instance == null) {
            instance = new SystemConfig();
        }

        return instance;
    }

    /**
     * Access method to Paramater class in this package, access is done like
     * this so that SystemConfig can be subclasses in diffrent applications
     * 
     * @param param
     */
    protected static Parameter createParameter(String param) {
        return new Parameter(param);
    }

    /**
     * Access method to Paramater class in this package, access is done like
     * this so that SystemConfig can be subclasses in diffrent applications
     * 
     * @param param
     * @default default
     */
    protected static Parameter createParameter(String param, String defaultvalue) {
        return new Parameter(param, defaultvalue);
    }

    /**
     * Get the value of the system configuration property
     * 
     * @param param
     *            The type (i.e. name) of the system configuration property
     * @return String The String value of the property
     */
    public String getParameter(Parameter param) {

        String value = null;
        if (systemConfigProperties.getProperty(param.name) == null) {
            if (param.defaultValue == null) {
                if (param.defaultParameter == null) {
                    // throw new runtime exception
                    String errMsg = new StringBuffer("System property : ")
                            .append(param.name)
                            .append(
                                    " has not been found in the "
                                            + CONFIG_FILE_NAME + " file")
                            .append(
                                    " and has no default. Unable to continue without it")
                            .toString();
                    System.out.println(errMsg);

                    throw new SystemConfigException(errMsg);
                }
            } else {
                value = param.defaultValue;
            }
        } else {
            value = systemConfigProperties.getProperty(param.name).trim();
        }
        return value;
    }

    /**
     * Get the value of the system configuration property
     * 
     * @param String
     *            The type (i.e. name) of the system configuration property
     * @return String The String value of the property
     */
    public String getParameter(String paramName) {

        String value = null;

        if (systemConfigProperties.getProperty(paramName) == null) {
            // throw new runtime exception
            String errMsg = new StringBuffer("System property : ")
                    .append(paramName)
                    .append(
                            " has not been found in the " + CONFIG_FILE_NAME
                                    + " file")
                    .append(
                            " and has no default. Unable to continue without it")
                    .toString();
            System.out.println(errMsg);

            throw new SystemConfigException(errMsg);
        }
        value = systemConfigProperties.getProperty(paramName).trim();

        return value;
    }

    private void loadProperties(String jndi, String jvmFileNameProperty,
            String fileName, boolean faliure) {
        String systemConfigFile = null;
        Properties systemProps = null;
        URL configFileURL;

        try {
            if (jndi != null) {
                try {
                    // firstly attempt to read system.properties from a jndi
                    // referance
                    configFileURL = (URL) J2EEUtility.getJndiResource(jndi,
                            java.net.URL.class);
                    URLConnection connection = configFileURL.openConnection();

                    // Get the name of the system config file from the jndi ref.
                    systemConfigFile = configFileURL.getFile();
                } catch (NamingException e) {
                    System.out.println("JNDI naming error whilst looking for "
                            + jndi + " from resource: " + e);
                }

            } else {
                // Get the name of the system config file from the property ref.
                systemProps = System.getProperties();
            }

            if (systemConfigFile != null) {
                // Load up the system configuration properties from the props
                // file
                FileInputStream systemFile = new FileInputStream(
                        systemConfigFile);
                systemConfigProperties.loadUnexpanded(systemFile);
            } else {
                // The configuration file String was not found, check on
                // classpath through jvm file property

                systemConfigFile = System.getProperties().getProperty(
                        jvmFileNameProperty);

                if (systemConfigFile != null) {
                    configFileURL = ClassLoader
                            .getSystemResource(systemConfigFile);
                    // Convert between the URL object and the absolute file path
                    // that it represents
                    systemConfigFile = loadConfigFileProperties(
                            systemConfigFile, configFileURL);
                } else {
                    // The default file has not been found in JNDI or through
                    // JVM paramater
                    // check the claspath with the default file name

                    configFileURL = ClassLoader.getSystemResource(fileName);
                    if (configFileURL != null) {
                        systemConfigFile = loadConfigFileProperties(
                                systemConfigFile, configFileURL);
                    } else {
                        // Inform the user that the file has not been found.
                        System.err.print(systemConfigFile
                                + " resource has not been found.");
                        if (faliure == true) {
                            throw new SystemConfigException(
                                    systemConfigFile
                                            + " file was not found. Unable to continue without it");
                        }
                    }
                }
            }

        } catch (FileNotFoundException e) {
            System.out
                    .println("System properties file was not found. File searched for was called: "
                            + systemConfigFile + e);
            throw new SystemConfigException(
                    "System properties file was not found. Unable to continue without it");
        } catch (IOException e) {
            System.out.println("IO error whilst reading master " + fileName
                    + " from resource: " + e);
            throw new SystemConfigException(
                    "System properties file was not found. Unable to continue without it");
        }
    }

    /**
     * @param fileName
     * @param configFileURL
     * @return
     * @throws IOException
     * @throws FileNotFoundException
     */
    private String loadConfigFileProperties(String fileName, URL configFileURL)
            throws IOException, FileNotFoundException {
        String systemConfigFile = null;
        if (configFileURL != null) {
            systemConfigFile = configFileURL.getFile();
            // Inform the user that the default configuration file has
            // been found
            System.out.println("INFO - The " + fileName
                    + " configuration file has been found. Configuring: "
                    + systemConfigFile);
            systemConfigProperties.loadUnexpanded(new FileInputStream(
                    systemConfigFile));
        }
        return systemConfigFile;
    }

}