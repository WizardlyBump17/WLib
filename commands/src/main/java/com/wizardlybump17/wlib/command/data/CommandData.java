package com.wizardlybump17.wlib.command.data;

import com.wizardlybump17.wlib.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface CommandData {

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
    @NotNull String getExecution();

    /**
     * @return which permission the sender must have to trigger this command
     */
    default @Nullable String getPermission() {
        return null;
    }

    /**
     * <p>Used when the {@link CommandSender} does not have the required {@link #getPermission()}.</p>
     * @return the message to be sent when the {@link CommandSender} does not have the required {@link #getPermission()}
     */
    default @Nullable String getPermissionMessage() {
        return null;
    }

    /**
     * @return the priority of this command
     */
    default int getPriority() {
        return getExecution().split(" ").length;
    }

    /**
     * @return the description of this command
     */
    default @Nullable String getDescription() {
        return null;
    }

    /**
     * <p>Used when the {@link CommandSender} is not valid for this command.</p>
     * @return the message to be sent when the {@link CommandSender} is not valid for this command
     */
    default @Nullable String getInvalidSenderMessage() {
        return null;
    }
}
