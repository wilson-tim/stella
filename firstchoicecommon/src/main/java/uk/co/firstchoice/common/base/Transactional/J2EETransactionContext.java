package uk.co.firstchoice.common.base.Transactional;

import java.sql.Connection;

import javax.ejb.EJBContext;

import uk.co.firstchoice.common.base.util.J2EEUtility;

/**
 * Implementation of a TransactionContext suitable for applications running
 * inside a J2EE container. This is also usable to locally debug classes
 * typically running in a J2EE container that manage transactions.
 * 
 *  
 */
public class J2EETransactionContext extends AbstractTransactionContext
		implements TransactionContext {
	protected J2EETransactionContext() {
	}

	public J2EETransactionContext(EJBContext sessionContext) {
		if (sessionContext == null)
			throw new IllegalArgumentException("Null context not allowed.");
		_sessionContext = sessionContext;
	}

	public Connection getNewConnection(String connectPoolName)
			throws TransactionException {
		if (connectPoolName == null)
			throw new IllegalArgumentException(
					"Null connectPoolName not allowed.");
		if (connectPoolName.equals(""))
			throw new IllegalArgumentException(
					"Blank connectPoolName not allowed.");

		Connection conn = null;

		try {
			conn = J2EEUtility.getConnection(connectPoolName);
			;
		} catch (Throwable t) {
			throw new TransactionException(
					"Connection creation filed for pool " + connectPoolName, t);
		}
		return conn;
	}

	public void commitAll() throws TransactionException {
		try {
			_sessionContext.getUserTransaction().commit();
		} catch (Throwable t) {
			throw new TransactionException("Commit if transaction failed.", t);
		}
	}

	public void rollbackAll() throws TransactionException {
		try {
			_sessionContext.getUserTransaction().rollback();
		} catch (Throwable t) {
			throw new TransactionException("Begin if transaction failed.", t);
		}
	}

	public void begin() throws TransactionException {
		try {
			_sessionContext.getUserTransaction().begin();
		} catch (Throwable t) {
			throw new TransactionException("Begin if transaction failed.", t);
		}
	}

	private EJBContext _sessionContext = null;

}