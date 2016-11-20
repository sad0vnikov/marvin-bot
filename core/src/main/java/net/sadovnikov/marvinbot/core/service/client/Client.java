package net.sadovnikov.marvinbot.core.service.client;

public abstract class Client {

    public abstract void connect() throws ClientException;

    public abstract void disconnect() throws ClientException;

}
