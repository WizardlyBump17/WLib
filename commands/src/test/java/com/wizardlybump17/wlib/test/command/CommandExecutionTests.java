package com.wizardlybump17.wlib.test.command;

import com.wizardlybump17.wlib.command.Command;
import com.wizardlybump17.wlib.command.context.CommandContext;
import com.wizardlybump17.wlib.command.exception.CommandExecutionException;
import com.wizardlybump17.wlib.command.exception.InputParsingException;
import com.wizardlybump17.wlib.command.executor.CommandNodeExecutor;
import com.wizardlybump17.wlib.command.input.primitive.number.AllowedIntegerInputs;
import com.wizardlybump17.wlib.command.input.string.AllowedStringInputs;
import com.wizardlybump17.wlib.command.manager.CommandManager;
import com.wizardlybump17.wlib.command.node.CommandNode;
import com.wizardlybump17.wlib.command.node.LiteralCommandNode;
import com.wizardlybump17.wlib.command.node.primitive.number.IntegerCommandNode;
import com.wizardlybump17.wlib.command.node.string.StringCommandNode;
import com.wizardlybump17.wlib.command.result.CommandResult;
import com.wizardlybump17.wlib.command.sender.BasicCommandSender;
import com.wizardlybump17.wlib.command.sender.CommandSender;
import com.wizardlybump17.wlib.util.CollectionUtil;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;

public class CommandExecutionTests {

    static final @NotNull Consumer<String> SENDER_MESSAGE_CONSUMER = System.out::println;
    static final @NotNull CommandSender<Object> CHAD_SENDER = new BasicCommandSender<>(new Object(), "Chad", UUID.nameUUIDFromBytes("Chad".getBytes()), SENDER_MESSAGE_CONSUMER, $ -> true);
    static final @NotNull CommandSender<Object> BETA_SENDER = new BasicCommandSender<>(new Object(), "Beta", UUID.nameUUIDFromBytes("Beta".getBytes()), SENDER_MESSAGE_CONSUMER, $ -> false);

    @Test
    void testSuccessHello() {
        LiteralCommandNode helloNode = new LiteralCommandNode(
                "hello",
                List.of(),
                context -> CommandResult.successful("hello"),
                null
        );
        Command command = new Command(helloNode);

        CommandManager commandManager = new CommandManager();
        commandManager.registerCommand("test", command);

        Assertions.assertEquals(
                CommandResult.successful("hello"),
                commandManager.execute(CHAD_SENDER, List.of("hello"))
        );
    }

    @Test
    void testSuccessHelloWorld() {
        LiteralCommandNode worldNode = new LiteralCommandNode(
                "world",
                List.of(),
                context -> CommandResult.successful("hello world"),
                null
        );
        Command command = new Command(
                new LiteralCommandNode(
                        "hello",
                        List.of(worldNode),
                        null,
                        null
                )
        );

        CommandManager commandManager = new CommandManager();
        commandManager.registerCommand("test", command);

        Assertions.assertEquals(
                CommandResult.successful("hello world"),
                commandManager.execute(CHAD_SENDER, List.of("hello", "world"))
        );
    }

    @Test
    void testSuccessHelloWorldHi() {
        LiteralCommandNode hiNode = new LiteralCommandNode(
                "hi",
                List.of(),
                context -> CommandResult.successful("hello world hi"),
                null
        );
        Command command = new Command(
                new LiteralCommandNode(
                        "hello",
                        List.of(
                                new LiteralCommandNode(
                                        "world",
                                        List.of(hiNode),
                                        null,
                                        null
                                )
                        ),
                        null,
                        null
                )
        );

        CommandManager commandManager = new CommandManager();
        commandManager.registerCommand("test", command);

        Assertions.assertEquals(
                CommandResult.successful("hello world hi"),
                commandManager.execute(CHAD_SENDER, List.of("hello", "world", "hi"))
        );
    }

    @Test
    @DisplayName("Multiple children 0 (hello, hi): success")
    void test0() {
        LiteralCommandNode hiNode = new LiteralCommandNode(
                "hi",
                List.of(),
                context -> CommandResult.successful("hello hi"),
                null
        );
        Command command = new Command(new LiteralCommandNode(
                "hello",
                List.of(
                        new LiteralCommandNode(
                                "world",
                                List.of(),
                                context -> CommandResult.successful("hello world"),
                                null
                        ),
                        hiNode
                ),
                null,
                null
        ));

        CommandManager commandManager = new CommandManager();
        commandManager.registerCommand("test", command);

        Assertions.assertEquals(
                CommandResult.successful("hello hi"),
                commandManager.execute(CHAD_SENDER, List.of("hello", "hi"))
        );
    }

    @Test
    @DisplayName("Multiple children 1 (hello, hi, world): success")
    void test1() {
        LiteralCommandNode hiWorldNode = new LiteralCommandNode(
                "world0",
                List.of(),
                context -> CommandResult.successful("hello hi world"),
                null
        );
        Command command = new Command(new LiteralCommandNode(
                "hello",
                List.of(
                        new LiteralCommandNode(
                                "world",
                                List.of(),
                                context -> CommandResult.successful("hello world"),
                                null
                        ),
                        new LiteralCommandNode(
                                "hi",
                                List.of(hiWorldNode),
                                null,
                                null
                        )
                ),
                null,
                null
        ));

        CommandManager commandManager = new CommandManager();
        commandManager.registerCommand("test", command);

        Assertions.assertEquals(
                CommandResult.successful("hello hi world"),
                commandManager.execute(CHAD_SENDER, List.of("hello", "hi", "world0"))
        );
    }

    @Test
    void testExtraArguments0() {
        LiteralCommandNode helloNode = new LiteralCommandNode(
                "hello",
                List.of(),
                context -> CommandResult.successful("hello"),
                null
        );
        Command command = new Command(helloNode);

        CommandManager commandManager = new CommandManager();
        commandManager.registerCommand("test", command);

        Assertions.assertEquals(
                CommandResult.notFound(CommandResult.ErrorDetails.nodeNotFound(1, "world")),
                commandManager.execute(CHAD_SENDER, List.of("hello", "world", "hi", "there"))
        );
    }

    @Test
    void testExtraArguments1() {
        LiteralCommandNode worldNode = new LiteralCommandNode(
                "world",
                List.of(),
                context -> CommandResult.successful("hello world"),
                null
        );
        Command command = new Command(
                new LiteralCommandNode(
                        "hello",
                        List.of(worldNode),
                        null,
                        null
                )
        );

        CommandManager commandManager = new CommandManager();
        commandManager.registerCommand("test", command);

        Assertions.assertEquals(
                CommandResult.notFound(CommandResult.ErrorDetails.nodeNotFound(2, "hi")),
                commandManager.execute(CHAD_SENDER, List.of("hello", "world", "hi", "there"))
        );
    }

    @Test
    void testExtraArguments2() {
        LiteralCommandNode hiWorldNode = new LiteralCommandNode(
                "world0",
                List.of(),
                context -> CommandResult.successful("hello hi world"),
                null
        );
        Command command = new Command(new LiteralCommandNode(
                "hello",
                List.of(
                        new LiteralCommandNode(
                                "world",
                                List.of(),
                                context -> CommandResult.successful("hello world"),
                                null
                        ),
                        new LiteralCommandNode(
                                "hi",
                                List.of(hiWorldNode),
                                null,
                                null
                        )
                ),
                null,
                null
        ));

        CommandManager commandManager = new CommandManager();
        commandManager.registerCommand("test", command);

        Assertions.assertEquals(
                CommandResult.notFound(CommandResult.ErrorDetails.nodeNotFound(3, "extra")),
                commandManager.execute(CHAD_SENDER, List.of("hello", "hi", "world0", "extra"))
        );
    }

    @Test
    void testException() {
        RuntimeException helloException = new RuntimeException("hello");
        LiteralCommandNode helloNode = new LiteralCommandNode(
                "hello",
                List.of(),
                context -> {
                    throw helloException;
                },
                null
        );

        Command command = new Command(helloNode);

        CommandManager commandManager = new CommandManager();
        commandManager.registerCommand("test", command);

        CommandExecutionException actual = Assertions.assertThrowsExactly(
                CommandExecutionException.class,
                () -> commandManager.execute(CHAD_SENDER, List.of("hello"))
        );
        Assertions.assertEquals(
                CommandExecutionException.MESSAGE.formatted("[hello]", "0", "hello"),
                actual.getMessage()
        );
        Assertions.assertEquals(
                helloException.getMessage(),
                Assertions.assertInstanceOf(
                        helloException.getClass(),
                        actual.getCause()
                ).getMessage()
        );
    }

    @Test
    void testInsufficientArguments() {
        Command command = new Command(
                new LiteralCommandNode(
                        "hello",
                        List.of(),
                        context -> CommandResult.successful("hello"),
                        null
                )
        );

        CommandManager commandManager = new CommandManager();
        commandManager.registerCommand("test", command);

        Assertions.assertEquals(
                CommandResult.badRequest(CommandResult.ErrorDetails.emptyInput()),
                commandManager.execute(CHAD_SENDER, List.of())
        );
    }

    @Test
    void testNoPermission() {
        LiteralCommandNode helloNode = new LiteralCommandNode(
                "hello",
                List.of(),
                context -> CommandResult.successful("hello"),
                "permission"
        );
        Command command = new Command(helloNode);

        CommandManager commandManager = new CommandManager();
        commandManager.registerCommand("test", command);

        Assertions.assertEquals(
                CommandResult.forbidden(CommandResult.ErrorDetails.noPermission(BETA_SENDER.getName(), "permission")),
                commandManager.execute(BETA_SENDER, List.of("hello"))
        );
    }

    @Test
    void testOutOfRange() {
        LiteralCommandNode helloNode = new LiteralCommandNode(
                "hello",
                List.of(),
                context -> CommandResult.successful("hello"),
                null
        );
        Command command = new Command(helloNode);

        CommandManager commandManager = new CommandManager();
        commandManager.registerCommand("test", command);

        Assertions.assertEquals(
                CommandResult.notFound(CommandResult.ErrorDetails.nodeNotFound(0, "hello0")),
                commandManager.execute(CHAD_SENDER, List.of("hello0"))
        );
    }

    @Test
    void testParseInputException() {
        IntegerCommandNode worldNode = new IntegerCommandNode(
                "world",
                List.of(),
                AllowedIntegerInputs.value(10),
                null,
                context -> CommandResult.successful(10),
                null
        );
        Command command = new Command(
                new LiteralCommandNode(
                        "hello",
                        List.of(worldNode),
                        null,
                        null
                )
        );

        CommandManager commandManager = new CommandManager();
        commandManager.registerCommand("test", command);

        Assertions.assertEquals(
                CommandResult.badRequest(CommandResult.ErrorDetails.parseError("hello", "world", new InputParsingException("Could not parse as int: world", new NumberFormatException("For input string: \"world\"")))),
                commandManager.execute(CHAD_SENDER, List.of("hello", "world"))
        );
    }

    @Test
    void testCommandNodeExecutorNotFound() {
        LiteralCommandNode helloNode = new LiteralCommandNode(
                "hello",
                List.of(),
                null,
                null
        );
        Command command = new Command(helloNode);

        CommandManager commandManager = new CommandManager();
        commandManager.registerCommand("test", command);

        Assertions.assertEquals(
                CommandResult.notImplemented(CommandResult.ErrorDetails.noCommandExecutor("hello", "hello")),
                commandManager.execute(CHAD_SENDER, List.of("hello"))
        );
    }

    /**
     * <p>
     * The CommandNode#merge(CommandNode) method wasnt merging the executor and permission.
     * This test will ensure that we have the correct executor and permission after merging.
     * </p>
     */
    @Test
    void testMerge() {
        CommandNodeExecutor<Object> test0Executor = context -> CommandResult.successful("It works 0!");
        CommandNodeExecutor<Object> test1Executor = context -> CommandResult.successful("It works 1!");

        CommandNode<String> expected = new LiteralCommandNode(
                "test0",
                List.of(
                        new LiteralCommandNode(
                                "test1",
                                List.of(),
                                test1Executor,
                                "permission1"
                        )
                ),
                test0Executor,
                "permission0"
        );
        CommandNode<String> actual = new LiteralCommandNode(
                "test0",
                List.of(),
                null,
                null
        ).merge(new LiteralCommandNode(
                "test0",
                List.of(
                        new LiteralCommandNode(
                                "test1",
                                List.of(),
                                test1Executor,
                                "permission1"
                        )
                ),
                test0Executor,
                "permission0"
        ));

        Assertions.assertEquals(expected, actual);

        Assertions.assertEquals(
                new TestMerge0(
                        new LiteralCommandNode(
                                "hello",
                                List.of(
                                        new LiteralCommandNode(
                                                "world",
                                                List.of(),
                                                null,
                                                null
                                        )
                                ),
                                null,
                                null
                        )
                ),
                new Command(
                        new LiteralCommandNode(
                                "hello",
                                List.of(),
                                null,
                                null
                        )
                ).merge(
                        new TestMerge0(
                                new LiteralCommandNode(
                                        "hello",
                                        List.of(
                                                new LiteralCommandNode(
                                                        "world",
                                                        List.of(),
                                                        null,
                                                        null
                                                )
                                        ),
                                        null,
                                        null
                                )
                        )
                )
        );

        Assertions.assertEquals(
                new TestMerge1(
                        new LiteralCommandNode(
                                "hi",
                                List.of(
                                        new LiteralCommandNode(
                                                "there",
                                                List.of(),
                                                null,
                                                null
                                        )
                                ),
                                null,
                                null
                        )
                ),
                new Command(
                        new LiteralCommandNode(
                                "hi",
                                List.of(),
                                null,
                                null
                        )
                ).merge(
                        new TestMerge1(
                                new LiteralCommandNode(
                                        "hi",
                                        List.of(
                                                new LiteralCommandNode(
                                                        "there",
                                                        List.of(),
                                                        null,
                                                        null
                                                )
                                        ),
                                        null,
                                        null
                                )
                        )
                )
        );

        Assertions.assertEquals(
                new TestMerge0(
                        new LiteralCommandNode(
                                "test",
                                List.of(
                                        new LiteralCommandNode(
                                                "test0",
                                                List.of(),
                                                null,
                                                null
                                        )
                                ),
                                null,
                                null
                        )
                ),
                new TestMerge0(
                        new LiteralCommandNode(
                                "test",
                                List.of(),
                                null,
                                null
                        )
                ).merge(
                        new TestMerge1(
                                new LiteralCommandNode(
                                        "test",
                                        List.of(
                                                new LiteralCommandNode(
                                                        "test0",
                                                        List.of(),
                                                        null,
                                                        null
                                                )
                                        ),
                                        null,
                                        null
                                )
                        )
                )
        );
    }

    static class TestMerge0 extends Command {

        public TestMerge0(@NotNull LiteralCommandNode root) {
            super(root);
        }

        @Override
        public @NotNull Command merge(@NotNull Command other) {
            return new TestMerge0(getRoot().merge(other.getRoot()));
        }
    }

    static class TestMerge1 extends Command {

        public TestMerge1(@NotNull LiteralCommandNode root) {
            super(root);
        }

        @Override
        public @NotNull Command merge(@NotNull Command other) {
            return new TestMerge1(getRoot().merge(other.getRoot()));
        }
    }

    @Test
    void testCommandArguments() {
        {
            LiteralCommandNode world = new LiteralCommandNode(
                    "world",
                    List.of(),
                    context -> {
                        Assertions.assertEquals(Map.of(), context.arguments().getArguments());
                        return CommandResult.successful("Hello World");
                    },
                    null
            );

            Command command = new Command(
                    new LiteralCommandNode(
                            "hello",
                            List.of(world),
                            null,
                            null
                    )
            );

            CommandManager commandManager = new CommandManager();
            commandManager.registerCommand("test", command);

            CommandResult<?> result = commandManager.execute(CHAD_SENDER, List.of("hello", "world"));

            Assertions.assertEquals(CommandResult.successful("Hello World"), result);
        }

        {
            AtomicReference<IntegerCommandNode> worldReference = new AtomicReference<>();
            IntegerCommandNode world = new IntegerCommandNode(
                    "world",
                    List.of(),
                    AllowedIntegerInputs.unlimited(),
                    null,
                    context -> {
                        Assertions.assertEquals(Map.of("world", new CommandContext.CommandNodeArgument<>(worldReference.get(), "10", 10)), context.arguments().getArguments());
                        return CommandResult.successful(context.arguments().getArgumentData("world").orElseThrow());
                    },
                    null
            );
            worldReference.set(world);

            Command command = new Command(
                    new LiteralCommandNode(
                            "hello",
                            List.of(world),
                            null,
                            null
                    )
            );

            CommandManager commandManager = new CommandManager();
            commandManager.registerCommand("test", command);

            CommandResult<?> result = commandManager.execute(CHAD_SENDER, List.of("hello", "10"));

            Assertions.assertEquals(CommandResult.successful(10), result);
        }

        {
            AtomicReference<IntegerCommandNode> worldReference = new AtomicReference<>();
            LiteralCommandNode test = new LiteralCommandNode(
                    "test",
                    List.of(),
                    context -> {
                        Assertions.assertEquals(Map.of("world", new CommandContext.CommandNodeArgument<>(worldReference.get(), "10", 10)), context.arguments().getArguments());
                        return CommandResult.successful("Hello " + context.arguments().getArgumentData("world").orElseThrow() + " world");
                    },
                    null
            );
            IntegerCommandNode world = new IntegerCommandNode(
                    "world",
                    List.of(test),
                    AllowedIntegerInputs.unlimited(),
                    null,
                    null,
                    null
            );
            worldReference.set(world);

            Command command = new Command(
                    new LiteralCommandNode(
                            "hello",
                            List.of(world),
                            null,
                            null
                    )
            );

            CommandManager commandManager = new CommandManager();
            commandManager.registerCommand("test", command);

            CommandResult<?> result = commandManager.execute(CHAD_SENDER, List.of("hello", "10", "test"));

            Assertions.assertEquals(CommandResult.successful("Hello 10 world"), result);
        }
    }

    @Test
    void testNullArgumentsSuccess() {
        Command command = new Command(
                new LiteralCommandNode(
                        "test",
                        List.of(
                                new StringCommandNode(
                                        "nullable0",
                                        List.of(),
                                        AllowedStringInputs.anyNullable(),
                                        null,
                                        context -> CommandResult.successful(context.arguments().getArgumentData("nullable0").orElse(null)),
                                        null
                                )
                        ),
                        null,
                        null
                )
        );

        CommandManager commandManager = new CommandManager();
        commandManager.registerCommand("test", command);

        Assertions.assertEquals(
                CommandResult.successful("Hello World"),
                commandManager.execute(CHAD_SENDER, List.of("test", "Hello World"))
        );
        Assertions.assertEquals(
                CommandResult.successful(),
                commandManager.execute(CHAD_SENDER, CollectionUtil.listOf("test", null))
        );
    }

    @Test
    void testNullArgumentsError() {
        Command command = new Command(
                new LiteralCommandNode(
                        "test",
                        List.of(
                                new StringCommandNode(
                                        "nullable0",
                                        List.of(),
                                        AllowedStringInputs.anyNullable(),
                                        null,
                                        context -> CommandResult.successful(context.arguments().getArgumentData("nullable0").orElse(null)),
                                        null
                                )
                        ),
                        null,
                        null
                )
        );

        CommandManager commandManager = new CommandManager();
        commandManager.registerCommand("test", command);

        Assertions.assertEquals(
                CommandResult.successful("Hello World"),
                commandManager.execute(CHAD_SENDER, List.of("test", "Hello World"))
        );
        Assertions.assertEquals(
                CommandResult.successful(),
                commandManager.execute(CHAD_SENDER, CollectionUtil.listOf("test", null))
        );
    }
}
