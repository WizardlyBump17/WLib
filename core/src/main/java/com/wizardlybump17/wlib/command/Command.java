package com.wizardlybump17.wlib.command;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Command {

    String execution();
    String permission() default "";
    String permissionMessage() default "";

    /**
     * @return if the pattern should receive .* at the end
     */
    boolean acceptAny() default true;

    /**
     * The priority of this command. -1 meaning {@link Command#execution()}.split(" ").length
     * @return the priority
     */
    int priority() default -1;
}
