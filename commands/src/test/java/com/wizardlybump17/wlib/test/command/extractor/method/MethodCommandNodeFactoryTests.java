package com.wizardlybump17.wlib.test.command.extractor.method;

import com.wizardlybump17.wlib.command.Command;
import com.wizardlybump17.wlib.command.extractor.method.MethodCommandExtractor;
import com.wizardlybump17.wlib.command.input.primitive.number.AllowedByteInputs;
import com.wizardlybump17.wlib.command.input.primitive.number.AllowedIntegerInputs;
import com.wizardlybump17.wlib.command.node.LiteralCommandNode;
import com.wizardlybump17.wlib.command.node.primitive.number.ByteCommandNode;
import com.wizardlybump17.wlib.command.node.primitive.number.IntegerCommandNode;
import com.wizardlybump17.wlib.command.registry.MethodCommandNodeFactoryRegistry;
import com.wizardlybump17.wlib.test.util.AssertionUtil;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

class MethodCommandNodeFactoryTests {

    static MethodCommandExtractor commandExtractor;

    @BeforeAll
    static void setup() {
        MethodCommandNodeFactoryRegistry registry = new MethodCommandNodeFactoryRegistry();
        registry.registerDefaults();
        commandExtractor = new MethodCommandExtractor(registry);
    }

    @AfterAll
    static void shutdown() {
        if (commandExtractor == null)
            return;

        MethodCommandNodeFactoryRegistry registry = commandExtractor.getFactoryRegistry();
        registry.clear();
        commandExtractor = null;
    }

    @Test
    void testByte() {
        Command test0 = new Command(
                new LiteralCommandNode(
                        "test",
                        List.of(
                                new ByteCommandNode(
                                        "byte0",
                                        AllowedByteInputs.unlimited()
                                )
                        )
                )
        );
        Command test1 = new Command(
                new LiteralCommandNode(
                        "test",
                        List.of(
                                new ByteCommandNode(
                                        "byte0",
                                        List.of(
                                                new LiteralCommandNode(
                                                        "test1",
                                                        List.of(
                                                                new ByteCommandNode(
                                                                        "byte1",
                                                                        AllowedByteInputs.unlimited()
                                                                )
                                                        )
                                                )
                                        ),
                                        AllowedByteInputs.unlimited(),
                                        null,
                                        null
                                )
                        )
                )
        );

        TestByte object = new TestByte();

        List<Command> expected = new ArrayList<>(List.of(test0, test1));
        List<Command> actual = Assertions.assertDoesNotThrow(() -> commandExtractor.extract(object));

        expected.sort(null);
        actual.sort(null);

        AssertionUtil.assertCommandsEquals(expected, actual);
    }

    public static final class TestByte {

        @com.wizardlybump17.wlib.command.annotation.Command("test <byte0>")
        public void test(byte int0) {
        }

        @com.wizardlybump17.wlib.command.annotation.Command("test <byte0> test1 <byte1>")
        public void test(byte int0, byte int1) {
        }
    }

    @Test
    void testInt() {
        Command test0 = new Command(
                new LiteralCommandNode(
                        "test",
                        List.of(
                                new IntegerCommandNode(
                                        "int0",
                                        AllowedIntegerInputs.unlimited()
                                )
                        )
                )
        );
        Command test1 = new Command(
                new LiteralCommandNode(
                        "test",
                        List.of(
                                new IntegerCommandNode(
                                        "int0",
                                        List.of(
                                                new LiteralCommandNode(
                                                        "test1",
                                                        List.of(
                                                                new IntegerCommandNode(
                                                                        "int1",
                                                                        AllowedIntegerInputs.unlimited()
                                                                )
                                                        )
                                                )
                                        ),
                                        AllowedIntegerInputs.unlimited(),
                                        null,
                                        null
                                )
                        )
                )
        );

        TestInt object = new TestInt();

        List<Command> expected = new ArrayList<>(List.of(test0, test1));
        List<Command> actual = Assertions.assertDoesNotThrow(() -> commandExtractor.extract(object));

        expected.sort(null);
        actual.sort(null);

        AssertionUtil.assertCommandsEquals(expected, actual);
    }

    public static final class TestInt {

        @com.wizardlybump17.wlib.command.annotation.Command("test <int0>")
        public void test(int int0) {
        }

        @com.wizardlybump17.wlib.command.annotation.Command("test <int0> test1 <int1>")
        public void test(int int0, int int1) {
        }
    }
}
