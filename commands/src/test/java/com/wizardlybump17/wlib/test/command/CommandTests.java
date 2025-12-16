package com.wizardlybump17.wlib.test.command;

import com.wizardlybump17.wlib.command.Command;
import com.wizardlybump17.wlib.command.context.CommandContext;
import com.wizardlybump17.wlib.command.exception.InputParsingException;
import com.wizardlybump17.wlib.command.executor.CommandNodeExecutor;
import com.wizardlybump17.wlib.command.input.primitive.number.AllowedIntegerInputs;
import com.wizardlybump17.wlib.command.node.CommandNode;
import com.wizardlybump17.wlib.command.node.LiteralCommandNode;
import com.wizardlybump17.wlib.command.node.primitive.number.IntegerCommandNode;
import com.wizardlybump17.wlib.command.result.CommandResult;
import com.wizardlybump17.wlib.command.result.error.*;
import com.wizardlybump17.wlib.command.result.full.FullCommandResult;
import com.wizardlybump17.wlib.command.sender.BasicCommandSender;
import com.wizardlybump17.wlib.command.sender.CommandSender;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;

public class CommandTests {

    static final @NotNull Consumer<String> SENDER_MESSAGE_CONSUMER = System.out::println;
    static final @NotNull CommandSender<Object> CHAD_SENDER = new BasicCommandSender<>(new Object(), "Chad", UUID.nameUUIDFromBytes("Chad".getBytes()), SENDER_MESSAGE_CONSUMER, $ -> true);
    static final @NotNull CommandSender<Object> BETA_SENDER = new BasicCommandSender<>(new Object(), "Beta", UUID.nameUUIDFromBytes("Beta".getBytes()), SENDER_MESSAGE_CONSUMER, $ -> false);

    @Test
    void testSuccessHello() {
        LiteralCommandNode helloNode = new LiteralCommandNode(
                "hello",
                List.of(),
                context -> CommandResult.successful(context, "hello"),
                null
        );
        Command command = new Command(helloNode);

        FullCommandResult expected = new FullCommandResult(CHAD_SENDER, List.of("hello"), 0, helloNode, CommandResult.successful(0, helloNode, "hello"));
        FullCommandResult actual = command.execute(CHAD_SENDER, List.of("hello"));

        Assertions.assertEquals(expected, actual);
    }

    @Test
    void testSuccessHelloWorld() {
        LiteralCommandNode worldNode = new LiteralCommandNode(
                "world",
                List.of(),
                context -> CommandResult.successful(context, "hello world"),
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

        FullCommandResult expected = new FullCommandResult(CHAD_SENDER, List.of("hello", "world"), 1, worldNode, CommandResult.successful(1, worldNode, "hello world"));
        FullCommandResult actual = command.execute(CHAD_SENDER, List.of("hello", "world"));

        Assertions.assertEquals(expected, actual);
    }

    @Test
    void testSuccessHelloWorldHi() {
        LiteralCommandNode hiNode = new LiteralCommandNode(
                "hi",
                List.of(),
                context -> CommandResult.successful(context, "hello world hi"),
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

        FullCommandResult expected = new FullCommandResult(CHAD_SENDER, List.of("hello", "world", "hi"), 2, hiNode, CommandResult.successful(2, hiNode, "hello world hi"));
        FullCommandResult actual = command.execute(CHAD_SENDER, List.of("hello", "world", "hi"));

        Assertions.assertEquals(expected, actual);
    }

    @Test
    @DisplayName("Multiple children 0 (hello, hi): success")
    void test0() {
        LiteralCommandNode hiNode = new LiteralCommandNode(
                "hi",
                List.of(),
                context -> CommandResult.successful(context, "hello hi"),
                null
        );
        Command command = new Command(new LiteralCommandNode(
                "hello",
                List.of(
                        new LiteralCommandNode(
                                "world",
                                List.of(),
                                context -> CommandResult.successful(context, "hello world"),
                                null
                        ),
                        hiNode
                ),
                null,
                null
        ));

        FullCommandResult expected = new FullCommandResult(CHAD_SENDER, List.of("hello", "hi"), 1, hiNode, CommandResult.successful(1, hiNode, "hello hi"));
        FullCommandResult actual = command.execute(CHAD_SENDER, List.of("hello", "hi"));

        Assertions.assertEquals(expected, actual);
    }

    @Test
    @DisplayName("Multiple children 1 (hello, hi, world): success")
    void test1() {
        LiteralCommandNode hiWorldNode = new LiteralCommandNode(
                "world0",
                List.of(),
                context -> CommandResult.successful(context, "hello hi world"),
                null
        );
        Command command = new Command(new LiteralCommandNode(
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
                                List.of(hiWorldNode),
                                null,
                                null
                        )
                ),
                null,
                null
        ));

        FullCommandResult expected = new FullCommandResult(CHAD_SENDER, List.of("hello", "hi", "world0"), 2, hiWorldNode, CommandResult.successful(2, hiWorldNode, "hello hi world"));
        FullCommandResult actual = command.execute(CHAD_SENDER, List.of("hello", "hi", "world0"));

        Assertions.assertEquals(expected, actual);
    }

    @Test
    void testExtraArguments0() {
        LiteralCommandNode helloNode = new LiteralCommandNode(
                "hello",
                List.of(),
                context -> CommandResult.successful(context, "hello"),
                null
        );
        Command command = new Command(helloNode);

        FullCommandResult expected = new FullCommandResult(CHAD_SENDER, List.of("hello", "world"), 2, helloNode, CommandResult.extraArguments(1, helloNode));
        FullCommandResult actual = command.execute(CHAD_SENDER, List.of("hello", "world"));

        Assertions.assertEquals(expected, actual);
    }

    @Test
    void testExtraArguments1() {
        LiteralCommandNode worldNode = new LiteralCommandNode(
                "world",
                List.of(),
                context -> CommandResult.successful(context, "hello world"),
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

        FullCommandResult expected = new FullCommandResult(CHAD_SENDER, List.of("hello", "world", "hi"), 2, worldNode, CommandResult.extraArguments(2, worldNode));
        FullCommandResult actual = command.execute(CHAD_SENDER, List.of("hello", "world", "hi"));

        Assertions.assertEquals(expected, actual);
    }

    @Test
    void testExtraArguments2() {
        LiteralCommandNode hiWorldNode = new LiteralCommandNode(
                "world0",
                List.of(),
                context -> CommandResult.successful(context, "hello hi world"),
                null
        );
        Command command = new Command(new LiteralCommandNode(
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
                                List.of(hiWorldNode),
                                null,
                                null
                        )
                ),
                null,
                null
        ));

        FullCommandResult expected = new FullCommandResult(CHAD_SENDER, List.of("hello", "hi", "world0", "extra"), 2, hiWorldNode, CommandResult.extraArguments(3, hiWorldNode));
        FullCommandResult actual = command.execute(CHAD_SENDER, List.of("hello", "hi", "world0", "extra"));

        Assertions.assertEquals(expected, actual);
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

        ExceptionResult<?> expected = CommandResult.exceptionally(0, helloNode, helloException);
        FullCommandResult actual = command.execute(CHAD_SENDER, List.of("hello"));

        Assertions.assertEquals(expected, actual);
    }

    @Test
    void testInsufficientArguments() {
        Command command = new Command(
                new LiteralCommandNode(
                        "hello",
                        List.of(),
                        context -> CommandResult.successful(context, "hello"),
                        null
                )
        );

        InsufficientArgumentsResult<?> expected = CommandResult.insufficientArguments(command);
        FullCommandResult actual = command.execute(CHAD_SENDER, List.of());

        Assertions.assertEquals(expected, actual);
    }

    @Test
    void testNoPermission() {
        LiteralCommandNode helloNode = new LiteralCommandNode(
                "hello",
                List.of(),
                context -> CommandResult.successful(context, "hello"),
                "permission"
        );
        Command command = new Command(helloNode);

        NoPermissionResult<?> expected = CommandResult.noPermission(0, helloNode);
        FullCommandResult actual = command.execute(BETA_SENDER, List.of("hello"));

        Assertions.assertEquals(expected, actual);
    }

    @Test
    void testOutOfRange() {
        LiteralCommandNode helloNode = new LiteralCommandNode(
                "hello",
                List.of(),
                context -> CommandResult.successful(context, "hello"),
                null
        );
        Command command = new Command(helloNode);

        OutOfRangeInputResult<?> expected = CommandResult.outOfRangeInput(0, helloNode);
        FullCommandResult actual = command.execute(CHAD_SENDER, List.of("hello0"));

        Assertions.assertEquals(expected, actual);
    }

    @Test
    void testParseInputException() {
        IntegerCommandNode worldNode = new IntegerCommandNode(
                "world",
                List.of(),
                AllowedIntegerInputs.value(10),
                null,
                context -> CommandResult.successful(context, 10),
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

        ParseInputExceptionResult<?> expected = CommandResult.parseInputException(1, worldNode, new InputParsingException("Could not parse as int: world", new NumberFormatException("For input string: \"world\"")));
        FullCommandResult actual = command.execute(CHAD_SENDER, List.of("hello", "world"));

        Assertions.assertInstanceOf(ParseInputExceptionResult.class, actual);

        ParseInputExceptionResult<?> actualException = (ParseInputExceptionResult<?>) actual.result();
        Assertions.assertEquals(expected.lastInputIndex(), actual.lastInputIndex());
        Assertions.assertEquals(expected.lastNode(), actual.lastNode());
        Assertions.assertEquals(expected.exception().getMessage(), actualException.exception().getMessage());
        Assertions.assertEquals(expected.exception().getCause().getMessage(), actualException.exception().getCause().getMessage());
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

        CommandNodeExecutorNotFoundResult<?> expected = CommandResult.noCommandNodeExecutor(0, helloNode);
        FullCommandResult actual = command.execute(CHAD_SENDER, List.of("hello"));

        Assertions.assertEquals(expected, actual);
    }

    /**
     * <p>
     * The CommandNode#merge(CommandNode) method wasnt merging the executor and permission.
     * This test will ensure that we have the correct executor and permission after merging.
     * </p>
     */
    @Test
    void testMerge() {
        CommandNodeExecutor<Object> test0Executor = context -> CommandResult.successful(context, "It works 0!");
        CommandNodeExecutor<Object> test1Executor = context -> CommandResult.successful(context, "It works 1!");

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
    }

    @Test
    void testCommandArguments() {
        {
            LiteralCommandNode world = new LiteralCommandNode(
                    "world",
                    List.of(),
                    context -> {
                        Assertions.assertEquals(Map.of(), context.arguments().getArguments());
                        return CommandResult.successful(context, "Hello World");
                    },
                    null
            );
            FullCommandResult result = new Command(
                    new LiteralCommandNode(
                            "hello",
                            List.of(world),
                            null,
                            null
                    )
            ).execute(CHAD_SENDER, List.of("hello", "world"));

            Assertions.assertEquals(CommandResult.successful(1, world, "Hello World"), result);
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
                        return CommandResult.successful(context, context.arguments().getArgumentData("world").orElseThrow());
                    },
                    null
            );
            worldReference.set(world);

            FullCommandResult result = new Command(
                    new LiteralCommandNode(
                            "hello",
                            List.of(world),
                            null,
                            null
                    )
            ).execute(CHAD_SENDER, List.of("hello", "10"));

            Assertions.assertEquals(CommandResult.successful(1, world, 10), result);
        }

        {
            AtomicReference<IntegerCommandNode> worldReference = new AtomicReference<>();
            LiteralCommandNode test = new LiteralCommandNode(
                    "test",
                    List.of(),
                    context -> {
                        Assertions.assertEquals(Map.of("world", new CommandContext.CommandNodeArgument<>(worldReference.get(), "10", 10)), context.arguments().getArguments());
                        return CommandResult.successful(context, "Hello " + context.arguments().getArgumentData("world").orElseThrow() + " world");
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

            FullCommandResult result = new Command(
                    new LiteralCommandNode(
                            "hello",
                            List.of(world),
                            null,
                            null
                    )
            ).execute(CHAD_SENDER, List.of("hello", "10", "test"));

            Assertions.assertEquals(CommandResult.successful(2, test, "Hello 10 world"), result);
        }
    }
}
