package uk.co.firstchoice.common.base;

/**
 * This exception is targeted toward improper usage by end-users or client
 * callers in the case of APIs. Typically these are not severe system errors or
 * the intentional result of bug reports. Typically, the message is displayed to
 * the end-user.
 * 
 *  
 */
public class ImproperUsageException extends ApplicationRuntimeException {

    protected ImproperUsageException() {
    }

    public ImproperUsageException(String message) {
        super(message);
    }

    public ImproperUsageException(String message, Throwable t) {
        super(message, t);
    }

}