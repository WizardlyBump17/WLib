package com.wizardlybump17.wlib.config;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * This annotation stores the basic info for a config
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface ConfigInfo {

    /**
     * The name of the config.<br>
     * Generally, the config path
     * @return the name of the config
     */
    String name();

    String holderType();

    String holderName();
}
