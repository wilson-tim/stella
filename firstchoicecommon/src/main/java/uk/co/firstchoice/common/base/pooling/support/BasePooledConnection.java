package uk.co.firstchoice.common.base.pooling.support;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.SQLWarning;
import java.sql.Statement;
import java.util.Map;
import java.util.Properties;

import uk.co.firstchoice.common.base.pooling.ResourceException;

public abstract class BasePooledConnection {
	public BasePooledConnection() {
	}

	public BasePooledConnection(Properties props) throws ResourceException {
		this.initialize(props);
	}

	public static final String TEST_STATEMENT_PROPERTY = "conn.test.sql";

	public static final String DRIVER_PROPERTY = "conn.driver";

	public static final String CONNECT_PROPERTY = "conn.connect";

	public static final String USER_PROPERTY = "conn.user";

	public static final String PASSWORD_PROPERTY = "conn.password";

	protected Connection _internalConnection = null;

	private String _testSqlStatement = null;

	/**
	 * Creates the privately held JDBC connection.
	 */
	public void initialize(Properties props) throws ResourceException {
		if (props == null)
			throw new ResourceException("Null properties not allowed.");

		String driverName = props.getProperty(DRIVER_PROPERTY);
		String connectString = props.getProperty(CONNECT_PROPERTY);
		String user = props.getProperty(USER_PROPERTY);
		String password = props.getProperty(PASSWORD_PROPERTY);
		_testSqlStatement = props.getProperty(TEST_STATEMENT_PROPERTY);

		if (driverName == null)
			throw new ResourceException("Null driver not allowed.");
		if (connectString == null)
			throw new ResourceException("Null connect string not allowed.");
		if (user == null)
			throw new ResourceException("Null user not allowed.");
		if (password == null)
			throw new ResourceException("Null password not allowed.");
		if (_testSqlStatement == null)
			throw new ResourceException("Null test statement not allowed.");

		try {
			Class.forName(driverName);
			_internalConnection = DriverManager.getConnection(connectString,
					user, password);
		} catch (Throwable t) {
			throw new ResourceException(t.getMessage());
		}

	}

	/**
	 * Terminates the privately held JDBC Connection.
	 */
	public void terminate() throws ResourceException {
		try {
			_internalConnection.close();
		} catch (Throwable t) {
			throw new ResourceException(t.getMessage());
		}
	}

	/**
	 * Tests the privately held JDBC connection to see if it's still usable.
	 */
	public boolean isValid() throws ResourceException {
		boolean answer = true;
		PreparedStatement pStmt = null;

		try {
			pStmt = _internalConnection.prepareStatement(_testSqlStatement);
			pStmt.close();
		} catch (Throwable t) {
			answer = false;
		}

		return answer;
	}

	// --> Forward all connection methods to internal connection.
	public Statement createStatement() throws SQLException {
		return _internalConnection.createStatement();
	}

	public PreparedStatement prepareStatement(String sql) throws SQLException {
		return _internalConnection.prepareStatement(sql);
	}

	public CallableStatement prepareCall(String sql) throws SQLException {
		return _internalConnection.prepareCall(sql);
	}

	public String nativeSQL(String sql) throws SQLException {
		return _internalConnection.nativeSQL(sql);
	}

	public void setAutoCommit(boolean autoCommit) throws SQLException {
		_internalConnection.setAutoCommit(autoCommit);
	}

	public boolean getAutoCommit() throws SQLException {
		return _internalConnection.getAutoCommit();
	}

	public void commit() throws SQLException {
		_internalConnection.commit();
	}

	public void rollback() throws SQLException {
		_internalConnection.rollback();
	}

	public void close() throws SQLException {
		_internalConnection.close();
	}

	public boolean isClosed() throws SQLException {
		return _internalConnection.isClosed();
	}

	public DatabaseMetaData getMetaData() throws SQLException {
		return _internalConnection.getMetaData();
	}

	public void setReadOnly(boolean readOnly) throws SQLException {
		_internalConnection.setReadOnly(readOnly);
	}

	public boolean isReadOnly() throws SQLException {
		return _internalConnection.isReadOnly();
	}

	public void setCatalog(String catalog) throws SQLException {
		_internalConnection.setCatalog(catalog);
	}

	public String getCatalog() throws SQLException {
		return _internalConnection.getCatalog();
	}

	public void setTransactionIsolation(int level) throws SQLException {
		_internalConnection.setTransactionIsolation(level);
	}

	public int getTransactionIsolation() throws SQLException {
		return _internalConnection.getTransactionIsolation();
	}

	public SQLWarning getWarnings() throws SQLException {
		return _internalConnection.getWarnings();
	}

	public void clearWarnings() throws SQLException {
		_internalConnection.clearWarnings();
	}

	public Statement createStatement(int resultSetType, int resultSetConcurrency)
			throws SQLException {
		return _internalConnection.createStatement(resultSetType,
				resultSetConcurrency);
	}

	public PreparedStatement prepareStatement(String sql, int resultSetType,
			int resultSetConcurrency) throws SQLException {
		return _internalConnection.prepareStatement(sql, resultSetType,
				resultSetConcurrency);
	}

	public CallableStatement prepareCall(String sql, int resultSetType,
			int resultSetConcurrency) throws SQLException {
		return _internalConnection.prepareCall(sql, resultSetType,
				resultSetConcurrency);
	}

	public Map getTypeMap() throws SQLException {
		return _internalConnection.getTypeMap();
	}

	public void setTypeMap(Map map) throws SQLException {
		_internalConnection.setTypeMap(map);
	}
}