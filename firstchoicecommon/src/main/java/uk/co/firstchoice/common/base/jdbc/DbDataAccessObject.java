package uk.co.firstchoice.common.base.jdbc;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * Generic functionality for data access objects for relational databases. Data
 * access objects (DAO) are objects which contain database access. Typically,
 * database connections are managed at a deployment level and passed in to make
 * DAOs usable in any type of deployment (e.g. Enterprise beans, RMI services,
 * Web services, Applications, etc.).
 * 
 *  
 */
public abstract class DbDataAccessObject {

	protected DbDataAccessObject() {
	}

	protected DbDataAccessObject(Connection conn) throws SQLException {
		this.setDbConnection(conn);
	}

	protected Connection getDbConnection() {
		return _dbConnection;
	}

	protected void setDbConnection(Connection conn) throws SQLException {
		if (conn == null)
			throw new IllegalArgumentException(
					"Null database connection not allowed.");
		if (conn.isClosed())
			throw new IllegalArgumentException(
					"Closed database connection not allowed.");
		_dbConnection = conn;
		_sqlAssistant = new SQLAssistant(_dbConnection);
	}

	protected Connection _dbConnection = null;

	protected SQLAssistant _sqlAssistant = null;
}