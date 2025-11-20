package com.wizardlybump17.wlib.test.command.extractor.method;

import com.wizardlybump17.wlib.command.Command;
import com.wizardlybump17.wlib.command.extractor.CommandExtractor;
import com.wizardlybump17.wlib.command.node.LiteralCommandNode;
import com.wizardlybump17.wlib.test.util.AssertionUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

class MethodCommandExtractorStructureTests {

    @Test
    void test0() {
        Command test0 = new Command(
                new LiteralCommandNode(
                        "test0",
                        List.of(
                                new LiteralCommandNode(
                                        "123"
                                )
                        )
                )
        );
        Command test1 = new Command(
                new LiteralCommandNode(
                        "test1",
                        List.of(
                                new LiteralCommandNode(
                                        "123",
                                        List.of(
                                                new LiteralCommandNode(
                                                        "456"
                                                )
                                        )
                                )
                        )
                )
        );

        Test0 object = new Test0();

        List<Command> expected = new ArrayList<>(List.of(test0, test1));
        List<Command> actual = Assertions.assertDoesNotThrow(() -> CommandExtractor.METHOD.extract(object));

        expected.sort(null);
        actual.sort(null);

        AssertionUtil.assertCommandsEquals(expected, actual);
    }

    public static class Test0 {

        @com.wizardlybump17.wlib.command.annotation.Command("test0 123")
        public void test0() {
        }

        @com.wizardlybump17.wlib.command.annotation.Command("test1 123 456")
        public void test1() {
        }
    }
}
