package com.wizardlybump17.wlib.command.test.rework.manager;

import com.wizardlybump17.wlib.command.rework.Command;
import com.wizardlybump17.wlib.command.rework.input.AllowedNumberInputs;
import com.wizardlybump17.wlib.command.rework.manager.CommandManager;
import com.wizardlybump17.wlib.command.rework.node.IntegerCommandNode;
import com.wizardlybump17.wlib.command.rework.node.LiteralCommandNode;
import com.wizardlybump17.wlib.command.rework.result.CommandResult;
import com.wizardlybump17.wlib.command.sender.BasicCommandSender;
import com.wizardlybump17.wlib.command.sender.CommandSender;
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
        Command command0 = new Command(new LiteralCommandNode(
                "hello",
                context -> CommandResult.successful(context, "hello"),
                "permission"
        ));
        Command command1 = new Command(
                new LiteralCommandNode(
                        "hi",
                        List.of(
                                new LiteralCommandNode(
                                        "there",
                                        context -> CommandResult.successful(context, "hi there"),
                                        "permission"
                                )
                        ),
                        "permission"
                )
        );
        Command command2 = new Command(
                new LiteralCommandNode(
                        "welcome",
                        context -> CommandResult.successful(context, "welcome"),
                        "permission"
                )
        );

        CommandManager manager = new CommandManager();
        manager.registerCommand("test", command0);
        manager.registerCommand("test", command1);
        manager.registerCommand("test", command2);

        List<Object> expected = List.of("hello", "hi", "welcome");
        List<Object> actual = manager.getSuggestions(CHAD_SENDER, List.of());

        Assertions.assertTrue(CollectionUtil.contentEquals(expected, actual));
    }

    @Test
    void testSuggestionsSuccessListChad1() {
        Command command0 = new Command(new LiteralCommandNode(
                "hello",
                context -> CommandResult.successful(context, "hello"),
                "permission"
        ));
        Command command1 = new Command(
                new LiteralCommandNode(
                        "hi",
                        List.of(
                                new LiteralCommandNode(
                                        "there",
                                        context -> CommandResult.successful(context, "hi there"),
                                        "permission"
                                )
                        ),
                        "permission"
                )
        );
        Command command2 = new Command(
                new LiteralCommandNode(
                        "welcome",
                        context -> CommandResult.successful(context, "welcome"),
                        "permission"
                )
        );

        CommandManager manager = new CommandManager();
        manager.registerCommand("test", command0);
        manager.registerCommand("test", command1);
        manager.registerCommand("test", command2);

        List<Object> expected = List.of("hello", "hi", "welcome");
        List<Object> actual = manager.getSuggestions(CHAD_SENDER, List.of("he"));

        Assertions.assertTrue(CollectionUtil.contentEquals(expected, actual));
    }

    @Test
    void testSuggestionsSuccessListChad2() {
        Command command0 = new Command(new LiteralCommandNode(
                "hello",
                context -> CommandResult.successful(context, "hello"),
                "permission"
        ));
        Command command1 = new Command(
                new LiteralCommandNode(
                        "hi",
                        List.of(
                                new LiteralCommandNode(
                                        "there",
                                        context -> CommandResult.successful(context, "hi there"),
                                        "permission"
                                )
                        ),
                        "permission"
                )
        );
        Command command2 = new Command(
                new LiteralCommandNode(
                        "welcome",
                        context -> CommandResult.successful(context, "welcome"),
                        "permission"
                )
        );

        CommandManager manager = new CommandManager();
        manager.registerCommand("test", command0);
        manager.registerCommand("test", command1);
        manager.registerCommand("test", command2);

        List<Object> expected = List.of("there");
        List<Object> actual = manager.getSuggestions(CHAD_SENDER, List.of("hi", ""));

        Assertions.assertTrue(CollectionUtil.contentEquals(expected, actual));
    }

    @Test
    void testSuggestionsSuccessListChad3() {
        Command command0 = new Command(new LiteralCommandNode(
                "hello",
                context -> CommandResult.successful(context, "hello"),
                "permission"
        ));
        Command command1 = new Command(
                new LiteralCommandNode(
                        "hi",
                        List.of(
                                new LiteralCommandNode(
                                        "there",
                                        context -> CommandResult.successful(context, "hi there"),
                                        "permission"
                                )
                        ),
                        "permission"
                )
        );
        Command command2 = new Command(
                new LiteralCommandNode(
                        "welcome",
                        context -> CommandResult.successful(context, "welcome"),
                        "permission"
                )
        );

        CommandManager manager = new CommandManager();
        manager.registerCommand("test", command0);
        manager.registerCommand("test", command1);
        manager.registerCommand("test", command2);

        List<Object> expected = List.of("there");
        List<Object> actual = manager.getSuggestions(CHAD_SENDER, List.of("hi", "ther"));

        Assertions.assertTrue(CollectionUtil.contentEquals(expected, actual));
    }

    @Test
    void testSuggestionsSuccessListChad4() {
        Command command0 = new Command(new LiteralCommandNode(
                "hello",
                context -> CommandResult.successful(context, "hello"),
                "permission"
        ));
        Command command1 = new Command(
                new LiteralCommandNode(
                        "hi",
                        List.of(
                                new LiteralCommandNode(
                                        "there",
                                        context -> CommandResult.successful(context, "hi there"),
                                        "permission"
                                )
                        ),
                        "permission"
                )
        );
        Command command2 = new Command(
                new LiteralCommandNode(
                        "welcome",
                        List.of(
                                new IntegerCommandNode(
                                        "repeat",
                                        new AllowedNumberInputs.AllowedIntegerInputs.Positive(),
                                        context -> CommandResult.successful(context, "welcome".repeat(context.arguments().<Integer>getArgument("repeat").orElseThrow().data())),
                                        "permission"
                                )
                        ),
                        context -> CommandResult.successful(context, "welcome"),
                        "permission"
                )
        );

        CommandManager manager = new CommandManager();
        manager.registerCommand("test", command0);
        manager.registerCommand("test", command1);
        manager.registerCommand("test", command2);

        List<Object> expected = List.of(0, 50, 200, 3000, 50000, 100000);
        List<Object> actual = manager.getSuggestions(CHAD_SENDER, List.of("welcome", ""));

        Assertions.assertTrue(CollectionUtil.contentEquals(expected, actual));
    }

    @Test
    void testSuggestionsSuccessListChad6() {
        Command command0 = new Command(new LiteralCommandNode(
                "hello",
                context -> CommandResult.successful(context, "hello"),
                "permission"
        ));
        Command command1 = new Command(
                new LiteralCommandNode(
                        "hi",
                        List.of(
                                new LiteralCommandNode(
                                        "there",
                                        context -> CommandResult.successful(context, "hi there"),
                                        "permission"
                                )
                        ),
                        "permission"
                )
        );
        Command command2 = new Command(
                new LiteralCommandNode(
                        "welcome",
                        List.of(
                                new IntegerCommandNode(
                                        "repeat",
                                        new AllowedNumberInputs.AllowedIntegerInputs.Positive(),
                                        context -> CommandResult.successful(context, "welcome".repeat(context.arguments().<Integer>getArgument("repeat").orElseThrow().data())),
                                        "permission"
                                )
                        ),
                        context -> CommandResult.successful(context, "welcome"),
                        "permission"
                )
        );

        CommandManager manager = new CommandManager();
        manager.registerCommand("test", command0);
        manager.registerCommand("test", command1);
        manager.registerCommand("test", command2);

        List<Object> expected = List.of(0, 50, 200, 3000, 50000, 100000);
        List<Object> actual = manager.getSuggestions(CHAD_SENDER, List.of("welcome", "10"));

        Assertions.assertTrue(CollectionUtil.contentEquals(expected, actual));
    }

    @Test
    void testSuggestionsFailListBeta0() {
        Command command0 = new Command(new LiteralCommandNode(
                "hello",
                context -> CommandResult.successful(context, "hello"),
                "permission"
        ));
        Command command1 = new Command(
                new LiteralCommandNode(
                        "hi",
                        List.of(
                                new LiteralCommandNode(
                                        "there",
                                        context -> CommandResult.successful(context, "hi there"),
                                        "permission"
                                )
                        ),
                        "permission"
                )
        );
        Command command2 = new Command(
                new LiteralCommandNode(
                        "welcome",
                        context -> CommandResult.successful(context, "welcome"),
                        "permission"
                )
        );

        CommandManager manager = new CommandManager();
        manager.registerCommand("test", command0);
        manager.registerCommand("test", command1);
        manager.registerCommand("test", command2);

        List<Object> expected = List.of();
        List<Object> actual = manager.getSuggestions(BETA_SENDER, List.of());

        Assertions.assertTrue(CollectionUtil.contentEquals(expected, actual));
    }

    @Test
    void testSuggestionsSuccessStringChad0() {
        Command command0 = new Command(new LiteralCommandNode(
                "hello",
                context -> CommandResult.successful(context, "hello"),
                "permission"
        ));
        Command command1 = new Command(
                new LiteralCommandNode(
                        "hi",
                        List.of(
                                new LiteralCommandNode(
                                        "there",
                                        context -> CommandResult.successful(context, "hi there"),
                                        "permission"
                                )
                        ),
                        "permission"
                )
        );
        Command command2 = new Command(
                new LiteralCommandNode(
                        "welcome",
                        List.of(
                                new IntegerCommandNode(
                                        "repeat",
                                        new AllowedNumberInputs.AllowedIntegerInputs.Positive(),
                                        context -> CommandResult.successful(context, "welcome".repeat(context.arguments().<Integer>getArgument("repeat").orElseThrow().data())),
                                        "permission"
                                )
                        ),
                        context -> CommandResult.successful(context, "welcome"),
                        "permission"
                )
        );

        CommandManager manager = new CommandManager();
        manager.registerCommand("test", command0);
        manager.registerCommand("test", command1);
        manager.registerCommand("test", command2);

        List<Object> expected = List.of("hi", "hello", "welcome");
        List<Object> actual = manager.getSuggestions(CHAD_SENDER, "");

        Assertions.assertEquals(expected, actual);
    }

    @Test
    void testSuggestionsSuccessStringChad1() {
        Command command0 = new Command(new LiteralCommandNode(
                "hello",
                context -> CommandResult.successful(context, "hello"),
                "permission"
        ));
        Command command1 = new Command(
                new LiteralCommandNode(
                        "hi",
                        List.of(
                                new LiteralCommandNode(
                                        "there",
                                        context -> CommandResult.successful(context, "hi there"),
                                        "permission"
                                )
                        ),
                        "permission"
                )
        );
        Command command2 = new Command(
                new LiteralCommandNode(
                        "welcome",
                        List.of(
                                new IntegerCommandNode(
                                        "repeat",
                                        new AllowedNumberInputs.AllowedIntegerInputs.Positive(),
                                        context -> CommandResult.successful(context, "welcome".repeat(context.arguments().<Integer>getArgument("repeat").orElseThrow().data())),
                                        "permission"
                                )
                        ),
                        context -> CommandResult.successful(context, "welcome"),
                        "permission"
                )
        );

        CommandManager manager = new CommandManager();
        manager.registerCommand("test", command0);
        manager.registerCommand("test", command1);
        manager.registerCommand("test", command2);

        List<Object> expected = List.of("there");
        List<Object> actual = manager.getSuggestions(CHAD_SENDER, "hi t");

        Assertions.assertEquals(expected, actual);
    }

    @Test
    void testSuggestionsSuccessStringChad2() {
        Command command0 = new Command(new LiteralCommandNode(
                "hello",
                context -> CommandResult.successful(context, "hello"),
                "permission"
        ));
        Command command1 = new Command(
                new LiteralCommandNode(
                        "hi",
                        List.of(
                                new LiteralCommandNode(
                                        "there",
                                        context -> CommandResult.successful(context, "hi there"),
                                        "permission"
                                )
                        ),
                        "permission"
                )
        );
        Command command2 = new Command(
                new LiteralCommandNode(
                        "welcome",
                        List.of(
                                new IntegerCommandNode(
                                        "repeat",
                                        new AllowedNumberInputs.AllowedIntegerInputs.Positive(),
                                        context -> CommandResult.successful(context, "welcome".repeat(context.arguments().<Integer>getArgument("repeat").orElseThrow().data())),
                                        "permission"
                                )
                        ),
                        context -> CommandResult.successful(context, "welcome"),
                        "permission"
                )
        );

        CommandManager manager = new CommandManager();
        manager.registerCommand("test", command0);
        manager.registerCommand("test", command1);
        manager.registerCommand("test", command2);

        List<Object> expected = List.of(0, 50, 200, 3000, 50000, 100000);
        List<Object> actual = manager.getSuggestions(CHAD_SENDER, "welcome 1");

        Assertions.assertEquals(expected, actual);
    }

    @Test
    void testSuggestionsSuccessStringChad3() {
        Command command0 = new Command(new LiteralCommandNode(
                "hello",
                context -> CommandResult.successful(context, "hello"),
                "permission"
        ));
        Command command1 = new Command(
                new LiteralCommandNode(
                        "hi",
                        List.of(
                                new LiteralCommandNode(
                                        "there",
                                        context -> CommandResult.successful(context, "hi there"),
                                        "permission"
                                )
                        ),
                        "permission"
                )
        );
        Command command2 = new Command(
                new LiteralCommandNode(
                        "welcome",
                        List.of(
                                new IntegerCommandNode(
                                        "repeat",
                                        new AllowedNumberInputs.AllowedIntegerInputs.Positive(),
                                        context -> CommandResult.successful(context, "welcome".repeat(context.arguments().<Integer>getArgument("repeat").orElseThrow().data())),
                                        "permission"
                                )
                        ),
                        context -> CommandResult.successful(context, "welcome"),
                        "permission"
                )
        );
        Command command3 = new Command(
                new LiteralCommandNode(
                        "test",
                        List.of(
                                new LiteralCommandNode(
                                        "spaced string",
                                        context -> CommandResult.successful(context, "spaced string"),
                                        "permission"
                                )
                        ),
                        "permission"
                )
        );

        CommandManager manager = new CommandManager();
        manager.registerCommand("test", command0);
        manager.registerCommand("test", command1);
        manager.registerCommand("test", command2);
        manager.registerCommand("test", command3);

        List<Object> expected = List.of("spaced string");
        List<Object> actual = manager.getSuggestions(CHAD_SENDER, "test \"spaced ");

        Assertions.assertEquals(expected, actual);
    }
}
