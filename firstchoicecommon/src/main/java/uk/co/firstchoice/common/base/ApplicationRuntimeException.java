package uk.co.firstchoice.common.base;

import java.io.PrintStream;
import java.io.PrintWriter;
import java.io.Serializable;

import uk.co.firstchoice.common.base.util.CommonCharacter;
import uk.co.firstchoice.common.base.util.ExceptionUtility;

/**
 * Provides a generic runtime exception for applications that will retain and
 * provide information about the underlying exception. <BR>
 * 
 * Note that java.lang.ThreadDeath should not be "converted" into another
 * exception. If ThreadDeath is passed on construction, it will be re-thrown.
 * 
 * <p>
 * An example implementation follows: <blockquote>
 * 
 * <pre>
 * public class MyException extends ApplicationRuntimeException {
 *     protected MyException() {
 *     }
 * 
 *     public MyException(String message) {
 *         super(message);
 *     }
 * 
 *     public MyException(String message, Throwable t) {
 *         super(message, t);
 *     }
 * }
 * </pre>
 * 
 * </blockquote>
 * 
 * <p>.
 */
public abstract class ApplicationRuntimeException extends RuntimeException
        implements Serializable {

    protected ApplicationRuntimeException() {
    }

    protected ApplicationRuntimeException(String message) {
        super(message);
    }

    protected ApplicationRuntimeException(String message, Throwable t) {
        super(message);

        if (t instanceof ThreadDeath)
            throw (ThreadDeath) t;

        _baseThrowableStackTrace = ExceptionUtility.getStackTraceAsString(t);
        _baseThrowable = t;

    }

    public void printStackTrace(PrintWriter s) {
        super.printStackTrace(s);

        if (_baseThrowableStackTrace != null) {
            s.print(CommonCharacter.LINE_FEED);
            s.print("Underlying Throwable stack trace follows:");
            s.print(CommonCharacter.LINE_FEED);
            s.print(_baseThrowableStackTrace);
        }
    }

    public void printStackTrace() {
        this.printStackTrace(System.err);
    }

    public void printStackTrace(PrintStream s) {
        super.printStackTrace(s);

        if (_baseThrowableStackTrace != null) {
            s.print(CommonCharacter.LINE_FEED);
            s.print("Underlying Throwable stack trace follows:");
            s.print(CommonCharacter.LINE_FEED);
            s.print(_baseThrowableStackTrace);
        }
    }

    public Throwable getBaseThrowable() {
        return _baseThrowable;
    }

    private String _baseThrowableStackTrace = null;

    private transient Throwable _baseThrowable = null;
}