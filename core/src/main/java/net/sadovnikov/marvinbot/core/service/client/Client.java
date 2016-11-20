package net.sadovnikov.marvinbot.core.service.client;

public abstract class Client {

    public abstract void connect() throws ClientException;

    public abstract void setVisible() throws ClientException;

    public abstract void disconnect() throws ClientException;

    public abstract void setStatusText() throws ClientException;

}
