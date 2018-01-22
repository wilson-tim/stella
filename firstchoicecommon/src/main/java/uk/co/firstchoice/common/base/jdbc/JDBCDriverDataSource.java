package uk.co.firstchoice.common.base.jdbc;

import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.SQLException;

import javax.sql.DataSource;

import uk.co.firstchoice.common.base.logging.LogManager;

public abstract class JDBCDriverDataSource implements DataSource {

	protected JDBCDriverDataSource() {
	}

	public JDBCDriverDataSource(String driverName, String connectString,
			String dbUserID, String password) {
		_jdbcDriverName = driverName;
		_jdbcConnectString = connectString;
		_jdbcDbUserID = dbUserID;
		_jdbcDbPassword = password;
	}

	public Connection getConnection() throws SQLException {
		Connection conn = null;
		try {
			conn = DatabaseUtility.getJDBCConnection(_jdbcDriverName,
					_jdbcConnectString, _jdbcDbUserID, _jdbcDbPassword);
		} catch (SQLException s) {
			throw s;
		} catch (Throwable t) {
			StringBuffer message = new StringBuffer(256);
			message
					.append("Connection creation failed.  See log for details.  driver=");
			if (_jdbcDriverName != null)
				message.append(_jdbcDriverName);
			message.append(";connectStr=");
			if (_jdbcConnectString != null)
				message.append(_jdbcConnectString);
			message.append(";user=");
			if (_jdbcDbUserID != null)
				message.append(_jdbcDbUserID);
			message.append(";pwd=");
			if (_jdbcDbPassword != null)
				message.append(_jdbcDbPassword);

			LogManager.getLogger()
					.logError(message.toString(), t);
			throw new SQLException(message.toString());
		}

		return conn;
	}

	public Connection getConnection(String username, String password)
			throws SQLException {
		Connection conn = null;
		try {
			conn = DatabaseUtility.getJDBCConnection(_jdbcDriverName,
					_jdbcConnectString, username, password);
		} catch (SQLException s) {
			throw s;
		} catch (Throwable t) {
			StringBuffer message = new StringBuffer(256);
			message
					.append("Connection creation failed.  See log for details.  driver=");
			if (_jdbcDriverName != null)
				message.append(_jdbcDriverName);
			message.append(";connectStr=");
			if (_jdbcConnectString != null)
				message.append(_jdbcConnectString);
			message.append(";user=");
			if (username != null)
				message.append(username);
			message.append(";pwd=");
			if (password != null)
				message.append(password);

			LogManager.getLogger()
					.logError(message.toString(), t);
			throw new SQLException(message.toString());
		}

		return conn;
	}

	public PrintWriter getLogWriter() throws SQLException {
		/** @todo: Implement this javax.sql.DataSource method */
		throw new java.lang.UnsupportedOperationException(
				"Method getLogWriter() not yet implemented.");
	}

	public void setLogWriter(PrintWriter out) throws SQLException {
		/** @todo: Implement this javax.sql.DataSource method */
		throw new java.lang.UnsupportedOperationException(
				"Method setLogWriter() not yet implemented.");
	}

	public void setLoginTimeout(int seconds) throws SQLException {
		/** @todo: Implement this javax.sql.DataSource method */
		throw new java.lang.UnsupportedOperationException(
				"Method setLoginTimeout() not yet implemented.");
	}

	public int getLoginTimeout() throws SQLException {
		/** @todo: Implement this javax.sql.DataSource method */
		throw new java.lang.UnsupportedOperationException(
				"Method getLoginTimeout() not yet implemented.");
	}

	private String _jdbcDriverName = null;

	private String _jdbcConnectString = null;

	private String _jdbcDbUserID = null;

	private String _jdbcDbPassword = null;
}