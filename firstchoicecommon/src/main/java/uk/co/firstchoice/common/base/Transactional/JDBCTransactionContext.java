package uk.co.firstchoice.common.base.Transactional;

import java.sql.Connection;
import java.util.HashMap;

import uk.co.firstchoice.common.base.jdbc.DatabaseUtility;
import uk.co.firstchoice.common.base.jdbc.JDBCConnectionSpecification;

/**
 * Implementation of a TransactionContext suitable for applications running
 * outside a J2EE container. This is also usable to locally debug classes
 * typically running in a J2EE container that manage transactions.
 * 
 *  
 */
public class JDBCTransactionContext extends AbstractTransactionContext
		implements TransactionContext {
	public JDBCTransactionContext(String label, String driverName,
			String connectString, String dbUserID, String password) {
		this.registerConnectiontype(label, driverName, connectString, dbUserID,
				password);
	}

	public Connection getNewConnection(String label)
			throws TransactionException {
		if (label == null)
			throw new IllegalArgumentException("Null label not allowed.");
		if (!_connectionTypes.containsKey(label))
			throw new TransactionException("Connection Type \"" + label
					+ "\" not found.");
		Connection conn = null;
		JDBCConnectionSpecification spec = (JDBCConnectionSpecification) _connectionTypes
				.get(label);

		try {
			conn = DatabaseUtility.getJDBCConnection(spec.getDriverName(), spec
					.getConnectString(), spec.getUserId(), spec.getPassword());
			conn.setAutoCommit(false);
			this.registerAllocatedConnection(label, conn);
		} catch (Throwable t) {
			throw new TransactionException("Error creating JDBC connection "
					+ label, t);
		}

		return conn;
	}

	public void registerConnectiontype(String label, String driverName,
			String connectString, String dbUserID, String password) {
		if (label == null)
			throw new IllegalArgumentException("Null label not allowed.");
		_connectionTypes.put(label, new JDBCConnectionSpecification(driverName,
				connectString, dbUserID, password));
	}

	private HashMap _connectionTypes = new HashMap();

}