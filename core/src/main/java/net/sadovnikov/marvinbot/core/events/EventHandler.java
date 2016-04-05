package net.sadovnikov.marvinbot.core.events;

import ro.fortsoft.pf4j.ExtensionPoint;

/**
 * Event Handler is an object which takes an event from Event Listener and does some stuff then.
 *
 * Marvin-bot uses pf4j for extending it's functionality.
 * Any plugin can handle any event by extending this class.
 * For additional information see pf4j docs (https://github.com/decebals/pf4j)
 */
public abstract class EventHandler<T extends Event> implements ExtensionPoint {

    public abstract void handle(T ev);

}
