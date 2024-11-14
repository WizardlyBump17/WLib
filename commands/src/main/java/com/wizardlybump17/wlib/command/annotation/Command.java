package com.wizardlybump17.wlib.command.annotation;

import com.wizardlybump17.wlib.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * <p>An annotation that describes a command.</p>
 * <p>For a command to be valid the first parameter of the method must implements {@link CommandSender}</p>
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Command {

    /**
     * <p>How the sender must type the command in order for it to be triggered.
     * Example: <pre>command hello world</pre>
     * In the example above, the sender must type /command hello world, so it can be triggered.
     * To add parameters that depends on the sender input, just put the parameter between <>.
     * <br>
     * Example: <pre>command &lt;hello&gt; world</pre></p>
     * <p>The type of each parameter is defined in the method that have this annotation.
     * An example for the command above:
     * <pre>
     *  &#64;Command(execution = "command &lt;hello&gt; world")
     *  public void commandHelloWorld(GenericSender sender, String hello) {
     *      System.out.println(sender.getName() + " executed the hello world command with the argument " + hello);
     *  }
     * </pre></p>
     *
     * @return how the command must be sent to be triggered
     * @see com.wizardlybump17.wlib.command.args.reader.ArgsReader
     */
    String execution();

    /**
     * @return which permission the sender must have to trigger this command
     */
    String permission() default "";

    /**
     * <p>Used when the {@link CommandSender} does not have the required {@link #permission()}.</p>
     * @return the message to be sent when the {@link CommandSender} does not have the required {@link #permission()}
     */
    String permissionMessage() default "";

    /**
     * @return if the {@link #permissionMessage()} is a field in the class that have this annotation
     */
    boolean permissionMessageIsField() default false;

    /**
     * Sets the priority of this command. If the priority is -1, then the priority check is the same as
     * <pre>{@code this.execution().split(" ").length}</pre>
     *
     * @return the priority of this command
     */
    int priority() default -1;

    /**
     * Sets the options of this command.
     * The Bukkit implementation does nothing with this
     *
     * @return the options of this command
     */
    String[] options() default {};

    /**
     * @return the description of this command
     */
    String description() default "";

    /**
     * <p>Used when the {@link CommandSender} is not valid for this command.</p>
     * @return the message to be sent when the {@link CommandSender} is not valid for this command
     */
    String invalidSenderMessage() default "";

    /**
     * @return if the {@link #invalidSenderMessage()} is a field in the class that have this annotation
     */
    boolean invalidSenderMessageIsField() default false;

    /**
     * @return the type of the {@link CommandSender#getHandle()} that can execute this command
     */
    @NotNull Class<?> senderType() default Object.class;
}
