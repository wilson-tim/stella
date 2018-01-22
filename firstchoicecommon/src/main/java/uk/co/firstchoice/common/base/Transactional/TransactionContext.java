package uk.co.firstchoice.common.base.Transactional;

import java.sql.Connection;

/**
 * Abstracts transaction control so that classes can be deployment independent.
 * Transaction control for J2EE applications differ from normal Java
 * applications. Consequently it difficult to write applications that can either
 * work with or without a J2EE container.
 * 
 * <p>
 * TransactionContext decouples an application from the way transaction control
 * is managed. Most classes using a TransactionContext for transaction control
 * will be able to function outside a container as they do inside a container.
 * 
 * <p>
 * This interface is meant to be used in conjunction with class
 * BusinessLogicObject from package uk.co.firstchoice.base.
 * 
 *  
 */
public interface TransactionContext {

	/**
	 * Provides a JDBC connection given an identifying label. For applications
	 * running under a J2EE container, this label is typically your database
	 * connection pool name.
	 * 
	 * <p>
	 * If a connection has previously been created by this transaction context
	 * for this identifying label, that connection will be returned. A new
	 * connection will not be created. If you explicitly want a new connection,
	 * use getNewConnection().
	 * 
	 * <p>
	 * It is imperitive that you don't issue commits, rollbacks, or close
	 * connections received from a TransactionContext. Those tasks should be
	 * done via the TransactionContext.
	 * 
	 * @param label
	 * @return connection
	 * @throws TransactionException
	 */
	public Connection getConnection(String label) throws TransactionException;

	/**
	 * Provides a JDBC connection given an identifying label. For applications
	 * running under a J2EE container, this label is typically your database
	 * connection pool name.
	 * 
	 * <p>
	 * A new connection will be created and returned regardless of other
	 * connections previously created by this context.
	 * 
	 * <p>
	 * It is imperitive that you don't issue commits, rollbacks, or close
	 * connections received from a TransactionContext. Those tasks should be
	 * done via the TransactionContext.
	 * 
	 * @param label
	 * @return
	 * @throws TransactionException
	 */
	public Connection getNewConnection(String label)
			throws TransactionException;

	/**
	 * Commits your JTA transaction (or all JDBC transactions) in progress.
	 * 
	 * @throws TransactionException
	 */
	public void commitAll() throws TransactionException;

	/**
	 * Issues rollbacks for your JTA transaction (or all JDBC transactions) in
	 * progress.
	 * 
	 * @throws TransactionException
	 */
	public void rollbackAll() throws TransactionException;

	/**
	 * Demarks the beginning of a transaction.
	 * 
	 * @throws TransactionException
	 */
	public void begin() throws TransactionException;

	/**
	 * Closes all associated database connections.
	 * 
	 * @throws TransactionException
	 */
	public void closeAll() throws TransactionException;
}