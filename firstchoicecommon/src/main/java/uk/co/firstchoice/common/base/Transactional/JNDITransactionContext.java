package uk.co.firstchoice.common.base.Transactional;

import java.sql.Connection;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;

/**
 * JNDI-based TransactionContext for use in J2EE containers without needing EJB.
 * 
 * <p>
 * The default environment context name is "java:/comp/env".
 *  
 */
public class JNDITransactionContext extends AbstractTransactionContext
		implements TransactionContext {
	private String _environmentContextName = "java:/comp/env";

	public JNDITransactionContext() {
	}

	public JNDITransactionContext(String envContextName) {
		this.setEnvironmentContextName(envContextName);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see uk.co.firstchoice.base.trans.TransactionContext#getNewConnection(java.lang.String)
	 */
	public Connection getNewConnection(String label)
			throws TransactionException {
		Connection conn = null;

		try {
			Context initContext = new InitialContext();
			Context envContext = (Context) initContext
					.lookup(_environmentContextName);

			DataSource ds = (DataSource) envContext.lookup(label);
			conn = ds.getConnection();
		} catch (Throwable t) {
			throw new TransactionException("Error creating new Connection: "
					+ label, t);
		}

		return conn;
	}

	/**
	 * @return
	 */
	public String getEnvironmentContextName() {
		return _environmentContextName;
	}

	/**
	 * @param string
	 */
	public void setEnvironmentContextName(String string) {
		_environmentContextName = string;
	}

}