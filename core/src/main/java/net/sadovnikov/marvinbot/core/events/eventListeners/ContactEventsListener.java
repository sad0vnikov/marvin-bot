package net.sadovnikov.marvinbot.core.events.eventListeners;

import net.sadovnikov.marvinbot.core.events.EventListener;
import net.sadovnikov.marvinbot.core.events.eventHandlers.ContactRequestEventHandler;
import net.sadovnikov.marvinbot.core.events.eventTypes.ContactRequestEvent;
import org.apache.logging.log4j.LogManager;
import ro.fortsoft.pf4j.PluginManager;

import java.util.List;


public class ContactEventsListener extends EventListener<ContactRequestEvent> {


    PluginManager pluginManager;

    public ContactEventsListener(PluginManager pluginManager) {
        this.pluginManager = pluginManager;
    }

    @Override
    public void send(ContactRequestEvent ev) {
        LogManager.getLogger("core-logger").info("received new contact request from user " + ev.getContact().getName());

        List<ContactRequestEventHandler> handlers =  pluginManager.getExtensions(ContactRequestEventHandler.class);
        for (ContactRequestEventHandler handler : handlers) {
            handler.handle(ev);
        }
    }
}
