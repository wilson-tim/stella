package uk.co.firstchoice.common.base.jdbc;

import java.io.InputStream;
import java.io.Reader;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.NClob;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.RowId;
import java.sql.SQLException;
import java.sql.SQLType;
import java.sql.SQLXML;
import java.sql.Statement;
import java.sql.Timestamp;

import uk.co.firstchoice.common.base.logging.LogManager;

/**
 * A collection of database oriented utilities.
 *
 * <p>.
 */
public class DatabaseUtility {

	/**
	 * Constructor
	 */
	protected DatabaseUtility() {
	}

	/**
	 * Closes a prepared statement logging a warning if a SQLException is
	 * encountered.
	 *
	 * <p>
	 * Use of this version of close eliminates the need for verbose try/catch
	 * logic to handle the SQLException.
	 *
	 *
	 */
	public static void close(PreparedStatement pStmt) {
		if (pStmt == null)
			return;
		try {
			pStmt.close();
		} catch (SQLException e) {
			LogManager.getLogger().logWarning(
					"Prepared statement close error", e);
		}
	}

	/**
	 * Closes a statement logging a warning if a SQLException is encountered.
	 *
	 * <p>
	 * Use of this version of close eliminates the need for verbose try/catch
	 * logic to handle the SQLException.
	 *
	 *
	 */
	public static void close(Statement stmt) {
		if (stmt == null)
			return;
		try {
			stmt.close();
		} catch (SQLException e) {
			LogManager.getLogger().logWarning(
					"Statement close error", e);
		}
	}

	/**
	 * Closes a callable statement logging a warning if a SQLException is
	 * encountered.
	 *
	 * <p>
	 * Use of this version of close eliminates the need for verbose try/catch
	 * logic to handle the SQLException.
	 *
	 *
	 */
	public static void close(CallableStatement cStmt) {
		if (cStmt == null)
			return;
		try {
			cStmt.close();
		} catch (SQLException e) {
			LogManager.getLogger().logWarning(
					"Callable statement close error", e);
		}
	}

	/**
	 * Closes a result set logging a warning if a SQLException is encountered.
	 *
	 * <p>
	 * Use of this version of close eliminates the need for verbose try/catch
	 * logic to handle the SQLException.
	 *
	 *
	 */
	public static void close(ResultSet rs) {
		if (rs == null)
			return;
		try {
			rs.close();
		} catch (SQLException e) {
			LogManager.getLogger().logWarning(
					"ResultSet close error", e);
		}
	}

	/**
	 * Closes a database Connection logging a warning if a SQLException is
	 * encountered.
	 *
	 * <p>
	 * Use of this version of close eliminates the need for verbose try/catch
	 * logic to handle the SQLException.
	 *
	 *
	 */
	public static void close(Connection conn) {
		if (conn == null)
			return;
		try {
			if (!conn.isClosed())
				conn.close();
		} catch (SQLException e) {
			LogManager.getLogger().logWarning(
					"Connection close error", e);
		}
	}

	/**
	 * Closes a JDBC-related object logging a warning if a SQLException is
	 * encountered.
	 *
	 * <p>
	 * Use of this version of close eliminates the need for verbose try/catch
	 * logic to handle the SQLException.
	 *
	 * <p>
	 * This method will generate an IllegalArgumentException if the object
	 * passed isn't one of the following classes in java.sql: PreparedStatement,
	 * Statement, ResultSet, CallableStatement, or Connection.
	 *
	 *
	 */
	public static void close(Object dbObj) {
		if (dbObj == null)
			return;
		if (dbObj instanceof PreparedStatement)
			close((PreparedStatement) dbObj);
		else if (dbObj instanceof Statement)
			close((Statement) dbObj);
		else if (dbObj instanceof ResultSet)
			close((ResultSet) dbObj);
		else if (dbObj instanceof CallableStatement)
			close((CallableStatement) dbObj);
		else if (dbObj instanceof Connection)
			close((Connection) dbObj);
		else
			throw new IllegalArgumentException(
					"Close attempted on unrecognized Database Object!");
	}

	/**
	 * Closes JDBC-related objects in the order provided logging a warning if a
	 * SQLException is encountered.
	 *
	 * <p>
	 * Use of this version of close eliminates the need for verbose try/catch
	 * logic to handle the SQLException.
	 *
	 * <p>
	 * This method will generate an IllegalArgumentException if the object
	 * passed isn't one of the following classes in java.sql: PreparedStatement,
	 * Statement, ResultSet, CallableStatement, or Connection.
	 *
	 * <p>
	 * Objects will be closed in the order listed.
	 *
	 *
	 */
	public static void close(Object dbObj1, Object dbObj2) {
		close(dbObj1);
		close(dbObj2);
	}

	/**
	 * Closes JDBC-related objects in the order provided logging a warning if a
	 * SQLException is encountered.
	 *
	 * <p>
	 * Use of this version of close eliminates the need for verbose try/catch
	 * logic to handle the SQLException.
	 *
	 * <p>
	 * This method will generate an IllegalArgumentException if the object
	 * passed isn't one of the following classes in java.sql: PreparedStatement,
	 * Statement, ResultSet, CallableStatement, or Connection.
	 *
	 * <p>
	 * Objects will be closed in the order listed.
	 *
	 *
	 */
	public static void close(Object dbObj1, Object dbObj2, Object dbObj3) {
		close(dbObj1);
		close(dbObj2);
		close(dbObj3);
	}

	/**
	 * Closes JDBC-related objects in the order provided logging a warning if a
	 * SQLException is encountered.
	 *
	 * <p>
	 * Use of this version of close eliminates the need for verbose try/catch
	 * logic to handle the SQLException.
	 *
	 * <p>
	 * This method will generate an IllegalArgumentException if the object
	 * passed isn't one of the following classes in java.sql: PreparedStatement,
	 * Statement, ResultSet, CallableStatement, or Connection.
	 *
	 * <p>
	 * Objects will be closed in the order listed.
	 *
	 *
	 */
	public static void close(Object dbObj1, Object dbObj2, Object dbObj3,
			Object dbObj4) {
		close(dbObj1);
		close(dbObj2);
		close(dbObj3);
		close(dbObj4);
	}

	/**
	 * Closes JDBC-related objects in the order provided logging a warning if a
	 * SQLException is encountered.
	 *
	 * <p>
	 * Use of this version of close eliminates the need for verbose try/catch
	 * logic to handle the SQLException.
	 *
	 * <p>
	 * This method will generate an IllegalArgumentException if the object
	 * passed isn't one of the following classes in java.sql: PreparedStatement,
	 * Statement, ResultSet, CallableStatement, or Connection.
	 *
	 * <p>
	 * Objects will be closed in the order listed.
	 *
	 *
	 */
	public static void close(Object dbObj1, Object dbObj2, Object dbObj3,
			Object dbObj4, Object dbObj5) {
		close(dbObj1);
		close(dbObj2);
		close(dbObj3);
		close(dbObj4);
		close(dbObj5);
	}

	/**
	 * Converts a java.sql.Timestamp to a java.util.Date.
	 *
	 * <p>
	 * Null arguments generate an IllegalArgumentException.
	 *
	 * @param Timestamp
	 * @return Date
	 *
	 */
	public static java.util.Date convertTimestampToUtilDate(Timestamp ts) {
		if (ts == null)
			throw new IllegalArgumentException("Null timestamp not allowed.");
		return new java.util.Date(ts.getTime());
	}

	/**
	 * Converts a java.util.Date to a java.sql.Timestamp.
	 *
	 * <p>
	 * Null arguments generate an IllegalArgumentException.
	 *
	 * @param Date
	 * @return Timestamp
	 *
	 */
	public static Timestamp convertUtilDateToTimestamp(java.util.Date date) {
		if (date == null)
			throw new IllegalArgumentException("Null timestamp not allowed.");
		return new Timestamp(date.getTime());
	}

	/**
	 * Provides a java.sql.Timestamp set to the current ssytem time.
	 *
	 * @return Timestamp Current
	 *
	 */
	public static Timestamp getCurrentTimestamp() {
		return convertUtilDateToTimestamp(new java.util.Date());
	}

	/**
	 * This returns an Oracle JDBC Connection based on the connection string,
	 * user id and password given. Requires the Oracle JDBC driver
	 */
	public static Connection getOracleJDBCConnection(String serverName,
			int port, String dbInstance, String dbUserID, String password)
			throws ClassNotFoundException, IllegalAccessException,
			InstantiationException, SQLException {
		if (serverName == null)
			throw new IllegalArgumentException("Null serverName not allowed.");
		if (dbInstance == null)
			throw new IllegalArgumentException("Null dbInstance not allowed.");
		if (dbUserID == null)
			throw new IllegalArgumentException("Null dbUserID not allowed.");
		if (password == null)
			throw new IllegalArgumentException("Null password not allowed.");

		StringBuffer connectString = new StringBuffer();

		connectString.append("jdbc:oracle:thin:@");
		connectString.append(serverName);
		connectString.append(":");
		connectString.append(port);
		connectString.append(":");
		connectString.append(dbInstance);
		return getJDBCConnection(ORACLE_TYPE4_DRIVER, connectString.toString(),
				dbUserID, password);
	}

	/**
	 * Provides a JDBC connection given a DriverManager class name, connect
	 * string, userid, and password.
	 *
	 *
	 */
	public static Connection getJDBCConnection(String driverName,
			String connectString, String dbUserID, String password)
			throws ClassNotFoundException, IllegalAccessException,
			InstantiationException, SQLException {
		if (driverName == null)
			throw new IllegalArgumentException("Null driverName not allowed.");
		if (connectString == null)
			throw new IllegalArgumentException(
					"Null connectString not allowed.");

		Class.forName(driverName).newInstance();
		Connection conn = null;

		if (dbUserID == null)
			conn = DriverManager.getConnection(connectString);
		else
			conn = DriverManager.getConnection(connectString, dbUserID,
					password);

		return conn;
	}

	/**
	 * Generic utility that returns a database connection using a given driver.
	 *
	 * @param driverName
	 * @param connectString
	 * @return Connection
	 * @throws ClassNotFoundException
	 * @throws IllegalAccessException
	 * @throws InstantiationException
	 * @throws SQLException
	 */
	public static Connection getJDBCConnection(String driverName,
			String connectString) throws ClassNotFoundException,
			IllegalAccessException, InstantiationException, SQLException {
		return DatabaseUtility.getJDBCConnection(driverName, connectString,
				null, null);
	}

	/**
	 * Rolls back a dataabse transaction logging a warning if a SQLException is
	 * encountered.
	 *
	 * <p>
	 * Use of this version of rollback eliminates the need for verbose try/catch
	 * logic to handle the SQLException.
	 *
	 *
	 */
	public static void rollback(Connection dbConnection) {
		if (dbConnection == null)
			return;
		try {
			dbConnection.rollback();
		} catch (SQLException e) {
			LogManager.getLogger().logWarning(
					"Rollback close error", e);
		}
	}

	/**
	 * Converts a ResultSet into a serializable version that can be returned to
	 * remote clients.
	 *
	 * @throws SQLException
	 *
	 */
	public static ResultSet getSerializableResultSet(ResultSet inputResultSet)
			throws SQLException {
		return new SerializableResultSet(inputResultSet) {
                    public RowId getRowId(int columnIndex) throws SQLException {
                        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
                    }

                    public RowId getRowId(String columnLabel) throws SQLException {
                        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
                    }

                    public void updateRowId(int columnIndex, RowId x) throws SQLException {
                        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
                    }

                    public void updateRowId(String columnLabel, RowId x) throws SQLException {
                        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
                    }

                    public int getHoldability() throws SQLException {
                        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
                    }

                    public boolean isClosed() throws SQLException {
                        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
                    }

                    public void updateNString(int columnIndex, String nString) throws SQLException {
                        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
                    }

                    public void updateNString(String columnLabel, String nString) throws SQLException {
                        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
                    }

                    public void updateNClob(int columnIndex, NClob nClob) throws SQLException {
                        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
                    }

                    public void updateNClob(String columnLabel, NClob nClob) throws SQLException {
                        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
                    }

                    public NClob getNClob(int columnIndex) throws SQLException {
                        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
                    }

                    public NClob getNClob(String columnLabel) throws SQLException {
                        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
                    }

                    public SQLXML getSQLXML(int columnIndex) throws SQLException {
                        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
                    }

                    public SQLXML getSQLXML(String columnLabel) throws SQLException {
                        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
                    }

                    public void updateSQLXML(int columnIndex, SQLXML xmlObject) throws SQLException {
                        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
                    }

                    public void updateSQLXML(String columnLabel, SQLXML xmlObject) throws SQLException {
                        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
                    }

                    public String getNString(int columnIndex) throws SQLException {
                        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
                    }

                    public String getNString(String columnLabel) throws SQLException {
                        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
                    }

                    public Reader getNCharacterStream(int columnIndex) throws SQLException {
                        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
                    }

                    public Reader getNCharacterStream(String columnLabel) throws SQLException {
                        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
                    }

                    public void updateNCharacterStream(int columnIndex, Reader x, long length) throws SQLException {
                        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
                    }

                    public void updateNCharacterStream(String columnLabel, Reader reader, long length) throws SQLException {
                        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
                    }

                    public void updateAsciiStream(int columnIndex, InputStream x, long length) throws SQLException {
                        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
                    }

                    public void updateBinaryStream(int columnIndex, InputStream x, long length) throws SQLException {
                        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
                    }

                    public void updateCharacterStream(int columnIndex, Reader x, long length) throws SQLException {
                        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
                    }

                    public void updateAsciiStream(String columnLabel, InputStream x, long length) throws SQLException {
                        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
                    }

                    public void updateBinaryStream(String columnLabel, InputStream x, long length) throws SQLException {
                        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
                    }

                    public void updateCharacterStream(String columnLabel, Reader reader, long length) throws SQLException {
                        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
                    }

                    public void updateBlob(int columnIndex, InputStream inputStream, long length) throws SQLException {
                        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
                    }

                    public void updateBlob(String columnLabel, InputStream inputStream, long length) throws SQLException {
                        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
                    }

                    public void updateClob(int columnIndex, Reader reader, long length) throws SQLException {
                        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
                    }

                    public void updateClob(String columnLabel, Reader reader, long length) throws SQLException {
                        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
                    }

                    public void updateNClob(int columnIndex, Reader reader, long length) throws SQLException {
                        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
                    }

                    public void updateNClob(String columnLabel, Reader reader, long length) throws SQLException {
                        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
                    }

                    public void updateNCharacterStream(int columnIndex, Reader x) throws SQLException {
                        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
                    }

                    public void updateNCharacterStream(String columnLabel, Reader reader) throws SQLException {
                        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
                    }

                    public void updateAsciiStream(int columnIndex, InputStream x) throws SQLException {
                        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
                    }

                    public void updateBinaryStream(int columnIndex, InputStream x) throws SQLException {
                        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
                    }

                    public void updateCharacterStream(int columnIndex, Reader x) throws SQLException {
                        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
                    }

                    public void updateAsciiStream(String columnLabel, InputStream x) throws SQLException {
                        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
                    }

                    public void updateBinaryStream(String columnLabel, InputStream x) throws SQLException {
                        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
                    }

                    public void updateCharacterStream(String columnLabel, Reader reader) throws SQLException {
                        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
                    }

                    public void updateBlob(int columnIndex, InputStream inputStream) throws SQLException {
                        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
                    }

                    public void updateBlob(String columnLabel, InputStream inputStream) throws SQLException {
                        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
                    }

                    public void updateClob(int columnIndex, Reader reader) throws SQLException {
                        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
                    }

                    public void updateClob(String columnLabel, Reader reader) throws SQLException {
                        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
                    }

                    public void updateNClob(int columnIndex, Reader reader) throws SQLException {
                        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
                    }

                    public void updateNClob(String columnLabel, Reader reader) throws SQLException {
                        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
                    }

                    public <T> T getObject(int columnIndex, Class<T> type) throws SQLException {
                        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
                    }

                    public <T> T getObject(String columnLabel, Class<T> type) throws SQLException {
                        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
                    }

                    public void updateObject(int columnIndex, Object x, SQLType targetSqlType, int scaleOrLength) throws SQLException {
                        super.updateObject(columnIndex, x, targetSqlType, scaleOrLength); //To change body of generated methods, choose Tools | Templates.
                    }

                    public void updateObject(String columnLabel, Object x, SQLType targetSqlType, int scaleOrLength) throws SQLException {
                        super.updateObject(columnLabel, x, targetSqlType, scaleOrLength); //To change body of generated methods, choose Tools | Templates.
                    }

                    public void updateObject(int columnIndex, Object x, SQLType targetSqlType) throws SQLException {
                        super.updateObject(columnIndex, x, targetSqlType); //To change body of generated methods, choose Tools | Templates.
                    }

                    public void updateObject(String columnLabel, Object x, SQLType targetSqlType) throws SQLException {
                        super.updateObject(columnLabel, x, targetSqlType); //To change body of generated methods, choose Tools | Templates.
                    }

                    public <T> T unwrap(Class<T> iface) throws SQLException {
                        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
                    }

                    public boolean isWrapperFor(Class<?> iface) throws SQLException {
                        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
                    }
                };
	}

	public static final String ORACLE_TYPE4_DRIVER = "oracle.jdbc.driver.OracleDriver";

	public static final String SYBASE_JDBC_DRIVER_NAME = "com.sybase.jdbc2.jdbc.SybDriver";
}

