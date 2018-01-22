package uk.co.firstchoice.common.base.util;

import java.sql.Connection;
import java.sql.SQLException;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.rmi.PortableRemoteObject;
import javax.sql.DataSource;
import javax.transaction.SystemException;
import javax.transaction.UserTransaction;

import uk.co.firstchoice.common.base.logging.LogManager;

/**
 * Collection of J2EE Utility functions.
 *
 *
 */
public class J2EEUtility {

	protected J2EEUtility() {
	}

	/**
	 * Convenience one-liner to obtain a database connection from a given
	 * connection pool.
	 *
	 * @param connectionPoolName
	 * @return dbConnection
	 * @throws NamingException
	 * @throws SQLException
	 */
	public static Connection getConnection(String connectionPoolName)
			throws NamingException, SQLException {
		if (connectionPoolName == null)
			throw new IllegalArgumentException(
					"Null connectionPoolName not allowed.");
		if (connectionPoolName.equals(""))
			throw new IllegalArgumentException(
					"Blank connectionPoolName not allowed.");

		DataSource source = (DataSource) J2EEUtility.getJndiResource(
				connectionPoolName, DataSource.class);
		return source.getConnection();
	}

	/**
	 * Looks up a JNDI Resource
	 *
	 * @param resourceName
	 * @param resourceClass
	 * @return
	 * @throws NamingException
	 */
	public static Object getJndiResource(String resourceName,
			Class resourceClass) throws NamingException {
		return J2EEUtility.getJndiResource(new InitialContext(), resourceName,
				resourceClass);
	}

	/**
	 * Looks up a JNDI Reference given a context.
	 *
	 * @param ctx
	 * @param resourceName
	 * @param resourceClass
	 * @return
	 * @throws NamingException
	 */
	public static Object getJndiResource(Context ctx, String resourceName,
			Class resourceClass) throws NamingException {
		return PortableRemoteObject.narrow(ctx.lookup(resourceName),
				resourceClass);
	}

	/**
	 * Convenience one-liner to obtain current UserTransaction from the j2ee
	 * container.
	 *
	 * @return userTransaction
	 * @throws NamingException
	 */
	public static UserTransaction getUserTransaction() throws NamingException {
		Context ctx = new InitialContext();
		Object obj = ctx.lookup("java:comp/UserTransaction");
		if (obj == null)
			throw new IllegalStateException("User Transaction not available.");
		return (UserTransaction) obj;
	}

	/**
	 * Convenience one-liner to rollback the given UserTransaction logging a
	 * warning if an error is received.
	 *
	 * @param transaction
	 */
	public static void rollback(UserTransaction transaction) {
		if (transaction == null)
			return;
		try {
			transaction.rollback();
		} catch (SystemException se) {
			LogManager.getLogger().logWarning(
					"Rollback close error", se);
		}
	}
}