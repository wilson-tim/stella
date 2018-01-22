package uk.co.firstchoice.common.base.jdbc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Types;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

/**
 * Class providing support for simple SQL statement executions.
 * 
 *  
 */
public class SQLStatementExecutor extends DbDataAccessObject {
	protected SQLStatementExecutor() {
	}

	public SQLStatementExecutor(Connection conn) throws SQLException {
		super(conn);
	}

	/**
	 * Executes a SQL statement returning the number of rows affected.
	 * 
	 * @param sqlText
	 * @return NbrRowsAffected
	 * @throws SQLException
	 */
	public int executeUpdate(String sqlText) throws SQLException {
		return this.executeUpdate(sqlText, null);
	}

	/**
	 * Executes a SQL statement returning the number of rows affected.
	 * 
	 * @param sqlText
	 * @param hostVariableValue1
	 * @return NbrRowsAffected
	 * @throws SQLException
	 */
	public int executeUpdate(String sqlText, Object hostVariableValue1)
			throws SQLException {
		Object[] obj = new Object[1];
		obj[0] = hostVariableValue1;
		return this.executeUpdate(sqlText, obj);
	}

	/**
	 * Executes a SQL statement returning the number of rows affected.
	 * 
	 * @param sqlText
	 * @param hostVariableValue1
	 * @param hostVariableValue2
	 * @return NbrRowsAffected
	 * @throws SQLException
	 */
	public int executeUpdate(String sqlText, Object hostVariableValue1,
			Object hostVariableValue2) throws SQLException {
		Object[] obj = new Object[2];
		obj[0] = hostVariableValue1;
		obj[1] = hostVariableValue2;
		return this.executeUpdate(sqlText, obj);
	}

	/**
	 * Executes a SQL statement returning the number of rows affected.
	 * 
	 * @param sqlText
	 * @param hostVariableValue1
	 * @param hostVariableValue2
	 * @param hostVariableValue3
	 * @return NbrRowsAffected
	 * @throws SQLException
	 */
	public int executeUpdate(String sqlText, Object hostVariableValue1,
			Object hostVariableValue2, Object hostVariableValue3)
			throws SQLException {
		Object[] obj = new Object[3];
		obj[0] = hostVariableValue1;
		obj[1] = hostVariableValue2;
		obj[2] = hostVariableValue3;
		return this.executeUpdate(sqlText, obj);
	}

	/**
	 * Executes a SQL statement returning the number of rows affected.
	 * 
	 * @param sqlText
	 * @param hostVariableValue1
	 * @param hostVariableValue2
	 * @param hostVariableValue3
	 * @param hostVariableValue4
	 * @return NbrRowsAffected
	 * @throws SQLException
	 */
	public int executeUpdate(String sqlText, Object hostVariableValue1,
			Object hostVariableValue2, Object hostVariableValue3,
			Object hostVariableValue4) throws SQLException {
		Object[] obj = new Object[4];
		obj[0] = hostVariableValue1;
		obj[1] = hostVariableValue2;
		obj[2] = hostVariableValue3;
		obj[3] = hostVariableValue4;
		return this.executeUpdate(sqlText, obj);
	}

	/**
	 * Executes a SQL statement returning the number of rows affected.
	 * 
	 * @param sqlText
	 * @param hostVariableValue1
	 * @param hostVariableValue2
	 * @param hostVariableValue3
	 * @param hostVariableValue4
	 * @param hostVariableValue5
	 * @return NbrRowsAffected
	 * @throws SQLException
	 */
	public int executeUpdate(String sqlText, Object hostVariableValue1,
			Object hostVariableValue2, Object hostVariableValue3,
			Object hostVariableValue4, Object hostVariableValue5)
			throws SQLException {
		Object[] obj = new Object[5];
		obj[0] = hostVariableValue1;
		obj[1] = hostVariableValue2;
		obj[2] = hostVariableValue3;
		obj[3] = hostVariableValue4;
		obj[4] = hostVariableValue5;
		return this.executeUpdate(sqlText, obj);
	}

	/**
	 * Executes a SQL statement returning the number of rows affected.
	 * 
	 * <p>
	 * The parameter array is taken as host variable values. For each occurance
	 * on the array, a host variable value will be set. Data types will be
	 * treated as follows:
	 * 
	 * <li>Strings -- will use PreparedStatement.setString()</li>
	 * <li>Integer -- will use PreparedStatement.setInt()</li>
	 * <li>Long -- will use PreparedStatement.setLong()</li>
	 * <li>Double -- will use PreparedStatement.setDouble()</li>
	 * <li>Float -- will use PreparedStatement.setFloat()</li>
	 * <li>Short -- will use PreparedStatement.setShort()</li>
	 * <li>java.sql.Date -- will use PreparedStatement.setDate()</li>
	 * <li>java.util.Date -- will use PreparedStatement.setTimestamp()</li>
	 * <li>Timestamp -- will use PreparedStatement.setTimestamp()</li>
	 * <li>Others -- will use PreparedStatement.setObject()</li>
	 * 
	 * @param sqlText
	 * @param parms
	 * @return NbrRowsAffected
	 * @throws SQLException
	 */
	public int executeUpdate(String sqlText, Object[] parms)
			throws SQLException {
		if (sqlText == null)
			throw new IllegalArgumentException(
					"Null SQL statement not allowed.");
		if (sqlText.equals(""))
			throw new IllegalArgumentException(
					"Blank SQL statement not allowed.");

		int updateCount = 0;
		PreparedStatement pStmt = null;

		try {
			pStmt = this._dbConnection.prepareStatement(sqlText);
			this.setPreparedStatementHostVars(pStmt, parms);
			updateCount = pStmt.executeUpdate();
		} finally {
			DatabaseUtility.close(pStmt);
		}

		return updateCount;
	}

	public String[][] executeQueryReturningStrings(String sqlText,
			Object[] hostVars) throws SQLException {
		ArrayList stringList = new ArrayList();
		String[] currentRow = null;

		Object[][] result = this.executeQuery(sqlText, hostVars);
		for (int i = 0; result != null && i < result.length; i++) {
			currentRow = new String[result[i].length];
			for (int j = 0; j < result[i].length; j++) {
				if (result[i][j] == null)
					currentRow[j] = null;
				else if (result[i][j] instanceof String)
					currentRow[j] = (String) result[i][j];
				else if (result[i][j] instanceof java.util.Date)
					currentRow[j] = _dateFormat
							.format((java.util.Date) result[i][j]);
				else if (result[i][j] instanceof Double)
					currentRow[j] = _decimalFormat.format(result[i][j]);
				else if (result[i][j] instanceof Float)
					currentRow[j] = _decimalFormat.format(result[i][j]);
				else
					currentRow[j] = result[i][j].toString();
			}
			stringList.add(currentRow);
		}

		String[][] resultArray = null;
		if (stringList.size() > 0) {
			resultArray = (String[][]) stringList.toArray();
		}

		return resultArray;
	}

	/**
	 * Executes a query and returns the results as an object array.
	 * 
	 * @param sqlText
	 * @return results
	 * @throws SQLException
	 */
	public Object[][] executeQuery(String sqlText) throws SQLException {
		return this.executeQuery(sqlText, null);
	}

	/**
	 * Executes a query and returns the results as an object array.
	 * 
	 * @param sqlText
	 * @param hostVariableValue1
	 * @return results
	 * @throws SQLException
	 */
	public Object[][] executeQuery(String sqlText, Object hostVariableValue1)
			throws SQLException {
		Object[] obj = new Object[1];
		obj[0] = hostVariableValue1;
		return this.executeQuery(sqlText, obj);
	}

	/**
	 * Executes a query and returns the results as an object array.
	 * 
	 * @param sqlText
	 * @param hostVariableValue1
	 * @param hostVariableValue2
	 * @return results
	 * @throws SQLException
	 */
	public Object[][] executeQuery(String sqlText, Object hostVariableValue1,
			Object hostVariableValue2) throws SQLException {
		Object[] obj = new Object[2];
		obj[0] = hostVariableValue1;
		obj[1] = hostVariableValue2;
		return this.executeQuery(sqlText, obj);
	}

	/**
	 * Executes a query and returns the results as an object array.
	 * 
	 * @param sqlText
	 * @param hostVariableValue1
	 * @param hostVariableValue2
	 * @param hostVariableValue3
	 * @return results
	 * @throws SQLException
	 */
	public Object[][] executeQuery(String sqlText, Object hostVariableValue1,
			Object hostVariableValue2, Object hostVariableValue3)
			throws SQLException {
		Object[] obj = new Object[3];
		obj[0] = hostVariableValue1;
		obj[1] = hostVariableValue2;
		obj[2] = hostVariableValue3;
		return this.executeQuery(sqlText, obj);
	}

	/**
	 * Executes a query and returns the results as an object array.
	 * 
	 * @param sqlText
	 * @param hostVariableValue1
	 * @param hostVariableValue2
	 * @param hostVariableValue3
	 * @param hostVariableValue4
	 * @return results
	 * @throws SQLException
	 */
	public Object[][] executeQuery(String sqlText, Object hostVariableValue1,
			Object hostVariableValue2, Object hostVariableValue3,
			Object hostVariableValue4) throws SQLException {
		Object[] obj = new Object[4];
		obj[0] = hostVariableValue1;
		obj[1] = hostVariableValue2;
		obj[2] = hostVariableValue3;
		obj[3] = hostVariableValue4;
		return this.executeQuery(sqlText, obj);
	}

	/**
	 * Executes a query and returns the results as an object array.
	 * 
	 * @param sqlText
	 * @param hostVariableValue1
	 * @param hostVariableValue2
	 * @param hostVariableValue3
	 * @param hostVariableValue4
	 * @param hostVariableValue5
	 * @return results
	 * @throws SQLException
	 */
	public Object[][] executeQuery(String sqlText, Object hostVariableValue1,
			Object hostVariableValue2, Object hostVariableValue3,
			Object hostVariableValue4, Object hostVariableValue5)
			throws SQLException {
		Object[] obj = new Object[5];
		obj[0] = hostVariableValue1;
		obj[1] = hostVariableValue2;
		obj[2] = hostVariableValue3;
		obj[3] = hostVariableValue4;
		obj[4] = hostVariableValue5;
		return this.executeQuery(sqlText, obj);
	}

	/**
	 * Executes a query and returns the results as an object array.
	 * 
	 * @param sqlText
	 * @param hostVars
	 * @return results
	 * @throws SQLException
	 */
	public Object[][] executeQuery(String sqlText, Object[] hostVars)
			throws SQLException {
		ArrayList resultList = new ArrayList();
		Object[][] resultArray = null;
		PreparedStatement pStmt = null;
		ResultSet results = null;
		ResultSetMetaData metaData = null;
		Object[] currentRow = null;

		try {
			pStmt = this._dbConnection.prepareStatement(sqlText);
			this.setPreparedStatementHostVars(pStmt, hostVars);
			results = pStmt.executeQuery();
			metaData = pStmt.getMetaData();

			while (results.next()) {
				currentRow = new Object[metaData.getColumnCount()];

				for (int i = 0; i < currentRow.length; i++) {
					switch (metaData.getColumnType(i + 1)) {
					case Types.DATE:
					case Types.TIME:
					case Types.TIMESTAMP: {
						Timestamp ts = results.getTimestamp(i + 1);
						if (results.wasNull())
							currentRow[i] = null;
						else {
							long time = ts.getTime();
							currentRow[i] = new java.util.Date(time);
						}
						break;
					}

					case Types.CHAR:
					case Types.VARCHAR:
					case Types.LONGVARCHAR: {
						currentRow[i] = results.getString(i + 1);
						if (results.wasNull())
							currentRow[i] = null;
						break;
					}

					case Types.DOUBLE:
					case Types.NUMERIC:
					case Types.DECIMAL:
					case Types.REAL: {
						double d = results.getDouble(i + 1);
						if (results.wasNull())
							currentRow[i] = null;
						else
							currentRow[i] = new Double(d);
						break;
					}

					case Types.FLOAT: {
						float f = results.getFloat(i + 1);
						if (results.wasNull())
							currentRow[i] = null;
						else
							currentRow[i] = new Float(f);
						break;
					}

					case Types.INTEGER: {
						int j = results.getInt(i + 1);
						if (results.wasNull())
							currentRow[i] = null;
						else
							currentRow[i] = new Integer(j);
						break;
					}

					case Types.SMALLINT:
					case Types.TINYINT: {
						short s = results.getShort(i + 1);
						if (results.wasNull())
							currentRow[i] = null;
						else
							currentRow[i] = new Short(s);
						break;
					}

					default: {
						currentRow[i] = results.getObject(i + 1);
						if (results.wasNull())
							currentRow[i] = null;
					}
					}
				}
				resultList.add(currentRow);

			}
		} finally {
			DatabaseUtility.close(results, pStmt);
		}

		if (resultList.size() > 0) {
			resultArray = (Object[][]) resultList.toArray();
		}

		return resultArray;
	}

	private void setPreparedStatementHostVars(PreparedStatement pStmt,
			Object[] parms) throws SQLException {
		for (int i = 0; parms != null && i < parms.length; i++) {
			if (parms[i] != null) {
				if (parms[i] instanceof String)
					pStmt.setString(i + 1, (String) parms[i]);

				// Numeric data types
				else if (parms[i] instanceof Integer)
					pStmt.setInt(i + 1, ((Integer) parms[i]).intValue());
				else if (parms[i] instanceof Long)
					pStmt.setLong(i + 1, ((Long) parms[i]).longValue());
				else if (parms[i] instanceof Double)
					pStmt.setDouble(i + 1, ((Double) parms[i]).doubleValue());
				else if (parms[i] instanceof Float)
					pStmt.setFloat(i + 1, ((Float) parms[i]).floatValue());
				else if (parms[i] instanceof Short)
					pStmt.setShort(i + 1, ((Short) parms[i]).shortValue());

				// Date/Time data types
				else if (parms[i] instanceof java.sql.Date)
					pStmt.setDate(i + 1, ((java.sql.Date) parms[i]));
				else if (parms[i] instanceof java.util.Date) {
					Timestamp time = new Timestamp(((java.util.Date) parms[i])
							.getTime());
					pStmt.setTimestamp(i + 1, time);
				} else if (parms[i] instanceof Timestamp)
					pStmt.setTimestamp(i + 1, ((Timestamp) parms[i]));

				// Anything else...
				else
					pStmt.setObject(i + 1, parms[i]);
			} else {
				throw new IllegalArgumentException(
						"Null host variable value not allowed.  Occurance=" + i);
			}
		}
	}

	private SimpleDateFormat _dateFormat = new SimpleDateFormat("yyyy-MM-dd");

	private DecimalFormat _decimalFormat = new DecimalFormat();

}