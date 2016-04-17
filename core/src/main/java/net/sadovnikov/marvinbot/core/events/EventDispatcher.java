package net.sadovnikov.marvinbot.core.events;

import com.google.inject.Inject;
import com.sun.istack.internal.NotNull;
import net.sadovnikov.marvinbot.core.exceptions.UnknownEventTypeException;
import org.apache.logging.log4j.LogManager;
import ro.fortsoft.pf4j.PluginManager;

import java.util.*;

/**
 * An abstract class which concrete implementation can dispatch any type of net.sadovnikov.marvinbot.core.events bot's components can handle
 */
public abstract class EventDispatcher extends Thread {

    @NotNull
    PluginManager pluginManager;

    protected void dispatch(Event ev) {

        String eventHandlerClassname = "net.sadovnikov.marvinbot.core.events.event_handlers." + ev.getClass().getSimpleName() + "Handler";
        try {
            Class eventHandlerClass = Class.forName(eventHandlerClassname);
            List<EventHandler> eventHandlers = pluginManager.getExtensions(eventHandlerClass);
            for (EventHandler handler : eventHandlers) {
                LogManager.getLogger("core-logger").debug("handling " + ev.getClass().getName() + " to " + handler.getClass().getName());
                handler.handle(ev);
            }

        } catch (ClassNotFoundException e) {
            LogManager.getLogger("core-logger").error("No handler for " + ev.getClass().getName() + " found");
        }
    }

    public void setPluginManager(PluginManager pluginManager) {
        this.pluginManager = pluginManager;
    }


}
