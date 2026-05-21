//package com.wizardlybump17.wlib.test.command.manager;
//
//import com.wizardlybump17.wlib.command.Command;
//import com.wizardlybump17.wlib.command.executor.CommandNodeExecutor;
//import com.wizardlybump17.wlib.command.manager.CommandManager;
//import com.wizardlybump17.wlib.command.node.LiteralCommandNode;
//import com.wizardlybump17.wlib.command.result.CommandResult;
//import com.wizardlybump17.wlib.command.result.error.CommandNotFoundResult;
//import com.wizardlybump17.wlib.command.result.success.SuccessResult;
//import com.wizardlybump17.wlib.command.sender.BasicCommandSender;
//import com.wizardlybump17.wlib.command.sender.CommandSender;
//import org.jetbrains.annotations.NotNull;
//import org.junit.jupiter.api.Assertions;
//import org.junit.jupiter.api.Test;
//
//import java.util.List;
//import java.util.UUID;
//import java.util.function.Consumer;
//
//class CommandManagerTests {
//
//    static final @NotNull Consumer<String> SENDER_MESSAGE_CONSUMER = System.out::println;
//    static final @NotNull CommandSender<Object> CHAD_SENDER = new BasicCommandSender<>(new Object(), "Chad", UUID.nameUUIDFromBytes("Chad".getBytes()), SENDER_MESSAGE_CONSUMER, $ -> true);
//    static final @NotNull CommandSender<Object> BETA_SENDER = new BasicCommandSender<>(new Object(), "Beta", UUID.nameUUIDFromBytes("Beta".getBytes()), SENDER_MESSAGE_CONSUMER, $ -> false);
//
//    @Test
//    void testRegisterDifferent() {
//        Command command0 = new Command(
//                new LiteralCommandNode(
//                        "hello0",
//                        List.of(),
//                        context -> CommandResult.successful(context, "hello0"),
//                        "permission"
//                )
//        );
//        Command command1 = new Command(
//                new LiteralCommandNode(
//                        "hello1",
//                        List.of(),
//                        context -> CommandResult.successful(context, "hello1"),
//                        "permission"
//                )
//        );
//        Command command2 = new Command(
//                new LiteralCommandNode(
//                        "hello2",
//                        List.of(),
//                        context -> CommandResult.successful(context, "hello2"),
//                        "permission")
//        );
//        Command command3 = new Command(
//                new LiteralCommandNode(
//                        "hello3",
//                        List.of(),
//                        context -> CommandResult.successful(context, "hello3"),
//                        "permission"
//                )
//        );
//
//        CommandManager manager = new CommandManager();
//
//        Command registeredCommand0 = manager.registerCommand("test", command0);
//        Command registeredCommand1 = manager.registerCommand("test", command1);
//        Command registeredCommand2 = manager.registerCommand("test", command2);
//        Command registeredCommand3 = manager.registerCommand("test", command3);
//
//        Assertions.assertEquals(command0, registeredCommand0);
//        Assertions.assertEquals(command1, registeredCommand1);
//        Assertions.assertEquals(command2, registeredCommand2);
//        Assertions.assertEquals(command3, registeredCommand3);
//    }
//
//    @Test
//    void testRegisterMerging0() {
//        CommandNodeExecutor<?> helloExecutor = context -> CommandResult.successful(context, "hello");
//        CommandNodeExecutor<?> helloWorldExecutor = context -> CommandResult.successful(context, "hello world");
//        CommandNodeExecutor<?> helloWorldHiExecutor = context -> CommandResult.successful(context, "hello world hi");
//
//        Command command0 = new Command(
//                new LiteralCommandNode(
//                        "hello",
//                        List.of(),
//                        helloExecutor,
//                        "permission"
//                )
//        );
//        Command command1 = new Command(
//                new LiteralCommandNode(
//                        "hello",
//                        List.of(
//                                new LiteralCommandNode(
//                                        "world",
//                                        List.of(),
//                                        helloWorldExecutor,
//                                        "permission"
//                                )
//                        ),
//                        null,
//                        "permission"
//                )
//        );
//        Command command2 = new Command(
//                new LiteralCommandNode(
//                        "hello",
//                        List.of(
//                                new LiteralCommandNode(
//                                        "world",
//                                        List.of(
//                                                new LiteralCommandNode(
//                                                        "hi",
//                                                        List.of(),
//                                                        helloWorldHiExecutor,
//                                                        "permission"
//                                                )
//                                        ),
//                                        null,
//                                        "permission"
//                                )
//                        ),
//                        null,
//                        "permission"
//                )
//        );
//
//        CommandManager manager = new CommandManager();
//
//        Command registeredCommand0 = manager.registerCommand("test", command0);
//        Command registeredCommand1 = manager.registerCommand("test", command1);
//        Command registeredCommand2 = manager.registerCommand("test", command2);
//
//        Command expectedCommand0 = new Command(
//                new LiteralCommandNode(
//                        "hello",
//                        List.of(),
//                        helloExecutor,
//                        "permission"
//                )
//        );
//        Command expectedCommand1 = new Command(
//                new LiteralCommandNode(
//                        "hello",
//                        List.of(
//                                new LiteralCommandNode(
//                                        "world",
//                                        List.of(),
//                                        helloWorldExecutor,
//                                        "permission"
//                                )
//                        ),
//                        helloExecutor,
//                        "permission"
//                )
//        );
//        Command expectedCommand2 = new Command(
//                new LiteralCommandNode(
//                        "hello",
//                        List.of(
//                                new LiteralCommandNode(
//                                        "world",
//                                        List.of(
//                                                new LiteralCommandNode(
//                                                        "hi",
//                                                        List.of(),
//                                                        helloWorldHiExecutor,
//                                                        "permission"
//                                                )
//                                        ),
//                                        helloWorldExecutor,
//                                        "permission"
//                                )
//                        ),
//                        helloExecutor,
//                        "permission"
//                )
//        );
//
//        Assertions.assertEquals(expectedCommand0, registeredCommand0);
//        Assertions.assertEquals(expectedCommand1, registeredCommand1);
//        Assertions.assertEquals(expectedCommand2, registeredCommand2);
//    }
//
//    @Test
//    void testRegisterMerging1() {
//        CommandNodeExecutor<?> helloExecutor = context -> CommandResult.successful(context, "hello");
//        CommandNodeExecutor<?> helloWorldExecutor = context -> CommandResult.successful(context, "hello world");
//        CommandNodeExecutor<?> helloWorldHiExecutor = context -> CommandResult.successful(context, "hello world hi");
//        CommandNodeExecutor<?> helloThereExecutor = context -> CommandResult.successful(context, "hello there");
//
//        Command command0 = new Command(
//                new LiteralCommandNode(
//                        "hello",
//                        List.of(),
//                        helloExecutor,
//                        "permission"
//                )
//        );
//        Command command1 = new Command(
//                new LiteralCommandNode(
//                        "hello",
//                        List.of(
//                                new LiteralCommandNode(
//                                        "world",
//                                        List.of(),
//                                        helloWorldExecutor,
//                                        "permission"
//                                ),
//                                new LiteralCommandNode(
//                                        "there",
//                                        List.of(),
//                                        helloThereExecutor,
//                                        "permission"
//                                )
//                        ),
//                        null,
//                        "permission"
//                )
//        );
//        Command command2 = new Command(
//                new LiteralCommandNode(
//                        "hello",
//                        List.of(
//                                new LiteralCommandNode(
//                                        "world",
//                                        List.of(
//                                                new LiteralCommandNode(
//                                                        "hi",
//                                                        List.of(),
//                                                        helloWorldHiExecutor,
//                                                        "permission"
//                                                )
//                                        ),
//                                        null,
//                                        "permission"
//                                )
//                        ),
//                        null,
//                        "permission"
//                )
//        );
//
//        CommandManager manager = new CommandManager();
//
//        Command registeredCommand0 = manager.registerCommand("test", command0);
//        Command registeredCommand1 = manager.registerCommand("test", command1);
//        Command registeredCommand2 = manager.registerCommand("test", command2);
//
//        Command expectedCommand0 = new Command(
//                new LiteralCommandNode(
//                        "hello",
//                        List.of(),
//                        helloExecutor,
//                        "permission"
//                )
//        );
//        Command expectedCommand1 = new Command(
//                new LiteralCommandNode(
//                        "hello",
//                        List.of(
//                                new LiteralCommandNode(
//                                        "world",
//                                        List.of(),
//                                        helloWorldExecutor,
//                                        "permission"
//                                ),
//                                new LiteralCommandNode(
//                                        "there",
//                                        List.of(),
//                                        helloThereExecutor,
//                                        "permission"
//                                )
//                        ),
//                        helloExecutor,
//                        "permission"
//                )
//        );
//        Command expectedCommand2 = new Command(
//                new LiteralCommandNode(
//                        "hello",
//                        List.of(
//                                new LiteralCommandNode(
//                                        "world",
//                                        List.of(
//                                                new LiteralCommandNode(
//                                                        "hi",
//                                                        List.of(),
//                                                        helloWorldHiExecutor,
//                                                        "permission"
//                                                )
//                                        ),
//                                        helloWorldExecutor,
//                                        "permission"
//                                ),
//                                new LiteralCommandNode(
//                                        "there",
//                                        List.of(),
//                                        helloThereExecutor,
//                                        "permission"
//                                )
//                        ),
//                        helloExecutor,
//                        "permission"
//                )
//        );
//
//        Assertions.assertEquals(expectedCommand0, registeredCommand0);
//        Assertions.assertEquals(expectedCommand1, registeredCommand1);
//        Assertions.assertEquals(expectedCommand2, registeredCommand2);
//    }
//
//    @Test
//    void testCommandNotFound0() {
//        Command command = new Command(
//                new LiteralCommandNode(
//                        "hello",
//                        List.of(),
//                        context -> CommandResult.successful(context, "hello"),
//                        "permission"
//                )
//        );
//
//        CommandManager manager = new CommandManager();
//        manager.registerCommand("test", command);
//
//        CommandNotFoundResult<?> expected = CommandResult.commandNotFound("hello0");
//        CommandResult<?> actual = manager.execute(CHAD_SENDER, List.of("hello0"));
//
//        Assertions.assertEquals(expected, actual);
//    }
//
//    @Test
//    void testCommandNotFound1() {
//        Command command = new Command(
//                new LiteralCommandNode(
//                        "hello",
//                        List.of(),
//                        context -> CommandResult.successful(context, "hello"),
//                        "permission"
//                )
//        );
//
//        CommandManager manager = new CommandManager();
//        manager.registerCommand("test", command);
//
//        CommandNotFoundResult<?> expected = CommandResult.commandNotFound("");
//        CommandResult<?> actual = manager.execute(CHAD_SENDER, List.of());
//
//        Assertions.assertEquals(expected, actual);
//    }
//
//    @Test
//    void testSuccess0() {
//        LiteralCommandNode helloNode = new LiteralCommandNode(
//                "hello",
//                List.of(),
//                context -> CommandResult.successful(context, "hello"),
//                "permission"
//        );
//        Command command0 = new Command(helloNode);
//
//        Command command1 = new Command(
//                new LiteralCommandNode(
//                        "hi",
//                        List.of(),
//                        context -> CommandResult.successful(context, "hi"),
//                        "permission"
//                )
//        );
//        Command command2 = new Command(
//                new LiteralCommandNode(
//                        "welcome",
//                        List.of(),
//                        context -> CommandResult.successful(context, "welcome"),
//                        "permission"
//                )
//        );
//
//        CommandManager manager = new CommandManager();
//        manager.registerCommand("test", command0);
//        manager.registerCommand("test", command1);
//        manager.registerCommand("test", command2);
//
//        SuccessResult<String> expected = CommandResult.successful(0, helloNode, "hello");
//        CommandResult<?> actual = manager.execute(CHAD_SENDER, List.of("hello"));
//
//        Assertions.assertEquals(expected, actual);
//    }
//
//    @Test
//    void testSuccess1() {
//        Command command0 = new Command(
//                new LiteralCommandNode(
//                        "hello",
//                        List.of(),
//                        context -> CommandResult.successful(context, "hello"),
//                        "permission"
//                )
//        );
//
//        LiteralCommandNode thereNode = new LiteralCommandNode(
//                "there",
//                List.of(),
//                context -> CommandResult.successful(context, "hi there"),
//                "permission"
//        );
//        Command command1 = new Command(
//                new LiteralCommandNode(
//                        "hi",
//                        List.of(thereNode),
//                        null,
//                        "permission"
//                )
//        );
//
//        Command command2 = new Command(
//                new LiteralCommandNode(
//                        "welcome",
//                        List.of(),
//                        context -> CommandResult.successful(context, "welcome"),
//                        "permission"
//                )
//        );
//
//        CommandManager manager = new CommandManager();
//        manager.registerCommand("test", command0);
//        manager.registerCommand("test", command1);
//        manager.registerCommand("test", command2);
//
//        SuccessResult<String> expected = CommandResult.successful(1, thereNode, "hi there");
//        CommandResult<?> actual = manager.execute(CHAD_SENDER, List.of("hi", "there"));
//
//        Assertions.assertEquals(expected, actual);
//    }
//
//    @Test
//    void testRegisterMultipleDifferent0() {
//        Command command0 = new Command(
//                new LiteralCommandNode(
//                        "hello0",
//                        List.of(),
//                        context -> CommandResult.successful(context, "hello0"),
//                        "permission"
//                )
//        );
//        Command command1 = new Command(
//                new LiteralCommandNode(
//                        "hello1",
//                        List.of(),
//                        context -> CommandResult.successful(context, "hello1"),
//                        "permission"
//                )
//        );
//        Command command2 = new Command(
//                new LiteralCommandNode(
//                        "hello2",
//                        List.of(),
//                        context -> CommandResult.successful(context, "hello2"),
//                        "permission")
//        );
//        Command command3 = new Command(
//                new LiteralCommandNode(
//                        "hello3",
//                        List.of(),
//                        context -> CommandResult.successful(context, "hello3"),
//                        "permission"
//                )
//        );
//
//        CommandManager manager = new CommandManager();
//
//        List<Command> expected = List.of(command0, command1, command2, command3);
//        List<Command> actual = manager.registerCommands("test", List.of(command0, command1, command2, command3));
//
//        Assertions.assertEquals(expected, actual);
//    }
//
//    @Test
//    void testRegisterMultipleMerging0() {
//        CommandNodeExecutor<?> helloExecutor = context -> CommandResult.successful(context, "hello");
//        CommandNodeExecutor<?> helloWorldExecutor = context -> CommandResult.successful(context, "hello world");
//        CommandNodeExecutor<?> helloWorldHiExecutor = context -> CommandResult.successful(context, "hello world hi");
//
//        Command command0 = new Command(
//                new LiteralCommandNode(
//                        "hello",
//                        List.of(),
//                        helloExecutor,
//                        "permission"
//                )
//        );
//        Command command1 = new Command(
//                new LiteralCommandNode(
//                        "hello",
//                        List.of(
//                                new LiteralCommandNode(
//                                        "world",
//                                        List.of(),
//                                        helloWorldExecutor,
//                                        "permission"
//                                )
//                        ),
//                        null,
//                        "permission"
//                )
//        );
//        Command command2 = new Command(
//                new LiteralCommandNode(
//                        "hello",
//                        List.of(
//                                new LiteralCommandNode(
//                                        "world",
//                                        List.of(
//                                                new LiteralCommandNode(
//                                                        "hi",
//                                                        List.of(),
//                                                        helloWorldHiExecutor,
//                                                        "permission"
//                                                )
//                                        ),
//                                        null,
//                                        "permission"
//                                )
//                        ),
//                        null,
//                        "permission"
//                )
//        );
//
//        CommandManager manager = new CommandManager();
//
//        Command expectedCommand0 = new Command(
//                new LiteralCommandNode(
//                        "hello",
//                        List.of(),
//                        helloExecutor,
//                        "permission"
//                )
//        );
//        Command expectedCommand1 = new Command(
//                new LiteralCommandNode(
//                        "hello",
//                        List.of(
//                                new LiteralCommandNode(
//                                        "world",
//                                        List.of(),
//                                        helloWorldExecutor,
//                                        "permission"
//                                )
//                        ),
//                        helloExecutor,
//                        "permission"
//                )
//        );
//        Command expectedCommand2 = new Command(
//                new LiteralCommandNode(
//                        "hello",
//                        List.of(
//                                new LiteralCommandNode(
//                                        "world",
//                                        List.of(
//                                                new LiteralCommandNode(
//                                                        "hi",
//                                                        List.of(),
//                                                        helloWorldHiExecutor,
//                                                        "permission"
//                                                )
//                                        ),
//                                        helloWorldExecutor,
//                                        "permission"
//                                )
//                        ),
//                        helloExecutor,
//                        "permission"
//                )
//        );
//
//        List<Command> expected = List.of(expectedCommand0, expectedCommand1, expectedCommand2);
//        List<Command> actual = manager.registerCommands("test", List.of(command0, command1, command2));
//        Assertions.assertEquals(expected, actual);
//    }
//}
