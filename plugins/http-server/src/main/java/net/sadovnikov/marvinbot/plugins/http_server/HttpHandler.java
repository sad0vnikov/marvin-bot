package net.sadovnikov.marvinbot.plugins.http_server;


import ro.fortsoft.pf4j.ExtensionPoint;

public abstract class HttpHandler implements ExtensionPoint {

    public abstract HttpResponse handle(HttpRequest request);
}
