package uk.co.firstchoice.common.base;

/**
 * This exception is targeted toward application developers, administrators, and
 * support personnel. It represents an abnormal application error that can't
 * meaningfully be interpreted by end-users.
 * 
 *  
 */
public class InternalApplicationException extends ApplicationRuntimeException {
    protected InternalApplicationException() {
    }

    public InternalApplicationException(String message) {
        super(message);
    }

    public InternalApplicationException(String message, Throwable t) {
        super(message, t);
    }
}