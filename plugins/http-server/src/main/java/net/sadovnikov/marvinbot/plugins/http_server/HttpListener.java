package net.sadovnikov.marvinbot.plugins.http_server;


import com.sun.net.httpserver.*;
import org.apache.logging.log4j.LogManager;
import ro.fortsoft.pf4j.PluginManager;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.List;

public class HttpListener implements com.sun.net.httpserver.HttpHandler {

    private PluginManager pluginManager;

    public HttpListener(PluginManager pluginManager) {
        this.pluginManager = pluginManager;
    }

    public void handle(HttpExchange httpExchange) throws IOException {

        List<HttpHandler> handlers = pluginManager.getExtensions(HttpHandler.class);
        String responseBody = "<html><body>404 Not Found</body></html>";
        int statusCode = HttpResponse.STATUS_NOT_FOUND;

        String requestUri = httpExchange.getRequestURI().toString();
        LogManager.getLogger().debug("new HTTP request at  " + requestUri);
        LogManager.getLogger().debug("found http handlers: " + handlers);
        for (HttpHandler handler : handlers) {

            HttpEndpoint endpointAnnotation = handler.getClass().getAnnotation(HttpEndpoint.class);
            if (endpointAnnotation.value().equals(requestUri)) {

                HttpRequest httpRequest = HttpRequest.fromHttpExchange(httpExchange);
                HttpResponse response = handler.handle(httpRequest);

                responseBody = response.body();
                statusCode   = response.status();

                break;
            }

        }

        BufferedWriter out = new BufferedWriter(new OutputStreamWriter(httpExchange.getResponseBody()));
        httpExchange.sendResponseHeaders(statusCode, responseBody.length());
        out.write(responseBody);
        out.flush();
        httpExchange.close();


    }
}
