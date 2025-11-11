package com.wizardlybump17.wlib.test.command.extractor.method;

import com.wizardlybump17.wlib.command.Command;
import com.wizardlybump17.wlib.command.context.CommandContext;
import com.wizardlybump17.wlib.command.extractor.CommandExtractor;
import com.wizardlybump17.wlib.command.extractor.method.MethodCommandExtractor;
import com.wizardlybump17.wlib.command.input.AllowedNumberInputs;
import com.wizardlybump17.wlib.command.node.IntegerCommandNode;
import com.wizardlybump17.wlib.command.node.LiteralCommandNode;
import com.wizardlybump17.wlib.command.result.CommandResult;
import com.wizardlybump17.wlib.command.sender.CommandSender;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

class MethodCommandExtractorTests {

    /*
    No parameters
     */
    @Test
    void testNoParameters() {
        TestNoParameters object = new TestNoParameters();

        List<Command> expected = new ArrayList<>(List.of(
                new Command(
                        new LiteralCommandNode(
                                "hello",
                                Assertions.assertDoesNotThrow(() -> MethodCommandExtractor.createExecutor(object, "hello"))
                        )
                ),
                new Command(
                        new LiteralCommandNode(
                                "hello",
                                List.of(
                                        new LiteralCommandNode(
                                                "world",
                                                Assertions.assertDoesNotThrow(() -> MethodCommandExtractor.createExecutor(object, "helloWorld"))
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
                                                                Assertions.assertDoesNotThrow(() -> MethodCommandExtractor.createExecutor(object, "helloThereHi"))
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

    public static class TestNoParameters {

        @com.wizardlybump17.wlib.command.annotation.Command("hello")
        public void hello() {
        }

        @com.wizardlybump17.wlib.command.annotation.Command("hello world")
        public void helloWorld() {
        }

        @com.wizardlybump17.wlib.command.annotation.Command("hello there hi")
        public void helloThereHi() {
        }
    }

    /*
    No parameters returning CommandResult
     */
    @Test
    void testNoParametersCommandResult() {
        TestNoParametersCommandResult object = new TestNoParametersCommandResult();

        List<Command> expected = new ArrayList<>(List.of(
                new Command(
                        new LiteralCommandNode(
                                "hello",
                                Assertions.assertDoesNotThrow(() -> MethodCommandExtractor.createExecutor(object, "hello"))
                        )
                ),
                new Command(
                        new LiteralCommandNode(
                                "hello",
                                List.of(
                                        new LiteralCommandNode(
                                                "world",
                                                Assertions.assertDoesNotThrow(() -> MethodCommandExtractor.createExecutor(object, "helloWorld"))
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
                                                                Assertions.assertDoesNotThrow(() -> MethodCommandExtractor.createExecutor(object, "helloThereHi"))
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

    public static class TestNoParametersCommandResult {

        @com.wizardlybump17.wlib.command.annotation.Command("hello")
        public CommandResult<?> hello() {
            return null;
        }

        @com.wizardlybump17.wlib.command.annotation.Command("hello world")
        public CommandResult<?> helloWorld() {
            return null;
        }

        @com.wizardlybump17.wlib.command.annotation.Command("hello there hi")
        public CommandResult<?> helloThereHi() {
            return null;
        }
    }

    /*
    CommandSender/CommandContext only
     */

    @Test
    void testCommandSender() {
        TestCommandSender object = new TestCommandSender();

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

    public static class TestCommandSender {

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
    void testCommandContext() {
        TestCommandContext object = new TestCommandContext();

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

    public static class TestCommandContext {

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

    /*
    CommandSender/CommandSender returning CommandResult
     */

    @Test
    void testCommandContextCommandResult() {
        TestCommandContextCommandResult object = new TestCommandContextCommandResult();

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

    public static class TestCommandContextCommandResult {

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
    void testCommandSenderCommandResult() {
        TestCommandSenderCommandResult object = new TestCommandSenderCommandResult();

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

    public static class TestCommandSenderCommandResult {

        @com.wizardlybump17.wlib.command.annotation.Command("hello")
        public CommandResult<?> hello(@NotNull CommandSender<?> sender) {
            return null;
        }

        @com.wizardlybump17.wlib.command.annotation.Command("hello world")
        public CommandResult<?> helloWorld(@NotNull CommandSender<?> sender) {
            return null;
        }

        @com.wizardlybump17.wlib.command.annotation.Command("hello there hi")
        public CommandResult<?> helloThereHi(@NotNull CommandSender<?> sender) {
            return null;
        }
    }

    /*
    CommandSender/CommandContext + parameters
     */

    @Test
    void testCommandSenderParameters() {
        TestCommandSenderParameters object = new TestCommandSenderParameters();

        List<Command> expected = new ArrayList<>(List.of(
                new Command(
                        new LiteralCommandNode(
                                "hello",
                                List.of(
                                        new IntegerCommandNode(
                                                "int",
                                                new AllowedNumberInputs.AllowedIntegerInputs.Unlimited(),
                                                Assertions.assertDoesNotThrow(() -> MethodCommandExtractor.createExecutor(object, "hello", CommandSender.class, int.class))
                                        )
                                )
                        )
                ),
                new Command(
                        new LiteralCommandNode(
                                "hello",
                                List.of(
                                        new LiteralCommandNode(
                                                "world",
                                                List.of(
                                                        new IntegerCommandNode(
                                                                "int",
                                                                new AllowedNumberInputs.AllowedIntegerInputs.Unlimited(),
                                                                Assertions.assertDoesNotThrow(() -> MethodCommandExtractor.createExecutor(object, "helloWorld", CommandSender.class, int.class))
                                                        )
                                                )
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
                                                                List.of(
                                                                        new IntegerCommandNode(
                                                                                "int",
                                                                                new AllowedNumberInputs.AllowedIntegerInputs.Unlimited(),
                                                                                Assertions.assertDoesNotThrow(() -> MethodCommandExtractor.createExecutor(object, "helloThereHi", CommandSender.class, int.class))
                                                                        )
                                                                )
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

    public static class TestCommandSenderParameters {

        @com.wizardlybump17.wlib.command.annotation.Command("hello <int>")
        public void hello(@NotNull CommandSender<?> sender, int arg0) {
        }

        @com.wizardlybump17.wlib.command.annotation.Command("hello world <int>")
        public void helloWorld(@NotNull CommandSender<?> sender, int arg0) {
        }

        @com.wizardlybump17.wlib.command.annotation.Command("hello there hi <int>")
        public void helloThereHi(@NotNull CommandSender<?> sender, int arg0) {
        }
    }

    @Test
    void testCommandContextParameters() {
        TestCommandContextParameters object = new TestCommandContextParameters();

        List<Command> expected = new ArrayList<>(List.of(
                new Command(
                        new LiteralCommandNode(
                                "hello",
                                List.of(
                                        new IntegerCommandNode(
                                                "int",
                                                new AllowedNumberInputs.AllowedIntegerInputs.Unlimited(),
                                                Assertions.assertDoesNotThrow(() -> MethodCommandExtractor.createExecutor(object, "hello", CommandContext.class, int.class))
                                        )
                                )
                        )
                ),
                new Command(
                        new LiteralCommandNode(
                                "hello",
                                List.of(
                                        new LiteralCommandNode(
                                                "world",
                                                List.of(
                                                        new IntegerCommandNode(
                                                                "int",
                                                                new AllowedNumberInputs.AllowedIntegerInputs.Unlimited(),
                                                                Assertions.assertDoesNotThrow(() -> MethodCommandExtractor.createExecutor(object, "helloWorld", CommandContext.class, int.class))
                                                        )
                                                )
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
                                                                List.of(
                                                                        new IntegerCommandNode(
                                                                                "int",
                                                                                new AllowedNumberInputs.AllowedIntegerInputs.Unlimited(),
                                                                                Assertions.assertDoesNotThrow(() -> MethodCommandExtractor.createExecutor(object, "helloThereHi", CommandContext.class, int.class))
                                                                        )
                                                                )
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

    public static class TestCommandContextParameters {

        @com.wizardlybump17.wlib.command.annotation.Command("hello <int>")
        public void hello(@NotNull CommandContext context, int arg0) {
        }

        @com.wizardlybump17.wlib.command.annotation.Command("hello world <int>")
        public void helloWorld(@NotNull CommandContext context, int arg0) {
        }

        @com.wizardlybump17.wlib.command.annotation.Command("hello there hi <int>")
        public void helloThereHi(@NotNull CommandContext context, int arg0) {
        }
    }

    /*
    CommandSender/CommandContext + parameters returning CommandResult
     */

    @Test
    void testCommandSenderParametersCommandResult() {
        TestCommandSenderParametersCommandResult object = new TestCommandSenderParametersCommandResult();

        List<Command> expected = new ArrayList<>(List.of(
                new Command(
                        new LiteralCommandNode(
                                "hello",
                                List.of(
                                        new IntegerCommandNode(
                                                "int",
                                                new AllowedNumberInputs.AllowedIntegerInputs.Unlimited(),
                                                Assertions.assertDoesNotThrow(() -> MethodCommandExtractor.createExecutor(object, "hello", CommandSender.class, int.class))
                                        )
                                )
                        )
                ),
                new Command(
                        new LiteralCommandNode(
                                "hello",
                                List.of(
                                        new LiteralCommandNode(
                                                "world",
                                                List.of(
                                                        new IntegerCommandNode(
                                                                "int",
                                                                new AllowedNumberInputs.AllowedIntegerInputs.Unlimited(),
                                                                Assertions.assertDoesNotThrow(() -> MethodCommandExtractor.createExecutor(object, "helloWorld", CommandSender.class, int.class))
                                                        )
                                                )
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
                                                                List.of(
                                                                        new IntegerCommandNode(
                                                                                "int",
                                                                                new AllowedNumberInputs.AllowedIntegerInputs.Unlimited(),
                                                                                Assertions.assertDoesNotThrow(() -> MethodCommandExtractor.createExecutor(object, "helloThereHi", CommandSender.class, int.class))
                                                                        )
                                                                )
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

    public static class TestCommandSenderParametersCommandResult {

        @com.wizardlybump17.wlib.command.annotation.Command("hello <int>")
        public CommandResult<?> hello(@NotNull CommandSender<?> sender, int arg0) {
            return null;
        }

        @com.wizardlybump17.wlib.command.annotation.Command("hello world <int>")
        public CommandResult<?> helloWorld(@NotNull CommandSender<?> sender, int arg0) {
            return null;
        }

        @com.wizardlybump17.wlib.command.annotation.Command("hello there hi <int>")
        public CommandResult<?> helloThereHi(@NotNull CommandSender<?> sender, int arg0) {
            return null;
        }
    }

    @Test
    void testCommandContextParametersCommandResult() {
        TestCommandContextParametersCommandResult object = new TestCommandContextParametersCommandResult();

        List<Command> expected = new ArrayList<>(List.of(
                new Command(
                        new LiteralCommandNode(
                                "hello",
                                List.of(
                                        new IntegerCommandNode(
                                                "int",
                                                new AllowedNumberInputs.AllowedIntegerInputs.Unlimited(),
                                                Assertions.assertDoesNotThrow(() -> MethodCommandExtractor.createExecutor(object, "hello", CommandContext.class, int.class))
                                        )
                                )
                        )
                ),
                new Command(
                        new LiteralCommandNode(
                                "hello",
                                List.of(
                                        new LiteralCommandNode(
                                                "world",
                                                List.of(
                                                        new IntegerCommandNode(
                                                                "int",
                                                                new AllowedNumberInputs.AllowedIntegerInputs.Unlimited(),
                                                                Assertions.assertDoesNotThrow(() -> MethodCommandExtractor.createExecutor(object, "helloWorld", CommandContext.class, int.class))
                                                        )
                                                )
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
                                                                List.of(
                                                                        new IntegerCommandNode(
                                                                                "int",
                                                                                new AllowedNumberInputs.AllowedIntegerInputs.Unlimited(),
                                                                                Assertions.assertDoesNotThrow(() -> MethodCommandExtractor.createExecutor(object, "helloThereHi", CommandContext.class, int.class))
                                                                        )
                                                                )
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

    public static class TestCommandContextParametersCommandResult {

        @com.wizardlybump17.wlib.command.annotation.Command("hello <int>")
        public CommandResult<?> hello(@NotNull CommandContext context, int arg0) {
            return null;
        }

        @com.wizardlybump17.wlib.command.annotation.Command("hello world <int>")
        public CommandResult<?> helloWorld(@NotNull CommandContext context, int arg0) {
            return null;
        }

        @com.wizardlybump17.wlib.command.annotation.Command("hello there hi <int>")
        public CommandResult<?> helloThereHi(@NotNull CommandContext context, int arg0) {
            return null;
        }
    }

    /*
    Parameters not starting with CommandSender/CommandContext
     */

    @Test
    void testParametersPure() {
        TestParametersPure object = new TestParametersPure();

        List<Command> expected = new ArrayList<>(List.of(
                new Command(
                        new LiteralCommandNode(
                                "hello",
                                List.of(
                                        new IntegerCommandNode(
                                                "int",
                                                new AllowedNumberInputs.AllowedIntegerInputs.Unlimited(),
                                                Assertions.assertDoesNotThrow(() -> MethodCommandExtractor.createExecutor(object, "hello", int.class))
                                        )
                                )
                        )
                ),
                new Command(
                        new LiteralCommandNode(
                                "hello",
                                List.of(
                                        new LiteralCommandNode(
                                                "world",
                                                List.of(
                                                        new IntegerCommandNode(
                                                                "int",
                                                                new AllowedNumberInputs.AllowedIntegerInputs.Unlimited(),
                                                                Assertions.assertDoesNotThrow(() -> MethodCommandExtractor.createExecutor(object, "helloWorld", int.class))
                                                        )
                                                )
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
                                                                List.of(
                                                                        new IntegerCommandNode(
                                                                                "int",
                                                                                new AllowedNumberInputs.AllowedIntegerInputs.Unlimited(),
                                                                                Assertions.assertDoesNotThrow(() -> MethodCommandExtractor.createExecutor(object, "helloThereHi", int.class))
                                                                        )
                                                                )
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

    public static class TestParametersPure {

        @com.wizardlybump17.wlib.command.annotation.Command("hello <int>")
        public void hello(int arg0) {
        }

        @com.wizardlybump17.wlib.command.annotation.Command("hello world <int>")
        public void helloWorld(int arg0) {
        }

        @com.wizardlybump17.wlib.command.annotation.Command("hello there hi <int>")
        public void helloThereHi(int arg0) {
        }
    }

    /*
    Parameters not starting with CommandSender/CommandContext returning CommandResult
     */

    @Test
    void testParametersPureCommandResult() {
        TestParametersPureCommandResult object = new TestParametersPureCommandResult();

        List<Command> expected = new ArrayList<>(List.of(
                new Command(
                        new LiteralCommandNode(
                                "hello",
                                List.of(
                                        new IntegerCommandNode(
                                                "int",
                                                new AllowedNumberInputs.AllowedIntegerInputs.Unlimited(),
                                                Assertions.assertDoesNotThrow(() -> MethodCommandExtractor.createExecutor(object, "hello", int.class))
                                        )
                                )
                        )
                ),
                new Command(
                        new LiteralCommandNode(
                                "hello",
                                List.of(
                                        new LiteralCommandNode(
                                                "world",
                                                List.of(
                                                        new IntegerCommandNode(
                                                                "int",
                                                                new AllowedNumberInputs.AllowedIntegerInputs.Unlimited(),
                                                                Assertions.assertDoesNotThrow(() -> MethodCommandExtractor.createExecutor(object, "helloWorld", int.class))
                                                        )
                                                )
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
                                                                List.of(
                                                                        new IntegerCommandNode(
                                                                                "int",
                                                                                new AllowedNumberInputs.AllowedIntegerInputs.Unlimited(),
                                                                                Assertions.assertDoesNotThrow(() -> MethodCommandExtractor.createExecutor(object, "helloThereHi", int.class))
                                                                        )
                                                                )
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

    public static class TestParametersPureCommandResult {

        @com.wizardlybump17.wlib.command.annotation.Command("hello <int>")
        public CommandResult<?> hello(int arg0) {
            return null;
        }

        @com.wizardlybump17.wlib.command.annotation.Command("hello world <int>")
        public CommandResult<?> helloWorld(int arg0) {
            return null;
        }

        @com.wizardlybump17.wlib.command.annotation.Command("hello there hi <int>")
        public CommandResult<?> helloThereHi(int arg0) {
            return null;
        }
    }
}
