/*
 * DBManager.java
 *
 * Created on 08 October 2002, 11:07
 */

package uk.co.firstchoice.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * DBManager
 * 
 * Manages a database connection for use within the main application. The
 * manager first tries to connect to the internal connection (See Oracle 8i Java
 * Stored Procedures Developers Guide page 3-6). If that connection fails then
 * uses the hardcoded connection parameters, this is only intended for testing
 * purposes and should be replaced by command line parameters if this class is
 * reused.
 * 
 * @author rbryer
 */
public class DBManager {

	// connection defining variables
	private static String dbUrl;

	private static String dbDriver;

	private static String dbUser;

	private static String dbPass;

	// Connection items
	private Connection connection;

	/**
	 * Creates a new instance of DBManager with the specifed connection
	 * parametes
	 */
	public DBManager(String dbUrl, String dbDriver, String dbUser, String dbPass) {
	    DBManager.dbUrl = dbUrl;
	    DBManager.dbDriver = dbDriver;
	    DBManager.dbUser = dbUser;
	    DBManager.dbPass = dbPass;
	}

	/**
	 * Creates a DBManager with no connection parameters this is used only when
	 * the internal oracle connection is to be used.
	 */
	public DBManager() {
	}

	/**
	 * Connect to the database. As this class is intended for uses within an
	 * Oracle Java Stored Procedure it first assume that the class is running
	 * within the database JServer and tries to connect to the default internal
	 * session, this has the lowest overhead.
	 * 
	 * Failing this it connects using the database parameters set in the
	 * constructor,
	 * 
	 * @return the access mode of the resulting connection
	 */
	public AccessMode connect() {
		AccessMode accessMode;

		// first try and connect to internal connection
		System.out
				.println("Info    :Connecting to default internal connection ");
		try {
			connection = DriverManager.getConnection("jdbc:default:connection");
			System.out.println("Info    :Connected to internal conn");
			accessMode = AccessMode.JSERVER;
			return accessMode;
		} catch (SQLException sqlex) {
			System.out
					.println("Warning :Unable to connect to internal oracle connection.");
		}

		// internal connection failed try external connection
		System.out.println("Info    :Connecting to database " + dbUrl);
		// Load the database driver and connect to database
		try {
			Class.forName(dbDriver);
			connection = DriverManager.getConnection(dbUrl, dbUser, dbPass);
			System.out.println("Info    :Connected to " + dbUrl);
			accessMode = AccessMode.CLIENT;
		} catch (ClassNotFoundException cnfex) {
			System.out.println("Error   :Failed to load driver " + dbDriver);
			cnfex.printStackTrace();
			accessMode = AccessMode.FAILED;
		} catch (SQLException sqlex) {
			System.out.println("Error   :Unable to connect to " + dbUrl
					+ " as user " + dbUser + " with driver " + dbDriver);
			sqlex.printStackTrace();
			accessMode = AccessMode.FAILED;
		}

		return accessMode;
	}

	/**
	 * Shut down the DataManager
	 */
	public void shutDown() {
		try {
			connection.close();
		} catch (SQLException sqlex) {
			System.out.println("Error   :Unable to disconnect");
			sqlex.printStackTrace();
		}
	}

	/**
	 * Getter for property connection.
	 * 
	 * @return Value of property connection.
	 */
	public Connection getConnection() {
		return connection;
	}

	/**
	 * Setter for property connection.
	 * 
	 * @param connection
	 *            New value of property connection.
	 */
	public void setConnection(Connection connection) {
		this.connection = connection;
	}

}