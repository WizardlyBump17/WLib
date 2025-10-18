package com.wizardlybump17.wlib.command.test.rework;

import com.wizardlybump17.wlib.command.rework.Command;
import com.wizardlybump17.wlib.command.rework.node.LiteralCommandNode;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

class ReworkTests {

    @Test
    void testSuccess() {
        LiteralCommandNode world = new LiteralCommandNode("world", List.of());
        LiteralCommandNode hello = new LiteralCommandNode("hello", List.of(world));
        Command command = new Command(hello);

        Map<String, Command.NodeResult<?>> nodes = command.getNodes(List.of("hello", "world"));
        Assertions.assertEquals(Map.of(
                "hello", new Command.NodeResult<>(hello, "hello"),
                "world", new Command.NodeResult<>(world, "world")
        ), nodes);
    }

    @Test
    void testUnsuccessExtra1() {
        Command command = new Command(
                new LiteralCommandNode("hello", List.of(
                        new LiteralCommandNode("world", List.of())
                ))
        );

        Map<String, Command.NodeResult<?>> nodes = command.getNodes(List.of("hello", "world", "a"));
        Assertions.assertEquals(Map.of(), nodes);
    }

    @Test
    void testUnsuccessExtra2() {
        Command command = new Command(
                new LiteralCommandNode("hello", List.of(
                        new LiteralCommandNode("world", List.of())
                ))
        );

        Map<String, Command.NodeResult<?>> nodes = command.getNodes(List.of("hello", "world", "a", "b"));
        Assertions.assertEquals(Map.of(), nodes);
    }

    @Test
    void testUnsuccessExtra3() {
        Command command = new Command(
                new LiteralCommandNode("hello", List.of(
                        new LiteralCommandNode("world", List.of())
                ))
        );

        Map<String, Command.NodeResult<?>> nodes = command.getNodes(List.of("hello", "world", "a", "b", "c"));
        Assertions.assertEquals(Map.of(), nodes);
    }

    @Test
    void testUnsuccessLess1() {
        Command command = new Command(
                new LiteralCommandNode("hello", List.of(
                        new LiteralCommandNode("world", List.of(
                                new LiteralCommandNode("a", List.of(
                                        new LiteralCommandNode("b", List.of(
                                                new LiteralCommandNode("c", List.of())
                                        ))
                                ))
                        ))
                ))
        );

        Map<String, Command.NodeResult<?>> nodes = command.getNodes(List.of("hello", "world", "a", "b"));
        Assertions.assertEquals(Map.of(), nodes);
    }

    @Test
    void testUnsuccessLess2() {
        Command command = new Command(
                new LiteralCommandNode("hello", List.of(
                        new LiteralCommandNode("world", List.of(
                                new LiteralCommandNode("a", List.of(
                                        new LiteralCommandNode("b", List.of(
                                                new LiteralCommandNode("c", List.of())
                                        ))
                                ))
                        ))
                ))
        );

        Map<String, Command.NodeResult<?>> nodes = command.getNodes(List.of("hello", "world", "a"));
        Assertions.assertEquals(Map.of(), nodes);
    }

    @Test
    void testUnsuccessLess3() {
        Command command = new Command(
                new LiteralCommandNode("hello", List.of(
                        new LiteralCommandNode("world", List.of(
                                new LiteralCommandNode("a", List.of(
                                        new LiteralCommandNode("b", List.of(
                                                new LiteralCommandNode("c", List.of())
                                        ))
                                ))
                        ))
                ))
        );

        Map<String, Command.NodeResult<?>> nodes = command.getNodes(List.of("hello", "world"));
        Assertions.assertEquals(Map.of(), nodes);
    }

    @Test
    void testUnsuccessLess4() {
        Command command = new Command(
                new LiteralCommandNode("hello", List.of(
                        new LiteralCommandNode("world", List.of(
                                new LiteralCommandNode("a", List.of(
                                        new LiteralCommandNode("b", List.of(
                                                new LiteralCommandNode("c", List.of())
                                        ))
                                ))
                        ))
                ))
        );

        Map<String, Command.NodeResult<?>> nodes = command.getNodes(List.of("hello"));
        Assertions.assertEquals(Map.of(), nodes);
    }

    @Test
    void testUnsuccessLess5() {
        Command command = new Command(
                new LiteralCommandNode("hello", List.of(
                        new LiteralCommandNode("world", List.of(
                                new LiteralCommandNode("a", List.of(
                                        new LiteralCommandNode("b", List.of(
                                                new LiteralCommandNode("c", List.of())
                                        ))
                                ))
                        ))
                ))
        );

        Map<String, Command.NodeResult<?>> nodes = command.getNodes(List.of());
        Assertions.assertEquals(Map.of(), nodes);
    }
}
