package com.wizardlybump17.wlib.command.test.rework;

import com.wizardlybump17.wlib.command.rework.Command;
import com.wizardlybump17.wlib.command.rework.executor.CommandNodeExecutor;
import com.wizardlybump17.wlib.command.rework.input.AllowedNumberInputs;
import com.wizardlybump17.wlib.command.rework.manager.CommandManager;
import com.wizardlybump17.wlib.command.rework.node.IntegerCommandNode;
import com.wizardlybump17.wlib.command.rework.node.LiteralCommandNode;
import com.wizardlybump17.wlib.command.rework.result.CommandResult;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;

class CommandManagerTests {

    @Test
    void test() {
        CommandNodeExecutor<Object> executor = context -> CommandResult.successful(context, "a1");

        Command command0 = new Command(
                new LiteralCommandNode("hello", List.of(
                        new LiteralCommandNode("world", List.of(
                                new IntegerCommandNode("a1", new AllowedNumberInputs.AllowedIntegerInputs.Range(10, 100), executor)
                        ))
                ))
        );
        Command command1 = new Command(
                new LiteralCommandNode("hello", List.of(
                        new LiteralCommandNode("world1", List.of(
                                new IntegerCommandNode("a1", new AllowedNumberInputs.AllowedIntegerInputs.Range(10, 100), executor)
                        ))
                ))
        );
        Command command2 = new Command(
                new LiteralCommandNode("hello", List.of(
                        new LiteralCommandNode("world", List.of(
                                new IntegerCommandNode("a2", new AllowedNumberInputs.AllowedIntegerInputs.Range(10, 100), executor)
                        ))
                ))
        );
        Command command3 = new Command(
                new LiteralCommandNode("hello", List.of(
                        new LiteralCommandNode("world", List.of(
                                new IntegerCommandNode("a1", new AllowedNumberInputs.AllowedIntegerInputs.Range(10, 100), executor),
                                new IntegerCommandNode("a2", List.of(
                                        new LiteralCommandNode("hi")
                                ), new AllowedNumberInputs.AllowedIntegerInputs.Range(10, 100), executor)
                        )),
                        new LiteralCommandNode("world1", List.of(
                                new IntegerCommandNode("a1", new AllowedNumberInputs.AllowedIntegerInputs.Range(10, 100), executor)
                        ))
                ))
        );

        CommandManager manager = new CommandManager();
        manager.registerCommand("test", command0);
        manager.registerCommand("test", command1);
        manager.registerCommand("test", command2);
        manager.registerCommand("test", command3);

        Command expected = new Command(
                new LiteralCommandNode("hello", List.of(
                        new LiteralCommandNode("world", List.of(
                                new IntegerCommandNode("a1", new AllowedNumberInputs.AllowedIntegerInputs.Range(10, 100), executor),
                                new IntegerCommandNode("a2", List.of(
                                        new LiteralCommandNode("hi")
                                ), new AllowedNumberInputs.AllowedIntegerInputs.Range(10, 100), executor)
                        )),
                        new LiteralCommandNode("world1", List.of(
                                new IntegerCommandNode("a1", new AllowedNumberInputs.AllowedIntegerInputs.Range(10, 100), executor)
                        ))
                ))
        );

        Assertions.assertEquals(expected, manager.getCommand("hello").get());
    }
}
