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

    /**
     * @return the class of the holder of the config
     */
    Class<?> holderType();

    /**
     * @return if it should save the default config based on the variables in the class. Defaults to false
     */
    boolean saveDefault() default false;
}
