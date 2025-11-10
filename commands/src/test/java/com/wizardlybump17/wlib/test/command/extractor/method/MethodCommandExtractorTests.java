package com.wizardlybump17.wlib.test.command.extractor.method;

import com.wizardlybump17.wlib.command.Command;
import com.wizardlybump17.wlib.command.context.CommandContext;
import com.wizardlybump17.wlib.command.extractor.CommandExtractor;
import com.wizardlybump17.wlib.command.extractor.method.MethodCommandExtractor;
import com.wizardlybump17.wlib.command.node.LiteralCommandNode;
import com.wizardlybump17.wlib.command.result.CommandResult;
import com.wizardlybump17.wlib.command.sender.CommandSender;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

class MethodCommandExtractorTests {

    @Test
    void test0() {
        Test0 object = new Test0();

        List<Command> expected = new ArrayList<>(List.of(
                new Command(
                        new LiteralCommandNode(
                                "hello",
                                Assertions.assertDoesNotThrow(() -> MethodCommandExtractor.createExecutor(object, "hello", CommandSender.class))
                        )
                ),
                new Command(
                        new LiteralCommandNode(
                                "hello",
                                List.of(
                                        new LiteralCommandNode(
                                                "world",
                                                Assertions.assertDoesNotThrow(() -> MethodCommandExtractor.createExecutor(object, "helloWorld", CommandSender.class))
                                        )
                                )
                        )
                ),
                new Command(
                        new LiteralCommandNode(
                                "hello",
                                List.of(
                                        new LiteralCommandNode(
                                                "there",
                                                List.of(
                                                        new LiteralCommandNode(
                                                                "hi",
                                                                Assertions.assertDoesNotThrow(() -> MethodCommandExtractor.createExecutor(object, "helloThereHi", CommandSender.class))
                                                        )
                                                )
                                        )
                                )
                        )
                )
        ));
        List<Command> actual = Assertions.assertDoesNotThrow(() -> CommandExtractor.METHOD.extract(object));

        expected.sort(null);
        actual.sort(null);

        Assertions.assertEquals(expected, actual);
    }

    public static class Test0 {

        @com.wizardlybump17.wlib.command.annotation.Command("hello")
        public void hello(@NotNull CommandSender<?> sender) {
        }

        @com.wizardlybump17.wlib.command.annotation.Command("hello world")
        public void helloWorld(@NotNull CommandSender<?> sender) {
        }

        @com.wizardlybump17.wlib.command.annotation.Command("hello there hi")
        public void helloThereHi(@NotNull CommandSender<?> sender) {
        }
    }

    @Test
    void test1() {
        Test1 object = new Test1();

        List<Command> expected = new ArrayList<>(List.of(
                new Command(
                        new LiteralCommandNode(
                                "hello",
                                Assertions.assertDoesNotThrow(() -> MethodCommandExtractor.createExecutor(object, "hello", CommandContext.class))
                        )
                ),
                new Command(
                        new LiteralCommandNode(
                                "hello",
                                List.of(
                                        new LiteralCommandNode(
                                                "world",
                                                Assertions.assertDoesNotThrow(() -> MethodCommandExtractor.createExecutor(object, "helloWorld", CommandContext.class))
                                        )
                                )
                        )
                ),
                new Command(
                        new LiteralCommandNode(
                                "hello",
                                List.of(
                                        new LiteralCommandNode(
                                                "there",
                                                List.of(
                                                        new LiteralCommandNode(
                                                                "hi",
                                                                Assertions.assertDoesNotThrow(() -> MethodCommandExtractor.createExecutor(object, "helloThereHi", CommandContext.class))
                                                        )
                                                )
                                        )
                                )
                        )
                )
        ));
        List<Command> actual = Assertions.assertDoesNotThrow(() -> CommandExtractor.METHOD.extract(object));

        expected.sort(null);
        actual.sort(null);

        Assertions.assertEquals(expected, actual);
    }

    public static class Test1 {

        @com.wizardlybump17.wlib.command.annotation.Command("hello")
        public void hello(@NotNull CommandContext context) {
        }

        @com.wizardlybump17.wlib.command.annotation.Command("hello world")
        public void helloWorld(@NotNull CommandContext context) {
        }

        @com.wizardlybump17.wlib.command.annotation.Command("hello there hi")
        public void helloThereHi(@NotNull CommandContext context) {
        }
    }

    @Test
    void test2() {
        Test2 object = new Test2();

        List<Command> expected = new ArrayList<>(List.of(
                new Command(
                        new LiteralCommandNode(
                                "hello",
                                Assertions.assertDoesNotThrow(() -> MethodCommandExtractor.createExecutor(object, "hello", CommandContext.class))
                        )
                ),
                new Command(
                        new LiteralCommandNode(
                                "hello",
                                List.of(
                                        new LiteralCommandNode(
                                                "world",
                                                Assertions.assertDoesNotThrow(() -> MethodCommandExtractor.createExecutor(object, "helloWorld", CommandContext.class))
                                        )
                                )
                        )
                ),
                new Command(
                        new LiteralCommandNode(
                                "hello",
                                List.of(
                                        new LiteralCommandNode(
                                                "there",
                                                List.of(
                                                        new LiteralCommandNode(
                                                                "hi",
                                                                Assertions.assertDoesNotThrow(() -> MethodCommandExtractor.createExecutor(object, "helloThereHi", CommandContext.class))
                                                        )
                                                )
                                        )
                                )
                        )
                )
        ));
        List<Command> actual = Assertions.assertDoesNotThrow(() -> CommandExtractor.METHOD.extract(object));

        expected.sort(null);
        actual.sort(null);

        Assertions.assertEquals(expected, actual);
    }

    public static class Test2 {

        @com.wizardlybump17.wlib.command.annotation.Command("hello")
        public @NotNull CommandResult<?> hello(@NotNull CommandContext context) {
            return CommandResult.successful(context, null);
        }

        @com.wizardlybump17.wlib.command.annotation.Command("hello world")
        public @NotNull CommandResult<?> helloWorld(@NotNull CommandContext context) {
            return CommandResult.successful(context, null);
        }

        @com.wizardlybump17.wlib.command.annotation.Command("hello there hi")
        public @NotNull CommandResult<?> helloThereHi(@NotNull CommandContext context) {
            return CommandResult.successful(context, null);
        }
    }

    @Test
    void test3() {
        Test3 object = new Test3();

        List<Command> expected = new ArrayList<>(List.of(
                new Command(
                        new LiteralCommandNode(
                                "hello",
                                Assertions.assertDoesNotThrow(() -> MethodCommandExtractor.createExecutor(object, "hello", CommandSender.class))
                        )
                ),
                new Command(
                        new LiteralCommandNode(
                                "hello",
                                List.of(
                                        new LiteralCommandNode(
                                                "world",
                                                Assertions.assertDoesNotThrow(() -> MethodCommandExtractor.createExecutor(object, "helloWorld", CommandSender.class))
                                        )
                                )
                        )
                ),
                new Command(
                        new LiteralCommandNode(
                                "hello",
                                List.of(
                                        new LiteralCommandNode(
                                                "there",
                                                List.of(
                                                        new LiteralCommandNode(
                                                                "hi",
                                                                Assertions.assertDoesNotThrow(() -> MethodCommandExtractor.createExecutor(object, "helloThereHi", CommandSender.class))
                                                        )
                                                )
                                        )
                                )
                        )
                )
        ));
        List<Command> actual = Assertions.assertDoesNotThrow(() -> CommandExtractor.METHOD.extract(object));

        expected.sort(null);
        actual.sort(null);

        Assertions.assertEquals(expected, actual);
    }

    public static class Test3 {

        @com.wizardlybump17.wlib.command.annotation.Command("hello")
        public @NotNull CommandResult<?> hello(@NotNull CommandSender<?> sender) {
            return null;
        }

        @com.wizardlybump17.wlib.command.annotation.Command("hello world")
        public @NotNull CommandResult<?> helloWorld(@NotNull CommandSender<?> sender) {
            return null;
        }

        @com.wizardlybump17.wlib.command.annotation.Command("hello there hi")
        public @NotNull CommandResult<?> helloThereHi(@NotNull CommandSender<?> sender) {
            return null;
        }
    }
}
