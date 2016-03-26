package main;


import config.ConfigException;
import config.ConfigLoader;
import events.EventDispatcher;
import events.eventListeners.MessageEventListener;
import events.eventTypes.MessageEvent;
import factories.Skype4jFactory;
import factories.SkypeFactory;
import messageSender.MessageSender;

public class Main {

    public static void main(String[] args) {

        ConfigLoader config;
        try {
            config = new ConfigLoader("config/connection.properties");

        } catch (ConfigException e) {
            System.out.println(e.getMessage());
            return;
        }

        String skypeLogin    = config.getParam("login");
        String skypePassword = config.getParam("password");
        SkypeFactory skypeFactory = new Skype4jFactory(skypeLogin, skypePassword);

        EventDispatcher dispatcher = skypeFactory.getEventDispatcher();
        MessageSender sender       = skypeFactory.getMessageSender();
        dispatcher.addListener(MessageEvent.class.getTypeName(), new MessageEventListener(sender));

        dispatcher.start();
        try {
            dispatcher.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

}
