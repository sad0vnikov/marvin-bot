package net.sadovnikov.marvinbot.core.events;

import com.google.inject.Inject;
import net.sadovnikov.marvinbot.core.main.PluginLoader;
import net.sadovnikov.marvinbot.core.plugin.PluginException;
import net.sadovnikov.marvinbot.helpers.ReflectionHelper;
import org.apache.logging.log4j.LogManager;
import ro.fortsoft.pf4j.PluginManager;

import java.util.*;

/**
 * An abstract class which concrete implementation can dispatch any type of net.sadovnikov.marvinbot.core.events bot's components can handle
 */
public abstract class EventDispatcher extends Thread {

    @Inject
    PluginLoader pluginLoader;

    protected void dispatch(Event ev) {

        PluginManager pluginManager = pluginLoader.getPluginManager();

        try {
            Class eventHandlerClass = EventHandler.class;
            List<EventHandler> eventHandlers = pluginManager.getExtensions(eventHandlerClass);
            for (EventHandler handler : eventHandlers) {
                Class clazz = handler.getClass();
                String genericTypeName = ReflectionHelper.getGenericParameterClass(clazz);

                if (genericTypeName != null && genericTypeName.equals(ev.getClass().getName())) {
                   handler.handle(ev);
                }

            }
        } catch (PluginException e) {
            LogManager.getLogger("core-logger").catching(e);
        }



    }


}
