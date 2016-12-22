package net.sadovnikov.marvinbot.core.domain;

public class Channel {

    String id;

    public Channel(ChannelTypes type) {
        this.id = type.name().toUpperCase();
    }

    public String id() {
        return this.id;
    }
}
