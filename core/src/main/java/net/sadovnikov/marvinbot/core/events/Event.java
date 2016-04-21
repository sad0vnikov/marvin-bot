package net.sadovnikov.marvinbot.core.events;

import java.util.ArrayList;
import java.util.List;

public abstract class Event {

    protected List<EventFilter> filters = new ArrayList();

    public List<EventFilter> getFilters() {
        return this.filters;
    }
}
