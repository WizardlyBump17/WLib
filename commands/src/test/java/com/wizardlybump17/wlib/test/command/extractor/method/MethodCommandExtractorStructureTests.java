package com.wizardlybump17.wlib.test.command.extractor.method;

import com.wizardlybump17.wlib.command.Command;
import com.wizardlybump17.wlib.command.extractor.CommandExtractor;
import com.wizardlybump17.wlib.command.extractor.method.MethodCommandExtractor;
import com.wizardlybump17.wlib.command.input.primitive.number.AllowedIntegerInputs;
import com.wizardlybump17.wlib.command.node.LiteralCommandNode;
import com.wizardlybump17.wlib.command.node.primitive.number.IntegerCommandNode;
import com.wizardlybump17.wlib.command.registry.MethodCommandNodeFactoryRegistry;
import com.wizardlybump17.wlib.test.util.AssertionUtil;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

class MethodCommandExtractorStructureTests {

    static MethodCommandExtractor methodCommandExtractor;

    @BeforeAll
    static void setup() {
        MethodCommandNodeFactoryRegistry registry = new MethodCommandNodeFactoryRegistry();
        registry.registerDefaults();
        methodCommandExtractor = CommandExtractor.method(registry);
    }

    @AfterAll
    static void clear() {
        if (methodCommandExtractor == null)
            return;

        MethodCommandNodeFactoryRegistry registry = methodCommandExtractor.getFactoryRegistry();
        registry.clear();
        methodCommandExtractor = null;
    }

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
        List<Command> actual = Assertions.assertDoesNotThrow(() -> methodCommandExtractor.extract(object));

        expected.sort(null);
        actual.sort(null);

        AssertionUtil.assertCommandsEqualsIgnoreExecutor(expected, actual);
    }

    public static class Test0 {

        @com.wizardlybump17.wlib.command.annotation.Command("test0 123")
        public void test0() {
        }

        @com.wizardlybump17.wlib.command.annotation.Command("test1 123 456")
        public void test1() {
        }
    }

    @Test
    void test1() {
        Command test0 = new Command(
                new LiteralCommandNode(
                        "test0",
                        List.of(
                                new IntegerCommandNode(
                                        "int",
                                        List.of(
                                                new LiteralCommandNode(
                                                        "123"
                                                )
                                        ),
                                        AllowedIntegerInputs.unlimited()
                                )
                        )
                )
        );
        Command test1 = new Command(
                new LiteralCommandNode(
                        "test1",
                        List.of(
                                new IntegerCommandNode(
                                        "int0",
                                        List.of(
                                                new IntegerCommandNode(
                                                        "int1",
                                                        List.of(
                                                                new LiteralCommandNode(
                                                                        "123",
                                                                        List.of(
                                                                                new LiteralCommandNode(
                                                                                        "abc"
                                                                                )
                                                                        )
                                                                )
                                                        ),
                                                        AllowedIntegerInputs.unlimited()
                                                )
                                        ),
                                        AllowedIntegerInputs.unlimited()
                                )
                        )
                )
        );

        Test1 object = new Test1();

        List<Command> expected = new ArrayList<>(List.of(test0, test1));
        List<Command> actual = Assertions.assertDoesNotThrow(() -> methodCommandExtractor.extract(object));

        expected.sort(null);
        actual.sort(null);

        AssertionUtil.assertCommandsEqualsIgnoreExecutor(expected, actual);
    }

    public static class Test1 {

        @com.wizardlybump17.wlib.command.annotation.Command("test0 <int> 123")
        public void test0(int arg0) {
        }

        @com.wizardlybump17.wlib.command.annotation.Command("test1 <int0> <int1> 123 abc")
        public void test1(int arg0, int arg1) {
        }
    }
}
