package com.wizardlybump17.wlib.command.test.rework;

import com.wizardlybump17.wlib.command.rework.Command;
import com.wizardlybump17.wlib.command.rework.executor.CommandExecutor;
import com.wizardlybump17.wlib.command.rework.node.LiteralCommandNode;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;

class CommandTests {

    @Test
    void testCreate() {
        Command expected = new Command(
                new LiteralCommandNode("hello", List.of(
                        new LiteralCommandNode("world", List.of(), CommandExecutor.TEST_EXECUTOR)
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
                                                        new LiteralCommandNode("world2", List.of(), CommandExecutor.TEST_EXECUTOR)
                                                ))
                                        ))
                                ))
                        ))
                ))
        );
        Command created = Command.createCommand("hello world hello1 world1 hello2 world2");

        Assertions.assertEquals(expected, created);
    }
}
