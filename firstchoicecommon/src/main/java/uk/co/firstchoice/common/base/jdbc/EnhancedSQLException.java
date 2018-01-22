package uk.co.firstchoice.common.base.jdbc;

import java.io.PrintStream;
import java.io.PrintWriter;
import java.sql.SQLException;

import uk.co.firstchoice.common.base.util.CommonCharacter;

/**
 * SQL Exception containing additional information about the statement that
 * produced the error.
 * 
 *  
 */
public class EnhancedSQLException extends SQLException {

	private EnhancedSQLException() {
		super();
	}

	public EnhancedSQLException(SQLException s, String sqlText) {
		super(s.getMessage(), s.getSQLState(), 0);
		this.setSqlStatementText(sqlText);
		_rootSqlException = s;
	}

	public EnhancedSQLException(SQLException s, String sqlText,
			Object[] hostVariableSettings) {
		this(s, sqlText);
		this.setHostVariableSettings(hostVariableSettings);
	}

	/**
	 * Provides text of the SQL statement used in the operation that originated
	 * the SQLException.
	 * 
	 * @return text SQL text.
	 *  
	 */
	public String getSqlStatementText() {
		return _sqlStatementText;
	}

	/**
	 * Sets text of the SQL statement used in the operation that originated the
	 * SQLException.
	 * 
	 * @param text
	 *            SQL text.
	 *  
	 */
	public void setSqlStatementText(String sqlText) {
		_sqlStatementText = sqlText;
	}

	/**
	 * Provides values of host variables used in the operation that originated
	 * the SQLException.
	 * 
	 * @return values SQL host variable values.
	 *  
	 */
	public Object[] getHostVariableSettings() {
		return _hostVariableSettings;
	}

	/**
	 * Sets values of host variables used in the operation that originated the
	 * SQLException.
	 * 
	 * @param values
	 *            SQL host variable values.
	 *  
	 */
	public void setHostVariableSettings(Object[] parms) {
		_hostVariableSettings = parms;
	}

	/**
	 * Override for Throwable.printStacktrace().
	 *  
	 */
	public void printStackTrace(PrintWriter s) {
		s.print("Message: ");
		if (_rootSqlException != null && _rootSqlException.getMessage() != null)
			s.print(_rootSqlException.getMessage());
		else
			s.print("null");
		s.print(CommonCharacter.LINE_FEED);

		s.print("SQL Error Code: ");
		if (_rootSqlException != null)
			s.print(_rootSqlException.getErrorCode());
		s.print(CommonCharacter.LINE_FEED);

		s.print("SQL State: ");
		if (_rootSqlException != null
				&& _rootSqlException.getSQLState() != null)
			s.print(_rootSqlException.getSQLState());
		s.print(CommonCharacter.LINE_FEED);

		if (_sqlStatementText != null) {
			s.print(CommonCharacter.LINE_FEED);
			s.print("SQL Statement Text:");
			s.print(CommonCharacter.TAB);
			s.print(_sqlStatementText);
			s.print(CommonCharacter.LINE_FEED);

			if (_hostVariableSettings != null
					&& _hostVariableSettings.length > 0) {
				s.print("Host variable settings (type==value):");
				for (int i = 0; i < _hostVariableSettings.length; i++) {
					s.print(CommonCharacter.LINE_FEED);
					s.print(CommonCharacter.TAB);
					if (_hostVariableSettings[i] != null)
						s.print(_hostVariableSettings[i].getClass().getName());
					else
						s.print("null");

					s.print(" == ");
					if (_hostVariableSettings[i] != null)
						s.print(_hostVariableSettings[i]);
					else
						s.print("null");
				}
			}
		}

		if (_rootSqlException != null)
			_rootSqlException.printStackTrace(s);
	}

	/**
	 * Override for Throwable.printStacktrace().
	 *  
	 */
	public void printStackTrace() {
		this.printStackTrace(System.err);
	}

	/**
	 * Override for Throwable.printStacktrace().
	 *  
	 */
	public void printStackTrace(PrintStream s) {
		s.print("Message: ");
		if (_rootSqlException != null && _rootSqlException.getMessage() != null)
			s.print(_rootSqlException.getMessage());
		else
			s.print("null");
		s.print(CommonCharacter.LINE_FEED);

		s.print("SQL Error Code: ");
		if (_rootSqlException != null)
			s.print(_rootSqlException.getErrorCode());
		s.print(CommonCharacter.LINE_FEED);

		s.print("SQL State: ");
		if (_rootSqlException != null
				&& _rootSqlException.getSQLState() != null)
			s.print(_rootSqlException.getSQLState());
		s.print(CommonCharacter.LINE_FEED);

		if (_sqlStatementText != null) {
			s.print(CommonCharacter.LINE_FEED);
			s.print("SQL Statement Text:");
			s.print(CommonCharacter.TAB);
			s.print(_sqlStatementText);
			s.print(CommonCharacter.LINE_FEED);

			if (_hostVariableSettings != null
					&& _hostVariableSettings.length > 0) {
				s.print("Host variable settings (type==value):");
				for (int i = 0; i < _hostVariableSettings.length; i++) {
					s.print(CommonCharacter.LINE_FEED);
					s.print(CommonCharacter.TAB);
					if (_hostVariableSettings[i] != null)
						s.print(_hostVariableSettings[i].getClass().getName());
					else
						s.print("null");

					s.print(" == ");
					if (_hostVariableSettings[i] != null)
						s.print(_hostVariableSettings[i]);
					else
						s.print("null");
				}
			}
		}

		if (_rootSqlException != null)
			_rootSqlException.printStackTrace(s);
	}

	private String _sqlStatementText = null;

	private Object[] _hostVariableSettings = null;

	private SQLException _rootSqlException = null;
}