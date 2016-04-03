package events.eventListeners;

import events.EventListener;
import events.eventHandlers.MessageEventHandler;
import events.eventTypes.MessageEvent;
import messageSender.MessageSender;
import org.apache.logging.log4j.LogManager;
import ro.fortsoft.pf4j.PluginManager;

import java.util.List;

public class MessageEventListener extends EventListener<MessageEvent> {

    MessageSender sender;
    PluginManager pluginManager;

    public MessageEventListener(PluginManager pluginManager, MessageSender sender) {
        this.sender = sender;
        this.pluginManager = pluginManager;
    }

    public void send(MessageEvent ev) {
        String msgText = ev.getMessage().getText();
        String usrName = ev.getMessage().getUser().getName();

        LogManager.getLogger("core-logger").debug("[received message]" + usrName + ": " + msgText);

        List<MessageEventHandler> handlers =  pluginManager.getExtensions(MessageEventHandler.class);

        for (MessageEventHandler handler : handlers) {
            handler.setMessageSender(sender);
            handler.handle(ev);
        }
    }
}
