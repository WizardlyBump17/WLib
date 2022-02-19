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
     * @return if this field is immutable. Defaults to false
     */
    boolean immutable() default false;

    String defaultValue() default "";

    String[] options() default {};
}
