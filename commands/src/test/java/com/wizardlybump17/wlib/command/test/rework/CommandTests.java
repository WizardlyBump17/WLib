package com.wizardlybump17.wlib.command.test.rework;

import com.wizardlybump17.wlib.command.rework.Command;
import com.wizardlybump17.wlib.command.rework.node.LiteralCommandNode;
import com.wizardlybump17.wlib.command.rework.result.CommandResult;
import com.wizardlybump17.wlib.command.sender.BasicCommandSender;
import com.wizardlybump17.wlib.command.sender.CommandSender;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.UUID;
import java.util.function.Consumer;

public class CommandTests {

    static final @NotNull Consumer<String> SENDER_MESSAGE_CONSUMER = System.out::println;
    static final @NotNull CommandSender<Object> ALL_KNOWING_SENDER = new BasicCommandSender<>(new Object(), "Test", UUID.nameUUIDFromBytes("Test".getBytes()), SENDER_MESSAGE_CONSUMER, $ -> true);

    @Test
    void testSuccessHello() {
        LiteralCommandNode helloNode = new LiteralCommandNode("hello", context -> CommandResult.successful(context, "hello"));
        Command command = new Command(helloNode);

        CommandResult<String> expected = CommandResult.successful(0, helloNode, "hello");
        CommandResult<?> actual = command.execute(ALL_KNOWING_SENDER, List.of("hello"));

        Assertions.assertEquals(expected, actual);
    }

    @Test
    void testSuccessHelloWorld() {
        LiteralCommandNode worldNode = new LiteralCommandNode("world", context -> CommandResult.successful(context, "hello world"));
        Command command = new Command(new LiteralCommandNode("hello", List.of(worldNode)));

        CommandResult<String> expected = CommandResult.successful(1, worldNode, "hello world");
        CommandResult<?> actual = command.execute(ALL_KNOWING_SENDER, List.of("hello", "world"));

        Assertions.assertEquals(expected, actual);
    }

    @Test
    void testSuccessHelloWorldHi() {
        LiteralCommandNode hiNode = new LiteralCommandNode("hi", context -> CommandResult.successful(context, "hello world hi"));
        Command command = new Command(new LiteralCommandNode("hello", List.of(new LiteralCommandNode("world", List.of(hiNode)))));

        CommandResult<String> expected = CommandResult.successful(2, hiNode, "hello world hi");
        CommandResult<?> actual = command.execute(ALL_KNOWING_SENDER, List.of("hello", "world", "hi"));

        Assertions.assertEquals(expected, actual);
    }
}
