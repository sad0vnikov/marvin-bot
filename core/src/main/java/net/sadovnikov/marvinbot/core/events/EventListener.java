package net.sadovnikov.marvinbot.core.events;

/**
 * Event Listener is an object which listens for net.sadovnikov.marvinbot.core.events of some concrete type and passes them to Event Handlers
 *
 * @param <T>
 */
public abstract class EventListener<T extends Event>{

    public abstract void send(T ev);

}
