package com.wizardlybump17.wlib.command.rework;

import com.wizardlybump17.wlib.command.rework.context.CommandContext;
import com.wizardlybump17.wlib.command.rework.exception.InputParsingException;
import com.wizardlybump17.wlib.command.rework.exception.InvalidInputException;
import com.wizardlybump17.wlib.command.rework.executor.CommandNodeExecutor;
import com.wizardlybump17.wlib.command.rework.input.AllowedNumberInputs;
import com.wizardlybump17.wlib.command.rework.node.CommandNode;
import com.wizardlybump17.wlib.command.rework.node.IntegerCommandNode;
import com.wizardlybump17.wlib.command.rework.node.LiteralCommandNode;
import com.wizardlybump17.wlib.command.rework.result.CommandResult;
import com.wizardlybump17.wlib.command.sender.CommandSender;
import com.wizardlybump17.wlib.util.StringUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Command {

    private final @NotNull LiteralCommandNode root;

    public Command(@NotNull LiteralCommandNode root) {
        this.root = root;
    }

    public @NotNull LiteralCommandNode getRoot() {
        return root;
    }

    public @NotNull CommandResult<?> execute(@NotNull CommandSender<?> sender, @NotNull String input) {
        List<String> inputList = getInputList(input);
        return execute(sender, inputList);
    }

    @SuppressWarnings("unchecked")
    public @NotNull CommandResult<?> execute(@NotNull CommandSender<?> sender, @NotNull List<String> input) {
        if (input.isEmpty())
            return CommandResult.insufficientArguments(-1, root);

        List<CommandContext.CommandNodeArgument<?>> arguments = new ArrayList<>();
        List<CommandNode<?>> children = List.of(root);

        CommandNode<?> lastNode = null;
        int lastInputIndex = 0;
        InputParsingException lastParsingError = null;
        InvalidInputException lastInputError = null;

        inputLoop: for (int i = 0; i < input.size(); i++) {
            String inputString = input.get(i);
            lastInputIndex = i;

            for (CommandNode<?> child : children) {
                lastNode = child;

                try {
                    Object result = child.parseOrInvalid(inputString);

                    arguments.add(new CommandContext.CommandNodeArgument<>((CommandNode<Object>) child, inputString, result));

                    children = child.getChildren();

                    lastParsingError = null;
                    lastInputError = null;

                    continue inputLoop;
                } catch (InputParsingException e) {
                    lastParsingError = e;
                } catch (InvalidInputException e) {
                    lastInputError = e;
                }
            }

            if (lastParsingError != null)
                return CommandResult.parseInputException(lastInputIndex, lastNode, lastParsingError);
            if (lastInputError != null)
                return CommandResult.outOfRangeInput(lastInputIndex, lastNode);

            return CommandResult.extraArguments(lastInputIndex, lastNode);
        }

        CommandNodeExecutor<?> executor = lastNode.getExecutor();
        if (executor == null)
            return CommandResult.noCommandNodeExecutor(lastInputIndex, lastNode);

        String nodePermission = lastNode.getPermission();
        if (nodePermission != null && !sender.hasPermission(nodePermission))
            return CommandResult.noPermission(lastInputIndex, lastNode);

        CommandContext context = new CommandContext(
                this,
                sender,
                new CommandContext.CommandNodeArguments(arguments),
                lastInputIndex,
                lastNode
        );
        return executor.execute(context);
    }

    public @NotNull List<String> getInputList(@NotNull String original) {
        return StringUtil.parseQuotedStrings(original);
    }

    public @NotNull List<String> getSuggestions(@NotNull CommandSender<?> sender, @NotNull List<String> input) {
        if (input.isEmpty())
            return List.of();

        String currentInput = input.getLast();

        List<String> suggestions = new ArrayList<>();
        List<CommandNode<?>> children = List.of(root);

        CommandNode<?> lastNode = null;
        InputParsingException lastParsingError = null;

        inputLoop: for (int i = 0; i < input.size(); i++) {
            String inputString = input.get(i);

            for (CommandNode<?> child : children) {
                lastNode = child;

                try {
                    child.parse(inputString);

                    suggestions.addAll(child.getSuggestions(sender, input, currentInput).stream().map(String::valueOf).toList());

                    children = child.getChildren();

                    lastParsingError = null;

                    continue inputLoop;
                } catch (InputParsingException e) {
                    lastParsingError = e;
                }
            }

            if (lastParsingError != null)
                return List.of();

            return List.of();
        }

        if (lastNode == null)
            return List.of();

        String nodePermission = lastNode.getPermission();
        if (nodePermission != null && !sender.hasPermission(nodePermission))
            return List.of();

        return suggestions;
    }

    public static @Nullable Command createCommand(@NotNull String execution) {
        LiteralCommandNode firstNode = null;

        List<CommandNode<?>> children = new ArrayList<>();
        String[] strings = execution.split(" ");

        for (int i = 0; i < strings.length; i++) {
            String string = strings[i];
            if (i == 0) {
                firstNode = new LiteralCommandNode(string, children);
                continue;
            }

            List<CommandNode<?>> newChildren = new ArrayList<>();
            LiteralCommandNode newNode = new LiteralCommandNode(string, newChildren);
            children.add(newNode);

            children = newChildren;
        }

        return firstNode == null ? null : new Command(firstNode);
    }

    @Override
    public String toString() {
        return "Command{" +
                "root=" + root +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass())
            return false;
        Command command = (Command) o;
        return Objects.equals(root, command.root);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(root);
    }

    public static @NotNull Command fromMethod(@NotNull Method method, @NotNull Object object) {
        com.wizardlybump17.wlib.command.rework.annotation.Command annotation = method.getDeclaredAnnotation(com.wizardlybump17.wlib.command.rework.annotation.Command.class);
        if (annotation == null)
            throw new IllegalArgumentException("Method " + method.getName() + " does not have a Command annotation");

        Class<?>[] parameterTypes = method.getParameterTypes();
        if (parameterTypes.length == 0)
            throw new IllegalArgumentException("Method " + method.getName() + " does not have any parameter");

        if (!parameterTypes[0].equals(CommandContext.class))
            throw new IllegalArgumentException("Method " + method.getName() + "'s first parameter is not a CommandContext");

        LiteralCommandNode firstNode = null;

        List<CommandNode<?>> children = new ArrayList<>();
        String[] parts = annotation.value().split(" ");

        int parameterIndex = 1;
        for (int i = 0; i < parts.length; i++) {
            String part = parts[i];

            CommandNodeExecutor<?> executor;
            if (i + 1 >= parts.length) {
                executor = new CommandNodeExecutor<>() {
                    @NotNull MethodHandle methodHandle; {
                        try {
                            methodHandle = MethodHandles.publicLookup().findVirtual(object.getClass(), method.getName(), MethodType.methodType(method.getReturnType(), method.getParameterTypes()));
                        } catch (NoSuchMethodException | IllegalAccessException e) {
                            throw new RuntimeException(e);
                        }
                    }

                    @SuppressWarnings("unchecked")
                    @Override
                    public @NotNull CommandResult<Object> execute(@NotNull CommandContext context) {
                        List<Object> arguments = new ArrayList<>();

                        arguments.add(object);
                        arguments.add(context);

                        arguments.addAll(context.arguments().getArguments().values()
                                .stream()
                                .filter(argument -> !(argument.node() instanceof LiteralCommandNode))
                                .map(CommandContext.CommandNodeArgument::data)
                                .toList()
                        );

                        try {
                            Object invoke = methodHandle.invokeWithArguments(arguments);
                            return invoke instanceof CommandResult<?> result ? (CommandResult<Object>) result : CommandResult.successful(context, invoke);
                        } catch (Throwable e) {
                            throw new RuntimeException(e);
                        }
                    }
                };
            } else {
                executor = null;
            }

            if (i == 0) {
                firstNode = new LiteralCommandNode(part, children, executor);
                continue;
            }

            List<CommandNode<?>> newChildren = new ArrayList<>();
            CommandNode<?> newNode;

            if (part.charAt(0) == '<' && part.charAt(part.length() - 1) == '>') {
                String nodeName = part.substring(1, part.length() - 1);

                Class<?> parameterType = parameterTypes[parameterIndex++];
                if (parameterType == int.class || parameterType == Integer.class)
                    newNode = new IntegerCommandNode(nodeName, newChildren, new AllowedNumberInputs.AllowedIntegerInputs.Unlimited(), executor);
                else
                    throw new IllegalArgumentException("Just trying stuff for now. Come back later");

            } else {
                newNode = new LiteralCommandNode(part, newChildren, executor);
            }

            children.add(newNode);

            children = newChildren;
        }

        return firstNode == null ? null : new Command(firstNode);
    }
}
