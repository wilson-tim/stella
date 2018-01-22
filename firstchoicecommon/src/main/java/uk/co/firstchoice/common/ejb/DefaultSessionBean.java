package uk.co.firstchoice.common.ejb;

import java.rmi.RemoteException;

import javax.ejb.EJBException;
import javax.ejb.SessionBean;
import javax.ejb.SessionContext;
import javax.transaction.UserTransaction;

/**
 * Default implementation for session bean. This class will do the mundane
 * mechanics of tracking context. It also provides default implementations for
 * ejbRemove(), ejbActivate(), and ejbPassivate(), which most developers stump
 * anyway.
 * 
 *  
 */
public class DefaultSessionBean implements SessionBean {

	public DefaultSessionBean() {
	}

	/**
	 * Records session context provided by the J2EE container.
	 * 
	 * @throws EJBException
	 * @throws RemoteException
	 */
	public void setSessionContext(SessionContext context)
			throws javax.ejb.EJBException, java.rmi.RemoteException {
		_sessionContext = context;
	}

	/**
	 * Hollow implementation to satisfy SessionBean interface. Override it you
	 * actually want something to happen here.
	 * 
	 * @throws EJBException
	 * @throws RemoteException
	 */
	public void ejbRemove() throws javax.ejb.EJBException,
			java.rmi.RemoteException {
	}

	/**
	 * Hollow implementation to satisfy SessionBean interface. Override it you
	 * actually want something to happen here.
	 * 
	 * @throws EJBException
	 * @throws RemoteException
	 */
	public void ejbActivate() throws javax.ejb.EJBException,
			java.rmi.RemoteException {
	}

	/**
	 * Hollow implementation to satisfy SessionBean interface. Override it you
	 * actually want something to happen here.
	 * 
	 * @throws EJBException
	 * @throws RemoteException
	 */
	public void ejbPassivate() throws javax.ejb.EJBException,
			java.rmi.RemoteException {
	}

	/**
	 * Returns the container-provided session context.
	 * 
	 * @return SessionContext
	 */
	public SessionContext getSessionContext() {
		return _sessionContext;
	}

	/**
	 * Obtains the UserTransaction from the container-provided session context.
	 * This will generate an IllegalStateException if the UserTransaction isn't
	 * obtainable.
	 * 
	 * @return UserTransaction
	 */
	public UserTransaction getUserTransaction() {
		UserTransaction ut = _sessionContext.getUserTransaction();
		if (ut == null)
			throw new IllegalStateException(
					"Session bean not deployed with UserTransaction.");
		return ut;
	}

	protected SessionContext _sessionContext = null;
}