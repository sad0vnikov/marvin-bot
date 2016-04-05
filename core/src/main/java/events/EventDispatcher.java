package events;

import exceptions.UnknownEventTypeException;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * An abstract class which concrete implementation can dispatch any type of events bot's components can handle
 */
public abstract class EventDispatcher extends Thread {

    public HashMap<String, HashSet<EventListener>> listeners = new HashMap<>();

    protected void dispatch(Event ev) {
        HashSet<EventListener> evListeners = listeners.get(ev.getClass().getCanonicalName());
        if (evListeners == null) {
            return;
        }
        for (EventListener listener : evListeners) {
            listener.send(ev);
        }
    }

    public void addListener(String eventType, EventListener listener) throws UnknownEventTypeException {

        try {
            Class eventClass = Class.forName(eventType);
            if (!Event.class.isAssignableFrom(eventClass)) {
                throw new UnknownEventTypeException("class " + eventType + " is not " + Event.class.getCanonicalName() + " subtype");
            }

            HashSet<EventListener> evListeners = listeners.get(eventType);
            if (evListeners == null) {
                evListeners = new HashSet<>();

                listeners.put(eventType, evListeners);
            }

            evListeners.add(listener);

        } catch (ClassNotFoundException e) {
            throw new UnknownEventTypeException("Can't find Event " + eventType);
        }
    }

    public void removeListener(EventListener listener) {

        for (Map.Entry<String, HashSet<EventListener>> entry : listeners.entrySet()) {
            entry.getValue().remove(listener);
        }
    }
}
