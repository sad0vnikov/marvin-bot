package net.sadovnikov.marvinbot.core.annotations;

import net.sadovnikov.marvinbot.core.domain.user.Role;

import java.lang.annotation.*;

/**
 * This annotation shows us permissions which are required to execute command
 */
@Target(ElementType.TYPE)
@Documented
@Retention(RetentionPolicy.RUNTIME)
public @interface RequiredRole {
    Class<? extends Role> value();
}
