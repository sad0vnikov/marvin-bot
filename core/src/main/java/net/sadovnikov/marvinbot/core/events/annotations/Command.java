package net.sadovnikov.marvinbot.core.events.annotations;

import java.lang.annotation.*;

@Target(ElementType.TYPE)
@Documented
@Retention(RetentionPolicy.RUNTIME)
public @interface Command {
    String value();
}