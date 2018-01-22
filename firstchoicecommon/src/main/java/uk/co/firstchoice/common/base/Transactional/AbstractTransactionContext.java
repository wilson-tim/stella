package uk.co.firstchoice.common.base.Transactional;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

import uk.co.firstchoice.common.base.jdbc.DatabaseUtility;

public abstract class AbstractTransactionContext implements TransactionContext {

	private ArrayList _allocatedConnection = new ArrayList();

	private HashMap _labelConnectionMap = new HashMap();

	public Connection getConnection(String label) throws TransactionException {
		if (label == null)
			throw new IllegalArgumentException("Null label not allowed.");
		Connection conn = this.getRegisteredConnection(label);
		if (conn == null)
			conn = this.getNewConnection(label);

		return conn;
	}

	public abstract Connection getNewConnection(String label)
			throws TransactionException;

	public void commitAll() throws TransactionException {
		Connection conn = null;
		TransactionException exp = null;

		for (int i = 0; i < _allocatedConnection.size(); i++) {
			conn = (Connection) _allocatedConnection.get(i);
			try {
				conn.commit();
			} catch (SQLException s) {
				if (exp == null)
					exp = new TransactionException("Commit problem reported", s);
			}
		}
		if (exp != null)
			throw exp;
	}

	public void rollbackAll() throws TransactionException {
		Connection conn = null;
		TransactionException exp = null;

		for (int i = 0; i < _allocatedConnection.size(); i++) {
			conn = (Connection) _allocatedConnection.get(i);
			try {
				conn.rollback();
			} catch (SQLException s) {
				if (exp == null)
					exp = new TransactionException("Commit problem reported", s);
			}
		}
		if (exp != null)
			throw exp;
	}

	public void begin() throws TransactionException {
		/** @todo: Implement this org.cementj.context.TransactionContext method */
		//throw new java.lang.UnsupportedOperationException("Method begin() not
		// yet implemented.");
	}

	public void closeAll() throws TransactionException {
		for (int i = _allocatedConnection.size() - 1; i >= 0; i--) {
			DatabaseUtility.close((Connection) _allocatedConnection.get(i));
		}
		_allocatedConnection = new ArrayList();
		_labelConnectionMap = new HashMap();
	}

	protected void registerAllocatedConnection(String label, Connection conn) {
		if (conn != null && label != null) {
			_allocatedConnection.add(conn);
			_labelConnectionMap.put(label, conn);
		}
	}

	protected Connection getRegisteredConnection(String label) {
		Connection conn = null;
		if (label != null && _labelConnectionMap.containsKey(label)) {
			conn = (Connection) _labelConnectionMap.get(label);
		}
		return conn;
	}
}