package com.wizardlybump17.wlib.test.command.extractor.method;

import com.wizardlybump17.wlib.command.Command;
import com.wizardlybump17.wlib.command.extractor.CommandExtractor;
import com.wizardlybump17.wlib.command.extractor.method.MethodCommandExtractor;
import com.wizardlybump17.wlib.command.node.LiteralCommandNode;
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
                                MethodCommandExtractor.createExecutor(object, "hello", CommandSender.class)
                        )
                ),
                new Command(
                        new LiteralCommandNode(
                                "hello",
                                List.of(
                                        new LiteralCommandNode(
                                                "world",
                                                MethodCommandExtractor.createExecutor(object, "helloWorld", CommandSender.class)
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
                                                                MethodCommandExtractor.createExecutor(object, "helloThereHi", CommandSender.class)
                                                        )
                                                )
                                        )
                                )
                        )
                )
        ));
        List<Command> actual = CommandExtractor.METHOD.extract(object);

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
}
