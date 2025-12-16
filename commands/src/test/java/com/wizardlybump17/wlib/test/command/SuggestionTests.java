package com.wizardlybump17.wlib.test.command;

import com.wizardlybump17.wlib.command.Command;
import com.wizardlybump17.wlib.command.input.object.AllowedUUIDInputs;
import com.wizardlybump17.wlib.command.input.primitive.number.*;
import com.wizardlybump17.wlib.command.input.string.AllowedStringInputs;
import com.wizardlybump17.wlib.command.node.LiteralCommandNode;
import com.wizardlybump17.wlib.command.node.object.UUIDCommandNode;
import com.wizardlybump17.wlib.command.node.primitive.number.*;
import com.wizardlybump17.wlib.command.node.string.StringCommandNode;
import com.wizardlybump17.wlib.command.result.CommandResult;
import com.wizardlybump17.wlib.command.sender.BasicCommandSender;
import com.wizardlybump17.wlib.command.sender.CommandSender;
import com.wizardlybump17.wlib.command.suggestion.object.UUIDSuggester;
import com.wizardlybump17.wlib.command.suggestion.primitive.number.*;
import com.wizardlybump17.wlib.command.suggestion.string.StringSuggester;
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

        List<String> expected = List.of("hello");
        List<String> actual = Assertions.assertDoesNotThrow(() -> command.getSuggestions(CHAD_SENDER, List.of()));

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

        List<String> expected = List.of("hello");
        List<String> actual = Assertions.assertDoesNotThrow(() -> command.getSuggestions(CHAD_SENDER, List.of("he")));

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

        List<String> expected = List.of("world", "hi", "there");
        List<String> actual = Assertions.assertDoesNotThrow(() -> command.getSuggestions(CHAD_SENDER, List.of("hello", "")));

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

        List<String> expected = List.of("world", "hi", "1", "3", "5", "7", "10");
        List<String> actual = Assertions.assertDoesNotThrow(() -> command.getSuggestions(CHAD_SENDER, List.of("hello", "")));

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

        List<String> expected = List.of("world", "hi", "1", "3", "5", "7", "10", "happy");
        List<String> actual = Assertions.assertDoesNotThrow(() -> command.getSuggestions(CHAD_SENDER, List.of("hello", "h")));

        Assertions.assertEquals(expected, actual);
    }

    @Test
    void testDefaultSuggestions() {
        //literal
        Assertions.assertEquals(
                List.of("hello"),
                Assertions.assertDoesNotThrow(() -> new Command(
                        new LiteralCommandNode(
                                "hello",
                                List.of(),
                                null,
                                null
                        )
                ).getSuggestions(CHAD_SENDER, List.of()))
        );
        Assertions.assertEquals(
                List.of("hello"),
                Assertions.assertDoesNotThrow(() -> new Command(
                        new LiteralCommandNode(
                                "hello",
                                List.of(),
                                null,
                                null
                        )
                ).getSuggestions(CHAD_SENDER, List.of("")))
        );
        Assertions.assertEquals(
                List.of("hello"),
                Assertions.assertDoesNotThrow(() -> new Command(
                        new LiteralCommandNode(
                                "hello",
                                List.of(),
                                null,
                                null
                        )
                ).getSuggestions(CHAD_SENDER, List.of("hel")))
        );

        //byte
        Assertions.assertEquals(
                List.of("-128", "-100", "-50", "0", "50", "100", "127"),
                Assertions.assertDoesNotThrow(() -> new Command(
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
                ).getSuggestions(CHAD_SENDER, List.of("hello", "")))
        );
        Assertions.assertEquals(
                List.of("-128", "-100", "-50", "0", "50", "100", "127"),
                Assertions.assertDoesNotThrow(() -> new Command(
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
                ).getSuggestions(CHAD_SENDER, List.of("hello", "0")))
        );
        Assertions.assertEquals(
                List.of("-128", "-100", "-50", "0", "50", "100", "127"),
                Assertions.assertDoesNotThrow(() -> new Command(
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
                ).getSuggestions(CHAD_SENDER, List.of("hello", "10")))
        );

        //short
        Assertions.assertEquals(
                List.of("-32768", "-3000", "-100", "-50", "0", "50", "100", "3000", "32767"),
                Assertions.assertDoesNotThrow(() -> new Command(
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
                ).getSuggestions(CHAD_SENDER, List.of("hello", "")))
        );
        Assertions.assertEquals(
                List.of("-32768", "-3000", "-100", "-50", "0", "50", "100", "3000", "32767"),
                Assertions.assertDoesNotThrow(() -> new Command(
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
                ).getSuggestions(CHAD_SENDER, List.of("hello", "0")))
        );
        Assertions.assertEquals(
                List.of("-32768", "-3000", "-100", "-50", "0", "50", "100", "3000", "32767"),
                Assertions.assertDoesNotThrow(() -> new Command(
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
                ).getSuggestions(CHAD_SENDER, List.of("hello", "10")))
        );

        //int
        Assertions.assertEquals(
                List.of("-100000", "-50000", "-3000", "-200", "-50", "0", "50", "200", "3000", "50000", "100000"),
                Assertions.assertDoesNotThrow(() -> new Command(
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
                ).getSuggestions(CHAD_SENDER, List.of("hello", "")))
        );
        Assertions.assertEquals(
                List.of("-100000", "-50000", "-3000", "-200", "-50", "0", "50", "200", "3000", "50000", "100000"),
                Assertions.assertDoesNotThrow(() -> new Command(
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
                ).getSuggestions(CHAD_SENDER, List.of("hello", "0")))
        );
        Assertions.assertEquals(
                List.of("-100000", "-50000", "-3000", "-200", "-50", "0", "50", "200", "3000", "50000", "100000"),
                Assertions.assertDoesNotThrow(() -> new Command(
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
                ).getSuggestions(CHAD_SENDER, List.of("hello", "10")))
        );

        //long
        Assertions.assertEquals(
                List.of("-100000", "-50000", "-3000", "-200", "-50", "0", "50", "200", "3000", "50000", "100000"),
                Assertions.assertDoesNotThrow(() -> new Command(
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
                ).getSuggestions(CHAD_SENDER, List.of("hello", "")))
        );
        Assertions.assertEquals(
                List.of("-100000", "-50000", "-3000", "-200", "-50", "0", "50", "200", "3000", "50000", "100000"),
                Assertions.assertDoesNotThrow(() -> new Command(
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
                ).getSuggestions(CHAD_SENDER, List.of("hello", "0")))
        );
        Assertions.assertEquals(
                List.of("-100000", "-50000", "-3000", "-200", "-50", "0", "50", "200", "3000", "50000", "100000"),
                Assertions.assertDoesNotThrow(() -> new Command(
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
                ).getSuggestions(CHAD_SENDER, List.of("hello", "10")))
        );

        //float
        Assertions.assertEquals(
                List.of("-100000.5", "-50000.5", "-3000.5", "-200.5", "-50.5", "0.0", "50.5", "200.5", "3000.5", "50000.5", "100000.5"),
                Assertions.assertDoesNotThrow(() -> new Command(
                        new LiteralCommandNode(
                                "hello",
                                List.of(
                                        new FloatCommandNode(
                                                "world",
                                                List.of(),
                                                AllowedFloatInputs.unlimited(),
                                                FloatSuggester.unlimited(),
                                                null,
                                                null
                                        )
                                ),
                                null,
                                null
                        )
                ).getSuggestions(CHAD_SENDER, List.of("hello", "")))
        );
        Assertions.assertEquals(
                List.of("-100000.5", "-50000.5", "-3000.5", "-200.5", "-50.5", "0.0", "50.5", "200.5", "3000.5", "50000.5", "100000.5"),
                Assertions.assertDoesNotThrow(() -> new Command(
                        new LiteralCommandNode(
                                "hello",
                                List.of(
                                        new FloatCommandNode(
                                                "world",
                                                List.of(),
                                                AllowedFloatInputs.unlimited(),
                                                FloatSuggester.unlimited(),
                                                null,
                                                null
                                        )
                                ),
                                null,
                                null
                        )
                ).getSuggestions(CHAD_SENDER, List.of("hello", "0")))
        );
        Assertions.assertEquals(
                List.of("-100000.5", "-50000.5", "-3000.5", "-200.5", "-50.5", "0.0", "50.5", "200.5", "3000.5", "50000.5", "100000.5"),
                Assertions.assertDoesNotThrow(() -> new Command(
                        new LiteralCommandNode(
                                "hello",
                                List.of(
                                        new FloatCommandNode(
                                                "world",
                                                List.of(),
                                                AllowedFloatInputs.unlimited(),
                                                FloatSuggester.unlimited(),
                                                null,
                                                null
                                        )
                                ),
                                null,
                                null
                        )
                ).getSuggestions(CHAD_SENDER, List.of("hello", "10")))
        );

        //double
        Assertions.assertEquals(
                List.of("-100000.5", "-50000.5", "-3000.5", "-200.5", "-50.5", "0.0", "50.5", "200.5", "3000.5", "50000.5", "100000.5"),
                Assertions.assertDoesNotThrow(() -> new Command(
                        new LiteralCommandNode(
                                "hello",
                                List.of(
                                        new DoubleCommandNode(
                                                "world",
                                                List.of(),
                                                AllowedDoubleInputs.unlimited(),
                                                DoubleSuggester.unlimited(),
                                                null,
                                                null
                                        )
                                ),
                                null,
                                null
                        )
                ).getSuggestions(CHAD_SENDER, List.of("hello", "")))
        );
        Assertions.assertEquals(
                List.of("-100000.5", "-50000.5", "-3000.5", "-200.5", "-50.5", "0.0", "50.5", "200.5", "3000.5", "50000.5", "100000.5"),
                Assertions.assertDoesNotThrow(() -> new Command(
                        new LiteralCommandNode(
                                "hello",
                                List.of(
                                        new DoubleCommandNode(
                                                "world",
                                                List.of(),
                                                AllowedDoubleInputs.unlimited(),
                                                DoubleSuggester.unlimited(),
                                                null,
                                                null
                                        )
                                ),
                                null,
                                null
                        )
                ).getSuggestions(CHAD_SENDER, List.of("hello", "0")))
        );
        Assertions.assertEquals(
                List.of("-100000.5", "-50000.5", "-3000.5", "-200.5", "-50.5", "0.0", "50.5", "200.5", "3000.5", "50000.5", "100000.5"),
                Assertions.assertDoesNotThrow(() -> new Command(
                        new LiteralCommandNode(
                                "hello",
                                List.of(
                                        new DoubleCommandNode(
                                                "world",
                                                List.of(),
                                                AllowedDoubleInputs.unlimited(),
                                                DoubleSuggester.unlimited(),
                                                null,
                                                null
                                        )
                                ),
                                null,
                                null
                        )
                ).getSuggestions(CHAD_SENDER, List.of("hello", "10")))
        );

        //String
        Assertions.assertEquals(
                List.of(""),
                Assertions.assertDoesNotThrow(() -> new Command(
                        new LiteralCommandNode(
                                "hello",
                                List.of(
                                        new StringCommandNode(
                                                "world",
                                                List.of(),
                                                AllowedStringInputs.anyNullable(),
                                                StringSuggester.any(),
                                                null,
                                                null
                                        )
                                ),
                                null,
                                null
                        )
                ).getSuggestions(CHAD_SENDER, List.of("hello", "")))
        );
        Assertions.assertEquals(
                List.of("wo"),
                Assertions.assertDoesNotThrow(() -> new Command(
                        new LiteralCommandNode(
                                "hello",
                                List.of(
                                        new StringCommandNode(
                                                "world",
                                                List.of(),
                                                AllowedStringInputs.anyNullable(),
                                                StringSuggester.any(),
                                                null,
                                                null
                                        )
                                ),
                                null,
                                null
                        )
                ).getSuggestions(CHAD_SENDER, List.of("hello", "wo")))
        );
        Assertions.assertEquals(
                List.of("spaced "),
                Assertions.assertDoesNotThrow(() -> new Command(
                        new LiteralCommandNode(
                                "hello",
                                List.of(
                                        new StringCommandNode(
                                                "world",
                                                List.of(),
                                                AllowedStringInputs.anyNullable(),
                                                StringSuggester.any(),
                                                null,
                                                null
                                        )
                                ),
                                null,
                                null
                        )
                ).getSuggestions(CHAD_SENDER, List.of("hello", "spaced ")))
        );
        Assertions.assertEquals(
                List.of("spaced string"),
                Assertions.assertDoesNotThrow(() -> new Command(
                        new LiteralCommandNode(
                                "hello",
                                List.of(
                                        new StringCommandNode(
                                                "world",
                                                List.of(),
                                                AllowedStringInputs.anyNullable(),
                                                StringSuggester.any(),
                                                null,
                                                null
                                        )
                                ),
                                null,
                                null
                        )
                ).getSuggestions(CHAD_SENDER, List.of("hello", "spaced string")))
        );

        //UUID
        Assertions.assertEquals(
                List.of("9b07bd8a-a4c0-3681-997f-6b6df78c0abe", "2931e955-084c-3d9e-aea4-8e5c2c1089c1", "2931e955-084c-3d9e-aea4-8e5c2c1089c1"),
                Assertions.assertDoesNotThrow(() -> new Command(
                        new LiteralCommandNode(
                                "hello",
                                List.of(
                                        new UUIDCommandNode(
                                                "world",
                                                List.of(),
                                                AllowedUUIDInputs.anyNullable(),
                                                UUIDSuggester.values(
                                                        UUID.nameUUIDFromBytes("WizardlyBump17".getBytes()),
                                                        UUID.nameUUIDFromBytes("WizardlyBump18".getBytes()),
                                                        UUID.nameUUIDFromBytes("WizardlyBump18".getBytes())
                                                ),
                                                null,
                                                null
                                        )
                                ),
                                null,
                                null
                        )
                ).getSuggestions(CHAD_SENDER, List.of("hello", "")))
        );
        Assertions.assertEquals(
                List.of("9b07bd8a-a4c0-3681-997f-6b6df78c0abe", "2931e955-084c-3d9e-aea4-8e5c2c1089c1", "2931e955-084c-3d9e-aea4-8e5c2c1089c1"),
                Assertions.assertDoesNotThrow(() -> new Command(
                        new LiteralCommandNode(
                                "hello",
                                List.of(
                                        new UUIDCommandNode(
                                                "world",
                                                List.of(),
                                                AllowedUUIDInputs.anyNullable(),
                                                UUIDSuggester.values(
                                                        UUID.nameUUIDFromBytes("WizardlyBump17".getBytes()),
                                                        UUID.nameUUIDFromBytes("WizardlyBump18".getBytes()),
                                                        UUID.nameUUIDFromBytes("WizardlyBump18".getBytes())
                                                ),
                                                null,
                                                null
                                        )
                                ),
                                null,
                                null
                        )
                ).getSuggestions(CHAD_SENDER, List.of("hello", "9b07bd8a-a4c0")))
        );
    }
}
