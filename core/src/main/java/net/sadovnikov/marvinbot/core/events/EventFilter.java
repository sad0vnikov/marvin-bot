package net.sadovnikov.marvinbot.core.events;


public abstract class EventFilter<T extends Event> {

    public abstract boolean filter(T evt, EventHandler value);
}
