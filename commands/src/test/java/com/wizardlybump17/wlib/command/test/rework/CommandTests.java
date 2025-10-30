package com.wizardlybump17.wlib.command.test.rework;

import com.wizardlybump17.wlib.command.rework.Command;
import com.wizardlybump17.wlib.command.rework.node.LiteralCommandNode;
import com.wizardlybump17.wlib.command.rework.result.CommandResult;
import com.wizardlybump17.wlib.command.rework.result.error.ExceptionResult;
import com.wizardlybump17.wlib.command.rework.result.error.ExtraArgumentsResult;
import com.wizardlybump17.wlib.command.rework.result.error.InsufficientArgumentsResult;
import com.wizardlybump17.wlib.command.sender.BasicCommandSender;
import com.wizardlybump17.wlib.command.sender.CommandSender;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.UUID;
import java.util.function.Consumer;

public class CommandTests {

    static final @NotNull Consumer<String> SENDER_MESSAGE_CONSUMER = System.out::println;
    static final @NotNull CommandSender<Object> CHAD_SENDER = new BasicCommandSender<>(new Object(), "Chad", UUID.nameUUIDFromBytes("Chad".getBytes()), SENDER_MESSAGE_CONSUMER, $ -> true);
    static final @NotNull CommandSender<Object> BETA_SENDER = new BasicCommandSender<>(new Object(), "Beta", UUID.nameUUIDFromBytes("Beta".getBytes()), SENDER_MESSAGE_CONSUMER, $ -> false);

    @Test
    void testSuccessHello() {
        LiteralCommandNode helloNode = new LiteralCommandNode("hello", context -> CommandResult.successful(context, "hello"));
        Command command = new Command(helloNode);

        CommandResult<String> expected = CommandResult.successful(0, helloNode, "hello");
        CommandResult<?> actual = command.execute(CHAD_SENDER, List.of("hello"));

        Assertions.assertEquals(expected, actual);
    }

    @Test
    void testSuccessHelloWorld() {
        LiteralCommandNode worldNode = new LiteralCommandNode("world", context -> CommandResult.successful(context, "hello world"));
        Command command = new Command(new LiteralCommandNode("hello", List.of(worldNode)));

        CommandResult<String> expected = CommandResult.successful(1, worldNode, "hello world");
        CommandResult<?> actual = command.execute(CHAD_SENDER, List.of("hello", "world"));

        Assertions.assertEquals(expected, actual);
    }

    @Test
    void testSuccessHelloWorldHi() {
        LiteralCommandNode hiNode = new LiteralCommandNode("hi", context -> CommandResult.successful(context, "hello world hi"));
        Command command = new Command(new LiteralCommandNode("hello", List.of(new LiteralCommandNode("world", List.of(hiNode)))));

        CommandResult<String> expected = CommandResult.successful(2, hiNode, "hello world hi");
        CommandResult<?> actual = command.execute(CHAD_SENDER, List.of("hello", "world", "hi"));

        Assertions.assertEquals(expected, actual);
    }

    @Test
    @DisplayName("Multiple children 0 (hello, hi): success")
    void test0() {
        LiteralCommandNode hiNode = new LiteralCommandNode("hi", context -> CommandResult.successful(context, "hello hi"));
        Command command = new Command(new LiteralCommandNode(
                "hello",
                List.of(
                        new LiteralCommandNode("world", context -> CommandResult.successful(context, "hello world")),
                        hiNode
                )
        ));

        CommandResult<String> expected = CommandResult.successful(1, hiNode, "hello hi");
        CommandResult<?> actual = command.execute(CHAD_SENDER, List.of("hello", "hi"));

        Assertions.assertEquals(expected, actual);
    }

    @Test
    @DisplayName("Multiple children 1 (hello, hi, world): success")
    void test1() {
        LiteralCommandNode hiWorldNode = new LiteralCommandNode("world", context -> CommandResult.successful(context, "hello hi world"));
        Command command = new Command(new LiteralCommandNode(
                "hello",
                List.of(
                        new LiteralCommandNode("world", context -> CommandResult.successful(context, "hello world")),
                        new LiteralCommandNode("hi", List.of(hiWorldNode))
                )
        ));

        CommandResult<String> expected = CommandResult.successful(2, hiWorldNode, "hello hi world");
        CommandResult<?> actual = command.execute(CHAD_SENDER, List.of("hello", "hi", "world"));

        Assertions.assertEquals(expected, actual);
    }

    @Test
    void testExtraArguments0() {
        LiteralCommandNode helloNode = new LiteralCommandNode("hello", context -> CommandResult.successful(context, "hello"));
        Command command = new Command(helloNode);

        ExtraArgumentsResult<?> expected = CommandResult.extraArguments(1, helloNode);
        CommandResult<?> actual = command.execute(CHAD_SENDER, List.of("hello", "world"));

        Assertions.assertEquals(expected, actual);
    }

    @Test
    void testExtraArguments1() {
        LiteralCommandNode worldNode = new LiteralCommandNode("world", context -> CommandResult.successful(context, "hello world"));
        Command command = new Command(
                new LiteralCommandNode(
                        "hello",
                        List.of(worldNode)
                )
        );

        ExtraArgumentsResult<?> expected = CommandResult.extraArguments(2, worldNode);
        CommandResult<?> actual = command.execute(CHAD_SENDER, List.of("hello", "world", "hi"));

        Assertions.assertEquals(expected, actual);
    }

    @Test
    void testExtraArguments2() {
        LiteralCommandNode hiWorldNode = new LiteralCommandNode("world", context -> CommandResult.successful(context, "hello hi world"));
        Command command = new Command(new LiteralCommandNode(
                "hello",
                List.of(
                        new LiteralCommandNode("world", context -> CommandResult.successful(context, "hello world")),
                        new LiteralCommandNode("hi", List.of(hiWorldNode))
                )
        ));

        ExtraArgumentsResult<?> expected = CommandResult.extraArguments(3, hiWorldNode);
        CommandResult<?> actual = command.execute(CHAD_SENDER, List.of("hello", "hi", "world", "extra"));

        Assertions.assertEquals(expected, actual);
    }

    @Test
    void testException() {
        RuntimeException helloException = new RuntimeException("hello");
        LiteralCommandNode helloNode = new LiteralCommandNode("hello", context -> {
            throw helloException;
        });
        Command command = new Command(helloNode);

        ExceptionResult<?> expected = CommandResult.exceptionally(0, helloNode, helloException);
        CommandResult<?> actual = command.execute(CHAD_SENDER, List.of("hello"));

        Assertions.assertEquals(expected, actual);
    }

    @Test
    void testInsufficientArguments() {
        Command command = new Command(new LiteralCommandNode("hello", context -> CommandResult.successful(context, "hello")));

        InsufficientArgumentsResult<?> expected = CommandResult.insufficientArguments(command);
        CommandResult<?> actual = command.execute(CHAD_SENDER, List.of());

        Assertions.assertEquals(expected, actual);
    }
}
