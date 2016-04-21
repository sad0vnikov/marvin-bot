package net.sadovnikov.marvinbot.core.events.annotations;

import net.sadovnikov.marvinbot.core.events.event_filters.CommandFilter;

import java.lang.annotation.*;

@Target(ElementType.TYPE)
@Documented
@Retention(RetentionPolicy.RUNTIME)
public @interface Command {
    String value();
}