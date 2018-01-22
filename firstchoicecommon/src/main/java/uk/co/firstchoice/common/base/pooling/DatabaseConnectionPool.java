package uk.co.firstchoice.common.base.pooling;

import java.sql.Connection;
import java.util.Properties;

/**
 * This is an implementation of a ResourcePool for JDBC database connections.
 * <p>
 * Required properties are the following:
 * <li>conn.test.sql -- Test SQL select statement</li>
 * <li>conn.driver -- JDBC Driver name used to create connections.</li>
 * <li>conn.connect -- Connection string used to create connections.</li>
 * <li>conn.user -- Database userid.</li>
 * <li>conn.password -- Database password.</li>
 *  
 */

public class DatabaseConnectionPool extends AbstractResourcePool {

	public DatabaseConnectionPool(String poolName, Properties creationProps)
			throws ResourceException {
		super(poolName, creationProps);
	}

	public DatabaseConnectionPool(String poolName, Properties creationProps,
			int poolSize) throws ResourceException {
		super(poolName, creationProps, poolSize);
	}

	public DatabaseConnectionPool(String poolName, Properties creationProps,
			int poolSize, long sleepIntervalInMillis) throws ResourceException {
		super(poolName, creationProps, poolSize, sleepIntervalInMillis);
	}

	/**
	 * Creates a new PooledConnection for the pool. This is required.
	 */
	public Resource createResource(Properties props) throws ResourceException {
		Resource resource = null;
		String version = System.getProperty("java.version");

		try {
			if (version.startsWith("1.0") || version.startsWith("1.1")
					|| version.startsWith("1.2") || version.startsWith("1.3"))
				resource = (Resource) Class
						.forName(
								"uk.co.firstchoice.common.base.pooling.support.PooledConnection12")
						.newInstance();
			else
				resource = (Resource) Class
						.forName(
								"uk.co.firstchoice.common.base.pooling.support.PooledConnection14")
						.newInstance();
		} catch (Throwable t) {
			throw new ResourceException("Error creating connection", t);
		}
		return resource;
	}

	/**
	 * Gets a Pooled Connection from the Resouce Pool. This method is
	 * unnecessary as the calling class could do the same thing. This just makes
	 * it easier on the calling class as it doesn't have to do as much in the
	 * way of casting.
	 */
	public Connection getPooledConnection() throws ResourceException {
		Connection conn = null;
		conn = (Connection) this.getResource();
		return conn;
	}

}