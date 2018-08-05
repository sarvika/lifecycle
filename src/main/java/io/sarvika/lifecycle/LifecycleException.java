package io.sarvika.lifecycle;

public class LifecycleException extends Exception {

    private static final long serialVersionUID = -5066698780902949480L;

    // ------------------------------------------------------------ Constructors

    /** Construct a new LifecycleException with no other information. */
    public LifecycleException() {
        super();
    }

    /**
     * Construct a new LifecycleException for the specified message.
     *
     * @param message Message describing this exception
     */
    public LifecycleException(String message) {
        super(message);
    }

    /**
     * Construct a new CriticalException for the specified throwable.
     *
     * @param throwable Throwable that caused this exception
     */
    public LifecycleException(Throwable throwable) {
        super(throwable);
    }

    /**
     * Construct a new LifecycleException for the specified message and throwable.
     *
     * @param message Message describing this exception
     * @param throwable Throwable that caused this exception
     */
    public LifecycleException(String message, Throwable throwable) {
        super(message, throwable);
    }
}
