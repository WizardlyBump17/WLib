package com.wizardlybump17.wlib.test.command.extractor.method;

import com.wizardlybump17.wlib.command.Command;
import com.wizardlybump17.wlib.command.extractor.method.MethodCommandExtractor;
import com.wizardlybump17.wlib.command.input.object.AllowedUUIDInputs;
import com.wizardlybump17.wlib.command.input.primitive.AllowedCharacterInputs;
import com.wizardlybump17.wlib.command.input.primitive.number.*;
import com.wizardlybump17.wlib.command.input.string.AllowedStringInputs;
import com.wizardlybump17.wlib.command.node.LiteralCommandNode;
import com.wizardlybump17.wlib.command.node.object.UUIDCommandNode;
import com.wizardlybump17.wlib.command.node.primitive.CharacterCommandNode;
import com.wizardlybump17.wlib.command.node.primitive.number.*;
import com.wizardlybump17.wlib.command.node.string.StringCommandNode;
import com.wizardlybump17.wlib.command.registry.MethodCommandNodeFactoryRegistry;
import com.wizardlybump17.wlib.test.util.AssertionUtil;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

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
                                        "arg0-arg",
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
                                        "arg0-arg",
                                        List.of(
                                                new LiteralCommandNode(
                                                        "test1",
                                                        List.of(
                                                                new ByteCommandNode(
                                                                        "arg1-arg",
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

        AssertionUtil.assertCommandsEqualsIgnoreExecutor(expected, actual);
    }

    public static final class TestByte {

        @com.wizardlybump17.wlib.command.annotation.Command("test <arg0>")
        public void test(byte arg0) {
        }

        @com.wizardlybump17.wlib.command.annotation.Command("test <arg0> test1 <arg1>")
        public void test(byte arg0, byte arg1) {
        }
    }

    @Test
    void testShort() {
        Command test0 = new Command(
                new LiteralCommandNode(
                        "test",
                        List.of(
                                new ShortCommandNode(
                                        "arg0-arg",
                                        AllowedShortInputs.unlimited()
                                )
                        )
                )
        );
        Command test1 = new Command(
                new LiteralCommandNode(
                        "test",
                        List.of(
                                new ShortCommandNode(
                                        "arg0-arg",
                                        List.of(
                                                new LiteralCommandNode(
                                                        "test1",
                                                        List.of(
                                                                new ShortCommandNode(
                                                                        "arg1-arg",
                                                                        AllowedShortInputs.unlimited()
                                                                )
                                                        )
                                                )
                                        ),
                                        AllowedShortInputs.unlimited(),
                                        null,
                                        null
                                )
                        )
                )
        );

        TestShort object = new TestShort();

        List<Command> expected = new ArrayList<>(List.of(test0, test1));
        List<Command> actual = Assertions.assertDoesNotThrow(() -> commandExtractor.extract(object));

        expected.sort(null);
        actual.sort(null);

        AssertionUtil.assertCommandsEqualsIgnoreExecutor(expected, actual);
    }

    public static final class TestShort {

        @com.wizardlybump17.wlib.command.annotation.Command("test <arg0>")
        public void test(short arg0) {
        }

        @com.wizardlybump17.wlib.command.annotation.Command("test <arg0> test1 <arg1>")
        public void test(short arg0, short arg1) {
        }
    }

    @Test
    void testInt() {
        Command test0 = new Command(
                new LiteralCommandNode(
                        "test",
                        List.of(
                                new IntegerCommandNode(
                                        "arg0-arg",
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
                                        "arg0-arg",
                                        List.of(
                                                new LiteralCommandNode(
                                                        "test1",
                                                        List.of(
                                                                new IntegerCommandNode(
                                                                        "arg1-arg",
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

        AssertionUtil.assertCommandsEqualsIgnoreExecutor(expected, actual);
    }

    public static final class TestInt {

        @com.wizardlybump17.wlib.command.annotation.Command("test <arg0>")
        public void test(int arg0) {
        }

        @com.wizardlybump17.wlib.command.annotation.Command("test <arg0> test1 <arg1>")
        public void test(int arg0, int arg1) {
        }
    }

    @Test
    void testLong() {
        Command test0 = new Command(
                new LiteralCommandNode(
                        "test",
                        List.of(
                                new LongCommandNode(
                                        "arg0-arg",
                                        AllowedLongInputs.unlimited()
                                )
                        )
                )
        );
        Command test1 = new Command(
                new LiteralCommandNode(
                        "test",
                        List.of(
                                new LongCommandNode(
                                        "arg0-arg",
                                        List.of(
                                                new LiteralCommandNode(
                                                        "test1",
                                                        List.of(
                                                                new LongCommandNode(
                                                                        "arg1-arg",
                                                                        AllowedLongInputs.unlimited()
                                                                )
                                                        )
                                                )
                                        ),
                                        AllowedLongInputs.unlimited(),
                                        null,
                                        null
                                )
                        )
                )
        );

        TestLong object = new TestLong();

        List<Command> expected = new ArrayList<>(List.of(test0, test1));
        List<Command> actual = Assertions.assertDoesNotThrow(() -> commandExtractor.extract(object));

        expected.sort(null);
        actual.sort(null);

        AssertionUtil.assertCommandsEqualsIgnoreExecutor(expected, actual);
    }

    public static final class TestLong {

        @com.wizardlybump17.wlib.command.annotation.Command("test <arg0>")
        public void test(long arg0) {
        }

        @com.wizardlybump17.wlib.command.annotation.Command("test <arg0> test1 <arg1>")
        public void test(long arg0, long arg1) {
        }
    }

    @Test
    void testFloat() {
        Command test0 = new Command(
                new LiteralCommandNode(
                        "test",
                        List.of(
                                new FloatCommandNode(
                                        "arg0-arg",
                                        AllowedFloatInputs.unlimited()
                                )
                        )
                )
        );
        Command test1 = new Command(
                new LiteralCommandNode(
                        "test",
                        List.of(
                                new FloatCommandNode(
                                        "arg0-arg",
                                        List.of(
                                                new LiteralCommandNode(
                                                        "test1",
                                                        List.of(
                                                                new FloatCommandNode(
                                                                        "arg1-arg",
                                                                        AllowedFloatInputs.unlimited()
                                                                )
                                                        )
                                                )
                                        ),
                                        AllowedFloatInputs.unlimited(),
                                        null,
                                        null
                                )
                        )
                )
        );

        TestFloat object = new TestFloat();

        List<Command> expected = new ArrayList<>(List.of(test0, test1));
        List<Command> actual = Assertions.assertDoesNotThrow(() -> commandExtractor.extract(object));

        expected.sort(null);
        actual.sort(null);

        AssertionUtil.assertCommandsEqualsIgnoreExecutor(expected, actual);
    }

    public static final class TestFloat {

        @com.wizardlybump17.wlib.command.annotation.Command("test <arg0>")
        public void test(float arg0) {
        }

        @com.wizardlybump17.wlib.command.annotation.Command("test <arg0> test1 <arg1>")
        public void test(float arg0, float arg1) {
        }
    }

    @Test
    void testDouble() {
        Command test0 = new Command(
                new LiteralCommandNode(
                        "test",
                        List.of(
                                new DoubleCommandNode(
                                        "arg0-arg",
                                        AllowedDoubleInputs.unlimited()
                                )
                        )
                )
        );
        Command test1 = new Command(
                new LiteralCommandNode(
                        "test",
                        List.of(
                                new DoubleCommandNode(
                                        "arg0-arg",
                                        List.of(
                                                new LiteralCommandNode(
                                                        "test1",
                                                        List.of(
                                                                new DoubleCommandNode(
                                                                        "arg1-arg",
                                                                        AllowedDoubleInputs.unlimited()
                                                                )
                                                        )
                                                )
                                        ),
                                        AllowedDoubleInputs.unlimited(),
                                        null,
                                        null
                                )
                        )
                )
        );

        TestDouble object = new TestDouble();

        List<Command> expected = new ArrayList<>(List.of(test0, test1));
        List<Command> actual = Assertions.assertDoesNotThrow(() -> commandExtractor.extract(object));

        expected.sort(null);
        actual.sort(null);

        AssertionUtil.assertCommandsEqualsIgnoreExecutor(expected, actual);
    }

    public static final class TestDouble {

        @com.wizardlybump17.wlib.command.annotation.Command("test <arg0>")
        public void test(double arg0) {
        }

        @com.wizardlybump17.wlib.command.annotation.Command("test <arg0> test1 <arg1>")
        public void test(double arg0, double arg1) {
        }
    }

    @Test
    void testChar() {
        Command test0 = new Command(
                new LiteralCommandNode(
                        "test",
                        List.of(
                                new CharacterCommandNode(
                                        "arg0-arg",
                                        AllowedCharacterInputs.anyNotNull()
                                )
                        )
                )
        );
        Command test1 = new Command(
                new LiteralCommandNode(
                        "test",
                        List.of(
                                new CharacterCommandNode(
                                        "arg0-arg",
                                        List.of(
                                                new LiteralCommandNode(
                                                        "test1",
                                                        List.of(
                                                                new CharacterCommandNode(
                                                                        "arg1-arg",
                                                                        AllowedCharacterInputs.anyNotNull()
                                                                )
                                                        )
                                                )
                                        ),
                                        AllowedCharacterInputs.anyNotNull(),
                                        null,
                                        null
                                )
                        )
                )
        );

        TestChar object = new TestChar();

        List<Command> expected = new ArrayList<>(List.of(test0, test1));
        List<Command> actual = Assertions.assertDoesNotThrow(() -> commandExtractor.extract(object));

        expected.sort(null);
        actual.sort(null);

        AssertionUtil.assertCommandsEqualsIgnoreExecutor(expected, actual);
    }

    public static final class TestChar {

        @com.wizardlybump17.wlib.command.annotation.Command("test <arg0>")
        public void test(char arg0) {
        }

        @com.wizardlybump17.wlib.command.annotation.Command("test <arg0> test1 <arg1>")
        public void test(char arg0, char arg1) {
        }
    }

    @Test
    void testString() {
        Command test0 = new Command(
                new LiteralCommandNode(
                        "test",
                        List.of(
                                new StringCommandNode(
                                        "arg0-arg",
                                        AllowedStringInputs.anyNullable()
                                )
                        )
                )
        );
        Command test1 = new Command(
                new LiteralCommandNode(
                        "test",
                        List.of(
                                new StringCommandNode(
                                        "arg0-arg",
                                        List.of(
                                                new LiteralCommandNode(
                                                        "test1",
                                                        List.of(
                                                                new StringCommandNode(
                                                                        "arg1-arg",
                                                                        AllowedStringInputs.anyNullable()
                                                                )
                                                        )
                                                )
                                        ),
                                        AllowedStringInputs.anyNullable(),
                                        null,
                                        null
                                )
                        )
                )
        );

        TestString object = new TestString();

        List<Command> expected = new ArrayList<>(List.of(test0, test1));
        List<Command> actual = Assertions.assertDoesNotThrow(() -> commandExtractor.extract(object));

        expected.sort(null);
        actual.sort(null);

        AssertionUtil.assertCommandsEqualsIgnoreExecutor(expected, actual);
    }

    public static final class TestString {

        @com.wizardlybump17.wlib.command.annotation.Command("test <arg0>")
        public void test(String arg0) {
        }

        @com.wizardlybump17.wlib.command.annotation.Command("test <arg0> test1 <arg1>")
        public void test(String arg0, String arg1) {
        }
    }

    @Test
    void testUUID() {
        Command test0 = new Command(
                new LiteralCommandNode(
                        "test",
                        List.of(
                                new UUIDCommandNode(
                                        "arg0-arg",
                                        AllowedUUIDInputs.anyNullable()
                                )
                        )
                )
        );
        Command test1 = new Command(
                new LiteralCommandNode(
                        "test",
                        List.of(
                                new UUIDCommandNode(
                                        "arg0-arg",
                                        List.of(
                                                new LiteralCommandNode(
                                                        "test1",
                                                        List.of(
                                                                new UUIDCommandNode(
                                                                        "arg1-arg",
                                                                        AllowedUUIDInputs.anyNullable()
                                                                )
                                                        )
                                                )
                                        ),
                                        AllowedUUIDInputs.anyNullable(),
                                        null,
                                        null
                                )
                        )
                )
        );

        TestUUID object = new TestUUID();

        List<Command> expected = new ArrayList<>(List.of(test0, test1));
        List<Command> actual = Assertions.assertDoesNotThrow(() -> commandExtractor.extract(object));

        expected.sort(null);
        actual.sort(null);

        AssertionUtil.assertCommandsEqualsIgnoreExecutor(expected, actual);
    }

    public static final class TestUUID {

        @com.wizardlybump17.wlib.command.annotation.Command("test <arg0>")
        public void test(UUID arg0) {
        }

        @com.wizardlybump17.wlib.command.annotation.Command("test <arg0> test1 <arg1>")
        public void test(UUID arg0, UUID arg1) {
        }
    }

    @Test
    void testMix() {
        Command test0 = new Command(new LiteralCommandNode(
                "test",
                List.of(
                        new IntegerCommandNode(
                                "int0-arg",
                                List.of(
                                        new IntegerCommandNode(
                                                "int1-arg",
                                                List.of(
                                                        new LiteralCommandNode(
                                                                "test1",
                                                                List.of(
                                                                        new ByteCommandNode(
                                                                                "byte0-arg",
                                                                                List.of(
                                                                                        new ByteCommandNode(
                                                                                                "byte1-arg",
                                                                                                List.of(
                                                                                                        new LiteralCommandNode(
                                                                                                                "test2",
                                                                                                                List.of(
                                                                                                                        new FloatCommandNode(
                                                                                                                                "float0-arg",
                                                                                                                                List.of(
                                                                                                                                        new LiteralCommandNode(
                                                                                                                                                "test3",
                                                                                                                                                List.of(
                                                                                                                                                        new FloatCommandNode(
                                                                                                                                                                "float1-arg",
                                                                                                                                                                List.of(
                                                                                                                                                                        new LongCommandNode(
                                                                                                                                                                                "long0-arg",
                                                                                                                                                                                List.of(
                                                                                                                                                                                        new LongCommandNode(
                                                                                                                                                                                                "long1-arg",
                                                                                                                                                                                                List.of(
                                                                                                                                                                                                        new LiteralCommandNode(
                                                                                                                                                                                                                "test4",
                                                                                                                                                                                                                List.of(
                                                                                                                                                                                                                        new ShortCommandNode(
                                                                                                                                                                                                                                "short0-arg",
                                                                                                                                                                                                                                List.of(
                                                                                                                                                                                                                                        new ShortCommandNode(
                                                                                                                                                                                                                                                "short1-arg",
                                                                                                                                                                                                                                                List.of(
                                                                                                                                                                                                                                                        new LiteralCommandNode(
                                                                                                                                                                                                                                                                "test5",
                                                                                                                                                                                                                                                                List.of(
                                                                                                                                                                                                                                                                        new DoubleCommandNode(
                                                                                                                                                                                                                                                                                "double0-arg",
                                                                                                                                                                                                                                                                                List.of(
                                                                                                                                                                                                                                                                                        new DoubleCommandNode(
                                                                                                                                                                                                                                                                                                "double1-arg",
                                                                                                                                                                                                                                                                                                List.of(
                                                                                                                                                                                                                                                                                                        new LiteralCommandNode(
                                                                                                                                                                                                                                                                                                                "test6",
                                                                                                                                                                                                                                                                                                                List.of(
                                                                                                                                                                                                                                                                                                                        new CharacterCommandNode(
                                                                                                                                                                                                                                                                                                                                "char0-arg",
                                                                                                                                                                                                                                                                                                                                List.of(
                                                                                                                                                                                                                                                                                                                                        new CharacterCommandNode(
                                                                                                                                                                                                                                                                                                                                                "char1-arg",
                                                                                                                                                                                                                                                                                                                                                List.of(
                                                                                                                                                                                                                                                                                                                                                        new LiteralCommandNode(
                                                                                                                                                                                                                                                                                                                                                                "test7",
                                                                                                                                                                                                                                                                                                                                                                List.of(
                                                                                                                                                                                                                                                                                                                                                                        new StringCommandNode(
                                                                                                                                                                                                                                                                                                                                                                                "string0-arg",
                                                                                                                                                                                                                                                                                                                                                                                List.of(
                                                                                                                                                                                                                                                                                                                                                                                        new LiteralCommandNode(
                                                                                                                                                                                                                                                                                                                                                                                                "test8",
                                                                                                                                                                                                                                                                                                                                                                                                List.of(
                                                                                                                                                                                                                                                                                                                                                                                                        new StringCommandNode(
                                                                                                                                                                                                                                                                                                                                                                                                                "string1-arg",
                                                                                                                                                                                                                                                                                                                                                                                                                List.of(
                                                                                                                                                                                                                                                                                                                                                                                                                        new UUIDCommandNode(
                                                                                                                                                                                                                                                                                                                                                                                                                                "uuid0-arg",
                                                                                                                                                                                                                                                                                                                                                                                                                                List.of(
                                                                                                                                                                                                                                                                                                                                                                                                                                        new LiteralCommandNode(
                                                                                                                                                                                                                                                                                                                                                                                                                                                "test9",
                                                                                                                                                                                                                                                                                                                                                                                                                                                List.of(
                                                                                                                                                                                                                                                                                                                                                                                                                                                        new UUIDCommandNode(
                                                                                                                                                                                                                                                                                                                                                                                                                                                                "uuid1-arg",
                                                                                                                                                                                                                                                                                                                                                                                                                                                                AllowedUUIDInputs.anyNullable()
                                                                                                                                                                                                                                                                                                                                                                                                                                                        )
                                                                                                                                                                                                                                                                                                                                                                                                                                                )
                                                                                                                                                                                                                                                                                                                                                                                                                                        )
                                                                                                                                                                                                                                                                                                                                                                                                                                ),
                                                                                                                                                                                                                                                                                                                                                                                                                                AllowedUUIDInputs.anyNullable(),
                                                                                                                                                                                                                                                                                                                                                                                                                                null,
                                                                                                                                                                                                                                                                                                                                                                                                                                null
                                                                                                                                                                                                                                                                                                                                                                                                                        )
                                                                                                                                                                                                                                                                                                                                                                                                                ),
                                                                                                                                                                                                                                                                                                                                                                                                                AllowedStringInputs.anyNullable(),
                                                                                                                                                                                                                                                                                                                                                                                                                null,
                                                                                                                                                                                                                                                                                                                                                                                                                null
                                                                                                                                                                                                                                                                                                                                                                                                        )
                                                                                                                                                                                                                                                                                                                                                                                                )
                                                                                                                                                                                                                                                                                                                                                                                        )
                                                                                                                                                                                                                                                                                                                                                                                ),
                                                                                                                                                                                                                                                                                                                                                                                AllowedStringInputs.anyNullable(),
                                                                                                                                                                                                                                                                                                                                                                                null,
                                                                                                                                                                                                                                                                                                                                                                                null
                                                                                                                                                                                                                                                                                                                                                                        )
                                                                                                                                                                                                                                                                                                                                                                )
                                                                                                                                                                                                                                                                                                                                                        )
                                                                                                                                                                                                                                                                                                                                                ),
                                                                                                                                                                                                                                                                                                                                                AllowedCharacterInputs.anyNotNull(),
                                                                                                                                                                                                                                                                                                                                                null,
                                                                                                                                                                                                                                                                                                                                                null
                                                                                                                                                                                                                                                                                                                                        )
                                                                                                                                                                                                                                                                                                                                ),
                                                                                                                                                                                                                                                                                                                                AllowedCharacterInputs.anyNotNull(),
                                                                                                                                                                                                                                                                                                                                null,
                                                                                                                                                                                                                                                                                                                                null
                                                                                                                                                                                                                                                                                                                        )
                                                                                                                                                                                                                                                                                                                )
                                                                                                                                                                                                                                                                                                        )
                                                                                                                                                                                                                                                                                                ),
                                                                                                                                                                                                                                                                                                AllowedDoubleInputs.unlimited(),
                                                                                                                                                                                                                                                                                                null,
                                                                                                                                                                                                                                                                                                null
                                                                                                                                                                                                                                                                                        )
                                                                                                                                                                                                                                                                                ),
                                                                                                                                                                                                                                                                                AllowedDoubleInputs.unlimited(),
                                                                                                                                                                                                                                                                                null,
                                                                                                                                                                                                                                                                                null
                                                                                                                                                                                                                                                                        )
                                                                                                                                                                                                                                                                )
                                                                                                                                                                                                                                                        )
                                                                                                                                                                                                                                                ),
                                                                                                                                                                                                                                                AllowedShortInputs.unlimited(),
                                                                                                                                                                                                                                                null,
                                                                                                                                                                                                                                                null
                                                                                                                                                                                                                                        )
                                                                                                                                                                                                                                ),
                                                                                                                                                                                                                                AllowedShortInputs.unlimited(),
                                                                                                                                                                                                                                null,
                                                                                                                                                                                                                                null
                                                                                                                                                                                                                        )
                                                                                                                                                                                                                )
                                                                                                                                                                                                        )
                                                                                                                                                                                                ),
                                                                                                                                                                                                AllowedLongInputs.unlimited(),
                                                                                                                                                                                                null,
                                                                                                                                                                                                null
                                                                                                                                                                                        )
                                                                                                                                                                                ),
                                                                                                                                                                                AllowedLongInputs.unlimited(),
                                                                                                                                                                                null,
                                                                                                                                                                                null
                                                                                                                                                                        )
                                                                                                                                                                ),
                                                                                                                                                                AllowedFloatInputs.unlimited(),
                                                                                                                                                                null,
                                                                                                                                                                null
                                                                                                                                                        )
                                                                                                                                                )
                                                                                                                                        )
                                                                                                                                ),
                                                                                                                                AllowedFloatInputs.unlimited(),
                                                                                                                                null,
                                                                                                                                null
                                                                                                                        )
                                                                                                                )
                                                                                                        )
                                                                                                ),
                                                                                                AllowedByteInputs.unlimited(),
                                                                                                null,
                                                                                                null
                                                                                        )
                                                                                ),
                                                                                AllowedByteInputs.unlimited(),
                                                                                null,
                                                                                null
                                                                        )
                                                                )
                                                        )
                                                ),
                                                AllowedIntegerInputs.unlimited(),
                                                null,
                                                null
                                        )
                                ),
                                AllowedIntegerInputs.unlimited(),
                                null,
                                null
                        )
                )
        ));

        TestMix object = new TestMix();

        List<Command> expected = new ArrayList<>(List.of(test0));
        List<Command> actual = Assertions.assertDoesNotThrow(() -> commandExtractor.extract(object));

        expected.sort(null);
        actual.sort(null);

        AssertionUtil.assertCommandsEqualsIgnoreExecutor(expected, actual);
    }

    public static final class TestMix {

        @com.wizardlybump17.wlib.command.annotation.Command("test <int0> <int1> test1 <byte0> <byte1> test2 <float0> test3 <float1> <long0> <long1> test4 <short0> <short1> test5 <double0> <double1> test6 <char0> <char1> test7 <string0> test8 <string1> <uuid0> test9 <uuid1>")
        public void test(int int0, int int1, byte byte0, byte byte1, float float0, float float1, long long0, long log1, short short0, short short1, double double0, double double1, char char0, char char1, String string0, String string1, UUID uuid0, UUID uuid1) {
        }
    }
}
