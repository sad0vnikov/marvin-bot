package net.sadovnikov.marvinbot.core.domain.message;


public abstract class Message {

    protected String text;

    public String text() {
        return this.text;
    }
}
