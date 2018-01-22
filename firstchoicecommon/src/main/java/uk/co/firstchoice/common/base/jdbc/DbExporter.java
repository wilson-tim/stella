package uk.co.firstchoice.common.base.jdbc;

import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.PrintStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Types;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.StringTokenizer;

import uk.co.firstchoice.common.base.InternalApplicationException;
import uk.co.firstchoice.common.base.util.ExceptionUtility;

/**
 * Class to export and import data from a relational database using any JDBC
 * driver.
 */
public class DbExporter {
	private Connection _exportConnection = null;

	private Connection _importConnection = null;

	private String _importTableName = null;

	private String _exportTableName = null;

	private String _delimiter = DEFAULT_DELIMITER;

	public static final String DEFAULT_DELIMITER = "|";

	public static final SimpleDateFormat TIMESTAMP_FORMAT = new SimpleDateFormat(
			"yyyy-MM-dd-HH.mm.ss.SSS");

	/**
	 * Exports a file to delimited text.
	 * 
	 * @param fileName
	 */
	public void exportFile(String fileName) {
		ExceptionUtility.exceptForNullsAndBlanks(fileName, "fileName");
		if (_exportConnection == null || _exportTableName == null) {
			throw new IllegalStateException(
					"Connection and table name required.");
		}

		PrintStream output = null;
		PreparedStatement pStmt = null;
		ResultSet results = null;
		try {
			output = new PrintStream(new FileOutputStream(fileName));

			String sqlText = "select * from " + _exportTableName;
			SQLAssistant sql = new SQLAssistant(_exportConnection);
			sql.setDefaultDateFormat(TIMESTAMP_FORMAT);

			pStmt = _exportConnection.prepareStatement(sqlText);
			results = pStmt.executeQuery();

			ResultSetMetaData metaData = results.getMetaData();
			for (int i = 1; i <= metaData.getColumnCount(); i++) {
				if (i > 1)
					output.print(_delimiter);
				output.print(metaData.getColumnName(i));
			}
			output.println("");

			Object[] row = null;
			int col = 0;
			while (results.next()) {
				row = sql.getNextRow(results);
				for (col = 0; row != null && col < row.length; col++) {
					if (col > 0)
						output.print(_delimiter);
					if (row[col] != null)
						output.print(row[col]);
				}
				output.println("");
			}
		} catch (Throwable t) {
			StringBuffer message = new StringBuffer();

			message.append("Error exporting table ");
			message.append(_exportTableName);
			message.append(" to file ");
			message.append(fileName);
			throw new InternalApplicationException(message.toString());
		} finally {
			DatabaseUtility.close(pStmt, results);
			if (output != null)
				output.close();
		}
	}

	/**
	 * Imports a file from delimited text.
	 * 
	 * @param fileName
	 */
	public void importFile(String fileName) {
		ExceptionUtility.exceptForNullsAndBlanks(fileName, "fileName");
		if (_importConnection == null || _importTableName == null) {
			throw new IllegalStateException(
					"Connection and table name required.");
		}

		BufferedReader input = null;
		PreparedStatement pStmt = null;
		PreparedStatement insertPStmt = null;
		ResultSet results = null;

		try {
			input = new BufferedReader(new FileReader(fileName));

			String selectText = "select * from " + _importTableName
					+ " where 1 = 2";
			pStmt = _importConnection.prepareStatement(selectText);
			results = pStmt.executeQuery();

			HashMap fileToTableMap = new HashMap();
			ArrayList columnsToImport = new ArrayList();

			ResultSetMetaData metaData = results.getMetaData();
			String headerLine = input.readLine();
			StringTokenizer headerToken = new StringTokenizer(headerLine,
					_delimiter, false);
			String importColumnName = null;

			while (headerToken.hasMoreTokens()) {
				importColumnName = headerToken.nextToken();
				for (int i = 1; i <= metaData.getColumnCount(); i++) {
					if (importColumnName.equalsIgnoreCase(metaData
							.getColumnName(i))) {
						columnsToImport.add(importColumnName);
						fileToTableMap.put(importColumnName, new Integer(i));
						i = metaData.getColumnCount();
					}
				}
			}

			StringBuffer insertSqlText = new StringBuffer(256);
			insertSqlText.append("insert into ");
			insertSqlText.append(_importTableName);
			insertSqlText.append(" (");
			insertSqlText.append(this.getDelimitedString(columnsToImport, ","));
			insertSqlText.append(" ) values (");
			insertSqlText.append(this.getDelimitedString("?", ",",
					columnsToImport.size()));
			insertSqlText.append(")");
			insertPStmt = _importConnection.prepareStatement(insertSqlText
					.toString());

			String dataLine = input.readLine();
			int startOffset, endOffset;
			String value;
			Object javaValue = null;
			int commitCount = 0;

			while (dataLine != null) {
				startOffset = 0;
				endOffset = dataLine.indexOf(_delimiter);
				int sqlType = 0;
				int colOffset = 0;
				if (endOffset < 0)
					endOffset = dataLine.length();

				for (int i = 0; i < columnsToImport.size(); i++) {
					if (endOffset > dataLine.length())
						break;
					else if (endOffset > startOffset) {
						value = dataLine.substring(startOffset, endOffset);
					} else
						value = null;
					colOffset = ((Integer) fileToTableMap.get(columnsToImport
							.get(i))).intValue();
					sqlType = metaData.getColumnType(colOffset);

					if (value == null) {
						insertPStmt.setNull(i + 1, sqlType);
					} else {
						switch (sqlType) {
						case Types.DATE:
						case Types.TIME:
						case Types.TIMESTAMP: {
							javaValue = new java.sql.Timestamp(TIMESTAMP_FORMAT
									.parse(value).getTime());
							break;
						}
						case Types.DECIMAL:
						case Types.DOUBLE:
						case Types.FLOAT:
						case Types.NUMERIC: {
							javaValue = new Double(value);
							break;
						}
						case Types.BIGINT:
						case Types.INTEGER:
						case Types.SMALLINT:
						case Types.TINYINT: {
							javaValue = new Long(value);
							break;
						}
						default: {
							javaValue = value;
						}
						}
						insertPStmt.setObject(i + 1, javaValue);
					}

					startOffset = endOffset + 1;
					endOffset = dataLine.indexOf(_delimiter, startOffset);
					if (endOffset < 0)
						endOffset = dataLine.length();
				}
				insertPStmt.executeUpdate();
				if (commitCount > 100) {
					commitCount = 0;
					_importConnection.commit();
				} else
					commitCount++;

				dataLine = input.readLine();
			}
			_importConnection.commit();

			input.close();
		} catch (Throwable t) {
			StringBuffer message = new StringBuffer();

			message.append("Error importing table ");
			message.append(_importTableName);
			message.append(" from file ");
			message.append(fileName);
			throw new InternalApplicationException(message.toString(), t);
		} finally {
			DatabaseUtility.close(pStmt, results);
		}
	}

	private String getDelimitedString(List list, String delimiter) {
		StringBuffer delimitedStr = new StringBuffer();

		for (int i = 0; i < list.size(); i++) {
			if (i > 0)
				delimitedStr.append(delimiter);
			delimitedStr.append(list.get(i));
		}

		return delimitedStr.toString();
	}

	private String getDelimitedString(String value, String delimiter,
			int nbrTimes) {
		StringBuffer delimitedStr = new StringBuffer();

		for (int i = 0; i < nbrTimes; i++) {
			if (i > 0)
				delimitedStr.append(delimiter);
			delimitedStr.append(value);
		}

		return delimitedStr.toString();
	}

	public static void main(String[] args) {
	}

	/**
	 * Connection used for the export operation.
	 * 
	 * @return
	 */
	public Connection getExportConnection() {
		return _exportConnection;
	}

	/**
	 * Table name used for export.
	 * 
	 * @return
	 */
	public String getExportTableName() {
		return _exportTableName;
	}

	/**
	 * Connection used for Import operation.
	 * 
	 * @return
	 */
	public Connection getImportConnection() {
		return _importConnection;
	}

	/**
	 * Table name used for import.
	 * 
	 * @return
	 */
	public String getImportTableName() {
		return _importTableName;
	}

	/**
	 * Connection used for the export operation.
	 * 
	 * @param connection
	 */
	public void setExportConnection(Connection connection) {
		_exportConnection = connection;
	}

	/**
	 * Table name used for export.
	 * 
	 * @param string
	 */
	public void setExportTableName(String string) {
		_exportTableName = string;
	}

	/**
	 * Connection used for Import operation.
	 * 
	 * @param connection
	 */
	public void setImportConnection(Connection connection) {
		_importConnection = connection;
	}

	/**
	 * Table name used for import.
	 * 
	 * @param string
	 */
	public void setImportTableName(String string) {
		_importTableName = string;
	}

	/**
	 * Delimiter used in export file.
	 * 
	 * @return
	 */
	public String getDelimiter() {
		return _delimiter;
	}

	/**
	 * Delimiter used in export file.
	 * 
	 * @param string
	 */
	public void setDelimiter(String string) {
		_delimiter = string;
	}

}