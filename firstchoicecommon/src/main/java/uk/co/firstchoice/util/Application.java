/*
 * Application.java
 * 
 * Created on 19 July 2002, 12:46
 */

package uk.co.firstchoice.util;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;

/**
 * Application class that provides access to properties and facilities. This
 * class provides basic services to the an application. It provides
 * 
 * 1. Logging facilities 2. Access mode 3. Properties registery
 * 
 * @author Russell Bryer
 */
public class Application {

	// Define the app name and description
	private String applicationName;

	private String applicationDesc;

	// Variables used to retrieve properties from the database
	private String applicationKey; // The app identifer

	private String applicationMode; // The app mode

	private String userKey;

	private Connection registeryConnection;

	private PreparedStatement lookupStmt;

	private String runID = "XXX";

	// Define a application logger. This is public and accessed via
	// Application.log
	public Logger log;

	// set the default mode
	private AccessMode accessMode = AccessMode.CLIENT;

	/**
	 * Constructor Creates a new application object with the givern name
	 */
	public Application(String name, String desc) {
		this.applicationName = name;
		this.applicationDesc = desc;
	}

	/*
	 * setLogger() Set up the logger
	 */
	public void setLogger(String name) {
		log = Logger.getLogger(name);
	}

	public void setLoggerLevel(String level) {
		// get integer representation of this string
		Level setLevel;
		setLevel = Level.parse(level);
		log.setLevel(setLevel.intValue());
	}

	public void setLogSysOut(boolean required) {
		// is sys out required by logger as well as to file?
		log.setLogSysOut(required);
	}

	/*
	 * Application registry settings this method overloads the one below that
	 * actually does the work this method can be used for internal Oracle use as
	 * does not need Connection parameter
	 */

	/*
	 * Application registry settings
	 */

	public boolean setRegistry(String applicationKey, String applicationMode,
			String userKey, Connection registeryConnection) throws SQLException {
		// set the class varaibles to the passed variables
		// after doing some rudimentary checking

		this.applicationKey = applicationKey;
		this.applicationMode = applicationMode;

		if (userKey.compareTo("") == 0) {
			this.userKey = "ALL";
		} else {
			this.userKey = userKey;
		}

		// define the lookup statement
		lookupStmt = registeryConnection
				.prepareStatement("SELECT parameter_value "
						+ "FROM application_registry "
						+ "WHERE trim(application_key)  = trim(?) "
						+ "AND   trim(application_mode) = trim(?) "
						+ "AND   trim(user_key)         = trim(?) "
						+ "AND   trim(parameter_key)    = trim(?) ");

		// lookup basic name to determine if this is a valid
		// registery set up
		try {

			getRegisteryProperty("Name");
		} catch (PropertyNotFoundException ex) {
			return false;
		}
		return true;
	}

	/**
	 * getRegisteryProperty() looks up the passed parameterKey in the registry.
	 * The registry must have been defined in a previous call to setRegistry().
	 * If the registry is not set or no data exists for the passed key then a
	 * PropertyNotFoundException if thrown
	 *  
	 */
	public String getRegisteryProperty(String parameterKey)
			throws PropertyNotFoundException {
		ResultSet rs;
		String value = "";

		try {
			// first look for specific mode and user combination

			lookupStmt.clearParameters();
			lookupStmt.setString(1, applicationKey);
			lookupStmt.setString(2, applicationMode);
			lookupStmt.setString(3, userKey);
			lookupStmt.setString(4, parameterKey);

			rs = lookupStmt.executeQuery();

			if (rs.next()) {
				//System.out.println("has next");
				value = rs.getString("parameter_value");
			}

			// if no specifc then look for general user but specifc mode
			if (value.compareTo("") == 0) {
				lookupStmt.setString(3, "ALL");
				rs = lookupStmt.executeQuery();
				if (rs.next()) {
					value = rs.getString("parameter_value");
				}
			}

			// if no general user info then look for general user and mode
			if (value.compareTo("") == 0) {
				lookupStmt.setString(2, "ALL");
				rs = lookupStmt.executeQuery();
				if (rs.next()) {
					value = rs.getString("parameter_value");
				}
			}

		} catch (SQLException ex) {
			ex.printStackTrace();
			throw new PropertyNotFoundException(
					"SQL Error while retrieving property: " + applicationKey
							+ " " + applicationMode + " " + userKey + " "
							+ parameterKey + " SQLErr " + ex.getErrorCode()
							+ " SQLMsg " + ex.getMessage());
		}

		if (value.compareTo("") == 0) {
			throw new PropertyNotFoundException(
					"Parameter not defined or has no value: " + applicationKey
							+ " " + applicationMode + " " + userKey + " "
							+ parameterKey);
		} else {
			return value;
		}
	}

	/**
	 * Getter for property accessMode.
	 * 
	 * @return Value of property accessMode.
	 */
	public AccessMode getAccessMode() {
		return accessMode;
	}

	/**
	 * Setter for property accessMode.
	 * 
	 * @param accessMode
	 *            New value of property accessMode.
	 */
	public void setAccessMode(AccessMode accessMode) {
		this.accessMode = accessMode;
	}

	private void createRunID(Connection conn) {

		this.runID = "XXXX";

		try {
			// Get the run ID using SQL stored procedure
			CallableStatement idstmt = conn
					.prepareCall("{?=call jutil.app_util.create_run_id()}");
			idstmt.registerOutParameter(1, Types.CHAR);
			idstmt.execute();
			this.runID = idstmt.getString(1);
			idstmt.close();
		} catch (SQLException sqlex) {
			sqlex.printStackTrace();
		}
	}

	public int logStartTime(Connection conn, String progName) {
		if (this.runID.equals("XXX")) {
			createRunID(conn);
		}

		try {
			// Log the start time
			CallableStatement newcstmt = conn
					.prepareCall("{call jutil.app_util.store_start_time(?,?)}");
			newcstmt.setString(1, progName);
			newcstmt.setString(2, this.runID);
			newcstmt.execute();

			newcstmt.close();
		} catch (SQLException sqlex) {
			sqlex.printStackTrace();
			return 1;
		}
		return 0;
	}

	public int logEndTime(Connection conn, String progName) {
		if (this.runID.equals("XXX")) {
			createRunID(conn);
		}

		try {
			// Log the end time

			CallableStatement endcstmt = conn
					.prepareCall("{call jutil.app_util.store_end_time(?,?)}");
			endcstmt.setString(1, progName);
			endcstmt.setString(2, this.runID);
			endcstmt.execute();

			endcstmt.close();
		} catch (SQLException sqlex) {
			sqlex.printStackTrace();
			return 1;
		}

		return 0;
	}
}

