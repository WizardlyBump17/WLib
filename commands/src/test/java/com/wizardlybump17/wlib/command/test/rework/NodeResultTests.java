package com.wizardlybump17.wlib.command.test.rework;

import com.wizardlybump17.wlib.command.rework.Command;
import com.wizardlybump17.wlib.command.rework.context.CommandContext;
import com.wizardlybump17.wlib.command.rework.node.LiteralCommandNode;
import com.wizardlybump17.wlib.command.rework.result.CommandResult;
import com.wizardlybump17.wlib.command.rework.result.ExtraArgumentsResult;
import com.wizardlybump17.wlib.command.rework.result.InsufficientArgumentsResult;
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
        LiteralCommandNode worldNode = new LiteralCommandNode("world", List.of());
        LiteralCommandNode helloNode = new LiteralCommandNode("hello", List.of(worldNode));
        Command command = new Command(helloNode);

        CommandContext.CommandNodeArguments expected = new CommandContext.CommandNodeArguments(
                List.of(
                        new CommandContext.CommandNodeArgument<>(helloNode, "hello", CommandResult.successful("hello")),
                        new CommandContext.CommandNodeArgument<>(worldNode, "world", CommandResult.successful("world"))
                ),
                new ExtraArgumentsResult<>("a"),
                worldNode,
                "world"
        );
        CommandContext.CommandNodeArguments actual = command.getArguments(List.of("hello", "world", "a"));
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void testUnsuccessExtra2() {
        LiteralCommandNode worldNode = new LiteralCommandNode("world", List.of());
        LiteralCommandNode helloNode = new LiteralCommandNode("hello", List.of(worldNode));
        Command command = new Command(helloNode);

        CommandContext.CommandNodeArguments expected = new CommandContext.CommandNodeArguments(
                List.of(
                        new CommandContext.CommandNodeArgument<>(helloNode, "hello", CommandResult.successful("hello")),
                        new CommandContext.CommandNodeArgument<>(worldNode, "world", CommandResult.successful("world"))
                ),
                new ExtraArgumentsResult<>("a"),
                worldNode,
                "world"
        );
        CommandContext.CommandNodeArguments actual = command.getArguments(List.of("hello", "world", "a", "b"));
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void testUnsuccessExtra3() {
        LiteralCommandNode worldNode = new LiteralCommandNode("world", List.of());
        LiteralCommandNode helloNode = new LiteralCommandNode("hello", List.of(worldNode));
        Command command = new Command(helloNode);

        CommandContext.CommandNodeArguments expected = new CommandContext.CommandNodeArguments(
                List.of(
                        new CommandContext.CommandNodeArgument<>(helloNode, "hello", CommandResult.successful("hello")),
                        new CommandContext.CommandNodeArgument<>(worldNode, "world", CommandResult.successful("world"))
                ),
                new ExtraArgumentsResult<>("a"),
                worldNode,
                "world"
        );
        CommandContext.CommandNodeArguments actual = command.getArguments(List.of("hello", "world", "a", "b", "c"));
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void testUnsuccessLess1() {
        LiteralCommandNode cNode = new LiteralCommandNode("c", List.of());
        LiteralCommandNode bNode = new LiteralCommandNode("b", List.of(cNode));
        LiteralCommandNode aNode = new LiteralCommandNode("a", List.of(bNode));
        LiteralCommandNode worldNode = new LiteralCommandNode("world", List.of(aNode));
        LiteralCommandNode helloNode = new LiteralCommandNode("hello", List.of(worldNode));
        Command command = new Command(helloNode);

        CommandContext.CommandNodeArguments expected = new CommandContext.CommandNodeArguments(
                List.of(
                        new CommandContext.CommandNodeArgument<>(helloNode, "hello", CommandResult.successful("hello")),
                        new CommandContext.CommandNodeArgument<>(worldNode, "world", CommandResult.successful("world")),
                        new CommandContext.CommandNodeArgument<>(aNode, "a", CommandResult.successful("a")),
                        new CommandContext.CommandNodeArgument<>(bNode, "b", CommandResult.successful("b"))
                ),
                new InsufficientArgumentsResult<>("b", bNode),
                bNode,
                "b"
        );
        CommandContext.CommandNodeArguments actual = command.getArguments(List.of("hello", "world", "a", "b"));
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void testUnsuccessLess2() {
        LiteralCommandNode cNode = new LiteralCommandNode("c", List.of());
        LiteralCommandNode bNode = new LiteralCommandNode("b", List.of(cNode));
        LiteralCommandNode aNode = new LiteralCommandNode("a", List.of(bNode));
        LiteralCommandNode worldNode = new LiteralCommandNode("world", List.of(aNode));
        LiteralCommandNode helloNode = new LiteralCommandNode("hello", List.of(worldNode));
        Command command = new Command(helloNode);

        CommandContext.CommandNodeArguments expected = new CommandContext.CommandNodeArguments(
                List.of(
                        new CommandContext.CommandNodeArgument<>(helloNode, "hello", CommandResult.successful("hello")),
                        new CommandContext.CommandNodeArgument<>(worldNode, "world", CommandResult.successful("world")),
                        new CommandContext.CommandNodeArgument<>(aNode, "a", CommandResult.successful("a"))
                ),
                new InsufficientArgumentsResult<>("a", aNode),
                aNode,
                "a"
        );
        CommandContext.CommandNodeArguments actual = command.getArguments(List.of("hello", "world", "a"));
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void testUnsuccessLess3() {
        LiteralCommandNode cNode = new LiteralCommandNode("c", List.of());
        LiteralCommandNode bNode = new LiteralCommandNode("b", List.of(cNode));
        LiteralCommandNode aNode = new LiteralCommandNode("a", List.of(bNode));
        LiteralCommandNode worldNode = new LiteralCommandNode("world", List.of(aNode));
        LiteralCommandNode helloNode = new LiteralCommandNode("hello", List.of(worldNode));
        Command command = new Command(helloNode);

        CommandContext.CommandNodeArguments expected = new CommandContext.CommandNodeArguments(
                List.of(
                        new CommandContext.CommandNodeArgument<>(helloNode, "hello", CommandResult.successful("hello")),
                        new CommandContext.CommandNodeArgument<>(worldNode, "world", CommandResult.successful("world"))
                ),
                new InsufficientArgumentsResult<>("world", worldNode),
                worldNode,
                "world"
        );
        CommandContext.CommandNodeArguments actual = command.getArguments(List.of("hello", "world"));
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void testUnsuccessLess4() {
        LiteralCommandNode cNode = new LiteralCommandNode("c", List.of());
        LiteralCommandNode bNode = new LiteralCommandNode("b", List.of(cNode));
        LiteralCommandNode aNode = new LiteralCommandNode("a", List.of(bNode));
        LiteralCommandNode worldNode = new LiteralCommandNode("world", List.of(aNode));
        LiteralCommandNode helloNode = new LiteralCommandNode("hello", List.of(worldNode));
        Command command = new Command(helloNode);

        CommandContext.CommandNodeArguments expected = new CommandContext.CommandNodeArguments(
                List.of(
                        new CommandContext.CommandNodeArgument<>(helloNode, "hello", CommandResult.successful("hello"))
                ),
                new InsufficientArgumentsResult<>("hello", helloNode),
                helloNode,
                "hello"
        );
        CommandContext.CommandNodeArguments actual = command.getArguments(List.of("hello"));
        Assertions.assertEquals(expected, actual);
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

        CommandContext.CommandNodeArguments expected = CommandContext.CommandNodeArguments.EMPTY;
        CommandContext.CommandNodeArguments actual = command.getArguments(List.of());
        Assertions.assertEquals(expected, actual);
    }
}
