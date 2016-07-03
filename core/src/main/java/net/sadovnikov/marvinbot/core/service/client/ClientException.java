package net.sadovnikov.marvinbot.core.service.client;

public class ClientException extends RuntimeException {

    public ClientException() {
        super();
    }

    public ClientException (String message) {
        super(message);
    }

    public ClientException (Throwable e) {
        super(e);
    }
}

