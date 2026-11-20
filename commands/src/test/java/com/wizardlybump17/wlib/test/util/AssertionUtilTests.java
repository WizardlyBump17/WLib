package com.wizardlybump17.wlib.test.util;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.opentest4j.AssertionFailedError;

import java.util.List;
import java.util.Set;

class AssertionUtilTests {

//    @Test
//    void testCommandsEqualsIgnoreExecutorTrue() {
//        List<Command> left = List.of(
//                new Command(
//                        new LiteralCommandNode(
//                                "test0",
//                                List.of(),
//                                context -> CommandResult.successful(context, "Hi"),
//                                "test0"
//                        )
//                ),
//                new Command(
//                        new LiteralCommandNode(
//                                "test1",
//                                List.of(
//                                        new LiteralCommandNode(
//                                                "test1_0",
//                                                List.of(
//                                                        new LiteralCommandNode(
//                                                                "test1_0_0",
//                                                                List.of(),
//                                                                context -> CommandResult.successful(context, "Hello world"),
//                                                                null
//                                                        )
//                                                ),
//                                                context -> CommandResult.successful(context, "Hello there"),
//                                                null
//                                        ),
//                                        new LiteralCommandNode(
//                                                "test1_1",
//                                                List.of(),
//                                                context -> CommandResult.successful(context, "I ran out of ideas"),
//                                                null
//                                        )
//                                ),
//                                context -> CommandResult.successful(context, "Hello"),
//                                "test1"
//                        )
//                )
//        );
//        List<Command> right = List.of(
//                new Command(
//                        new LiteralCommandNode(
//                                "test0",
//                                List.of(),
//                                null,
//                                "test0"
//                        )
//                ),
//                new Command(
//                        new LiteralCommandNode(
//                                "test1",
//                                List.of(
//                                        new LiteralCommandNode(
//                                                "test1_0",
//                                                List.of(
//                                                        new LiteralCommandNode(
//                                                                "test1_0_0",
//                                                                List.of(),
//                                                                context -> CommandResult.successful(context, "Welcome"),
//                                                                null
//                                                        )
//                                                ),
//                                                CommandResult::genericError,
//                                                null
//                                        ),
//                                        new LiteralCommandNode(
//                                                "test1_1",
//                                                List.of(),
//                                                CommandResult::noPermission,
//                                                null
//                                        )
//                                ),
//                                context -> CommandResult.successful(context, "Hello"),
//                                "test1"
//                        )
//                )
//        );
//
//        Assertions.assertDoesNotThrow(() -> AssertionUtil.assertCommandsEqualsIgnoreExecutor(left, right));
//    }
//
//    @Test
//    void testCommandsEqualsIgnoreExecutorFalse() {
//        List<Command> left = List.of(
//                new Command(
//                        new LiteralCommandNode(
//                                "test0",
//                                List.of(),
//                                context -> CommandResult.successful(context, "Hi"),
//                                "test0"
//                        )
//                ),
//                new Command(
//                        new LiteralCommandNode(
//                                "test1",
//                                List.of(
//                                        new LiteralCommandNode(
//                                                "test1_0",
//                                                List.of(
//                                                        new LiteralCommandNode(
//                                                                "test1_0_0",
//                                                                List.of(),
//                                                                context -> CommandResult.successful(context, "Hello world"),
//                                                                null
//                                                        )
//                                                ),
//                                                context -> CommandResult.successful(context, "Hello there"),
//                                                null
//                                        ),
//                                        new LiteralCommandNode(
//                                                "test1_1",
//                                                List.of(),
//                                                context -> CommandResult.successful(context, "I ran out of ideas"),
//                                                null
//                                        )
//                                ),
//                                context -> CommandResult.successful(context, "Hello"),
//                                "test1"
//                        )
//                )
//        );
//        List<Command> right = List.of(
//                new Command(
//                        new LiteralCommandNode(
//                                "test1",
//                                List.of(),
//                                null,
//                                "test0"
//                        )
//                ),
//                new Command(
//                        new LiteralCommandNode(
//                                "test1",
//                                List.of(
//                                        new LiteralCommandNode(
//                                                "test1_0",
//                                                List.of(
//                                                        new LiteralCommandNode(
//                                                                "test1_0_0",
//                                                                List.of(),
//                                                                context -> CommandResult.successful(context, "Welcome"),
//                                                                null
//                                                        )
//                                                ),
//                                                CommandResult::genericError,
//                                                null
//                                        ),
//                                        new LiteralCommandNode(
//                                                "test1_3",
//                                                List.of(),
//                                                CommandResult::noPermission,
//                                                null
//                                        )
//                                ),
//                                context -> CommandResult.successful(context, "Hello"),
//                                "test1"
//                        )
//                )
//        );
//
//        Assertions.assertThrows(AssertionFailedError.class, () -> AssertionUtil.assertCommandsEqualsIgnoreExecutor(left, right));
//    }

    @Test
    void testContentEqualsTrueLists() {
        List<String> right = List.of(
                "hi",
                "welcome",
                "hello",
                "nice to see you"
        );
        List<String> left = List.of(
                "welcome",
                "hi",
                "nice to see you",
                "hello"
        );

        Assertions.assertDoesNotThrow(() -> AssertionUtil.assertContentEquals(right, left));
    }

    @Test
    void testContentEqualsTrueListAndSet() {
        List<String> right = List.of(
                "hi",
                "welcome",
                "hello",
                "nice to see you"
        );
        Set<String> left = Set.of(
                "welcome",
                "hi",
                "nice to see you",
                "hello"
        );

        Assertions.assertDoesNotThrow(() -> AssertionUtil.assertContentEquals(right, left));
    }

    @Test
    void testContentEqualsFalseExtra() {
        List<String> right = List.of(
                "hi",
                "welcome",
                "hello",
                "nice to see you",
                "nice to see you"
        );
        List<String> left = List.of(
                "welcome",
                "hi",
                "nice to see you",
                "hello"
        );

        Assertions.assertThrows(AssertionFailedError.class, () -> AssertionUtil.assertContentEquals(right, left));
    }

    @Test
    void testContentEqualsFalseMissing() {
        List<String> right = List.of(
                "hi",
                "welcome",
                "hello"
        );
        List<String> left = List.of(
                "welcome",
                "hi",
                "nice to see you",
                "hello"
        );

        Assertions.assertThrows(AssertionFailedError.class, () -> AssertionUtil.assertContentEquals(right, left));
    }

    @Test
    void testContentEqualsFalseDifferent0() {
        List<String> right = List.of(
                "test0",
                "test1"
        );
        List<String> left = List.of(
                "welcome",
                "hi",
                "nice to see you",
                "hello"
        );

        Assertions.assertThrows(AssertionFailedError.class, () -> AssertionUtil.assertContentEquals(right, left));
    }

    @Test
    void testContentEqualsFalseDifferent1() {
        List<String> right = List.of(
                "test0",
                "test1"
        );
        Set<String> left = Set.of(
                "welcome",
                "hi",
                "nice to see you",
                "hello"
        );

        Assertions.assertThrows(AssertionFailedError.class, () -> AssertionUtil.assertContentEquals(right, left));
    }
}
