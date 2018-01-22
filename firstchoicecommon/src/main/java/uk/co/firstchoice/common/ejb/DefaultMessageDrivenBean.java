package uk.co.firstchoice.common.ejb;

import javax.ejb.EJBException;
import javax.ejb.MessageDrivenBean;
import javax.ejb.MessageDrivenContext;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.transaction.UserTransaction;

/**
 * Default implementation for message driven bean. This class will do the
 * mundane mechanics of tracking context and implement several methods most
 * developers stump anyway. All you have to do is extend this class, implement
 * onMessage() and deploy.
 * 
 *  
 */
public abstract class DefaultMessageDrivenBean implements MessageDrivenBean,
		MessageListener {

	public DefaultMessageDrivenBean() {
	}

	/**
	 * Records message driven context provided by the J2EE container.
	 * 
	 * @param context
	 * @throws EJBException
	 */
	public void setMessageDrivenContext(MessageDrivenContext context)
			throws javax.ejb.EJBException {
		_messageDrivenContext = context;
	}

	/**
	 * Hollow implementation to satisfy MessageDrivenBean interface. Override it
	 * you actually want something to happen here.
	 * 
	 * @throws EJBException
	 */
	public void ejbRemove() throws javax.ejb.EJBException {
	}

	/**
	 * Obtains the UserTransaction from the container-provided message-driven
	 * context. This will generate an IllegalStateException if the
	 * UserTransaction isn't obtainable.
	 * 
	 * @return UserTransaction
	 */
	public UserTransaction getUserTransaction() {
		UserTransaction ut = _messageDrivenContext.getUserTransaction();
		if (ut == null)
			throw new IllegalStateException(
					"Message Driven bean not deployed with UserTransaction.");
		return ut;
	}

	/**
	 * Returns the container-provided message-driven context.
	 * 
	 * @return MessageDrivenContext
	 */
	public MessageDrivenContext getMessageDrivenContext() {
		return _messageDrivenContext;
	}

	/**
	 * You must implement processing for messages for a MessageDrivenBean to be
	 * usedful.
	 * 
	 * @param message
	 */
	public abstract void onMessage(Message message);

	protected MessageDrivenContext _messageDrivenContext = null;
}