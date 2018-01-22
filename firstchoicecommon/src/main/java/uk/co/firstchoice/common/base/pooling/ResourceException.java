package uk.co.firstchoice.common.base.pooling;

import uk.co.firstchoice.common.base.ApplicationRuntimeException;

/**
 * Exception for defined resources.
 *  
 */
public class ResourceException extends ApplicationRuntimeException {
	public ResourceException() {
		super();
	}

	public ResourceException(String message) {
		super(message);
	}

	public ResourceException(String message, Throwable t) {
		super(message, t);
	}
}

