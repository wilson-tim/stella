package uk.co.firstchoice.common.base.jdbc;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Types;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Class providing SQL statement execution assistance. This is a JDBC code
 * reduction technique.
 *
 * <p>
 * SQLAssistant can greatly reduce the amount of JDBC-related code you write and
 * maintain. Examples of how to instantiate and use a SQLAssistant follow.
 *
 * <p>
 * Example instantiation of a SQLAssistant <blockquote>
 *
 * <pre>
 * SQLAssistant sql = new SQLAssistant(_dbConnection);
 * </pre>
 *
 * </blockquote>
 *
 * <p>
 * Example of a simple select: <blockquote>
 *
 * <pre>
 * Object[][] results = null;
 * results = sql.executeQuery(&quot;select * from purchase_order&quot;);
 * </pre>
 *
 * </blockquote>
 *
 * <p>
 * Example of a select with host variables: <blockquote>
 *
 * <pre>
 * Object[][] results = null;
 * results = sql.executeQuery(
 *         &quot;select * from purchase_order where DATE_CREATED &lt; ?&quot;,
 *         new java.util.Date());
 * </pre>
 *
 * </blockquote>
 *
 * <p>
 * Example of an update with host variables: <blockquote>
 *
 * <pre>
 * int nbrRowsAffected = sql.executeUpdate(
 *         &quot;update purchase_order set date_shipped = ? where order_nbr = ?&quot;,
 *         new java.util.Date(), new Integer(809));
 * </pre>
 *
 * </blockquote>
 *
 * <p>
 */
public class SQLAssistant {
    private Connection _dbConnection = null;

    private SimpleDateFormat _defaultDateFormat = null;

    private DecimalFormat _defaultDecimalFormat = null;

    private boolean _spaceTrimOn = false;

    private boolean _convertStringOn = false;

    private static class NullValue {
    }

    public static final NullValue NULL = new NullValue();

    public SQLAssistant(Connection conn) {
        this.setConnection(conn);
    }

    /**
     * Executes a SQL statement making host variable substitutions where
     * appropriate.
     *
     * @param sqlText
     * @return NbrRows affected by SQL Statement
     * @throws SQLException
     */
    public int executeUpdate(String sqlText) throws SQLException {
        return this.executeUpdate(sqlText, null);
    }

    /**
     * Executes a SQL statement making host variable substitutions where
     * appropriate.
     *
     * @param sqlText
     * @param hostVar1
     * @return NbrRows affected by SQL Statement
     * @throws SQLException
     */
    public int executeUpdate(String sqlText, Object hostVar1)
            throws SQLException {
        ArrayList list = new ArrayList();

        list.add(this.getVariableObj(hostVar1));

        return this.executeUpdate(sqlText, list);
    }

    /**
     * Executes a SQL statement making host variable substitutions where
     * appropriate.
     *
     * @param sqlText
     * @param hostVar1
     * @param hostVar2
     * @return NbrRows affected by SQL Statement
     * @throws SQLException
     */
    public int executeUpdate(String sqlText, Object hostVar1, Object hostVar2)
            throws SQLException {
        ArrayList list = new ArrayList();

        list.add(this.getVariableObj(hostVar1));
        list.add(this.getVariableObj(hostVar2));

        return this.executeUpdate(sqlText, list);
    }

    /**
     * Executes a SQL statement making host variable substitutions where
     * appropriate.
     *
     * @param sqlText
     * @param hostVar1
     * @param hostVar2
     * @param hostVar3
     * @return NbrRows affected by SQL Statement
     * @throws SQLException
     */
    public int executeUpdate(String sqlText, Object hostVar1, Object hostVar2,
            Object hostVar3) throws SQLException {
        ArrayList list = new ArrayList();

        list.add(this.getVariableObj(hostVar1));
        list.add(this.getVariableObj(hostVar2));
        list.add(this.getVariableObj(hostVar3));

        return this.executeUpdate(sqlText, list);
    }

    /**
     * Executes a SQL statement making host variable substitutions where
     * appropriate.
     *
     * @param sqlText
     * @param hostVar1
     * @param hostVar2
     * @param hostVar3
     * @param hostVar4
     * @return NbrRows affected by SQL Statement
     * @throws SQLException
     */
    public int executeUpdate(String sqlText, Object hostVar1, Object hostVar2,
            Object hostVar3, Object hostVar4) throws SQLException {
        ArrayList list = new ArrayList();

        list.add(this.getVariableObj(hostVar1));
        list.add(this.getVariableObj(hostVar2));
        list.add(this.getVariableObj(hostVar3));
        list.add(this.getVariableObj(hostVar4));

        return this.executeUpdate(sqlText, list);
    }

    /**
     * Executes a SQL statement making host variable substitutions where
     * appropriate.
     *
     * @param sqlText
     * @param hostVar1
     * @param hostVar2
     * @param hostVar3
     * @param hostVar4
     * @param hostVar5
     * @return NbrRows affected by SQL Statement
     * @throws SQLException
     */
    public int executeUpdate(String sqlText, Object hostVar1, Object hostVar2,
            Object hostVar3, Object hostVar4, Object hostVar5)
            throws SQLException {
        ArrayList list = new ArrayList();

        list.add(this.getVariableObj(hostVar1));
        list.add(this.getVariableObj(hostVar2));
        list.add(this.getVariableObj(hostVar3));
        list.add(this.getVariableObj(hostVar4));
        list.add(this.getVariableObj(hostVar5));

        return this.executeUpdate(sqlText, list);
    }

    /**
     * Executes a SQL statement making host variable substitutions where
     * appropriate.
     *
     * @param sqlText
     * @param hostVar1
     * @param hostVar2
     * @param hostVar3
     * @param hostVar4
     * @param hostVar5
     * @param hostVar6
     * @return NbrRows affected by SQL Statement
     * @throws SQLException
     */
    public int executeUpdate(String sqlText, Object hostVar1, Object hostVar2,
            Object hostVar3, Object hostVar4, Object hostVar5, Object hostVar6)
            throws SQLException {
        ArrayList list = new ArrayList();

        list.add(this.getVariableObj(hostVar1));
        list.add(this.getVariableObj(hostVar2));
        list.add(this.getVariableObj(hostVar3));
        list.add(this.getVariableObj(hostVar4));
        list.add(this.getVariableObj(hostVar5));
        list.add(this.getVariableObj(hostVar6));

        return this.executeUpdate(sqlText, list);
    }

    /**
     * Executes a SQL statement making host variable substitutions where
     * appropriate.
     *
     * @param sqlText
     * @param hostVar1
     * @param hostVar2
     * @param hostVar3
     * @param hostVar4
     * @param hostVar5
     * @param hostVar6
     * @param hostVar7
     * @return NbrRows affected by SQL Statement
     * @throws SQLException
     */
    public int executeUpdate(String sqlText, Object hostVar1, Object hostVar2,
            Object hostVar3, Object hostVar4, Object hostVar5, Object hostVar6,
            Object hostVar7) throws SQLException {
        ArrayList list = new ArrayList();

        list.add(this.getVariableObj(hostVar1));
        list.add(this.getVariableObj(hostVar2));
        list.add(this.getVariableObj(hostVar3));
        list.add(this.getVariableObj(hostVar4));
        list.add(this.getVariableObj(hostVar5));
        list.add(this.getVariableObj(hostVar6));
        list.add(this.getVariableObj(hostVar7));

        return this.executeUpdate(sqlText, list);
    }

    /**
     * Executes a SQL statement making host variable substitutions where
     * appropriate.
     *
     * @param sqlText
     * @param hostVar1
     * @param hostVar2
     * @param hostVar3
     * @param hostVar4
     * @param hostVar5
     * @param hostVar6
     * @param hostVar7
     * @param hostVar8
     * @return NbrRows affected by SQL Statement
     * @throws SQLException
     */
    public int executeUpdate(String sqlText, Object hostVar1, Object hostVar2,
            Object hostVar3, Object hostVar4, Object hostVar5, Object hostVar6,
            Object hostVar7, Object hostVar8) throws SQLException {
        ArrayList list = new ArrayList();

        list.add(this.getVariableObj(hostVar1));
        list.add(this.getVariableObj(hostVar2));
        list.add(this.getVariableObj(hostVar3));
        list.add(this.getVariableObj(hostVar4));
        list.add(this.getVariableObj(hostVar5));
        list.add(this.getVariableObj(hostVar6));
        list.add(this.getVariableObj(hostVar7));
        list.add(this.getVariableObj(hostVar8));

        return this.executeUpdate(sqlText, list);
    }

    /**
     * Executes a SQL statement making host variable substitutions where
     * appropriate.
     *
     * @param sqlText
     * @param hostVar1
     * @param hostVar2
     * @param hostVar3
     * @param hostVar4
     * @param hostVar5
     * @param hostVar6
     * @param hostVar7
     * @param hostVar8
     * @param hostVar9
     * @return NbrRows affected by SQL Statement
     * @throws SQLException
     */
    public int executeUpdate(String sqlText, Object hostVar1, Object hostVar2,
            Object hostVar3, Object hostVar4, Object hostVar5, Object hostVar6,
            Object hostVar7, Object hostVar8, Object hostVar9)
            throws SQLException {
        ArrayList list = new ArrayList();

        list.add(this.getVariableObj(hostVar1));
        list.add(this.getVariableObj(hostVar2));
        list.add(this.getVariableObj(hostVar3));
        list.add(this.getVariableObj(hostVar4));
        list.add(this.getVariableObj(hostVar5));
        list.add(this.getVariableObj(hostVar6));
        list.add(this.getVariableObj(hostVar7));
        list.add(this.getVariableObj(hostVar8));
        list.add(this.getVariableObj(hostVar9));

        return this.executeUpdate(sqlText, list);
    }

    /**
     * Executes a SQL statement making host variable substitutions where
     * appropriate.
     *
     * @param sqlText
     * @param hostVar1
     * @param hostVar2
     * @param hostVar3
     * @param hostVar4
     * @param hostVar5
     * @param hostVar6
     * @param hostVar7
     * @param hostVar8
     * @param hostVar9
     * @return NbrRows affected by SQL Statement
     * @throws SQLException
     */
    public int executeUpdate(String sqlText, List hostVariableList)
            throws SQLException {
        if (sqlText == null)
            throw new IllegalArgumentException("Null sqlText not allowed.");
        if (sqlText.equals(""))
            throw new IllegalArgumentException("Blank sqlText not allowed.");

        PreparedStatement pStmt = null;
        int pStmtReturn = -1;
        try {
            pStmt = this.getPreparedStatement(sqlText, hostVariableList);
            pStmtReturn = pStmt.executeUpdate();
        } finally {
            DatabaseUtility.close(pStmt);
        }

        return pStmtReturn;
    }

    /**
     * Executes a query and returns results.
     *
     * <p>
     * Object Types used are determined by the underlying JDBC driver.
     *
     * @param sqlText
     * @return QueryResults
     * @throws SQLException
     */
    public Object[][] executeQuery(String sqlText) throws SQLException {
        return this.executeQuery(sqlText, null);
    }


    /**
     * Executes a query and returns results.
     *
     * <p>
     * Object Types used are determined by the underlying JDBC driver.
     *
     * @param sqlText
     * @param hostVar1
     * @return QueryResults
     * @throws SQLException
     */
    public Object[][] executeQuery(String sqlText, Object hostVar1)
            throws SQLException {
        ArrayList list = new ArrayList();

        list.add(this.getVariableObj(hostVar1));

        return this.executeQuery(sqlText, list);
    }

    /**
     * Executes a query and returns results.
     *
     * <p>
     * Object Types used are determined by the underlying JDBC driver.
     *
     * @param sqlText
     * @param hostVar1
     * @param hostVar2
     * @return QueryResults
     * @throws SQLException
     */
    public Object[][] executeQuery(String sqlText, Object hostVar1,
            Object hostVar2) throws SQLException {
        ArrayList list = new ArrayList();

        list.add(this.getVariableObj(hostVar1));
        list.add(this.getVariableObj(hostVar2));

        return this.executeQuery(sqlText, list);
    }


    /**
     * Executes a callable statement and returns results.
     *
     * <p>
     * Object Types used are determined by the underlying JDBC driver.
     *
     * @param sqlText
     * @return QueryResults
     * @throws SQLException
     */
    public Object[][] executeCallableStatement(String sqlText) throws SQLException {
        return this.executeCallableStatement(sqlText, null);
    }


    /**
     * Executes a callable statement and returns results.
     *
     * <p>
     * Object Types used are determined by the underlying JDBC driver.
     *
     * @param sqlText
     * @param hostVar1
     * @return QueryResults
     * @throws SQLException
     */
    public Object[][] executeCallableStatement(String sqlText, Object hostVar1)
            throws SQLException {
        ArrayList list = new ArrayList();

        list.add(this.getVariableObj(hostVar1));

        return this.executeCallableStatement(sqlText, list);
    }

    /**
     * Executes a query and returns results.
     *
     * <p>
     * Object Types used are determined by the underlying JDBC driver.
     *
     * @param sqlText
     * @param hostVar1
     * @param hostVar2
     * @param hostVar3
     * @return QueryResults
     * @throws SQLException
     */
    public Object[][] executeQuery(String sqlText, Object hostVar1,
            Object hostVar2, Object hostVar3) throws SQLException {
        ArrayList list = new ArrayList();

        list.add(this.getVariableObj(hostVar1));
        list.add(this.getVariableObj(hostVar2));
        list.add(this.getVariableObj(hostVar3));

        return this.executeQuery(sqlText, list);
    }

    /**
     * Executes a query and returns results.
     *
     * <p>
     * Object Types used are determined by the underlying JDBC driver.
     *
     * @param sqlText
     * @param hostVar1
     * @param hostVar2
     * @param hostVar3
     * @param hostVar4
     * @return QueryResults
     * @throws SQLException
     */
    public Object[][] executeQuery(String sqlText, Object hostVar1,
            Object hostVar2, Object hostVar3, Object hostVar4)
            throws SQLException {
        ArrayList list = new ArrayList();

        list.add(this.getVariableObj(hostVar1));
        list.add(this.getVariableObj(hostVar2));
        list.add(this.getVariableObj(hostVar3));
        list.add(this.getVariableObj(hostVar4));

        return this.executeQuery(sqlText, list);
    }

    /**
     * Executes a query and returns results.
     *
     * <p>
     * Object Types used are determined by the underlying JDBC driver.
     *
     * @param sqlText
     * @param hostVar1
     * @param hostVar2
     * @param hostVar3
     * @param hostVar4
     * @param hostVar5
     * @return QueryResults
     * @throws SQLException
     */
    public Object[][] executeQuery(String sqlText, Object hostVar1,
            Object hostVar2, Object hostVar3, Object hostVar4, Object hostVar5)
            throws SQLException {
        ArrayList list = new ArrayList();

        list.add(this.getVariableObj(hostVar1));
        list.add(this.getVariableObj(hostVar2));
        list.add(this.getVariableObj(hostVar3));
        list.add(this.getVariableObj(hostVar4));
        list.add(this.getVariableObj(hostVar5));

        return this.executeQuery(sqlText, list);
    }

    /**
     * Executes a query and returns results.
     *
     * <p>
     * Object Types used are determined by the underlying JDBC driver.
     *
     * @param sqlText
     * @param hostVar1
     * @param hostVar2
     * @param hostVar3
     * @param hostVar4
     * @param hostVar5
     * @param hostVar6
     * @return QueryResults
     * @throws SQLException
     */
    public Object[][] executeQuery(String sqlText, Object hostVar1,
            Object hostVar2, Object hostVar3, Object hostVar4, Object hostVar5,
            Object hostVar6) throws SQLException {
        ArrayList list = new ArrayList();

        list.add(this.getVariableObj(hostVar1));
        list.add(this.getVariableObj(hostVar2));
        list.add(this.getVariableObj(hostVar3));
        list.add(this.getVariableObj(hostVar4));
        list.add(this.getVariableObj(hostVar5));
        list.add(this.getVariableObj(hostVar6));

        return this.executeQuery(sqlText, list);
    }

    /**
     * Executes a query and returns results.
     *
     * <p>
     * Object Types used are determined by the underlying JDBC driver.
     *
     * @param sqlText
     * @param hostVar1
     * @param hostVar2
     * @param hostVar3
     * @param hostVar4
     * @param hostVar5
     * @param hostVar6
     * @param hostVar7
     * @return QueryResults
     * @throws SQLException
     */
    public Object[][] executeQuery(String sqlText, Object hostVar1,
            Object hostVar2, Object hostVar3, Object hostVar4, Object hostVar5,
            Object hostVar6, Object hostVar7) throws SQLException {
        ArrayList list = new ArrayList();

        list.add(this.getVariableObj(hostVar1));
        list.add(this.getVariableObj(hostVar2));
        list.add(this.getVariableObj(hostVar3));
        list.add(this.getVariableObj(hostVar4));
        list.add(this.getVariableObj(hostVar5));
        list.add(this.getVariableObj(hostVar6));
        list.add(this.getVariableObj(hostVar7));

        return this.executeQuery(sqlText, list);
    }

    /**
     * Executes a query and returns results.
     *
     * <p>
     * Object Types used are determined by the underlying JDBC driver.
     *
     * @param sqlText
     * @param hostVar1
     * @param hostVar2
     * @param hostVar3
     * @param hostVar4
     * @param hostVar5
     * @param hostVar6
     * @param hostVar7
     * @param hostVar8
     * @return QueryResults
     * @throws SQLException
     */
    public Object[][] executeQuery(String sqlText, Object hostVar1,
            Object hostVar2, Object hostVar3, Object hostVar4, Object hostVar5,
            Object hostVar6, Object hostVar7, Object hostVar8)
            throws SQLException {
        ArrayList list = new ArrayList();

        list.add(this.getVariableObj(hostVar1));
        list.add(this.getVariableObj(hostVar2));
        list.add(this.getVariableObj(hostVar3));
        list.add(this.getVariableObj(hostVar4));
        list.add(this.getVariableObj(hostVar5));
        list.add(this.getVariableObj(hostVar6));
        list.add(this.getVariableObj(hostVar7));
        list.add(this.getVariableObj(hostVar8));

        return this.executeQuery(sqlText, list);
    }

    /**
     * Executes a query and returns results.
     *
     * <p>
     * Object Types used are determined by the underlying JDBC driver.
     *
     * @param sqlText
     * @param hostVar1
     * @param hostVar2
     * @param hostVar3
     * @param hostVar4
     * @param hostVar5
     * @param hostVar6
     * @param hostVar7
     * @param hostVar8
     * @param hostVar9
     * @return QueryResults
     * @throws SQLException
     */
    public Object[][] executeQuery(String sqlText, Object hostVar1,
            Object hostVar2, Object hostVar3, Object hostVar4, Object hostVar5,
            Object hostVar6, Object hostVar7, Object hostVar8, Object hostVar9)
            throws SQLException {
        ArrayList list = new ArrayList();

        list.add(this.getVariableObj(hostVar1));
        list.add(this.getVariableObj(hostVar2));
        list.add(this.getVariableObj(hostVar3));
        list.add(this.getVariableObj(hostVar4));
        list.add(this.getVariableObj(hostVar5));
        list.add(this.getVariableObj(hostVar6));
        list.add(this.getVariableObj(hostVar7));
        list.add(this.getVariableObj(hostVar8));
        list.add(this.getVariableObj(hostVar9));

        return this.executeQuery(sqlText, list);
    }

    /**
     * Executes a query and returns results.
     *
     * <p>
     * Object Types used are determined by the underlying JDBC driver.
     *
     * @param sqlText
     * @param hostVariableList
     * @return QueryResults
     * @throws SQLException
     */
    public Object[][] executeQuery(String sqlText, List hostVariableList)
            throws SQLException {
        if (sqlText == null)
            throw new IllegalArgumentException("Null sqlText not allowed.");
        if (sqlText.equals(""))
            throw new IllegalArgumentException("Blank sqlText not allowed.");

        Object[][] queryResults = null;
        ArrayList queryResultList = new ArrayList();
        Object[] rowResults = null;

        PreparedStatement pStmt = null;
        ResultSet results = null;
        ResultSetMetaData resultsMetaData = null;

        try {
            pStmt = this.getPreparedStatement(sqlText, hostVariableList);
            results = pStmt.executeQuery();
            resultsMetaData = results.getMetaData();

            while (results.next()) {
                rowResults = this.getNextRow(results, false);
                queryResultList.add(rowResults);
            }

            if (queryResultList.size() > 0) {
                queryResults = new Object[queryResultList.size()][resultsMetaData.getColumnCount()];
                queryResults = (Object[][]) queryResultList.toArray(queryResults);
            }
        } finally {
            DatabaseUtility.close(results, pStmt);
        }

        return queryResults;
    }

    /**
     * Gets a row from a ResultSet as an array of Java objects.
     *
     * @param results
     * @return
     * @throws SQLException
     */
    public Object[] getNextRow(ResultSet results) throws SQLException {
        return this.getNextRow(results, true);
    }

    private Object[] getNextRow(ResultSet results, boolean issueNext)
            throws SQLException {
        Object[] rowResults = null;
        Object tempObj = null;
        ResultSetMetaData resultsMetaData = results.getMetaData();

        boolean rowsAvailable = true;
        if (issueNext) {
            rowsAvailable = results.next();
        }

        if (rowsAvailable) {
            rowResults = new Object[resultsMetaData.getColumnCount()];
            for (int offset = 1; offset <= resultsMetaData.getColumnCount(); offset++) {
                tempObj = results.getObject(offset);
                if (results.wasNull())
                    rowResults[offset - 1] = null;
                else if (tempObj instanceof java.util.Date
                        && _defaultDateFormat != null) {
                    rowResults[offset - 1] = _defaultDateFormat.format(tempObj);
                } else if (tempObj instanceof String && _spaceTrimOn) {
                    rowResults[offset - 1] = ((String) tempObj).trim();
                } else if (!(tempObj instanceof String) && _convertStringOn) {
                    rowResults[offset - 1] = tempObj.toString();
                } else
                    rowResults[offset - 1] = tempObj;
            }
        }

        return rowResults;
    }


    private Object[] getColumnName(ResultSet results, boolean issueNext)
            throws SQLException {
        Object[] rowResults = null;
        Object tempObj = null;
        ResultSetMetaData resultsMetaData = results.getMetaData();

        boolean rowsAvailable = true;
        if (issueNext) {
            rowsAvailable = results.next();
        }
        if (rowsAvailable) {
            rowResults = new Object[resultsMetaData.getColumnCount()];
            for (int offset = 1; offset <= resultsMetaData.getColumnCount(); offset++) {

            	String colName = resultsMetaData.getColumnName(offset) ;

            	int aa = results.getRow();
                //tempObj = results.getObject(resultsMetaData.getColumnName(offset));
                //if (results.wasNull()) {
            	if ((((colName).trim()).length()) == 0 ){
                    rowResults[offset - 1] = null;
                } else {
                    rowResults[offset - 1] = colName;

                }
            }
        }

        return rowResults; // returns array with the size same as no of columns
    }

    // Returns sql types from java.sql
    private Object[] getColumnType(ResultSet results, boolean issueNext)
    throws SQLException {
    	Object[] rowResults = null;
    	Object tempObj = null;
    	ResultSetMetaData resultsMetaData = results.getMetaData();

    	boolean rowsAvailable = true;
    	if (issueNext) {
    		rowsAvailable = results.next();
    	}
    	if (rowsAvailable) {
    		rowResults = new Object[resultsMetaData.getColumnCount()];
    		for (int offset = 1; offset <= resultsMetaData.getColumnCount(); offset++) {

    			int colType = resultsMetaData.getColumnType(offset) ;

    			int aa = results.getRow();
    			//tempObj = results.getObject(resultsMetaData.getColumnName(offset));
    			//if (results.wasNull()) {
    			//if (colType == 0 ){
    			//	rowResults[offset - 1] = null;
    			//} else {
    				rowResults[offset - 1] = Integer.toString(colType);

    			//}
    		}
    	}

return rowResults; // returns array with the size same as no of columns
}


	public Object[][] executeCallableStatement(String sqlStatement,List hostVariableList) throws SQLException {
		Object[][] queryResults = null;
		ArrayList queryResultList = new ArrayList();
		Object[] rowResults = null;
		Object[] metaResults = null;

		CallableStatement pStmt = null;
		ResultSet results = null;

		try {
			CallableStatement stmt = this.getCallableStatement(sqlStatement, hostVariableList);
			stmt.registerOutParameter(1, oracle.jdbc.OracleTypes.CURSOR);
			stmt.execute();
			results = (ResultSet) stmt.getObject(1);

	        ResultSetMetaData resultsMetaData = results.getMetaData();
			resultsMetaData = results.getMetaData();

			 while (results.next()) {
                rowResults = this.getNextRow(results, false);
                queryResultList.add(rowResults);
            }

			 // add column names into result list
             queryResultList.add(getColumnName(results,false));
             // add Column sql data types at the end
             queryResultList.add(getColumnType(results,false));

            if (queryResultList.size() > 0) {
                queryResults = new Object[queryResultList.size()][resultsMetaData.getColumnCount()];
                queryResults = (Object[][]) queryResultList.toArray(queryResults);
            }


		}  finally {
			DatabaseUtility.close(results, pStmt);
		}

		return queryResults;
	}


    /**
     * Establishes and returns a ResultSet given a query and host variables.
     *
     * @param sqlText
     * @param hostVariableList
     * @return CallableStatement
     * @throws SQLException
     */
    public CallableStatement getCallableStatement(String sqlStatement,
            List hostVariableList) throws SQLException {
        if (sqlStatement == null) {
            throw new IllegalArgumentException("Null sqlText not allowed.");
        }
        if (sqlStatement.equals("")) {
            throw new IllegalArgumentException("Blank sqlText not allowed.");
        }

        CallableStatement statement = null;
        int hostOffset = 1;

        statement = _dbConnection.prepareCall(sqlStatement);

        for (int i = 0; hostVariableList != null && i < hostVariableList.size(); i++) {
            this.setHostVariable(statement, hostOffset, hostVariableList.get(i));
            hostOffset++;
        }

        return statement;
    }


    /**
     * Establishes and returns a ResultSet given a query and host variables.
     *
     * @param sqlText
     * @param hostVar1
     * @param hostVar2
     * @param hostVar3
     * @param hostVar4
     * @param hostVar5
     * @param hostVar6
     * @param hostVar7
     * @param hostVar8
     * @return ResultSet
     * @throws SQLException
     */
    public PreparedStatement getPreparedStatement(String sqlText,
            Object hostVar1, Object hostVar2, Object hostVar3, Object hostVar4,
            Object hostVar5, Object hostVar6, Object hostVar7, Object hostVar8)
            throws SQLException {
        ArrayList list = new ArrayList();

        list.add(this.getVariableObj(hostVar1));
        list.add(this.getVariableObj(hostVar2));
        list.add(this.getVariableObj(hostVar3));
        list.add(this.getVariableObj(hostVar4));
        list.add(this.getVariableObj(hostVar5));
        list.add(this.getVariableObj(hostVar6));
        list.add(this.getVariableObj(hostVar7));
        list.add(this.getVariableObj(hostVar8));

        return this.getPreparedStatement(sqlText, list);
    }

    /**
     * Establishes and returns a ResultSet given a query and host variables.
     *
     * @param sqlText
     * @param hostVar1
     * @param hostVar2
     * @param hostVar3
     * @param hostVar4
     * @param hostVar5
     * @param hostVar6
     * @param hostVar7
     * @return ResultSet
     * @throws SQLException
     */
    public PreparedStatement getPreparedStatement(String sqlText,
            Object hostVar1, Object hostVar2, Object hostVar3, Object hostVar4,
            Object hostVar5, Object hostVar6, Object hostVar7)
            throws SQLException {
        ArrayList list = new ArrayList();

        list.add(this.getVariableObj(hostVar1));
        list.add(this.getVariableObj(hostVar2));
        list.add(this.getVariableObj(hostVar3));
        list.add(this.getVariableObj(hostVar4));
        list.add(this.getVariableObj(hostVar5));
        list.add(this.getVariableObj(hostVar6));
        list.add(this.getVariableObj(hostVar7));

        return this.getPreparedStatement(sqlText, list);
    }

    /**
     * Establishes and returns a ResultSet given a query and host variables.
     *
     * @param sqlText
     * @param hostVar1
     * @param hostVar2
     * @param hostVar3
     * @param hostVar4
     * @param hostVar5
     * @param hostVar6
     * @return ResultSet
     * @throws SQLException
     */
    public PreparedStatement getPreparedStatement(String sqlText,
            Object hostVar1, Object hostVar2, Object hostVar3, Object hostVar4,
            Object hostVar5, Object hostVar6) throws SQLException {
        ArrayList list = new ArrayList();

        list.add(this.getVariableObj(hostVar1));
        list.add(this.getVariableObj(hostVar2));
        list.add(this.getVariableObj(hostVar3));
        list.add(this.getVariableObj(hostVar4));
        list.add(this.getVariableObj(hostVar5));
        list.add(this.getVariableObj(hostVar6));

        return this.getPreparedStatement(sqlText, list);
    }

    /**
     * Establishes and returns a ResultSet given a query and host variables.
     *
     * @param sqlText
     * @param hostVar1
     * @param hostVar2
     * @param hostVar3
     * @param hostVar4
     * @param hostVar5
     * @return ResultSet
     * @throws SQLException
     */
    public PreparedStatement getPreparedStatement(String sqlText,
            Object hostVar1, Object hostVar2, Object hostVar3, Object hostVar4,
            Object hostVar5) throws SQLException {
        ArrayList list = new ArrayList();

        list.add(this.getVariableObj(hostVar1));
        list.add(this.getVariableObj(hostVar2));
        list.add(this.getVariableObj(hostVar3));
        list.add(this.getVariableObj(hostVar4));
        list.add(this.getVariableObj(hostVar5));

        return this.getPreparedStatement(sqlText, list);
    }

    /**
     * Establishes and returns a ResultSet given a query and host variables.
     *
     * @param sqlText
     * @param hostVar1
     * @param hostVar2
     * @param hostVar3
     * @param hostVar4
     * @return ResultSet
     * @throws SQLException
     */
    public PreparedStatement getPreparedStatement(String sqlText,
            Object hostVar1, Object hostVar2, Object hostVar3, Object hostVar4)
            throws SQLException {
        ArrayList list = new ArrayList();

        list.add(this.getVariableObj(hostVar1));
        list.add(this.getVariableObj(hostVar2));
        list.add(this.getVariableObj(hostVar3));
        list.add(this.getVariableObj(hostVar4));

        return this.getPreparedStatement(sqlText, list);
    }

    /**
     * Establishes and returns a ResultSet given a query and host variables.
     *
     * @param sqlText
     * @param hostVar1
     * @param hostVar2
     * @param hostVar3
     * @return ResultSet
     * @throws SQLException
     */
    public PreparedStatement getPreparedStatement(String sqlText,
            Object hostVar1, Object hostVar2, Object hostVar3)
            throws SQLException {
        ArrayList list = new ArrayList();

        list.add(this.getVariableObj(hostVar1));
        list.add(this.getVariableObj(hostVar2));
        list.add(this.getVariableObj(hostVar3));

        return this.getPreparedStatement(sqlText, list);
    }

    /**
     * Establishes and returns a ResultSet given a query and host variables.
     *
     * @param sqlText
     * @param hostVar1
     * @param hostVar2
     * @return ResultSet
     * @throws SQLException
     */
    public PreparedStatement getPreparedStatement(String sqlText,
            Object hostVar1, Object hostVar2) throws SQLException {
        ArrayList list = new ArrayList();

        list.add(this.getVariableObj(hostVar1));
        list.add(this.getVariableObj(hostVar2));

        return this.getPreparedStatement(sqlText, list);
    }

    /**
     * Establishes and returns a ResultSet given a query and host variables.
     *
     * @param sqlText
     * @param hostVar1
     * @return ResultSet
     * @throws SQLException
     */
    public PreparedStatement getPreparedStatement(String sqlText,
            Object hostVar1) throws SQLException {
        ArrayList list = new ArrayList();

        list.add(this.getVariableObj(hostVar1));

        return this.getPreparedStatement(sqlText, list);
    }

    /**
     * Establishes and returns a ResultSet given a query and host variables.
     *
     * @param sqlText
     * @return ResultSet
     * @throws SQLException
     */
    public PreparedStatement getPreparedStatement(String sqlText)
            throws SQLException {
        ArrayList list = new ArrayList();

        return this.getPreparedStatement(sqlText, list);
    }

    /**
     * Establishes and returns a ResultSet given a query and host variables.
     *
     * @param sqlText
     * @param hostVar1
     * @param hostVar2
     * @param hostVar3
     * @param hostVar4
     * @param hostVar5
     * @param hostVar6
     * @param hostVar7
     * @param hostVar8
     * @param hostVar9
     * @return ResultSet
     * @throws SQLException
     */
    public PreparedStatement getPreparedStatement(String sqlText,
            Object hostVar1, Object hostVar2, Object hostVar3, Object hostVar4,
            Object hostVar5, Object hostVar6, Object hostVar7, Object hostVar8,
            Object hostVar9) throws SQLException {
        ArrayList list = new ArrayList();

        list.add(this.getVariableObj(hostVar1));
        list.add(this.getVariableObj(hostVar2));
        list.add(this.getVariableObj(hostVar3));
        list.add(this.getVariableObj(hostVar4));
        list.add(this.getVariableObj(hostVar5));

        list.add(this.getVariableObj(hostVar6));
        list.add(this.getVariableObj(hostVar7));
        list.add(this.getVariableObj(hostVar8));
        list.add(this.getVariableObj(hostVar9));

        return this.getPreparedStatement(sqlText, list);
    }

    /**
     * Establishes and returns a ResultSet given a query and host variables.
     *
     * @param sqlText
     * @param hostVariableList
     * @return PreparedStatement
     * @throws SQLException
     */
    public PreparedStatement getPreparedStatement(String sqlText,
            List hostVariableList) throws SQLException {
        if (sqlText == null) {
            throw new IllegalArgumentException("Null sqlText not allowed.");
        }
        if (sqlText.equals("")) {
            throw new IllegalArgumentException("Blank sqlText not allowed.");
        }

        PreparedStatement pStmt = null;
        int hostOffset = 1;

        pStmt = _dbConnection.prepareStatement(sqlText);

        for (int i = 0; hostVariableList != null && i < hostVariableList.size(); i++) {
            this.setHostVariable(pStmt, hostOffset, hostVariableList.get(i));
            hostOffset++;
        }

        return pStmt;
    }

    private void setHostVariable(PreparedStatement pStmt, int hostOffset,
            Object hostVar) throws SQLException {
        if (hostVar == null) {
            pStmt.setNull(hostOffset, Types.JAVA_OBJECT);
        } else if (hostVar.equals(NULL)) {
            pStmt.setNull(hostOffset, Types.JAVA_OBJECT);
        } else {
            Object localHostVar = hostVar;

            if (localHostVar instanceof java.util.Date
                    && !(localHostVar instanceof java.sql.Date || localHostVar instanceof java.sql.Timestamp)) {
                localHostVar = new java.sql.Timestamp(
                        ((java.util.Date) localHostVar).getTime());
            }
            pStmt.setObject(hostOffset, localHostVar);
        }
    }

    private Object getVariableObj(Object value) {
        if (value == null)
            return NULL;
       return value;
    }

    public void setConnection(Connection conn) {
        _dbConnection = conn;
    }

    public Connection getConnection() {
        return _dbConnection;
    }

    /**
     * @return
     */
    public SimpleDateFormat getDefaultDateFormat() {
        return _defaultDateFormat;
    }

    /**
     * @return
     */
    public DecimalFormat getDefaultDecimalFormat() {
        return _defaultDecimalFormat;
    }

    /**
     * @param format
     */
    public void setDefaultDateFormat(SimpleDateFormat format) {
        _defaultDateFormat = format;
    }

    /**
     * @param format
     */
    public void setDefaultDecimalFormat(DecimalFormat format) {
        _defaultDecimalFormat = format;
    }

    /**
     * @return
     */
    public boolean isSpaceTrimOn() {
        return _spaceTrimOn;
    }

    /**
     * @param b
     */
    public void setSpaceTrimOn(boolean b) {
        _spaceTrimOn = b;
    }

    /**
     * @return
     */
    public boolean isConvertStringOn() {
        return _convertStringOn;
    }

    /**
     * @param b
     */
    public void setConvertStringOn(boolean b) {
        _convertStringOn = b;
    }
}