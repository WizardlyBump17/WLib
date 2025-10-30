package com.wizardlybump17.wlib.command.test.rework;

import com.wizardlybump17.wlib.command.rework.Command;
import com.wizardlybump17.wlib.command.rework.manager.CommandManager;
import com.wizardlybump17.wlib.command.rework.node.LiteralCommandNode;
import com.wizardlybump17.wlib.command.rework.result.CommandResult;
import com.wizardlybump17.wlib.command.sender.BasicCommandSender;
import com.wizardlybump17.wlib.command.sender.CommandSender;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.UUID;
import java.util.function.Consumer;

class CommandManagerTests {

    static final @NotNull Consumer<String> SENDER_MESSAGE_CONSUMER = System.out::println;
    static final @NotNull CommandSender<Object> CHAD_SENDER = new BasicCommandSender<>(new Object(), "Chad", UUID.nameUUIDFromBytes("Chad".getBytes()), SENDER_MESSAGE_CONSUMER, $ -> true);
    static final @NotNull CommandSender<Object> BETA_SENDER = new BasicCommandSender<>(new Object(), "Beta", UUID.nameUUIDFromBytes("Beta".getBytes()), SENDER_MESSAGE_CONSUMER, $ -> false);

    @Test
    void testRegisterDifferent() {
        Command command0 = new Command(new LiteralCommandNode("hello0", context -> CommandResult.successful(context, "hello0")));
        Command command1 = new Command(new LiteralCommandNode("hello1", context -> CommandResult.successful(context, "hello1")));
        Command command2 = new Command(new LiteralCommandNode("hello2", context -> CommandResult.successful(context, "hello2")));
        Command command3 = new Command(new LiteralCommandNode("hello3", context -> CommandResult.successful(context, "hello3")));

        CommandManager manager = new CommandManager();

        Command registeredCommand0 = manager.registerCommand("test", command0);
        Command registeredCommand1 = manager.registerCommand("test", command1);
        Command registeredCommand2 = manager.registerCommand("test", command2);
        Command registeredCommand3 = manager.registerCommand("test", command3);

        Assertions.assertEquals(command0, registeredCommand0);
        Assertions.assertEquals(command1, registeredCommand1);
        Assertions.assertEquals(command2, registeredCommand2);
        Assertions.assertEquals(command3, registeredCommand3);
    }
}
