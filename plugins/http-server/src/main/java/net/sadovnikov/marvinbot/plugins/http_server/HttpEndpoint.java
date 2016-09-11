package net.sadovnikov.marvinbot.plugins.http_server;

import java.lang.annotation.*;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)

public @interface HttpEndpoint {
    String value();
}
