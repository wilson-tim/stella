package uk.co.firstchoice.common.base.jdbc;

public class JDBCConnectionSpecification {
	public JDBCConnectionSpecification() {
	}

	public JDBCConnectionSpecification(String driverName, String connectString,
			String dbUserID, String password) {
		this.setDriverName(driverName);
		this.setConnectString(connectString);
		this.setUserId(dbUserID);
		this.setPassword(password);
	}

	public String getDriverName() {
		return _driverName;
	}

	public void setDriverName(String name) {
		if (name == null)
			throw new IllegalArgumentException("Null driverName not allowed.");
		if (name.equals(""))
			throw new IllegalArgumentException("Blank driverName not allowed.");
		_driverName = name;
	}

	public String getConnectString() {
		return _connectString;
	}

	public void setConnectString(String value) {
		if (value == null)
			throw new IllegalArgumentException(
					"Null connectString not allowed.");
		if (value.equals(""))
			throw new IllegalArgumentException(
					"Blank connectString not allowed.");
		_connectString = value;
	}

	public String getUserId() {
		return _dbUserId;
	}

	public void setUserId(String id) {
		_dbUserId = id;
	}

	public String getPassword() {
		return _password;
	}

	public void setPassword(String password) {
		_password = password;
	}

	private String _driverName = null;

	private String _connectString = null;

	private String _dbUserId = null;

	private String _password = null;
}