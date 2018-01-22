package uk.co.firstchoice.common.ejb.util;

import javax.jms.Message;

import uk.co.firstchoice.common.base.logging.LogManager;

/**
 * Collection of JMS Utility functions.
 *
 *
 */
public class JMSUtility {

	protected JMSUtility() {
	}

	/**
	 * Convenience one-liner to acknowledge a JMS message.
	 *
	 * @param message
	 */
	public static void acknowledgeMessage(Message message) {
		if (message != null) {
			try {
				message.acknowledge();
			} // to prevent poison message.
			catch (Throwable t) {
				LogManager.getLogger().logError(
						"Error acknowledging JMS message", t);
			}
		}
	}
}