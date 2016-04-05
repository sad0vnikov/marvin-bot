package events;

import java.util.HashSet;
import java.util.Set;

/**
 * Event Listener is an object which listens for events of some concrete type and passes them to Event Handlers
 *
 * @param <T>
 */
public abstract class EventListener<T extends Event>{

    public abstract void send(T ev);

}
