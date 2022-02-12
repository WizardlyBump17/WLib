package com.wizardlybump17.wlib.command;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * An annotation that describes a command.
 * For a command to be valid it must contain at least one parameter in the method that implements {@link CommandSender},
 * or if it has more parameters, the first must be the CommandSender
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Command {

    /**
     * How the sender must type the command in order for it to be triggered.
     * Example: <pre>command hello world</pre>
     * In the example above, the sender must type /command hello world, so it can be triggered.
     * To add parameters that depends on the sender input, just put the parameter between <> or [] (not implemented yet).
     * The <> is for required parameters while [] is for the optional ones.
     * Example: <pre>command &lt;hello&gt; [world]</pre>
     * The type of each parameter is defined in the method that have this annotation.
     * An example for the command above:
     * <pre>
     *  &#64;Command(execution = "command &lt;hello&gt; [world]")
     *  public void commandHelloWorld(GenericSender sender, String hello, int world) {
     *      System.out.println(sender.getName() + " executed the hello world command with the args " + hello + " and " + world);
     *  }
     * </pre>
     * @return how the command must be sent to be triggered
     * @see com.wizardlybump17.wlib.command.args.reader.ArgsReader
     */
    String execution();

    /**
     * @return which permission the sender must have to trigger this command
     */
    String permission() default "";

    /**
     * Sets the priority of this command. If the priority is -1, then the priority check is the same as
     * <pre>{@code this.execution().split(" ").length}</pre>
     * @return the priority of this command
     */
    int priority() default -1;

    /**
     * Sets the options of this command.
     * The Bukkit implementation does nothing with this
     * @return the options of this command
     */
    String[] options() default {};

    /**
     * @return the description of this command
     */
    String description() default "";
}
