package net.sadovnikov.marvinbot.core.service.message_sender;


public class MessageSenderException extends Exception {

    public MessageSenderException(Throwable e) {
        super(e);
    }

    public MessageSenderException(String message) {
        super(message);
    }
}
