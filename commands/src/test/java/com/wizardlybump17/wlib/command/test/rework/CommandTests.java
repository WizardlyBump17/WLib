package com.wizardlybump17.wlib.command.test.rework;

import com.wizardlybump17.wlib.command.rework.Command;
import com.wizardlybump17.wlib.command.rework.context.CommandContext;
import com.wizardlybump17.wlib.command.rework.node.LiteralCommandNode;
import com.wizardlybump17.wlib.command.rework.result.CommandResult;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;
import java.util.List;

public class CommandTests {

    @Test
    void testCreate() {
        Command expected = new Command(
                new LiteralCommandNode("hello", List.of(
                        new LiteralCommandNode("world", List.of())
                ))
        );
        Command created = Command.createCommand("hello world");

        Assertions.assertEquals(expected, created);
    }

    @Test
    void testCreate1() {
        Command expected = new Command(
                new LiteralCommandNode("hello", List.of(
                        new LiteralCommandNode("world", List.of(
                                new LiteralCommandNode("hello1", List.of(
                                        new LiteralCommandNode("world1", List.of(
                                                new LiteralCommandNode("hello2", List.of(
                                                        new LiteralCommandNode("world2", List.of())
                                                ))
                                        ))
                                ))
                        ))
                ))
        );
        Command created = Command.createCommand("hello world hello1 world1 hello2 world2");

        Assertions.assertEquals(expected, created);
    }

    @Test
    public void testCreate2() {
        CommandTests object = new CommandTests();

        for (Method method : object.getClass().getMethods()) {
            if (method.isAnnotationPresent(com.wizardlybump17.wlib.command.rework.annotation.Command.class)) {
                Command command = Command.fromMethod(method, object);
                CommandResult<?> result = command.execute(null, List.of("test", "test2"));
                System.out.println(result);
            }
        }
    }

    @com.wizardlybump17.wlib.command.rework.annotation.Command("test")
    public void testCommand(@NotNull CommandContext context) {
    }

    @com.wizardlybump17.wlib.command.rework.annotation.Command("test test2")
    public void testCommand2(@NotNull CommandContext context) {
    }
}
