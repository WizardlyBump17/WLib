package com.wizardlybump17.wlib.command;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Command {

    /**
     * How the command must be executed.
     * It accepts regex inside ()
     * @return how the command must be executed
     */
    String execution();

    /**
     * @return the permission the sender must have to execute the command
     */
    String permission() default "";

    /**
     * @return the message sent to the sender when he does not have the permission to execute the command
     */
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
