package com.wizardlybump17.wlib.command.test.rework;

import com.wizardlybump17.wlib.command.rework.Command;
import com.wizardlybump17.wlib.command.rework.executor.CommandNodeExecutor;
import com.wizardlybump17.wlib.command.rework.manager.CommandManager;
import com.wizardlybump17.wlib.command.rework.node.LiteralCommandNode;
import com.wizardlybump17.wlib.command.rework.result.CommandResult;
import com.wizardlybump17.wlib.command.rework.result.error.CommandNotFoundResult;
import com.wizardlybump17.wlib.command.sender.BasicCommandSender;
import com.wizardlybump17.wlib.command.sender.CommandSender;
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
        Command command0 = new Command(new LiteralCommandNode("hello0", context -> CommandResult.successful(context, "hello0")));
        Command command1 = new Command(new LiteralCommandNode("hello1", context -> CommandResult.successful(context, "hello1")));
        Command command2 = new Command(new LiteralCommandNode("hello2", context -> CommandResult.successful(context, "hello2")));
        Command command3 = new Command(new LiteralCommandNode("hello3", context -> CommandResult.successful(context, "hello3")));

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
                        helloExecutor
                )
        );
        Command command1 = new Command(
                new LiteralCommandNode(
                        "hello",
                        List.of(
                                new LiteralCommandNode(
                                        "world",
                                        helloWorldExecutor
                                )
                        )
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
                                                        helloWorldHiExecutor
                                                )
                                        )
                                )
                        )
                )
        );

        CommandManager manager = new CommandManager();

        Command registeredCommand0 = manager.registerCommand("test", command0);
        Command registeredCommand1 = manager.registerCommand("test", command1);
        Command registeredCommand2 = manager.registerCommand("test", command2);

        Command expectedCommand0 = new Command(
                new LiteralCommandNode(
                        "hello",
                        helloExecutor
                )
        );
        Command expectedCommand1 = new Command(
                new LiteralCommandNode(
                        "hello",
                        List.of(
                                new LiteralCommandNode(
                                        "world",
                                        helloWorldExecutor
                                )
                        ),
                        helloExecutor
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
                                                        helloWorldHiExecutor
                                                )
                                        ),
                                        helloWorldExecutor
                                )
                        ),
                        helloExecutor
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
                        helloExecutor
                )
        );
        Command command1 = new Command(
                new LiteralCommandNode(
                        "hello",
                        List.of(
                                new LiteralCommandNode(
                                        "world",
                                        helloWorldExecutor
                                ),
                                new LiteralCommandNode(
                                        "there",
                                        helloThereExecutor
                                )
                        )
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
                                                        helloWorldHiExecutor
                                                )
                                        )
                                )
                        )
                )
        );

        CommandManager manager = new CommandManager();

        Command registeredCommand0 = manager.registerCommand("test", command0);
        Command registeredCommand1 = manager.registerCommand("test", command1);
        Command registeredCommand2 = manager.registerCommand("test", command2);

        Command expectedCommand0 = new Command(
                new LiteralCommandNode(
                        "hello",
                        helloExecutor
                )
        );
        Command expectedCommand1 = new Command(
                new LiteralCommandNode(
                        "hello",
                        List.of(
                                new LiteralCommandNode(
                                        "world",
                                        helloWorldExecutor
                                ),
                                new LiteralCommandNode(
                                        "there",
                                        helloThereExecutor
                                )
                        ),
                        helloExecutor
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
                                                        helloWorldHiExecutor
                                                )
                                        ),
                                        helloWorldExecutor
                                ),
                                new LiteralCommandNode(
                                        "there",
                                        helloThereExecutor
                                )
                        ),
                        helloExecutor
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
                        context -> CommandResult.successful(context, "hello")
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
                        context -> CommandResult.successful(context, "hello")
                )
        );

        CommandManager manager = new CommandManager();
        manager.registerCommand("test", command);

        CommandNotFoundResult<?> expected = CommandResult.commandNotFound("");
        CommandResult<?> actual = manager.execute(CHAD_SENDER, List.of());

        Assertions.assertEquals(expected, actual);
    }
}
