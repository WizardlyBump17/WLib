package com.wizardlybump17.wlib.test.command.extractor.method;

import com.wizardlybump17.wlib.command.context.CommandContext;
import com.wizardlybump17.wlib.command.executor.CommandNodeExecutor;
import com.wizardlybump17.wlib.command.extractor.method.AbstractMethodCommandNodeExecutor;
import com.wizardlybump17.wlib.command.extractor.method.MethodCommandExtractor;
import com.wizardlybump17.wlib.command.result.CommandResult;
import com.wizardlybump17.wlib.command.sender.CommandSender;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;

class MethodCommandExtractorRightExecutorTests {

    /*
    No parameters
     */

    @Test
    void testNoArgumentsCommandResult() {
        TestNoArgumentsCommandResultExecutor object = new TestNoArgumentsCommandResultExecutor();

        Method method = Assertions.assertDoesNotThrow(() -> object.getClass().getMethod("hello"));

        CommandNodeExecutor<?> expected = new AbstractMethodCommandNodeExecutor.NoArgumentsCommandResultExecutor<>(object, method);
        CommandNodeExecutor<?> actual = MethodCommandExtractor.createExecutor(object, "hello");

        Assertions.assertEquals(expected, actual);
    }

    public static final class TestNoArgumentsCommandResultExecutor {

        @com.wizardlybump17.wlib.command.annotation.Command("hello world")
        public CommandResult<?> hello() {
            return null;
        }
    }

    @Test
    void testNoArguments() {
        TestNoArgumentsExecutor object = new TestNoArgumentsExecutor();

        Method method = Assertions.assertDoesNotThrow(() -> object.getClass().getMethod("hello"));

        CommandNodeExecutor<?> expected = new AbstractMethodCommandNodeExecutor.NoArgumentsExecutor<>(object, method);
        CommandNodeExecutor<?> actual = MethodCommandExtractor.createExecutor(object, "hello");

        Assertions.assertEquals(expected, actual);
    }

    public static final class TestNoArgumentsExecutor {

        @com.wizardlybump17.wlib.command.annotation.Command("hello world")
        public void hello() {
        }
    }

    /*
    CommandSender/CommandContext only
     */

    @Test
    void testCommandSenderCommandResult() {
        TestCommandSenderCommandResultExecutor object = new TestCommandSenderCommandResultExecutor();

        Method method = Assertions.assertDoesNotThrow(() -> object.getClass().getMethod("hello", CommandSender.class));

        CommandNodeExecutor<?> expected = new AbstractMethodCommandNodeExecutor.CommandSenderCommandResultExecutor<>(object, method);
        CommandNodeExecutor<?> actual = MethodCommandExtractor.createExecutor(object, "hello", CommandSender.class);

        Assertions.assertEquals(expected, actual);
    }

    public static final class TestCommandSenderCommandResultExecutor {

        @com.wizardlybump17.wlib.command.annotation.Command("hello world")
        public CommandResult<?> hello(@NotNull CommandSender<?> sender) {
            return null;
        }
    }

    @Test
    void testCommandSender() {
        TestCommandSenderExecutor object = new TestCommandSenderExecutor();

        Method method = Assertions.assertDoesNotThrow(() -> object.getClass().getMethod("hello", CommandSender.class));

        CommandNodeExecutor<?> expected = new AbstractMethodCommandNodeExecutor.CommandSenderExecutor<>(object, method);
        CommandNodeExecutor<?> actual = MethodCommandExtractor.createExecutor(object, "hello", CommandSender.class);

        Assertions.assertEquals(expected, actual);
    }

    public static final class TestCommandSenderExecutor {

        @com.wizardlybump17.wlib.command.annotation.Command("hello world")
        public void hello(@NotNull CommandSender<?> sender) {
        }
    }

    @Test
    void testCommandContextCommandResult() {
        TestCommandContextCommandResultExecutor object = new TestCommandContextCommandResultExecutor();

        Method method = Assertions.assertDoesNotThrow(() -> object.getClass().getMethod("hello", CommandContext.class));

        CommandNodeExecutor<?> expected = new AbstractMethodCommandNodeExecutor.CommandContextCommandResultExecutor<>(object, method);
        CommandNodeExecutor<?> actual = MethodCommandExtractor.createExecutor(object, "hello", CommandContext.class);

        Assertions.assertEquals(expected, actual);
    }

    public static final class TestCommandContextCommandResultExecutor {

        @com.wizardlybump17.wlib.command.annotation.Command("hello world")
        public CommandResult<?> hello(@NotNull CommandContext context) {
            return null;
        }
    }

    @Test
    void testCommandContext() {
        TestCommandContextExecutor object = new TestCommandContextExecutor();

        Method method = Assertions.assertDoesNotThrow(() -> object.getClass().getMethod("hello", CommandContext.class));

        CommandNodeExecutor<?> expected = new AbstractMethodCommandNodeExecutor.CommandContextExecutor<>(object, method);
        CommandNodeExecutor<?> actual = MethodCommandExtractor.createExecutor(object, "hello", CommandContext.class);

        Assertions.assertEquals(expected, actual);
    }

    public static final class TestCommandContextExecutor {

        @com.wizardlybump17.wlib.command.annotation.Command("hello world")
        public void hello(@NotNull CommandContext context) {
        }
    }

    /*
    CommandSender/CommandContext and parameters
     */

    @Test
    void testCommandSenderAndArgumentsCommandResult() {
        TestCommandSenderAndArgumentsCommandResultExecutor object = new TestCommandSenderAndArgumentsCommandResultExecutor();

        Method method = Assertions.assertDoesNotThrow(() -> object.getClass().getMethod("hello", CommandSender.class, int.class));

        CommandNodeExecutor<?> expected = new AbstractMethodCommandNodeExecutor.CommandSenderAndArgumentsCommandResultExecutor<>(object, method);
        CommandNodeExecutor<?> actual = MethodCommandExtractor.createExecutor(object, "hello", CommandSender.class, int.class);

        Assertions.assertEquals(expected, actual);
    }

    public static final class TestCommandSenderAndArgumentsCommandResultExecutor {

        @com.wizardlybump17.wlib.command.annotation.Command("hello world <int>")
        public CommandResult<?> hello(@NotNull CommandSender<?> sender, int arg0) {
            return null;
        }
    }

    @Test
    void testCommandSenderAndArguments() {
        TestCommandSenderAndArgumentsExecutor object = new TestCommandSenderAndArgumentsExecutor();

        Method method = Assertions.assertDoesNotThrow(() -> object.getClass().getMethod("hello", CommandSender.class, int.class));

        CommandNodeExecutor<?> expected = new AbstractMethodCommandNodeExecutor.CommandSenderAndArgumentsExecutor<>(object, method);
        CommandNodeExecutor<?> actual = MethodCommandExtractor.createExecutor(object, "hello", CommandSender.class, int.class);

        Assertions.assertEquals(expected, actual);
    }

    public static final class TestCommandSenderAndArgumentsExecutor {

        @com.wizardlybump17.wlib.command.annotation.Command("hello world <int>")
        public void hello(@NotNull CommandSender<?> sender, int arg0) {
        }
    }

    @Test
    void testCommandContextAndArgumentsCommandResult() {
        TestCommandContextAndArgumentsCommandResultExecutor object = new TestCommandContextAndArgumentsCommandResultExecutor();

        Method method = Assertions.assertDoesNotThrow(() -> object.getClass().getMethod("hello", CommandContext.class, int.class));

        CommandNodeExecutor<?> expected = new AbstractMethodCommandNodeExecutor.CommandContextAndArgumentsCommandResultExecutor<>(object, method);
        CommandNodeExecutor<?> actual = MethodCommandExtractor.createExecutor(object, "hello", CommandContext.class, int.class);

        Assertions.assertEquals(expected, actual);
    }

    public static final class TestCommandContextAndArgumentsCommandResultExecutor {

        @com.wizardlybump17.wlib.command.annotation.Command("hello world <int>")
        public CommandResult<?> hello(@NotNull CommandContext context, int arg0) {
            return null;
        }
    }

    @Test
    void testCommandContextAndArguments() {
        TestCommandContextAndArgumentsExecutor object = new TestCommandContextAndArgumentsExecutor();

        Method method = Assertions.assertDoesNotThrow(() -> object.getClass().getMethod("hello", CommandContext.class, int.class));

        CommandNodeExecutor<?> expected = new AbstractMethodCommandNodeExecutor.CommandContextAndArgumentsExecutor<>(object, method);
        CommandNodeExecutor<?> actual = MethodCommandExtractor.createExecutor(object, "hello", CommandContext.class, int.class);

        Assertions.assertEquals(expected, actual);
    }

    public static final class TestCommandContextAndArgumentsExecutor {

        @com.wizardlybump17.wlib.command.annotation.Command("hello world <int>")
        public void hello(@NotNull CommandContext context, int arg0) {
        }
    }

    /*
    Parameters only
     */

    @Test
    void testArgumentsCommandResult() {
        TestArgumentsCommandResult object = new TestArgumentsCommandResult();

        Method method = Assertions.assertDoesNotThrow(() -> object.getClass().getMethod("hello", int.class));

        CommandNodeExecutor<?> expected = new AbstractMethodCommandNodeExecutor.ArgumentsCommandResultExecutor<>(object, method);
        CommandNodeExecutor<?> actual = MethodCommandExtractor.createExecutor(object, "hello", int.class);

        Assertions.assertEquals(expected, actual);
    }

    public static final class TestArgumentsCommandResult {

        @com.wizardlybump17.wlib.command.annotation.Command("hello world <int>")
        public CommandResult<?> hello(int arg0) {
            return null;
        }
    }

    @Test
    void testArguments() {
        TestArguments object = new TestArguments();

        Method method = Assertions.assertDoesNotThrow(() -> object.getClass().getMethod("hello", int.class));

        CommandNodeExecutor<?> expected = new AbstractMethodCommandNodeExecutor.ArgumentsExecutor<>(object, method);
        CommandNodeExecutor<?> actual = MethodCommandExtractor.createExecutor(object, "hello", int.class);

        Assertions.assertEquals(expected, actual);
    }

    public static final class TestArguments {

        @com.wizardlybump17.wlib.command.annotation.Command("hello world <int>")
        public void hello(int arg0) {
        }
    }
}
