package com.wizardlybump17.wlib.config;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Denotes the annotated field as a config path.<br>
 * This must be assigned to static fields
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface Path {

    /**
     * @return the path of this field
     */
    String value();

    /**
     * This stores some options for this path.<br>
     * It can be useful to do things like formatting the string etc.<br>
     * This is implementation dependent
     * @return the options for this path
     */
    String[] options() default {};
}
