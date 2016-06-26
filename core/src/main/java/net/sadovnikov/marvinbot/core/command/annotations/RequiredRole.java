package net.sadovnikov.marvinbot.core.command.annotations;

import java.lang.annotation.*;

/**
 * This annotation shows us permissions which are required to execute command
 */
@Target(ElementType.TYPE)
@Documented
@Retention(RetentionPolicy.RUNTIME)
public @interface RequiredRole {
    int value();
}
