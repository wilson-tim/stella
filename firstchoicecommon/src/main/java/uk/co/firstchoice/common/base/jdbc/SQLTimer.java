package uk.co.firstchoice.common.base.jdbc;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Properties;
import java.util.StringTokenizer;

import uk.co.firstchoice.common.base.ImproperUsageException;
import uk.co.firstchoice.common.base.InternalApplicationException;
import uk.co.firstchoice.common.base.util.ExceptionUtility;

/**
 * SQLTimer is a utility that will time a SQL statement given a test script.
 * This utility can be used to determine the effect of SQL statement
 * optimizations and database tuning efforts.
 * 
 * <p>
 * If run from a command prompt, the one and only argument is a properties file
 * that contains the following entries:
 * <li>sql.timer.sql -- SQL statement text</li>
 * <li>sql.timer.script -- Comma delimited test script</li>
 * <li>sql.timer.parms -- SQL statement parameter value types</li>
 * <li>sql.timer.db.driver -- JDBC Driver name</li>
 * <li>sql.timer.db.url -- JDBC Connection String</li>
 * <li>sql.timer.db.user -- Database User</li>
 * <li>sql.timer.db.password -- Database password</li>
 * 
 * <p>
 * Example property file: <blockquote>
 * 
 * <pre>
 * 
 *  
 *   
 *    
 *     sql.timer.sql=select * from all_tables where owner = ? and num_rows &gt; ?
 *     sql.timer.script=timer1.script
 *     sql.timer.parms=String
 *     sql.timer.db.driver=oracle.jdbc.driver.OracleDriver
 *     sql.timer.db.url=jdbc:oracle:thin:@localhost:1521:ORA92
 *     sql.timer.db.user=scott
 *     sql.timer.db.password=tiger
 *     
 *    
 *   
 *  
 * </pre>
 * 
 * </blockquote>
 * 
 * <p>
 * The test script is a comma-delimited set used to supply values for parameter
 * markers in the SQL statement.
 * 
 * <p>
 * Example script file: <blockquote>
 * 
 * <pre>
 * 
 *  
 *   
 *    
 *     SCOTT,5
 *     SYS,6
 *     SYSTEM,5
 *     
 *    
 *   
 *  
 * </pre>
 * 
 * </blockquote>
 * 
 *  
 */
public class SQLTimer implements Runnable {
	public static final String STRING_TYPE = "String";

	public static final String DATE_TYPE = "Date";

	public static final String NUMBER_TYPE = "Number";

	public static final String INTEGER_TYPE = "Integer";

	public static final String TIMESTAMP_TYPE = "Timestamp";

	private Connection _dbConnection = null;

	private String _sqlText = null;

	private BufferedReader _scriptReader = null;

	private String[] _parameterType = null;

	private long _totalExecuteTime = 0;

	private long _totalFetchTime = 0;

	private long _totalNbrRowsFetched = 0;

	private long _totalExecutions = 0;

	private boolean _isSelect = true;

	private int _nbrParameterMarkers = 0;

	private SimpleDateFormat _dateFormat = new SimpleDateFormat("yyyy-MM-dd");

	private SimpleDateFormat _timestampFormat = new SimpleDateFormat(
			"yyyy-MM-dd-HH:mm:ss.SSSSS");

	public SQLTimer() {
	}

	public SQLTimer(Properties testProps) throws FileNotFoundException {
		ExceptionUtility.exceptForNullsAndBlanks(testProps, "properties");

		StringBuffer message = new StringBuffer();
		if (testProps.containsKey("sql.timer.sql")) {
			this.setSqlText(testProps.getProperty("sql.timer.sql"));
		} else {
			if (message.length() > 0)
				message.append("sql.timer.sql");
		}

		if (testProps.containsKey("sql.timer.script")) {
			this.setScriptFileName(testProps.getProperty("sql.timer.script"));
		} else {
			if (message.length() > 0)
				message.append("sql.timer.script");
		}

		if (testProps.containsKey("sql.timer.parms")) {
			String parms = testProps.getProperty("sql.timer.parms");
			StringTokenizer token = new StringTokenizer(parms, ",");
			ArrayList parmList = new ArrayList();

			String parmType = null;
			while (token.hasMoreTokens()) {
				parmType = token.nextToken();
				if (parmType != null)
					parmType = parmType.trim();

				if (parmType.equalsIgnoreCase(STRING_TYPE)) {
					parmList.add(STRING_TYPE);
				} else if (parmType.equalsIgnoreCase(NUMBER_TYPE)) {
					parmList.add(NUMBER_TYPE);
				} else if (parmType.equalsIgnoreCase(INTEGER_TYPE)) {
					parmList.add(INTEGER_TYPE);
				} else if (parmType.equalsIgnoreCase(DATE_TYPE)) {
					parmList.add(DATE_TYPE);
				} else if (parmType.equalsIgnoreCase(TIMESTAMP_TYPE)) {
					parmList.add(TIMESTAMP_TYPE);
				} else {
					throw new IllegalArgumentException(
							"Parameter type "
									+ parmType
									+ " not allowed.  Check javadoc for allowed parmtypes.");
				}
			}

			_parameterType = new String[parmList.size()];
			for (int i = 0; i < parmList.size(); i++) {
				_parameterType[i] = (String) parmList.get(i);
			}
		} else {
			if (message.length() > 0)
				message.append("sql.timer.parms");
		}

		String driver = null;
		String url = null;
		String user = null;
		String password = null;

		if (testProps.containsKey("sql.timer.db.driver")) {
			driver = testProps.getProperty("sql.timer.db.driver");
		} else {
			if (message.length() > 0)
				message.append("sql.timer.db.driver");
		}

		if (testProps.containsKey("sql.timer.db.url")) {
			url = testProps.getProperty("sql.timer.db.url");
		} else {
			if (message.length() > 0)
				message.append("sql.timer.db.url");
		}

		if (testProps.containsKey("sql.timer.db.user")) {
			user = testProps.getProperty("sql.timer.db.user");
		} else {
			if (message.length() > 0)
				message.append("sql.timer.db.user");
		}

		if (testProps.containsKey("sql.timer.db.password")) {
			password = testProps.getProperty("sql.timer.db.password");
		} else {
			if (message.length() > 0)
				message.append("sql.timer.db.password");
		}

		try {
			Connection conn = DatabaseUtility.getJDBCConnection(driver, url,
					user, password);
			this.setDbConnection(conn);
		} catch (Throwable t) {
			StringBuffer errorMessage = new StringBuffer(256);
			errorMessage.append("Not able to connect using driver=");
			errorMessage.append(driver);
			errorMessage.append("; url=");
			errorMessage.append(url);
			errorMessage.append("; user=");
			errorMessage.append(user);
			errorMessage.append("; password=");
			errorMessage.append(password);

			throw new ImproperUsageException(errorMessage.toString(), t);
		}
	}

	public void run() {
		try {
			String scriptLineIn = _scriptReader.readLine();

			while (scriptLineIn != null) {
				if (_isSelect) {
					this.execSelectStmt(this
							.getParameterMarkerValues(scriptLineIn));
				} else {
					throw new UnsupportedOperationException(
							"Timing of non-select statements not currently supported.");
				}
				scriptLineIn = _scriptReader.readLine();
			}
		} catch (Throwable t) {
			throw new InternalApplicationException("Error running test", t);
		}
	}

	private ArrayList getParameterMarkerValues(String scriptLine)
			throws ParseException {
		ArrayList parmValueList = new ArrayList();
		StringTokenizer token = new StringTokenizer(scriptLine, ",", true);

		String value = null;
		int currentOffset = 0;
		while (token.hasMoreTokens()) {
			value = token.nextToken();

			if (value.equals(",")) {
				parmValueList.add(SQLAssistant.NULL);
				currentOffset++;
			} else {
				parmValueList.add(this.getParmValue(value,
						_parameterType[currentOffset]));
				currentOffset++;
				if (token.hasMoreTokens())
					token.nextToken();
			}

		}

		return parmValueList;
	}

	private Object getParmValue(String value, String type)
			throws ParseException {
		Object parmValue = null;
		if (value == null)
			return parmValue;

		if (type.equalsIgnoreCase(STRING_TYPE)) {
			parmValue = value;
		} else if (type.equalsIgnoreCase(INTEGER_TYPE)) {
			parmValue = new Integer(value);
		} else if (type.equalsIgnoreCase(NUMBER_TYPE)) {
			parmValue = new Double(value);
		} else if (type.equalsIgnoreCase(DATE_TYPE)) {
			parmValue = _dateFormat.parseObject(value);
		} else if (type.equalsIgnoreCase(TIMESTAMP_TYPE)) {
			parmValue = _timestampFormat.parseObject(value);
		} else {
			parmValue = value;
		}

		return parmValue;
	}

	private void execSelectStmt(ArrayList parmValueList) throws SQLException {
		SQLAssistant sqlAsst = new SQLAssistant(_dbConnection);
		PreparedStatement pStmt = null;
		ResultSet results = null;

		long beginQueryTime;
		long beginFetchTime;
		_totalExecutions++;

		try {
			pStmt = sqlAsst.getPreparedStatement(_sqlText, parmValueList);

			beginQueryTime = System.currentTimeMillis();
			results = pStmt.executeQuery();
			_totalExecuteTime += (System.currentTimeMillis() - beginQueryTime);

			beginFetchTime = System.currentTimeMillis();
			while (results.next()) {
				_totalNbrRowsFetched++;
			}
			_totalFetchTime += (System.currentTimeMillis() - beginFetchTime);
		} finally {
			DatabaseUtility.close(results, pStmt);
		}
	}

	public static void main(String[] args) {
		if (args.length > 0) {
			try {
				Properties testProps = new Properties();
				testProps.load(new FileInputStream(args[0]));
				SQLTimer timer = new SQLTimer(testProps);

				System.out.println("---------------------------------");
				System.out.println("Starting timing at " + new Date());
				System.out.println("\t" + testProps);
				timer.run();

				System.out.println("--------------");
				System.out.println("\tNbr Executions: "
						+ timer.getTotalExecutions() + " executions");
				System.out.println("\tNbr Rows: "
						+ timer.getTotalNbrRowsFetched() + " rows");
				System.out.println("\tExecution time: "
						+ timer.getTotalExecuteTime() + " ms");
				System.out.println("\tFetch time: " + timer.getTotalFetchTime()
						+ " ms");
			} catch (Throwable t) {
				t.printStackTrace();
			}
		} else {
			System.err.println("Properties file not provided.");
			System.exit(1);
		}
	}

	/**
	 * @return
	 */
	public Connection getDbConnection() {
		return _dbConnection;
	}

	/**
	 * @return
	 */
	public String getSqlText() {
		return _sqlText;
	}

	/**
	 * @return
	 */
	public long getTotalExecuteTime() {
		return _totalExecuteTime;
	}

	/**
	 * @return
	 */
	public long getTotalFetchTime() {
		return _totalFetchTime;
	}

	/**
	 * @param connection
	 */
	public void setDbConnection(Connection connection) {
		_dbConnection = connection;
	}

	/**
	 * @param string
	 */
	public void setSqlText(String sqlText) {
		if (sqlText.toUpperCase().startsWith("SELECT ")) {
			_isSelect = true;
		} else
			_isSelect = false;

		_sqlText = sqlText;
	}

	/**
	 * Sets a script file for the timing test.
	 * 
	 * <p>
	 * Script file format is comma delimited. Each line contains a value for a
	 * parameter marker in the SQL text provided.
	 * 
	 * @param name
	 * @throws FileNotFoundException
	 */
	public void setScriptFileName(String name) throws FileNotFoundException {
		ExceptionUtility.exceptForNullsAndBlanks(name, "Script file name");
		_scriptReader = new BufferedReader(new FileReader(name));
	}

	/**
	 * @return
	 */
	public String[] getParameterType() {
		return _parameterType;
	}

	/**
	 * @param is
	 */
	public void setParameterType(String[] is) {
		_parameterType = is;
	}

	/**
	 * @return
	 */
	public long getTotalNbrRowsFetched() {
		return _totalNbrRowsFetched;
	}

	/**
	 * @return
	 */
	public long getTotalExecutions() {
		return _totalExecutions;
	}

}