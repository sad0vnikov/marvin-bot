package net.sadovnikov.marvinbot.core.events.event_listeners;

import net.sadovnikov.marvinbot.core.events.EventListener;
import net.sadovnikov.marvinbot.core.events.event_handlers.MessageEventHandler;
import net.sadovnikov.marvinbot.core.events.event_types.MessageEvent;
import org.apache.logging.log4j.LogManager;
import ro.fortsoft.pf4j.PluginManager;

import java.util.List;

public class MessageEventListener extends EventListener<MessageEvent> {

    PluginManager pluginManager;

    public MessageEventListener(PluginManager pluginManager) {
        this.pluginManager = pluginManager;
    }

    public void send(MessageEvent ev) {
        String msgText = ev.getMessage().getText();
        String usrName = ev.getMessage().getSenderUsername();

        LogManager.getLogger("core-logger").info("received new message from user " + usrName + ": " + msgText);

        List<MessageEventHandler> handlers =  pluginManager.getExtensions(MessageEventHandler.class);

        for (MessageEventHandler handler : handlers) {
            handler.handle(ev);
        }
    }
}
