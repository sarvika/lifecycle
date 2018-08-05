package io.sarvika.lifecycle.util;

import io.sarvika.lifecycle.Lifecycle;
import io.sarvika.lifecycle.LifecycleEvent;
import io.sarvika.lifecycle.LifecycleListener;

public final class LifecycleSupport {

    private final Object lockingObject = new Object();
    private Lifecycle lifecycle;

    private LifecycleListener[] listeners = new LifecycleListener[0];

    public LifecycleSupport(Lifecycle lifecycle) {
        super();
        this.lifecycle = lifecycle;
    }

    public void addLifecycleListener(LifecycleListener listener) {

        synchronized (lockingObject) {
            LifecycleListener[] results = new LifecycleListener[listeners.length + 1];

            System.arraycopy(listeners, 0, results, 0, listeners.length);

            results[listeners.length] = listener;
            listeners = results;
        }
    }

    public LifecycleListener[] findLifecycleListeners() {
        return listeners;
    }

    public void fireLifecycleEvent(String type, Object data) {

        LifecycleEvent event = new LifecycleEvent(lifecycle, type, data);
        LifecycleListener[] events = listeners;
        for (LifecycleListener listener : events) {
            listener.lifecycleEvent(event);
        }
    }

    public void removeLifecycleListener(LifecycleListener listener) {

        synchronized (lockingObject) {
            int n = -1;
            for (int i = 0; i < listeners.length; i++) {
                if (listeners[i] == listener) {
                    n = i;
                    break;
                }
            }
            if (n < 0) return;
            LifecycleListener results[] = new LifecycleListener[listeners.length - 1];
            int j = 0;
            for (int i = 0; i < listeners.length; i++) {
                if (i != n) results[j++] = listeners[i];
            }
            listeners = results;
        }
    }
}
