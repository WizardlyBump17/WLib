package com.wizardlybump17.wlib.command.test.rework;

import com.wizardlybump17.wlib.command.rework.Command;
import com.wizardlybump17.wlib.command.rework.executor.CommandNodeExecutor;
import com.wizardlybump17.wlib.command.rework.input.AllowedNumberInputs;
import com.wizardlybump17.wlib.command.rework.manager.CommandManager;
import com.wizardlybump17.wlib.command.rework.node.IntegerCommandNode;
import com.wizardlybump17.wlib.command.rework.node.LiteralCommandNode;
import com.wizardlybump17.wlib.command.rework.result.CommandResult;
import com.wizardlybump17.wlib.command.rework.result.SuccessResult;
import com.wizardlybump17.wlib.command.rework.result.error.CommandNotFoundResult;
import com.wizardlybump17.wlib.command.sender.BasicCommandSender;
import com.wizardlybump17.wlib.command.sender.CommandSender;
import com.wizardlybump17.wlib.util.CollectionUtil;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.UUID;
import java.util.function.Consumer;

class CommandManagerTests {

    static final @NotNull Consumer<String> SENDER_MESSAGE_CONSUMER = System.out::println;
    static final @NotNull CommandSender<Object> CHAD_SENDER = new BasicCommandSender<>(new Object(), "Chad", UUID.nameUUIDFromBytes("Chad".getBytes()), SENDER_MESSAGE_CONSUMER, $ -> true);
    static final @NotNull CommandSender<Object> BETA_SENDER = new BasicCommandSender<>(new Object(), "Beta", UUID.nameUUIDFromBytes("Beta".getBytes()), SENDER_MESSAGE_CONSUMER, $ -> false);

    @Test
    void testRegisterDifferent() {
        Command command0 = new Command(new LiteralCommandNode("hello0", context -> CommandResult.successful(context, "hello0"), "permission"));
        Command command1 = new Command(new LiteralCommandNode("hello1", context -> CommandResult.successful(context, "hello1"), "permission"));
        Command command2 = new Command(new LiteralCommandNode("hello2", context -> CommandResult.successful(context, "hello2"), "permission"));
        Command command3 = new Command(new LiteralCommandNode("hello3", context -> CommandResult.successful(context, "hello3"), "permission"));

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

    @Test
    void testRegisterMerging0() {
        CommandNodeExecutor<?> helloExecutor = context -> CommandResult.successful(context, "hello");
        CommandNodeExecutor<?> helloWorldExecutor = context -> CommandResult.successful(context, "hello world");
        CommandNodeExecutor<?> helloWorldHiExecutor = context -> CommandResult.successful(context, "hello world hi");

        Command command0 = new Command(
                new LiteralCommandNode(
                        "hello",
                        helloExecutor,
                        "permission"
                )
        );
        Command command1 = new Command(
                new LiteralCommandNode(
                        "hello",
                        List.of(
                                new LiteralCommandNode(
                                        "world",
                                        helloWorldExecutor,
                                        "permission"
                                )
                        ),
                        "permission"
                )
        );
        Command command2 = new Command(
                new LiteralCommandNode(
                        "hello",
                        List.of(
                                new LiteralCommandNode(
                                        "world",
                                        List.of(
                                                new LiteralCommandNode(
                                                        "hi",
                                                        helloWorldHiExecutor,
                                                        "permission"
                                                )
                                        ),
                                        "permission"
                                )
                        ),
                        "permission"
                )
        );

        CommandManager manager = new CommandManager();

        Command registeredCommand0 = manager.registerCommand("test", command0);
        Command registeredCommand1 = manager.registerCommand("test", command1);
        Command registeredCommand2 = manager.registerCommand("test", command2);

        Command expectedCommand0 = new Command(
                new LiteralCommandNode(
                        "hello",
                        helloExecutor,
                        "permission"
                )
        );
        Command expectedCommand1 = new Command(
                new LiteralCommandNode(
                        "hello",
                        List.of(
                                new LiteralCommandNode(
                                        "world",
                                        helloWorldExecutor,
                                        "permission"
                                )
                        ),
                        helloExecutor,
                        "permission"
                )
        );
        Command expectedCommand2 = new Command(
                new LiteralCommandNode(
                        "hello",
                        List.of(
                                new LiteralCommandNode(
                                        "world",
                                        List.of(
                                                new LiteralCommandNode(
                                                        "hi",
                                                        helloWorldHiExecutor,
                                                        "permission"
                                                )
                                        ),
                                        helloWorldExecutor,
                                        "permission"
                                )
                        ),
                        helloExecutor,
                        "permission"
                )
        );

        Assertions.assertEquals(expectedCommand0, registeredCommand0);
        Assertions.assertEquals(expectedCommand1, registeredCommand1);
        Assertions.assertEquals(expectedCommand2, registeredCommand2);
    }

    @Test
    void testRegisterMerging1() {
        CommandNodeExecutor<?> helloExecutor = context -> CommandResult.successful(context, "hello");
        CommandNodeExecutor<?> helloWorldExecutor = context -> CommandResult.successful(context, "hello world");
        CommandNodeExecutor<?> helloWorldHiExecutor = context -> CommandResult.successful(context, "hello world hi");
        CommandNodeExecutor<?> helloThereExecutor = context -> CommandResult.successful(context, "hello there");

        Command command0 = new Command(
                new LiteralCommandNode(
                        "hello",
                        helloExecutor,
                        "permission"
                )
        );
        Command command1 = new Command(
                new LiteralCommandNode(
                        "hello",
                        List.of(
                                new LiteralCommandNode(
                                        "world",
                                        helloWorldExecutor,
                                        "permission"
                                ),
                                new LiteralCommandNode(
                                        "there",
                                        helloThereExecutor,
                                        "permission"
                                )
                        ),
                        "permission"
                )
        );
        Command command2 = new Command(
                new LiteralCommandNode(
                        "hello",
                        List.of(
                                new LiteralCommandNode(
                                        "world",
                                        List.of(
                                                new LiteralCommandNode(
                                                        "hi",
                                                        helloWorldHiExecutor,
                                                        "permission"
                                                )
                                        ),
                                        "permission"
                                )
                        ),
                        "permission"
                )
        );

        CommandManager manager = new CommandManager();

        Command registeredCommand0 = manager.registerCommand("test", command0);
        Command registeredCommand1 = manager.registerCommand("test", command1);
        Command registeredCommand2 = manager.registerCommand("test", command2);

        Command expectedCommand0 = new Command(
                new LiteralCommandNode(
                        "hello",
                        helloExecutor,
                        "permission"
                )
        );
        Command expectedCommand1 = new Command(
                new LiteralCommandNode(
                        "hello",
                        List.of(
                                new LiteralCommandNode(
                                        "world",
                                        helloWorldExecutor,
                                        "permission"
                                ),
                                new LiteralCommandNode(
                                        "there",
                                        helloThereExecutor,
                                        "permission"
                                )
                        ),
                        helloExecutor,
                        "permission"
                )
        );
        Command expectedCommand2 = new Command(
                new LiteralCommandNode(
                        "hello",
                        List.of(
                                new LiteralCommandNode(
                                        "world",
                                        List.of(
                                                new LiteralCommandNode(
                                                        "hi",
                                                        helloWorldHiExecutor,
                                                        "permission"
                                                )
                                        ),
                                        helloWorldExecutor,
                                        "permission"
                                ),
                                new LiteralCommandNode(
                                        "there",
                                        helloThereExecutor,
                                        "permission"
                                )
                        ),
                        helloExecutor,
                        "permission"
                )
        );

        Assertions.assertEquals(expectedCommand0, registeredCommand0);
        Assertions.assertEquals(expectedCommand1, registeredCommand1);
        Assertions.assertEquals(expectedCommand2, registeredCommand2);
    }

    @Test
    void testCommandNotFound0() {
        Command command = new Command(
                new LiteralCommandNode(
                        "hello",
                        context -> CommandResult.successful(context, "hello"),
                        "permission"
                )
        );

        CommandManager manager = new CommandManager();
        manager.registerCommand("test", command);

        CommandNotFoundResult<?> expected = CommandResult.commandNotFound("hello0");
        CommandResult<?> actual = manager.execute(CHAD_SENDER, List.of("hello0"));

        Assertions.assertEquals(expected, actual);
    }

    @Test
    void testCommandNotFound1() {
        Command command = new Command(
                new LiteralCommandNode(
                        "hello",
                        context -> CommandResult.successful(context, "hello"),
                        "permission"
                )
        );

        CommandManager manager = new CommandManager();
        manager.registerCommand("test", command);

        CommandNotFoundResult<?> expected = CommandResult.commandNotFound("");
        CommandResult<?> actual = manager.execute(CHAD_SENDER, List.of());

        Assertions.assertEquals(expected, actual);
    }

    @Test
    void testSuccess0() {
        LiteralCommandNode helloNode = new LiteralCommandNode(
                "hello",
                context -> CommandResult.successful(context, "hello"),
                "permission"
        );
        Command command0 = new Command(helloNode);

        Command command1 = new Command(
                new LiteralCommandNode(
                        "hi",
                        context -> CommandResult.successful(context, "hi"),
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

        SuccessResult<String> expected = CommandResult.successful(0, helloNode, "hello");
        CommandResult<?> actual = manager.execute(CHAD_SENDER, List.of("hello"));

        Assertions.assertEquals(expected, actual);
    }

    @Test
    void testSuccess1() {
        Command command0 = new Command(new LiteralCommandNode(
                "hello",
                context -> CommandResult.successful(context, "hello"),
                "permission"
        ));

        LiteralCommandNode thereNode = new LiteralCommandNode(
                "there",
                context -> CommandResult.successful(context, "hi there"),
                "permission"
        );
        Command command1 = new Command(
                new LiteralCommandNode(
                        "hi",
                        List.of(
                                thereNode
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

        SuccessResult<String> expected = CommandResult.successful(1, thereNode, "hi there");
        CommandResult<?> actual = manager.execute(CHAD_SENDER, List.of("hi", "there"));

        Assertions.assertEquals(expected, actual);
    }

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
