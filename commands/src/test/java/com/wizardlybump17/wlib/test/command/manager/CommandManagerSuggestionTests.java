package com.wizardlybump17.wlib.test.command.manager;

import com.wizardlybump17.wlib.command.Command;
import com.wizardlybump17.wlib.command.input.primitive.number.AllowedIntegerInputs;
import com.wizardlybump17.wlib.command.manager.CommandManager;
import com.wizardlybump17.wlib.command.node.LiteralCommandNode;
import com.wizardlybump17.wlib.command.node.primitive.number.IntegerCommandNode;
import com.wizardlybump17.wlib.command.result.CommandResult;
import com.wizardlybump17.wlib.command.sender.BasicCommandSender;
import com.wizardlybump17.wlib.command.sender.CommandSender;
import com.wizardlybump17.wlib.command.suggestion.primitive.number.IntegerSuggester;
import com.wizardlybump17.wlib.util.CollectionUtil;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.UUID;
import java.util.function.Consumer;

class CommandManagerSuggestionTests {

    static final @NotNull Consumer<String> SENDER_MESSAGE_CONSUMER = System.out::println;
    static final @NotNull CommandSender<Object> CHAD_SENDER = new BasicCommandSender<>(new Object(), "Chad", UUID.nameUUIDFromBytes("Chad".getBytes()), SENDER_MESSAGE_CONSUMER, $ -> true);
    static final @NotNull CommandSender<Object> BETA_SENDER = new BasicCommandSender<>(new Object(), "Beta", UUID.nameUUIDFromBytes("Beta".getBytes()), SENDER_MESSAGE_CONSUMER, $ -> false);

    @Test
    void testSuggestionsSuccessListChad0() {
        Command command0 = new Command(
                new LiteralCommandNode(
                        "hello",
                        List.of(),
                        context -> CommandResult.successful("hello"),
                        "permission"
                )
        );
        Command command1 = new Command(
                new LiteralCommandNode(
                        "hi",
                        List.of(
                                new LiteralCommandNode(
                                        "there",
                                        List.of(),
                                        context -> CommandResult.successful("hi there"),
                                        "permission"
                                )
                        ),
                        null,
                        "permission"
                )
        );
        Command command2 = new Command(
                new LiteralCommandNode(
                        "welcome",
                        List.of(),
                        context -> CommandResult.successful("welcome"),
                        "permission"
                )
        );

        CommandManager manager = new CommandManager();
        manager.registerCommand("test", command0);
        manager.registerCommand("test", command1);
        manager.registerCommand("test", command2);

        List<String> expected = List.of("hello", "hi", "welcome");
        List<String> actual = Assertions.assertDoesNotThrow(() -> manager.getSuggestions(CHAD_SENDER, List.of()));

        Assertions.assertTrue(CollectionUtil.contentEquals(expected, actual));
    }

    @Test
    void testSuggestionsSuccessListChad1() {
        Command command0 = new Command(
                new LiteralCommandNode(
                        "hello",
                        List.of(),
                        context -> CommandResult.successful("hello"),
                        "permission"
                )
        );
        Command command1 = new Command(
                new LiteralCommandNode(
                        "hi",
                        List.of(
                                new LiteralCommandNode(
                                        "there",
                                        List.of(),
                                        context -> CommandResult.successful("hi there"),
                                        "permission"
                                )
                        ),
                        null,
                        "permission"
                )
        );
        Command command2 = new Command(
                new LiteralCommandNode(
                        "welcome",
                        List.of(),
                        context -> CommandResult.successful("welcome"),
                        "permission"
                )
        );

        CommandManager manager = new CommandManager();
        manager.registerCommand("test", command0);
        manager.registerCommand("test", command1);
        manager.registerCommand("test", command2);

        List<String> expected = List.of("hello", "hi", "welcome");
        List<String> actual = Assertions.assertDoesNotThrow(() -> manager.getSuggestions(CHAD_SENDER, List.of("he")));

        Assertions.assertTrue(CollectionUtil.contentEquals(expected, actual));
    }

    @Test
    void testSuggestionsSuccessListChad2() {
        Command command0 = new Command(
                new LiteralCommandNode(
                        "hello",
                        List.of(),
                        context -> CommandResult.successful("hello"),
                        "permission"
                )
        );
        Command command1 = new Command(
                new LiteralCommandNode(
                        "hi",
                        List.of(
                                new LiteralCommandNode(
                                        "there",
                                        List.of(),
                                        context -> CommandResult.successful("hi there"),
                                        "permission"
                                )
                        ),
                        null,
                        "permission"
                )
        );
        Command command2 = new Command(
                new LiteralCommandNode(
                        "welcome",
                        List.of(),
                        context -> CommandResult.successful("welcome"),
                        "permission"
                )
        );

        CommandManager manager = new CommandManager();
        manager.registerCommand("test", command0);
        manager.registerCommand("test", command1);
        manager.registerCommand("test", command2);

        List<String> expected = List.of("there");
        List<String> actual = Assertions.assertDoesNotThrow(() -> manager.getSuggestions(CHAD_SENDER, List.of("hi", "")));

        Assertions.assertTrue(CollectionUtil.contentEquals(expected, actual));
    }

    @Test
    void testSuggestionsSuccessListChad3() {
        Command command0 = new Command(
                new LiteralCommandNode(
                        "hello",
                        List.of(),
                        context -> CommandResult.successful("hello"),
                        "permission"
                )
        );
        Command command1 = new Command(
                new LiteralCommandNode(
                        "hi",
                        List.of(
                                new LiteralCommandNode(
                                        "there",
                                        List.of(),
                                        context -> CommandResult.successful("hi there"),
                                        "permission"
                                )
                        ),
                        null,
                        "permission"
                )
        );
        Command command2 = new Command(
                new LiteralCommandNode(
                        "welcome",
                        List.of(),
                        context -> CommandResult.successful("welcome"),
                        "permission"
                )
        );

        CommandManager manager = new CommandManager();
        manager.registerCommand("test", command0);
        manager.registerCommand("test", command1);
        manager.registerCommand("test", command2);

        List<String> expected = List.of("there");
        List<String> actual = Assertions.assertDoesNotThrow(() -> manager.getSuggestions(CHAD_SENDER, List.of("hi", "ther")));

        Assertions.assertTrue(CollectionUtil.contentEquals(expected, actual));
    }

    @Test
    void testSuggestionsSuccessListChad4() {
        Command command0 = new Command(
                new LiteralCommandNode(
                        "hello",
                        List.of(),
                        context -> CommandResult.successful("hello"),
                        "permission"
                )
        );
        Command command1 = new Command(
                new LiteralCommandNode(
                        "hi",
                        List.of(
                                new LiteralCommandNode(
                                        "there",
                                        List.of(),
                                        context -> CommandResult.successful("hi there"),
                                        "permission"
                                )
                        ),
                        null,
                        "permission"
                )
        );
        Command command2 = new Command(
                new LiteralCommandNode(
                        "welcome",
                        List.of(
                                new IntegerCommandNode(
                                        "repeat",
                                        List.of(),
                                        AllowedIntegerInputs.positive(),
                                        IntegerSuggester.positive(),
                                        context -> CommandResult.successful("welcome".repeat(context.arguments().<Integer>getArgument("repeat").orElseThrow().data())),
                                        "permission"
                                )
                        ),
                        context -> CommandResult.successful("welcome"),
                        "permission"
                )
        );

        CommandManager manager = new CommandManager();
        manager.registerCommand("test", command0);
        manager.registerCommand("test", command1);
        manager.registerCommand("test", command2);

        List<String> expected = List.of("0", "50", "200", "3000", "50000", "100000");
        List<String> actual = Assertions.assertDoesNotThrow(() -> manager.getSuggestions(CHAD_SENDER, List.of("welcome", "")));

        Assertions.assertEquals(expected, actual);
    }

    @Test
    void testSuggestionsSuccessListChad6() {
        Command command0 = new Command(
                new LiteralCommandNode(
                        "hello",
                        List.of(),
                        context -> CommandResult.successful("hello"),
                        "permission"
                )
        );
        Command command1 = new Command(
                new LiteralCommandNode(
                        "hi",
                        List.of(
                                new LiteralCommandNode(
                                        "there",
                                        List.of(),
                                        context -> CommandResult.successful("hi there"),
                                        "permission"
                                )
                        ),
                        null,
                        "permission"
                )
        );
        Command command2 = new Command(
                new LiteralCommandNode(
                        "welcome",
                        List.of(
                                new IntegerCommandNode(
                                        "repeat",
                                        List.of(),
                                        AllowedIntegerInputs.positive(),
                                        IntegerSuggester.positive(),
                                        context -> CommandResult.successful("welcome".repeat(context.arguments().<Integer>getArgument("repeat").orElseThrow().data())),
                                        "permission"
                                )
                        ),
                        context -> CommandResult.successful("welcome"),
                        "permission"
                )
        );

        CommandManager manager = new CommandManager();
        manager.registerCommand("test", command0);
        manager.registerCommand("test", command1);
        manager.registerCommand("test", command2);

        List<String> expected = List.of("0", "50", "200", "3000", "50000", "100000");
        List<String> actual = Assertions.assertDoesNotThrow(() -> manager.getSuggestions(CHAD_SENDER, List.of("welcome", "10")));

        Assertions.assertTrue(CollectionUtil.contentEquals(expected, actual));
    }

    @Test
    void testSuggestionsFailListBeta0() {
        Command command0 = new Command(
                new LiteralCommandNode(
                        "hello",
                        List.of(),
                        context -> CommandResult.successful("hello"),
                        "permission"
                )
        );
        Command command1 = new Command(
                new LiteralCommandNode(
                        "hi",
                        List.of(
                                new LiteralCommandNode(
                                        "there",
                                        List.of(),
                                        context -> CommandResult.successful("hi there"),
                                        "permission"
                                )
                        ),
                        null,
                        "permission"
                )
        );
        Command command2 = new Command(
                new LiteralCommandNode(
                        "welcome",
                        List.of(),
                        context -> CommandResult.successful("welcome"),
                        "permission"
                )
        );

        CommandManager manager = new CommandManager();
        manager.registerCommand("test", command0);
        manager.registerCommand("test", command1);
        manager.registerCommand("test", command2);

        List<String> expected = List.of();
        List<String> actual = Assertions.assertDoesNotThrow(() -> manager.getSuggestions(BETA_SENDER, List.of()));

        Assertions.assertTrue(CollectionUtil.contentEquals(expected, actual));
    }

    @Test
    void testSuggestionsSuccessStringChad0() {
        Command command0 = new Command(
                new LiteralCommandNode(
                        "hello",
                        List.of(),
                        context -> CommandResult.successful("hello"),
                        "permission"
                )
        );
        Command command1 = new Command(
                new LiteralCommandNode(
                        "hi",
                        List.of(
                                new LiteralCommandNode(
                                        "there",
                                        List.of(),
                                        context -> CommandResult.successful("hi there"),
                                        "permission"
                                )
                        ),
                        null,
                        "permission"
                )
        );
        Command command2 = new Command(
                new LiteralCommandNode(
                        "welcome",
                        List.of(
                                new IntegerCommandNode(
                                        "repeat",
                                        List.of(),
                                        AllowedIntegerInputs.positive(),
                                        IntegerSuggester.positive(),
                                        context -> CommandResult.successful("welcome".repeat(context.arguments().<Integer>getArgument("repeat").orElseThrow().data())),
                                        "permission"
                                )
                        ),
                        context -> CommandResult.successful("welcome"),
                        "permission"
                )
        );

        CommandManager manager = new CommandManager();
        manager.registerCommand("test", command0);
        manager.registerCommand("test", command1);
        manager.registerCommand("test", command2);

        List<String> expected = List.of("hi", "hello", "welcome");
        List<String> actual = Assertions.assertDoesNotThrow(() -> manager.getSuggestions(CHAD_SENDER, ""));

        Assertions.assertEquals(expected, actual);
    }

    @Test
    void testSuggestionsSuccessStringChad1() {
        Command command0 = new Command(
                new LiteralCommandNode(
                        "hello",
                        List.of(),
                        context -> CommandResult.successful("hello"),
                        "permission"
                )
        );
        Command command1 = new Command(
                new LiteralCommandNode(
                        "hi",
                        List.of(
                                new LiteralCommandNode(
                                        "there",
                                        List.of(),
                                        context -> CommandResult.successful("hi there"),
                                        "permission"
                                )
                        ),
                        null,
                        "permission"
                )
        );
        Command command2 = new Command(
                new LiteralCommandNode(
                        "welcome",
                        List.of(
                                new IntegerCommandNode(
                                        "repeat",
                                        List.of(),
                                        AllowedIntegerInputs.positive(),
                                        IntegerSuggester.positive(),
                                        context -> CommandResult.successful("welcome".repeat(context.arguments().<Integer>getArgument("repeat").orElseThrow().data())),
                                        "permission"
                                )
                        ),
                        context -> CommandResult.successful("welcome"),
                        "permission"
                )
        );

        CommandManager manager = new CommandManager();
        manager.registerCommand("test", command0);
        manager.registerCommand("test", command1);
        manager.registerCommand("test", command2);

        List<String> expected = List.of("there");
        List<String> actual = Assertions.assertDoesNotThrow(() -> manager.getSuggestions(CHAD_SENDER, "hi t"));

        Assertions.assertEquals(expected, actual);
    }

    @Test
    void testSuggestionsSuccessStringChad2() {
        Command command0 = new Command(
                new LiteralCommandNode(
                        "hello",
                        List.of(),
                        context -> CommandResult.successful("hello"),
                        "permission"
                )
        );
        Command command1 = new Command(
                new LiteralCommandNode(
                        "hi",
                        List.of(
                                new LiteralCommandNode(
                                        "there",
                                        List.of(),
                                        context -> CommandResult.successful("hi there"),
                                        "permission"
                                )
                        ),
                        null,
                        "permission"
                )
        );
        Command command2 = new Command(
                new LiteralCommandNode(
                        "welcome",
                        List.of(
                                new IntegerCommandNode(
                                        "repeat",
                                        List.of(),
                                        AllowedIntegerInputs.positive(),
                                        IntegerSuggester.positive(),
                                        context -> CommandResult.successful("welcome".repeat(context.arguments().<Integer>getArgument("repeat").orElseThrow().data())),
                                        "permission"
                                )
                        ),
                        context -> CommandResult.successful("welcome"),
                        "permission"
                )
        );

        CommandManager manager = new CommandManager();
        manager.registerCommand("test", command0);
        manager.registerCommand("test", command1);
        manager.registerCommand("test", command2);

        List<String> expected = List.of("0", "50", "200", "3000", "50000", "100000");
        List<String> actual = Assertions.assertDoesNotThrow(() -> manager.getSuggestions(CHAD_SENDER, "welcome 1"));

        Assertions.assertEquals(expected, actual);
    }

    @Test
    void testSuggestionsSuccessStringChad3() {
        Command command0 = new Command(
                new LiteralCommandNode(
                        "hello",
                        List.of(),
                        context -> CommandResult.successful("hello"),
                        "permission"
                )
        );
        Command command1 = new Command(
                new LiteralCommandNode(
                        "hi",
                        List.of(
                                new LiteralCommandNode(
                                        "there",
                                        List.of(),
                                        context -> CommandResult.successful("hi there"),
                                        "permission"
                                )
                        ),
                        null,
                        "permission"
                )
        );
        Command command2 = new Command(
                new LiteralCommandNode(
                        "welcome",
                        List.of(
                                new IntegerCommandNode(
                                        "repeat",
                                        List.of(),
                                        AllowedIntegerInputs.positive(),
                                        IntegerSuggester.positive(),
                                        context -> CommandResult.successful("welcome".repeat(context.arguments().<Integer>getArgument("repeat").orElseThrow().data())),
                                        "permission"
                                )
                        ),
                        context -> CommandResult.successful("welcome"),
                        "permission"
                )
        );
        Command command3 = new Command(
                new LiteralCommandNode(
                        "test",
                        List.of(
                                new LiteralCommandNode(
                                        "spaced string",
                                        List.of(),
                                        context -> CommandResult.successful("spaced string"),
                                        "permission"
                                )
                        ),
                        null,
                        "permission"
                )
        );

        CommandManager manager = new CommandManager();
        manager.registerCommand("test", command0);
        manager.registerCommand("test", command1);
        manager.registerCommand("test", command2);
        manager.registerCommand("test", command3);

        List<String> expected = List.of("spaced string");
        List<String> actual = Assertions.assertDoesNotThrow(() -> manager.getSuggestions(CHAD_SENDER, "test \"spaced "));

        Assertions.assertEquals(expected, actual);
    }

    @Test
    void testSuggestionsSuccessStringArrayChad0() {
        Command command0 = new Command(
                new LiteralCommandNode(
                        "hello",
                        List.of(),
                        context -> CommandResult.successful("hello"),
                        "permission"
                )
        );
        Command command1 = new Command(
                new LiteralCommandNode(
                        "hi",
                        List.of(
                                new LiteralCommandNode(
                                        "there",
                                        List.of(),
                                        context -> CommandResult.successful("hi there"),
                                        "permission"
                                )
                        ),
                        null,
                        "permission"
                )
        );
        Command command2 = new Command(
                new LiteralCommandNode(
                        "welcome",
                        List.of(
                                new IntegerCommandNode(
                                        "repeat",
                                        List.of(),
                                        AllowedIntegerInputs.positive(),
                                        IntegerSuggester.positive(),
                                        context -> CommandResult.successful("welcome".repeat(context.arguments().<Integer>getArgument("repeat").orElseThrow().data())),
                                        "permission"
                                )
                        ),
                        context -> CommandResult.successful("welcome"),
                        "permission"
                )
        );
        Command command3 = new Command(
                new LiteralCommandNode(
                        "test",
                        List.of(
                                new LiteralCommandNode(
                                        "spaced string",
                                        List.of(),
                                        context -> CommandResult.successful("spaced string"),
                                        "permission"
                                )
                        ),
                        null,
                        "permission"
                )
        );

        CommandManager manager = new CommandManager();
        manager.registerCommand("test", command0);
        manager.registerCommand("test", command1);
        manager.registerCommand("test", command2);
        manager.registerCommand("test", command3);

        List<String> expected = List.of("hi", "test", "hello", "welcome");
        List<String> actual = Assertions.assertDoesNotThrow(() -> manager.getSuggestions(CHAD_SENDER, new String[]{}));

        Assertions.assertEquals(expected, actual);
    }

    @Test
    void testSuggestionsSuccessStringArrayChad1() {
        Command command0 = new Command(
                new LiteralCommandNode(
                        "hello",
                        List.of(),
                        context -> CommandResult.successful("hello"),
                        "permission"
                )
        );
        Command command1 = new Command(
                new LiteralCommandNode(
                        "hi",
                        List.of(
                                new LiteralCommandNode(
                                        "there",
                                        List.of(),
                                        context -> CommandResult.successful("hi there"),
                                        "permission"
                                )
                        ),
                        null,
                        "permission"
                )
        );
        Command command2 = new Command(
                new LiteralCommandNode(
                        "welcome",
                        List.of(
                                new IntegerCommandNode(
                                        "repeat",
                                        List.of(),
                                        AllowedIntegerInputs.positive(),
                                        IntegerSuggester.positive(),
                                        context -> CommandResult.successful("welcome".repeat(context.arguments().<Integer>getArgument("repeat").orElseThrow().data())),
                                        "permission"
                                )
                        ),
                        context -> CommandResult.successful("welcome"),
                        "permission"
                )
        );
        Command command3 = new Command(
                new LiteralCommandNode(
                        "test",
                        List.of(
                                new LiteralCommandNode(
                                        "spaced string",
                                        List.of(),
                                        context -> CommandResult.successful("spaced string"),
                                        "permission"
                                )
                        ),
                        null,
                        "permission"
                )
        );

        CommandManager manager = new CommandManager();
        manager.registerCommand("test", command0);
        manager.registerCommand("test", command1);
        manager.registerCommand("test", command2);
        manager.registerCommand("test", command3);

        List<String> expected = List.of("hi", "test", "hello", "welcome");
        List<String> actual = Assertions.assertDoesNotThrow(() -> manager.getSuggestions(CHAD_SENDER, new String[]{"he"}));

        Assertions.assertEquals(expected, actual);
    }

    @Test
    void testSuggestionsSuccessStringArrayChad2() {
        Command command0 = new Command(
                new LiteralCommandNode(
                        "hello",
                        List.of(),
                        context -> CommandResult.successful("hello"),
                        "permission"
                )
        );
        Command command1 = new Command(
                new LiteralCommandNode(
                        "hi",
                        List.of(
                                new LiteralCommandNode(
                                        "there",
                                        List.of(),
                                        context -> CommandResult.successful("hi there"),
                                        "permission"
                                )
                        ),
                        null,
                        "permission"
                )
        );
        Command command2 = new Command(
                new LiteralCommandNode(
                        "welcome",
                        List.of(
                                new IntegerCommandNode(
                                        "repeat",
                                        List.of(),
                                        AllowedIntegerInputs.positive(),
                                        IntegerSuggester.positive(),
                                        context -> CommandResult.successful("welcome".repeat(context.arguments().<Integer>getArgument("repeat").orElseThrow().data())),
                                        "permission"
                                )
                        ),
                        context -> CommandResult.successful("welcome"),
                        "permission"
                )
        );
        Command command3 = new Command(
                new LiteralCommandNode(
                        "test",
                        List.of(
                                new LiteralCommandNode(
                                        "spaced string",
                                        List.of(),
                                        context -> CommandResult.successful("spaced string"),
                                        "permission"
                                )
                        ),
                        null,
                        "permission"
                )
        );

        CommandManager manager = new CommandManager();
        manager.registerCommand("test", command0);
        manager.registerCommand("test", command1);
        manager.registerCommand("test", command2);
        manager.registerCommand("test", command3);

        List<String> expected = List.of("0", "50", "200", "3000", "50000", "100000");
        List<String> actual = Assertions.assertDoesNotThrow(() -> manager.getSuggestions(CHAD_SENDER, new String[]{"welcome", ""}));

        Assertions.assertEquals(expected, actual);
    }

    @Test
    void testSuggestionsSuccessStringArrayChad3() {
        Command command0 = new Command(
                new LiteralCommandNode(
                        "hello",
                        List.of(),
                        context -> CommandResult.successful("hello"),
                        "permission"
                )
        );
        Command command1 = new Command(
                new LiteralCommandNode(
                        "hi",
                        List.of(
                                new LiteralCommandNode(
                                        "there",
                                        List.of(),
                                        context -> CommandResult.successful("hi there"),
                                        "permission"
                                )
                        ),
                        null,
                        "permission"
                )
        );
        Command command2 = new Command(
                new LiteralCommandNode(
                        "welcome",
                        List.of(
                                new IntegerCommandNode(
                                        "repeat",
                                        List.of(),
                                        AllowedIntegerInputs.positive(),
                                        IntegerSuggester.positive(),
                                        context -> CommandResult.successful("welcome".repeat(context.arguments().<Integer>getArgument("repeat").orElseThrow().data())),
                                        "permission"
                                )
                        ),
                        context -> CommandResult.successful("welcome"),
                        "permission"
                )
        );
        Command command3 = new Command(
                new LiteralCommandNode(
                        "test",
                        List.of(
                                new LiteralCommandNode(
                                        "spaced string",
                                        List.of(),
                                        context -> CommandResult.successful("spaced string"),
                                        "permission"
                                )
                        ),
                        null,
                        "permission"
                )
        );

        CommandManager manager = new CommandManager();
        manager.registerCommand("test", command0);
        manager.registerCommand("test", command1);
        manager.registerCommand("test", command2);
        manager.registerCommand("test", command3);

        List<String> expected = List.of("spaced string");
        List<String> actual = Assertions.assertDoesNotThrow(() -> manager.getSuggestions(CHAD_SENDER, new String[]{"test", ""}));

        Assertions.assertEquals(expected, actual);
    }

    @Test
    void testSuggestionsSuccessStringArrayChad4() {
        Command command0 = new Command(
                new LiteralCommandNode(
                        "hello",
                        List.of(),
                        context -> CommandResult.successful("hello"),
                        "permission"
                )
        );
        Command command1 = new Command(
                new LiteralCommandNode(
                        "hi",
                        List.of(
                                new LiteralCommandNode(
                                        "there",
                                        List.of(),
                                        context -> CommandResult.successful("hi there"),
                                        "permission"
                                )
                        ),
                        null,
                        "permission"
                )
        );
        Command command2 = new Command(
                new LiteralCommandNode(
                        "welcome",
                        List.of(
                                new IntegerCommandNode(
                                        "repeat",
                                        List.of(),
                                        AllowedIntegerInputs.positive(),
                                        IntegerSuggester.positive(),
                                        context -> CommandResult.successful("welcome".repeat(context.arguments().<Integer>getArgument("repeat").orElseThrow().data())),
                                        "permission"
                                )
                        ),
                        context -> CommandResult.successful("welcome"),
                        "permission"
                )
        );
        Command command3 = new Command(
                new LiteralCommandNode(
                        "test",
                        List.of(
                                new LiteralCommandNode(
                                        "spaced string",
                                        List.of(),
                                        context -> CommandResult.successful("spaced string"),
                                        "permission"
                                )
                        ),
                        null,
                        "permission"
                )
        );

        CommandManager manager = new CommandManager();
        manager.registerCommand("test", command0);
        manager.registerCommand("test", command1);
        manager.registerCommand("test", command2);
        manager.registerCommand("test", command3);

        List<String> expected = List.of("spaced string");
        List<String> actual = Assertions.assertDoesNotThrow(() -> manager.getSuggestions(CHAD_SENDER, new String[]{"test", "spaced"}));

        Assertions.assertEquals(expected, actual);
    }

    @Test
    void testSuggestionsSuccessStringArrayChad5() {
        Command command0 = new Command(
                new LiteralCommandNode(
                        "hello",
                        List.of(),
                        context -> CommandResult.successful("hello"),
                        "permission"
                )
        );
        Command command1 = new Command(
                new LiteralCommandNode(
                        "hi",
                        List.of(
                                new LiteralCommandNode(
                                        "there",
                                        List.of(),
                                        context -> CommandResult.successful("hi there"),
                                        "permission"
                                )
                        ),
                        null,
                        "permission"
                )
        );
        Command command2 = new Command(
                new LiteralCommandNode(
                        "welcome",
                        List.of(
                                new IntegerCommandNode(
                                        "repeat",
                                        List.of(),
                                        AllowedIntegerInputs.positive(),
                                        IntegerSuggester.positive(),
                                        context -> CommandResult.successful("welcome".repeat(context.arguments().<Integer>getArgument("repeat").orElseThrow().data())),
                                        "permission"
                                )
                        ),
                        context -> CommandResult.successful("welcome"),
                        "permission"
                )
        );
        Command command3 = new Command(
                new LiteralCommandNode(
                        "test",
                        List.of(
                                new LiteralCommandNode(
                                        "spaced string",
                                        List.of(),
                                        context -> CommandResult.successful("spaced string"),
                                        "permission"
                                )
                        ),
                        null,
                        "permission"
                )
        );

        CommandManager manager = new CommandManager();
        manager.registerCommand("test", command0);
        manager.registerCommand("test", command1);
        manager.registerCommand("test", command2);
        manager.registerCommand("test", command3);

        List<String> expected = List.of("spaced string");
        List<String> actual = Assertions.assertDoesNotThrow(() -> manager.getSuggestions(CHAD_SENDER, new String[]{"test", "\"space"}));

        Assertions.assertEquals(expected, actual);
    }

    //TODO: add more tests
}
