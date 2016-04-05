package net.sadovnikov.marvinbot.core.exceptions;

public class UnknownEventTypeException extends RuntimeException {

    public UnknownEventTypeException(String message) {
        super(message);
    }
}