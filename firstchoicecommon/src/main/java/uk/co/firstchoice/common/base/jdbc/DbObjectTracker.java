package uk.co.firstchoice.common.base.jdbc;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Stack;

public abstract class DbObjectTracker {

	protected DbObjectTracker() {
	}

	protected void trackDbObject(Object obj) {
		if (obj == null)
			throw new IllegalArgumentException("Null object not allowed.");

		if (obj instanceof Statement)
			_createdObjectStack.push(obj);
		else if (obj instanceof ResultSet)
			_createdObjectStack.push(obj);
		else if (obj instanceof Connection)
			_createdObjectStack.push(obj);
	}

	public abstract void setDelegateObject(Object jdbcObject);

	protected void closeAll() {
		Statement stmt = null;
		ResultSet rSet = null;
		Connection conn = null;
		Object obj = null;

		while (_createdObjectStack.size() > 0) {
			obj = _createdObjectStack.pop();
			if (obj instanceof Statement) {
				stmt = (Statement) obj;
				DatabaseUtility.close(stmt);
			} else if (obj instanceof ResultSet) {
				rSet = (ResultSet) obj;
				DatabaseUtility.close(rSet);
			} else if (obj instanceof Connection) {
				conn = (Connection) obj;
				DatabaseUtility.close(conn);
			}

		}
	}

	protected String getSqlStatementText() {
		return _sqlStatementText;
	}

	protected void setSqlStatementText(String sqlText) {
		_sqlStatementText = sqlText;
	}

	protected Object[] getHostVariableSettings() {
		return _hostVariableSettings;
	}

	protected void setHostVariableSettings(Object[] parms) {
		_hostVariableSettings = parms;
	}

	protected void addHostVariableSetting(Object parm) {
		Object[] newArray = null;
		if (_hostVariableSettings == null)
			newArray = new Object[1];
		else {
			newArray = new Object[_hostVariableSettings.length + 1];
			System.arraycopy(_hostVariableSettings, 0, newArray, 0,
					_hostVariableSettings.length);
		}

		newArray[newArray.length - 1] = parm;
		_hostVariableSettings = newArray;
	}

	protected EnhancedSQLException generateEnhancedSQLException(SQLException s) {
		EnhancedSQLException sqlExc = null;

		if (s instanceof EnhancedSQLException)
			sqlExc = (EnhancedSQLException) s;
		else {
			sqlExc = new EnhancedSQLException(s, _sqlStatementText,
					_hostVariableSettings);
		}

		return sqlExc;
	}

	private Stack _createdObjectStack = new Stack();

	private String _sqlStatementText = null;

	private Object[] _hostVariableSettings = null;
}