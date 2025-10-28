package com.wizardlybump17.wlib.command.test.rework;

import com.wizardlybump17.wlib.command.rework.Command;
import com.wizardlybump17.wlib.command.rework.input.AllowedNumberInputs;
import com.wizardlybump17.wlib.command.rework.node.IntegerCommandNode;
import com.wizardlybump17.wlib.command.rework.node.LiteralCommandNode;
import com.wizardlybump17.wlib.command.rework.result.CommandResult;
import org.junit.jupiter.api.Test;

import java.util.List;

class NodeResultTests {

    @Test
    void test() {
        Command command = new Command(
                new LiteralCommandNode("hello", List.of(
                        new LiteralCommandNode("world", List.of(
                                new IntegerCommandNode("a1", List.of(), new AllowedNumberInputs.AllowedIntegerInputs.Range(10, 100))
                        ))
                ))
        );

        CommandResult<?> result = command.execute(null, List.of("hello", "world", "10a", "a"));
        System.out.println(result);
    }

//    @Test
//    void testSuccess() {
//        LiteralCommandNode world = new LiteralCommandNode("world", List.of());
//        LiteralCommandNode hello = new LiteralCommandNode("hello", List.of(world));
//        Command command = new Command(hello);
//
//        SuccessResult<String> worldResult = CommandResult.successful("world");
//        CommandContext.CommandNodeArguments expected = new CommandContext.CommandNodeArguments(
//                List.of(
//                        new CommandContext.CommandNodeArgument<>(hello, "hello", null),
//                        new CommandContext.CommandNodeArgument<>(world, "world", null)
//                )
//        );
//        CommandContext.CommandNodeArguments actual = command.execute(null, List.of("hello", "world"));
//        Assertions.assertEquals(expected, actual);
//    }
//
//    @Test
//    void testUnsuccessExtra1() {
//        LiteralCommandNode worldNode = new LiteralCommandNode("world", List.of());
//        LiteralCommandNode helloNode = new LiteralCommandNode("hello", List.of(worldNode));
//        Command command = new Command(helloNode);
//
//        CommandContext.CommandNodeArguments expected = new CommandContext.CommandNodeArguments(
//                List.of(
//                        new CommandContext.CommandNodeArgument<>(helloNode, "hello", CommandResult.successful("hello")),
//                        new CommandContext.CommandNodeArgument<>(worldNode, "world", CommandResult.successful("world"))
//                ),
//                new ExtraArgumentsResult<>("a"),
//                worldNode,
//                "world"
//        );
//        CommandContext.CommandNodeArguments actual = command.getArguments(List.of("hello", "world", "a"));
//        Assertions.assertEquals(expected, actual);
//    }
//
//    @Test
//    void testUnsuccessExtra2() {
//        LiteralCommandNode worldNode = new LiteralCommandNode("world", List.of());
//        LiteralCommandNode helloNode = new LiteralCommandNode("hello", List.of(worldNode));
//        Command command = new Command(helloNode);
//
//        CommandContext.CommandNodeArguments expected = new CommandContext.CommandNodeArguments(
//                List.of(
//                        new CommandContext.CommandNodeArgument<>(helloNode, "hello", CommandResult.successful("hello")),
//                        new CommandContext.CommandNodeArgument<>(worldNode, "world", CommandResult.successful("world"))
//                ),
//                new ExtraArgumentsResult<>("a"),
//                worldNode,
//                "world"
//        );
//        CommandContext.CommandNodeArguments actual = command.getArguments(List.of("hello", "world", "a", "b"));
//        Assertions.assertEquals(expected, actual);
//    }
//
//    @Test
//    void testUnsuccessExtra3() {
//        LiteralCommandNode worldNode = new LiteralCommandNode("world", List.of());
//        LiteralCommandNode helloNode = new LiteralCommandNode("hello", List.of(worldNode));
//        Command command = new Command(helloNode);
//
//        CommandContext.CommandNodeArguments expected = new CommandContext.CommandNodeArguments(
//                List.of(
//                        new CommandContext.CommandNodeArgument<>(helloNode, "hello", CommandResult.successful("hello")),
//                        new CommandContext.CommandNodeArgument<>(worldNode, "world", CommandResult.successful("world"))
//                ),
//                new ExtraArgumentsResult<>("a"),
//                worldNode,
//                "world"
//        );
//        CommandContext.CommandNodeArguments actual = command.getArguments(List.of("hello", "world", "a", "b", "c"));
//        Assertions.assertEquals(expected, actual);
//    }
//
//    @Test
//    void testUnsuccessLess1() {
//        LiteralCommandNode cNode = new LiteralCommandNode("c", List.of());
//        LiteralCommandNode bNode = new LiteralCommandNode("b", List.of(cNode));
//        LiteralCommandNode aNode = new LiteralCommandNode("a", List.of(bNode));
//        LiteralCommandNode worldNode = new LiteralCommandNode("world", List.of(aNode));
//        LiteralCommandNode helloNode = new LiteralCommandNode("hello", List.of(worldNode));
//        Command command = new Command(helloNode);
//
//        CommandContext.CommandNodeArguments expected = new CommandContext.CommandNodeArguments(
//                List.of(
//                        new CommandContext.CommandNodeArgument<>(helloNode, "hello", CommandResult.successful("hello")),
//                        new CommandContext.CommandNodeArgument<>(worldNode, "world", CommandResult.successful("world")),
//                        new CommandContext.CommandNodeArgument<>(aNode, "a", CommandResult.successful("a")),
//                        new CommandContext.CommandNodeArgument<>(bNode, "b", CommandResult.successful("b"))
//                ),
//                new InsufficientArgumentsResult<>("b", bNode),
//                bNode,
//                "b"
//        );
//        CommandContext.CommandNodeArguments actual = command.getArguments(List.of("hello", "world", "a", "b"));
//        Assertions.assertEquals(expected, actual);
//    }
//
//    @Test
//    void testUnsuccessLess2() {
//        LiteralCommandNode cNode = new LiteralCommandNode("c", List.of());
//        LiteralCommandNode bNode = new LiteralCommandNode("b", List.of(cNode));
//        LiteralCommandNode aNode = new LiteralCommandNode("a", List.of(bNode));
//        LiteralCommandNode worldNode = new LiteralCommandNode("world", List.of(aNode));
//        LiteralCommandNode helloNode = new LiteralCommandNode("hello", List.of(worldNode));
//        Command command = new Command(helloNode);
//
//        CommandContext.CommandNodeArguments expected = new CommandContext.CommandNodeArguments(
//                List.of(
//                        new CommandContext.CommandNodeArgument<>(helloNode, "hello", CommandResult.successful("hello")),
//                        new CommandContext.CommandNodeArgument<>(worldNode, "world", CommandResult.successful("world")),
//                        new CommandContext.CommandNodeArgument<>(aNode, "a", CommandResult.successful("a"))
//                ),
//                new InsufficientArgumentsResult<>("a", aNode),
//                aNode,
//                "a"
//        );
//        CommandContext.CommandNodeArguments actual = command.getArguments(List.of("hello", "world", "a"));
//        Assertions.assertEquals(expected, actual);
//    }
//
//    @Test
//    void testUnsuccessLess3() {
//        LiteralCommandNode cNode = new LiteralCommandNode("c", List.of());
//        LiteralCommandNode bNode = new LiteralCommandNode("b", List.of(cNode));
//        LiteralCommandNode aNode = new LiteralCommandNode("a", List.of(bNode));
//        LiteralCommandNode worldNode = new LiteralCommandNode("world", List.of(aNode));
//        LiteralCommandNode helloNode = new LiteralCommandNode("hello", List.of(worldNode));
//        Command command = new Command(helloNode);
//
//        CommandContext.CommandNodeArguments expected = new CommandContext.CommandNodeArguments(
//                List.of(
//                        new CommandContext.CommandNodeArgument<>(helloNode, "hello", CommandResult.successful("hello")),
//                        new CommandContext.CommandNodeArgument<>(worldNode, "world", CommandResult.successful("world"))
//                ),
//                new InsufficientArgumentsResult<>("world", worldNode),
//                worldNode,
//                "world"
//        );
//        CommandContext.CommandNodeArguments actual = command.getArguments(List.of("hello", "world"));
//        Assertions.assertEquals(expected, actual);
//    }
//
//    @Test
//    void testUnsuccessLess4() {
//        LiteralCommandNode cNode = new LiteralCommandNode("c", List.of());
//        LiteralCommandNode bNode = new LiteralCommandNode("b", List.of(cNode));
//        LiteralCommandNode aNode = new LiteralCommandNode("a", List.of(bNode));
//        LiteralCommandNode worldNode = new LiteralCommandNode("world", List.of(aNode));
//        LiteralCommandNode helloNode = new LiteralCommandNode("hello", List.of(worldNode));
//        Command command = new Command(helloNode);
//
//        CommandContext.CommandNodeArguments expected = new CommandContext.CommandNodeArguments(
//                List.of(
//                        new CommandContext.CommandNodeArgument<>(helloNode, "hello", CommandResult.successful("hello"))
//                ),
//                new InsufficientArgumentsResult<>("hello", helloNode),
//                helloNode,
//                "hello"
//        );
//        CommandContext.CommandNodeArguments actual = command.getArguments(List.of("hello"));
//        Assertions.assertEquals(expected, actual);
//    }
//
//    @Test
//    void testUnsuccessLess5() {
//        LiteralCommandNode helloNode = new LiteralCommandNode("hello", List.of(
//                new LiteralCommandNode("world", List.of(
//                        new LiteralCommandNode("a", List.of(
//                                new LiteralCommandNode("b", List.of(
//                                        new LiteralCommandNode("c", List.of())
//                                ))
//                        ))
//                ))
//        ));
//        Command command = new Command(helloNode);
//
//        CommandContext.CommandNodeArguments expected = new CommandContext.CommandNodeArguments(
//                Map.of(),
//                new InsufficientArgumentsResult<>("", helloNode),
//                helloNode,
//                ""
//        );
//        CommandContext.CommandNodeArguments actual = command.getArguments(List.of());
//        Assertions.assertEquals(expected, actual);
//    }
//
//    @Test
//    void testSuccessMultipleChildren0() {
//        Command command = new Command(
//                new LiteralCommandNode("hello", List.of(
//                        new LiteralCommandNode("world2", List.of(
//                                new LiteralCommandNode("world3", List.of()),
//                                new LiteralCommandNode("world4", List.of())
//                        )),
//                        new LiteralCommandNode("world1", List.of(
//                                new LiteralCommandNode("world3", List.of())
//                        )),
//                        new LiteralCommandNode("world", List.of())
//                ))
//        );
//
//        CommandContext.CommandNodeArguments actual = command.getArguments(List.of("hello", "world2", "world3", "a"));
//        Assertions.assertInstanceOf(ExtraArgumentsResult.class, actual.lastResult());
//    }
}
