package com.wizardlybump17.wlib.command.test.rework;

import com.wizardlybump17.wlib.command.rework.Command;
import com.wizardlybump17.wlib.command.rework.context.CommandContext;
import com.wizardlybump17.wlib.command.rework.node.LiteralCommandNode;
import com.wizardlybump17.wlib.command.rework.result.CommandResult;
import com.wizardlybump17.wlib.command.rework.result.SuccessResult;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

class NodeResultTests {

    @Test
    void testSuccess() {
        LiteralCommandNode world = new LiteralCommandNode("world", List.of());
        LiteralCommandNode hello = new LiteralCommandNode("hello", List.of(world));
        Command command = new Command(hello);

        SuccessResult<String> worldResult = CommandResult.successful("world");
        CommandContext.CommandNodeArguments expected = new CommandContext.CommandNodeArguments(
                Map.of(
                    "hello", new CommandContext.CommandNodeArgument<>(hello, "hello", CommandResult.successful("hello")),
                    "world", new CommandContext.CommandNodeArgument<>(world, "world", worldResult)
                ),
                worldResult,
                world,
                "world"
        );
        CommandContext.CommandNodeArguments actual = command.getArguments(List.of("hello", "world"));
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void testUnsuccessExtra1() {
        Command command = new Command(
                new LiteralCommandNode("hello", List.of(
                        new LiteralCommandNode("world", List.of())
                ))
        );

        CommandContext.CommandNodeArguments arguments = command.getArguments(List.of("hello", "world", "a"));
        Assertions.assertNull(arguments);
    }

    @Test
    void testUnsuccessExtra2() {
        Command command = new Command(
                new LiteralCommandNode("hello", List.of(
                        new LiteralCommandNode("world", List.of())
                ))
        );

        CommandContext.CommandNodeArguments arguments = command.getArguments(List.of("hello", "world", "a", "b"));
        Assertions.assertNull(arguments);
    }

    @Test
    void testUnsuccessExtra3() {
        Command command = new Command(
                new LiteralCommandNode("hello", List.of(
                        new LiteralCommandNode("world", List.of())
                ))
        );

        CommandContext.CommandNodeArguments arguments = command.getArguments(List.of("hello", "world", "a", "b", "c"));
        Assertions.assertNull(arguments);
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

        CommandContext.CommandNodeArguments arguments = command.getArguments(List.of("hello", "world", "a", "b"));
        Assertions.assertNull(arguments);
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

        CommandContext.CommandNodeArguments arguments = command.getArguments(List.of("hello", "world", "a"));
        Assertions.assertNull(arguments);
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

        CommandContext.CommandNodeArguments arguments = command.getArguments(List.of("hello", "world"));
        Assertions.assertNull(arguments);
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

        CommandContext.CommandNodeArguments arguments = command.getArguments(List.of("hello"));
        Assertions.assertNull(arguments);
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

        CommandContext.CommandNodeArguments arguments = command.getArguments(List.of());
        Assertions.assertNull(arguments);
    }
}
