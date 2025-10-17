package com.wizardlybump17.wlib.command.test.rework;

import com.wizardlybump17.wlib.command.rework.Command;
import com.wizardlybump17.wlib.command.rework.node.LiteralCommandNode;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

class ReworkTests {

    @Test
    void test() {
        LiteralCommandNode world = new LiteralCommandNode("world", List.of());
        LiteralCommandNode hello = new LiteralCommandNode("hello", List.of(world));
        Command command = new Command(hello);

        Map<String, Command.NodeResult<?>> nodes = command.getNodes(List.of("hello"));
        Assertions.assertEquals(Map.of(
                "hello", new Command.NodeResult<>(hello, "hello"),
                "world", new Command.NodeResult<>(world, "world")
        ), nodes);
        System.out.println(nodes);
    }
}
