package uk.co.firstchoice.common.base.jdbc;

import uk.co.firstchoice.common.base.ApplicationRuntimeException;

/**
 * This exception is used mainly within Data Access Objects (DAOs) where data
 * for a query or update isn't found. Often it's good to differentiate these
 * types of errors from other types of SQLExceptions or system errors as it may
 * be treated differently depending on context.
 * 
 * <p>.
 */
public class DataNotFoundException extends ApplicationRuntimeException {

	protected DataNotFoundException() {
	}

	public DataNotFoundException(String message) {
		super(message);
	}

	public DataNotFoundException(String message, Throwable t) {
		super(message, t);
	}

}