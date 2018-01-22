/*
 * ComUtils.java
 *
 * Created on 08 May 2003, 14:48
 */

package uk.co.firstchoice.fcutil;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

/**
 * 
 * @author Lsvensson
 */
public class ComUtils {

	String jndiName = null;

	Connection conn = null;

	CallableStatement cstmt = null;

	PreparedStatement pstmt = null;

	Statement stmt = null;

	/** Creates a new instance of ComUtils */
	public ComUtils(String jName) throws FCException {

		try {
			jndiName = jName;
			Context ctx = new InitialContext();
			DataSource ds = (DataSource) ctx.lookup(jndiName);
			conn = ds.getConnection();
		} catch (SQLException e) {
			throw new FCException("Error connecting to Oracle (" + jndiName
					+ "). Try restarting the application.", e);
		} catch (NamingException e) {
			throw new FCException("Error connecting to Oracle (" + jndiName
					+ "). Try restarting the application.", e);
		}
	}

	public Connection getConnection() {
		return conn;
	}

	public CallableStatement getCallableStatement(String stmt)
			throws FCException {
		try {
			if (cstmt != null)
				cstmt.close();
			cstmt = conn.prepareCall(stmt);
			if (cstmt == null)
				throw new SQLException(
						"Newly created CallableStatement is null");
			return cstmt;
		} catch (SQLException e) {
			throw new FCException(
					"Error communicating with Oracle (cstmt). Try restarting the application. ("
							+ e.getMessage() + ")", e);
		}
	}

	public PreparedStatement getPreparedStatement(String stmt)
			throws FCException {
		try {
			if (pstmt != null)
				pstmt.close();
			pstmt = conn.prepareStatement(stmt);
			if (pstmt == null)
				throw new SQLException(
						"Newly created PreparedStatement is null");
			return pstmt;
		} catch (SQLException e) {
			throw new FCException(
					"Error communicating with Oracle (pstmt). Try restarting the application. ("
							+ e.getMessage() + ")", e);
		}
	}

	public Statement getStatement() throws FCException {
		try {
			if (stmt != null)
				stmt.close();
			stmt = conn.createStatement();
			if (stmt == null)
				throw new SQLException(
						"Newly created CallableStatement is null");
			return stmt;
		} catch (SQLException e) {
			throw new FCException(
					"Error communicating with Oracle (stmt). Try restarting the application. ("
							+ e.getMessage() + ")", e);
		}
	}

	public void close() {
		try {
			if (cstmt != null) {
				cstmt.close();
				cstmt = null;
			}
			if (pstmt != null) {
				pstmt.close();
				pstmt = null;
			}
			if (stmt != null) {
				stmt.close();
				stmt = null;
			}
			if (conn != null) {
				conn.close();
				conn = null;
			}
		} catch (SQLException e) {
		}
	}

	protected void finalize() throws Throwable {
		close();
	}

}