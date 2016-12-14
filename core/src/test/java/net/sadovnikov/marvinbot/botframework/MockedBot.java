package net.sadovnikov.marvinbot.botframework;

import net.sadovnikov.mbf4j.Bot;
import net.sadovnikov.mbf4j.events.EventBroker;

public class MockedBot extends Bot {

    public EventBroker eventBroker() {
        return eventBroker;
    }
}
