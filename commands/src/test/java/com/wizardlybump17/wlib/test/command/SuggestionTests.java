package com.wizardlybump17.wlib.test.command;

import com.wizardlybump17.wlib.command.Command;
import com.wizardlybump17.wlib.command.input.primitive.number.AllowedIntegerInputs;
import com.wizardlybump17.wlib.command.node.IntegerCommandNode;
import com.wizardlybump17.wlib.command.node.LiteralCommandNode;
import com.wizardlybump17.wlib.command.result.CommandResult;
import com.wizardlybump17.wlib.command.sender.BasicCommandSender;
import com.wizardlybump17.wlib.command.sender.CommandSender;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.UUID;
import java.util.function.Consumer;

class SuggestionTests {

    static final @NotNull Consumer<String> SENDER_MESSAGE_CONSUMER = System.out::println;
    static final @NotNull CommandSender<Object> CHAD_SENDER = new BasicCommandSender<>(new Object(), "Chad", UUID.nameUUIDFromBytes("Chad".getBytes()), SENDER_MESSAGE_CONSUMER, $ -> true);
    static final @NotNull CommandSender<Object> BETA_SENDER = new BasicCommandSender<>(new Object(), "Beta", UUID.nameUUIDFromBytes("Beta".getBytes()), SENDER_MESSAGE_CONSUMER, $ -> false);

    @Test
    @DisplayName("Test Success: root 0")
    void testSuccess0() {
        Command command = new Command(
                new LiteralCommandNode(
                        "hello",
                        List.of(
                                new LiteralCommandNode(
                                        "world",
                                        context -> CommandResult.successful(context, "hello world")
                                )
                        )
                )
        );

        List<Object> expected = List.of("hello");
        List<Object> actual = command.getSuggestions(CHAD_SENDER, List.of());

        Assertions.assertEquals(expected, actual);
    }

    @Test
    @DisplayName("Test Success: root 1")
    void testSuccess1() {
        Command command = new Command(
                new LiteralCommandNode(
                        "hello",
                        List.of(
                                new LiteralCommandNode(
                                        "world",
                                        context -> CommandResult.successful(context, "hello world")
                                )
                        )
                )
        );

        List<Object> expected = List.of("hello");
        List<Object> actual = command.getSuggestions(CHAD_SENDER, List.of("he"));

        Assertions.assertEquals(expected, actual);
    }

    @Test
    @DisplayName("Test Success: children 0")
    void testSuccess2() {
        Command command = new Command(
                new LiteralCommandNode(
                        "hello",
                        List.of(
                                new LiteralCommandNode(
                                        "world",
                                        context -> CommandResult.successful(context, "hello world")
                                ),
                                new LiteralCommandNode(
                                        "hi",
                                        context -> CommandResult.successful(context, "hello hi")
                                ),
                                new LiteralCommandNode(
                                        "there",
                                        context -> CommandResult.successful(context, "hello there")
                                )
                        )
                )
        );

        List<Object> expected = List.of("world", "hi", "there");
        List<Object> actual = command.getSuggestions(CHAD_SENDER, List.of("hello", ""));

        Assertions.assertEquals(expected, actual);
    }

    @Test
    @DisplayName("Test Success: children 1")
    void testSuccess3() {
        Command command = new Command(
                new LiteralCommandNode(
                        "hello",
                        List.of(
                                new LiteralCommandNode(
                                        "world",
                                        context -> CommandResult.successful(context, "hello world")
                                ),
                                new LiteralCommandNode(
                                        "hi",
                                        context -> CommandResult.successful(context, "hello hi")
                                ),
                                new IntegerCommandNode(
                                        "there",
                                        AllowedIntegerInputs.range(1, 10),
                                        context -> CommandResult.successful(context, "hello there")
                                )
                        )
                )
        );

        List<Object> expected = List.of("world", "hi", 1, 3, 5, 7, 10);
        List<Object> actual = command.getSuggestions(CHAD_SENDER, List.of("hello", ""));

        Assertions.assertEquals(expected, actual);
    }

    @Test
    @DisplayName("Test Success: children 2")
    void testSuccess4() {
        Command command = new Command(
                new LiteralCommandNode(
                        "hello",
                        List.of(
                                new LiteralCommandNode(
                                        "world",
                                        context -> CommandResult.successful(context, "hello world")
                                ),
                                new LiteralCommandNode(
                                        "hi",
                                        context -> CommandResult.successful(context, "hello hi")
                                ),
                                new IntegerCommandNode(
                                        "there",
                                        AllowedIntegerInputs.range(1, 10),
                                        context -> CommandResult.successful(context, "hello there")
                                ),
                                new LiteralCommandNode(
                                        "happy",
                                        context -> CommandResult.successful(context, "hello happy")
                                )
                        )
                )
        );

        List<Object> expected = List.of("world", "hi", "happy");
        List<Object> actual = command.getSuggestions(CHAD_SENDER, List.of("hello", "h"));

        Assertions.assertEquals(expected, actual);
    }
}
