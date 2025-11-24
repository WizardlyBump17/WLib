package com.wizardlybump17.wlib.test.command.extractor.method;

import com.wizardlybump17.wlib.command.Command;
import com.wizardlybump17.wlib.command.context.CommandContext;
import com.wizardlybump17.wlib.command.extractor.CommandExtractor;
import com.wizardlybump17.wlib.command.extractor.method.MethodCommandExtractor;
import com.wizardlybump17.wlib.command.extractor.method.factory.MethodCommandNodeFactory;
import com.wizardlybump17.wlib.command.input.number.AllowedIntegerInputs;
import com.wizardlybump17.wlib.command.input.string.AllowedStringInputs;
import com.wizardlybump17.wlib.command.manager.CommandManager;
import com.wizardlybump17.wlib.command.node.CommandNode;
import com.wizardlybump17.wlib.command.node.IntegerCommandNode;
import com.wizardlybump17.wlib.command.node.LiteralCommandNode;
import com.wizardlybump17.wlib.command.node.StringCommandNode;
import com.wizardlybump17.wlib.command.registry.MethodCommandNodeFactoryRegistry;
import com.wizardlybump17.wlib.command.result.CommandResult;
import com.wizardlybump17.wlib.command.sender.BasicCommandSender;
import com.wizardlybump17.wlib.command.sender.CommandSender;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.function.Consumer;

class MethodCommandExtractorTests {

    @BeforeAll
    static void setup() {
        MethodCommandNodeFactoryRegistry.INSTANCE.addFactory(
                int.class,
                new MethodCommandNodeFactory<IntegerCommandNode>() {
                    @Override
                    public @NotNull IntegerCommandNode create(@NotNull Object object, @NotNull Method method, com.wizardlybump17.wlib.command.annotation.@NotNull Command commandAnnotation, @NotNull Parameter parameter, @NotNull String name, @Nullable CommandNode<?> root) {
                        return new IntegerCommandNode(name, root == null ? List.of() : List.of(root), AllowedIntegerInputs.unlimited());
                    }
                }
        );
        MethodCommandNodeFactoryRegistry.INSTANCE.addFactory(
                String.class,
                new MethodCommandNodeFactory<StringCommandNode>() {
                    @Override
                    public @NotNull StringCommandNode create(@NotNull Object object, @NotNull Method method, com.wizardlybump17.wlib.command.annotation.@NotNull Command commandAnnotation, @NotNull Parameter parameter, @NotNull String name, @Nullable CommandNode<?> root) {
                        return new StringCommandNode(name, root == null ? List.of() : List.of(root), AllowedStringInputs.anyNullable());
                    }
                }
        );
    }

    @AfterAll
    static void clear() {
        MethodCommandNodeFactoryRegistry.INSTANCE.clear();
    }

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
        List<Command> actual = Assertions.assertDoesNotThrow(() -> CommandExtractor.METHOD.extract(object));

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
        List<Command> actual = Assertions.assertDoesNotThrow(() -> CommandExtractor.METHOD.extract(object));

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
                                                AllowedIntegerInputs.unlimited(),
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
                                                                AllowedIntegerInputs.unlimited(),
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
                                                                                AllowedIntegerInputs.unlimited(),
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
                                                AllowedIntegerInputs.unlimited(),
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
                                                                AllowedIntegerInputs.unlimited(),
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
                                                                                AllowedIntegerInputs.unlimited(),
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
                                                AllowedIntegerInputs.unlimited(),
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
                                                                AllowedIntegerInputs.unlimited(),
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
                                                                                AllowedIntegerInputs.unlimited(),
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
                                                AllowedIntegerInputs.unlimited(),
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
                                                                AllowedIntegerInputs.unlimited(),
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
                                                                                AllowedIntegerInputs.unlimited(),
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
                                                AllowedIntegerInputs.unlimited(),
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
                                                                AllowedIntegerInputs.unlimited(),
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
                                                                                AllowedIntegerInputs.unlimited(),
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
                                                AllowedIntegerInputs.unlimited(),
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
                                                                AllowedIntegerInputs.unlimited(),
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
                                                                                AllowedIntegerInputs.unlimited(),
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

    /*
    Lets execute some commands
     */

    static final @NotNull Consumer<String> SENDER_MESSAGE_CONSUMER = System.out::println;
    static final @NotNull CommandSender<Object> CHAD_SENDER = new BasicCommandSender<>(new Object(), "Chad", UUID.nameUUIDFromBytes("Chad".getBytes()), SENDER_MESSAGE_CONSUMER, $ -> true);
    static final @NotNull CommandSender<Object> BETA_SENDER = new BasicCommandSender<>(new Object(), "Beta", UUID.nameUUIDFromBytes("Beta".getBytes()), SENDER_MESSAGE_CONSUMER, $ -> false);

    @Test
    void testExecute() {
        TestExecute object = new TestExecute();

        Command hello = new Command(
                new LiteralCommandNode(
                        "hello",
                        List.of(
                                new LiteralCommandNode(
                                        "world",
                                        Assertions.assertDoesNotThrow(() -> MethodCommandExtractor.createExecutor(object, "helloWorld"))
                                )
                        ),
                        Assertions.assertDoesNotThrow(() -> MethodCommandExtractor.createExecutor(object, "hello"))
                )
        );
        Command hi = new Command(
                new LiteralCommandNode(
                        "hi",
                        List.of(
                                new LiteralCommandNode(
                                        "world",
                                        Assertions.assertDoesNotThrow(() -> MethodCommandExtractor.createExecutor(object, "hiWorld", CommandSender.class))
                                )
                        ),
                        Assertions.assertDoesNotThrow(() -> MethodCommandExtractor.createExecutor(object, "hi", CommandSender.class))
                )
        );
        Command greetings = new Command(
                new LiteralCommandNode(
                        "greetings",
                        List.of(
                                new LiteralCommandNode(
                                        "world",
                                        Assertions.assertDoesNotThrow(() -> MethodCommandExtractor.createExecutor(object, "greetingsWorld", CommandContext.class))
                                )
                        ),
                        Assertions.assertDoesNotThrow(() -> MethodCommandExtractor.createExecutor(object, "greetings", CommandContext.class))
                )
        );
        Command welcome = new Command(
                new LiteralCommandNode(
                        "welcome",
                        List.of(
                                new StringCommandNode(
                                        "name",
                                        List.of(
                                                new LiteralCommandNode(
                                                        "world",
                                                        Assertions.assertDoesNotThrow(() -> MethodCommandExtractor.createExecutor(object, "welcomeWorld", CommandSender.class, String.class))
                                                )
                                        ),
                                        AllowedStringInputs.anyNullable(),
                                        Assertions.assertDoesNotThrow(() -> MethodCommandExtractor.createExecutor(object, "welcome", CommandSender.class, String.class)),
                                        null
                                )
                        )
                )
        );
        Command wassup = new Command(
                new LiteralCommandNode(
                        "wassup",
                        List.of(
                                new StringCommandNode(
                                        "name",
                                        List.of(
                                                new LiteralCommandNode(
                                                        "nice",
                                                        Assertions.assertDoesNotThrow(() -> MethodCommandExtractor.createExecutor(object, "wassupNice", CommandContext.class, String.class))
                                                )
                                        ),
                                        AllowedStringInputs.anyNullable(),
                                        Assertions.assertDoesNotThrow(() -> MethodCommandExtractor.createExecutor(object, "wassup", CommandContext.class, String.class)),
                                        null
                                )
                        )
                )
        );
        Command aye = new Command(
                new LiteralCommandNode(
                        "aye",
                        List.of(
                                new StringCommandNode(
                                        "name",
                                        List.of(
                                                new LiteralCommandNode(
                                                        "nice",
                                                        Assertions.assertDoesNotThrow(() -> MethodCommandExtractor.createExecutor(object, "ayeNice", String.class))
                                                )
                                        ),
                                        AllowedStringInputs.anyNullable(),
                                        Assertions.assertDoesNotThrow(() -> MethodCommandExtractor.createExecutor(object, "aye", String.class)),
                                        null
                                )
                        )
                )
        );

        List<Command> extractedCommands = Assertions.assertDoesNotThrow(() -> CommandExtractor.METHOD.extract(object));

        CommandManager manager = new CommandManager();

        List<Command> expectedCommands = new ArrayList<>(List.of(hello, hi, greetings, welcome, wassup, aye));
        List<Command> actualCommands = manager.registerCommands("test", extractedCommands);

        expectedCommands.sort(null);
        actualCommands.sort(null);

        //We already know that the register method works
        actualCommands.remove(0);
        actualCommands.remove(1);
        actualCommands.remove(2);
        actualCommands.remove(3);
        actualCommands.remove(4);
        actualCommands.remove(5);

        Assertions.assertEquals(expectedCommands, actualCommands);

        Assertions.assertEquals(
                CommandResult.successful(0, hello.getRoot(), null),
                manager.execute(CHAD_SENDER, "hello")
        );
        Assertions.assertEquals(
                CommandResult.successful(1, hello.findNode("world"), null),
                manager.execute(CHAD_SENDER, "hello world")
        );

        Assertions.assertEquals(
                CommandResult.successful(0, hi.getRoot(), null),
                manager.execute(CHAD_SENDER, "hi")
        );
        Assertions.assertEquals(
                CommandResult.successful(1, hi.findNode("world"), null),
                manager.execute(CHAD_SENDER, "hi world")
        );

        Assertions.assertEquals(
                CommandResult.successful(0, greetings.getRoot(), null),
                manager.execute(CHAD_SENDER, "greetings")
        );
        Assertions.assertEquals(
                CommandResult.successful(1, greetings.findNode("world"), "Hello, world!"),
                manager.execute(CHAD_SENDER, "greetings world")
        );

        Assertions.assertEquals(
                CommandResult.successful(1, welcome.findNode("name"), null),
                manager.execute(CHAD_SENDER, "welcome test")
        );
        Assertions.assertEquals(
                CommandResult.successful(2, welcome.findNode("world"), null),
                manager.execute(CHAD_SENDER, "welcome test world")
        );

        Assertions.assertEquals(
                CommandResult.successful(1, wassup.findNode("name"), null),
                manager.execute(CHAD_SENDER, "wassup test")
        );
        Assertions.assertEquals(
                CommandResult.successful(2, wassup.findNode("nice"), "Nice to meet you, test!"),
                manager.execute(CHAD_SENDER, "wassup test nice")
        );

        Assertions.assertEquals(
                CommandResult.successful(1, aye.findNode("name"), null),
                manager.execute(CHAD_SENDER, "aye test")
        );
        Assertions.assertEquals(
                CommandResult.successful(2, aye.findNode("nice"), null),
                manager.execute(CHAD_SENDER, "aye test nice")
        );
    }

    public static class TestExecute {

        @com.wizardlybump17.wlib.command.annotation.Command("hello")
        public void hello() {
        }

        @com.wizardlybump17.wlib.command.annotation.Command("hello world")
        public CommandResult<?> helloWorld() {
            return null;
        }

        @com.wizardlybump17.wlib.command.annotation.Command("hi")
        public void hi(@NotNull CommandSender<?> sender) {
        }

        @com.wizardlybump17.wlib.command.annotation.Command("hi world")
        public CommandResult<?> hiWorld(@NotNull CommandSender<?> sender) {
            return null;
        }

        @com.wizardlybump17.wlib.command.annotation.Command("greetings")
        public void greetings(@NotNull CommandContext context) {
        }

        @com.wizardlybump17.wlib.command.annotation.Command("greetings world")
        public @NotNull CommandResult<?> greetingsWorld(@NotNull CommandContext context) {
            return CommandResult.successful(context, "Hello, world!");
        }

        @com.wizardlybump17.wlib.command.annotation.Command("welcome <name>")
        public void welcome(@NotNull CommandSender<?> sender, @NotNull String name) {
        }

        @com.wizardlybump17.wlib.command.annotation.Command("welcome <name> world")
        public CommandResult<?> welcomeWorld(@NotNull CommandSender<?> sender, @NotNull String name) {
            return null;
        }

        @com.wizardlybump17.wlib.command.annotation.Command("wassup <name>")
        public void wassup(@NotNull CommandContext context, @NotNull String name) {
        }

        @com.wizardlybump17.wlib.command.annotation.Command("wassup <name> nice")
        public @NotNull CommandResult<?> wassupNice(@NotNull CommandContext context, @NotNull String name) {
            return CommandResult.successful(context, "Nice to meet you, " + name + "!");
        }

        @com.wizardlybump17.wlib.command.annotation.Command("aye <name>")
        public void aye(@NotNull String name) {
        }

        @com.wizardlybump17.wlib.command.annotation.Command("aye <name> nice")
        public CommandResult<?> ayeNice(@NotNull String name) {
            return null;
        }
    }
}
