package net.sadovnikov.marvinbot.plugins.http_server;

import net.sadovnikov.marvinbot.core.plugin.Plugin;
import ro.fortsoft.pf4j.PluginException;
import ro.fortsoft.pf4j.PluginWrapper;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.List;


public class HttpServer extends Plugin {

    private final int DEFAULT_HTTP_PORT = 8080;
    protected com.sun.net.httpserver.HttpServer httpServer;


    public HttpServer(PluginWrapper wrapper) throws IOException {
        super(wrapper);
        httpServer = com.sun.net.httpserver.HttpServer.create(new InetSocketAddress(DEFAULT_HTTP_PORT), 0);
    }

    @Override
    public void start() throws PluginException {
        List<HttpHandler> handlers = wrapper.getPluginManager().getExtensions(HttpHandler.class);
        httpServer.createContext("/", new HttpListener(wrapper.getPluginManager()));
        httpServer.start();
    }

    @Override
    public void stop() throws PluginException {
       httpServer.stop(0);
    }

}


