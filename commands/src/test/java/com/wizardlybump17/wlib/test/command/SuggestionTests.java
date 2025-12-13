package com.wizardlybump17.wlib.test.command;

import com.wizardlybump17.wlib.command.Command;
import com.wizardlybump17.wlib.command.input.primitive.number.AllowedByteInputs;
import com.wizardlybump17.wlib.command.input.primitive.number.AllowedIntegerInputs;
import com.wizardlybump17.wlib.command.input.primitive.number.AllowedLongInputs;
import com.wizardlybump17.wlib.command.input.primitive.number.AllowedShortInputs;
import com.wizardlybump17.wlib.command.node.LiteralCommandNode;
import com.wizardlybump17.wlib.command.node.primitive.number.ByteCommandNode;
import com.wizardlybump17.wlib.command.node.primitive.number.IntegerCommandNode;
import com.wizardlybump17.wlib.command.node.primitive.number.LongCommandNode;
import com.wizardlybump17.wlib.command.node.primitive.number.ShortCommandNode;
import com.wizardlybump17.wlib.command.result.CommandResult;
import com.wizardlybump17.wlib.command.sender.BasicCommandSender;
import com.wizardlybump17.wlib.command.sender.CommandSender;
import com.wizardlybump17.wlib.command.suggestion.primitive.number.ByteSuggester;
import com.wizardlybump17.wlib.command.suggestion.primitive.number.IntegerSuggester;
import com.wizardlybump17.wlib.command.suggestion.primitive.number.LongSuggester;
import com.wizardlybump17.wlib.command.suggestion.primitive.number.ShortSuggester;
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
                                        List.of(),
                                        context -> CommandResult.successful(context, "hello world"),
                                        null
                                )
                        ),
                        null,
                        null
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
                                        List.of(),
                                        context -> CommandResult.successful(context, "hello world"),
                                        null
                                )
                        ),
                        null,
                        null
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
                                        List.of(),
                                        context -> CommandResult.successful(context, "hello world"),
                                        null
                                ),
                                new LiteralCommandNode(
                                        "hi",
                                        List.of(),
                                        context -> CommandResult.successful(context, "hello hi"),
                                        null
                                ),
                                new LiteralCommandNode(
                                        "there",
                                        List.of(),
                                        context -> CommandResult.successful(context, "hello there"),
                                        null
                                )
                        ),
                        null,
                        null
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
                                        List.of(),
                                        context -> CommandResult.successful(context, "hello world"),
                                        null
                                ),
                                new LiteralCommandNode(
                                        "hi",
                                        List.of(),
                                        context -> CommandResult.successful(context, "hello hi"),
                                        null
                                ),
                                new IntegerCommandNode(
                                        "there",
                                        List.of(),
                                        AllowedIntegerInputs.range(1, 10),
                                        IntegerSuggester.range(1, 10),
                                        context -> CommandResult.successful(context, "hello there"),
                                        null
                                )
                        ),
                        null,
                        null
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
                                        List.of(),
                                        context -> CommandResult.successful(context, "hello world"),
                                        null
                                ),
                                new LiteralCommandNode(
                                        "hi",
                                        List.of(),
                                        context -> CommandResult.successful(context, "hello hi"),
                                        null
                                ),
                                new IntegerCommandNode(
                                        "there",
                                        List.of(),
                                        AllowedIntegerInputs.range(1, 10),
                                        IntegerSuggester.range(1, 10),
                                        context -> CommandResult.successful(context, "hello there"),
                                        null
                                ),
                                new LiteralCommandNode(
                                        "happy",
                                        List.of(),
                                        context -> CommandResult.successful(context, "hello happy"),
                                        null
                                )
                        ),
                        null,
                        null
                )
        );

        List<Object> expected = List.of("world", "hi", "happy");
        List<Object> actual = command.getSuggestions(CHAD_SENDER, List.of("hello", "h"));

        Assertions.assertEquals(expected, actual);
    }

    @Test
    void testDefaultSuggestions() {
        //literal
        Assertions.assertEquals(
                List.of("hello"),
                new Command(
                        new LiteralCommandNode(
                                "hello",
                                List.of(),
                                null,
                                null
                        )
                ).getSuggestions(CHAD_SENDER, List.of())
        );
        Assertions.assertEquals(
                List.of("hello"),
                new Command(
                        new LiteralCommandNode(
                                "hello",
                                List.of(),
                                null,
                                null
                        )
                ).getSuggestions(CHAD_SENDER, List.of(""))
        );
        Assertions.assertEquals(
                List.of("hello"),
                new Command(
                        new LiteralCommandNode(
                                "hello",
                                List.of(),
                                null,
                                null
                        )
                ).getSuggestions(CHAD_SENDER, List.of("hel"))
        );

        //byte
        Assertions.assertEquals(
                List.of((byte) -128, (byte) -100, (byte) -50, (byte) 0, (byte) 50, (byte) 100, (byte) 127),
                new Command(
                        new LiteralCommandNode(
                                "hello",
                                List.of(
                                        new ByteCommandNode(
                                                "world",
                                                List.of(),
                                                AllowedByteInputs.unlimited(),
                                                ByteSuggester.unlimited(),
                                                null,
                                                null
                                        )
                                ),
                                null,
                                null
                        )
                ).getSuggestions(CHAD_SENDER, List.of("hello", ""))
        );
        Assertions.assertEquals(
                List.of((byte) -128, (byte) -100, (byte) -50, (byte) 0, (byte) 50, (byte) 100, (byte) 127),
                new Command(
                        new LiteralCommandNode(
                                "hello",
                                List.of(
                                        new ByteCommandNode(
                                                "world",
                                                List.of(),
                                                AllowedByteInputs.unlimited(),
                                                ByteSuggester.unlimited(),
                                                null,
                                                null
                                        )
                                ),
                                null,
                                null
                        )
                ).getSuggestions(CHAD_SENDER, List.of("hello", "0"))
        );
        Assertions.assertEquals(
                List.of((byte) -128, (byte) -100, (byte) -50, (byte) 0, (byte) 50, (byte) 100, (byte) 127),
                new Command(
                        new LiteralCommandNode(
                                "hello",
                                List.of(
                                        new ByteCommandNode(
                                                "world",
                                                List.of(),
                                                AllowedByteInputs.unlimited(),
                                                ByteSuggester.unlimited(),
                                                null,
                                                null
                                        )
                                ),
                                null,
                                null
                        )
                ).getSuggestions(CHAD_SENDER, List.of("hello", "10"))
        );

        //short
        Assertions.assertEquals(
                List.of((short) -32768, (short) -3000, (short) -100, (short) -50, (short) 0, (short) 50, (short) 100, (short) 3000, (short) 32767),
                new Command(
                        new LiteralCommandNode(
                                "hello",
                                List.of(
                                        new ShortCommandNode(
                                                "world",
                                                List.of(),
                                                AllowedShortInputs.unlimited(),
                                                ShortSuggester.unlimited(),
                                                null,
                                                null
                                        )
                                ),
                                null,
                                null
                        )
                ).getSuggestions(CHAD_SENDER, List.of("hello", ""))
        );
        Assertions.assertEquals(
                List.of((short) -32768, (short) -3000, (short) -100, (short) -50, (short) 0, (short) 50, (short) 100, (short) 3000, (short) 32767),
                new Command(
                        new LiteralCommandNode(
                                "hello",
                                List.of(
                                        new ShortCommandNode(
                                                "world",
                                                List.of(),
                                                AllowedShortInputs.unlimited(),
                                                ShortSuggester.unlimited(),
                                                null,
                                                null
                                        )
                                ),
                                null,
                                null
                        )
                ).getSuggestions(CHAD_SENDER, List.of("hello", "0"))
        );
        Assertions.assertEquals(
                List.of((short) -32768, (short) -3000, (short) -100, (short) -50, (short) 0, (short) 50, (short) 100, (short) 3000, (short) 32767),
                new Command(
                        new LiteralCommandNode(
                                "hello",
                                List.of(
                                        new ShortCommandNode(
                                                "world",
                                                List.of(),
                                                AllowedShortInputs.unlimited(),
                                                ShortSuggester.unlimited(),
                                                null,
                                                null
                                        )
                                ),
                                null,
                                null
                        )
                ).getSuggestions(CHAD_SENDER, List.of("hello", "10"))
        );

        //int
        Assertions.assertEquals(
                List.of(-100000, -50000, -3000, -200, -50, 0, 50, 200, 3000, 50000, 100000),
                new Command(
                        new LiteralCommandNode(
                                "hello",
                                List.of(
                                        new IntegerCommandNode(
                                                "world",
                                                List.of(),
                                                AllowedIntegerInputs.unlimited(),
                                                IntegerSuggester.unlimited(),
                                                null,
                                                null
                                        )
                                ),
                                null,
                                null
                        )
                ).getSuggestions(CHAD_SENDER, List.of("hello", ""))
        );
        Assertions.assertEquals(
                List.of(-100000, -50000, -3000, -200, -50, 0, 50, 200, 3000, 50000, 100000),
                new Command(
                        new LiteralCommandNode(
                                "hello",
                                List.of(
                                        new IntegerCommandNode(
                                                "world",
                                                List.of(),
                                                AllowedIntegerInputs.unlimited(),
                                                IntegerSuggester.unlimited(),
                                                null,
                                                null
                                        )
                                ),
                                null,
                                null
                        )
                ).getSuggestions(CHAD_SENDER, List.of("hello", "0"))
        );
        Assertions.assertEquals(
                List.of(-100000, -50000, -3000, -200, -50, 0, 50, 200, 3000, 50000, 100000),
                new Command(
                        new LiteralCommandNode(
                                "hello",
                                List.of(
                                        new IntegerCommandNode(
                                                "world",
                                                List.of(),
                                                AllowedIntegerInputs.unlimited(),
                                                IntegerSuggester.unlimited(),
                                                null,
                                                null
                                        )
                                ),
                                null,
                                null
                        )
                ).getSuggestions(CHAD_SENDER, List.of("hello", "10"))
        );

        //long
        Assertions.assertEquals(
                List.of(-100000L, -50000L, -3000L, -200L, -50L, 0L, 50L, 200L, 3000L, 50000L, 100000L),
                new Command(
                        new LiteralCommandNode(
                                "hello",
                                List.of(
                                        new LongCommandNode(
                                                "world",
                                                List.of(),
                                                AllowedLongInputs.unlimited(),
                                                LongSuggester.unlimited(),
                                                null,
                                                null
                                        )
                                ),
                                null,
                                null
                        )
                ).getSuggestions(CHAD_SENDER, List.of("hello", ""))
        );
        Assertions.assertEquals(
                List.of(-100000L, -50000L, -3000L, -200L, -50L, 0L, 50L, 200L, 3000L, 50000L, 100000L),
                new Command(
                        new LiteralCommandNode(
                                "hello",
                                List.of(
                                        new LongCommandNode(
                                                "world",
                                                List.of(),
                                                AllowedLongInputs.unlimited(),
                                                LongSuggester.unlimited(),
                                                null,
                                                null
                                        )
                                ),
                                null,
                                null
                        )
                ).getSuggestions(CHAD_SENDER, List.of("hello", "0"))
        );
        Assertions.assertEquals(
                List.of(-100000L, -50000L, -3000L, -200L, -50L, 0L, 50L, 200L, 3000L, 50000L, 100000L),
                new Command(
                        new LiteralCommandNode(
                                "hello",
                                List.of(
                                        new LongCommandNode(
                                                "world",
                                                List.of(),
                                                AllowedLongInputs.unlimited(),
                                                LongSuggester.unlimited(),
                                                null,
                                                null
                                        )
                                ),
                                null,
                                null
                        )
                ).getSuggestions(CHAD_SENDER, List.of("hello", "10"))
        );
    }
}
