package uk.co.firstchoice.common.base.Transactional;

import uk.co.firstchoice.common.base.ApplicationRuntimeException;

/**
 * Exception for TransactionContext errors.
 */
public class TransactionException extends ApplicationRuntimeException {
	protected TransactionException() {
		super();
	}

	public TransactionException(String message) {
		super(message);
	}

	public TransactionException(String message, Throwable t) {
		super(message, t);
	}
}