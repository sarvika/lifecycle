package io.sarvika.lifecycle;

import java.util.EventObject;

public final class LifecycleEvent extends EventObject {

    private static final long serialVersionUID = 1L;

    // ----------------------------------------------------------- Constructors
    /** The event data associated with this event. */
    private Object data = null;

    // ----------------------------------------------------- Instance Variables
    /** The event type this instance represents. */
    private String type = null;

    /**
     * Construct a new LifecycleEvent with the specified parameters.
     *
     * @param lifecycle Component on which this event occurred
     * @param type Event type (required)
     * @param data Event data (if any)
     */
    public LifecycleEvent(Lifecycle lifecycle, String type, Object data) {

        super(lifecycle);
        this.type = type;
        this.data = data;
    }

    // ------------------------------------------------------------- Properties

    /** Return the event data of this event. */
    public Object getData() {

        return (this.data);
    }

    /** Return the Lifecycle on which this event occurred. */
    public Lifecycle getLifecycle() {

        return (Lifecycle) getSource();
    }

    /** Return the event type of this event. */
    public String getType() {

        return (this.type);
    }
}
