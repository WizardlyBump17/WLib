package com.wizardlybump17.wlib.command.extractor.method;

import com.wizardlybump17.wlib.command.Command;
import com.wizardlybump17.wlib.command.context.CommandContext;
import com.wizardlybump17.wlib.command.executor.CommandNodeExecutor;
import com.wizardlybump17.wlib.command.extractor.CommandExtractor;
import com.wizardlybump17.wlib.command.input.AllowedNumberInputs;
import com.wizardlybump17.wlib.command.node.CommandNode;
import com.wizardlybump17.wlib.command.node.IntegerCommandNode;
import com.wizardlybump17.wlib.command.node.LiteralCommandNode;
import com.wizardlybump17.wlib.command.sender.CommandSender;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class MethodCommandExtractor implements CommandExtractor {

    public static final @NotNull MethodCommandExtractor INSTANCE = new MethodCommandExtractor();

    @Override
    public boolean isAccepted(@NotNull Object object) {
        return true;
    }

    @Override
    public @NotNull List<Command> extract(@NotNull Object object) {
        List<Command> commands = new ArrayList<>();

        Class<?> clazz = object.getClass();

        for (Method method : clazz.getMethods()) {
            com.wizardlybump17.wlib.command.annotation.Command annotation = method.getAnnotation(com.wizardlybump17.wlib.command.annotation.Command.class);
            if (annotation == null)
                continue;

            Class<?>[] parameterTypes = method.getParameterTypes();
            Class<?> returnType = method.getReturnType();

            String[] commandParts = annotation.value().split(" ");

            CommandNode<?> root = null;

            if (parameterTypes.length == 0)
                throw new IllegalArgumentException();

            int parameterIndex = parameterTypes.length - 1;
            for (int i = commandParts.length - 1; i >= 0; i--) {
                String part = commandParts[i];

                CommandNode<?> oldRoot = root;
                root = createNode(part, parameterTypes[parameterIndex], root, annotation);
                if (!(root instanceof LiteralCommandNode))
                    parameterIndex--;

                if (oldRoot == null)
                    root = root.withExecutor(createExecutor(object, parameterTypes, returnType, method));
            }

            if (root == null)
                throw new IllegalArgumentException();

            commands.add(new Command((LiteralCommandNode) root));
        }

        return commands;
    }

    private static @NotNull CommandNode<?> createNode(@NotNull String part, @NotNull Class<?> type, @Nullable CommandNode<?> root, @NotNull com.wizardlybump17.wlib.command.annotation.Command annotation) {
        CommandNode<?> newNode;

        if (part.charAt(0) == '<' && part.charAt(part.length() - 1) == '>') {
            if (type == int.class || type == Integer.class) {
                newNode = new IntegerCommandNode(part, root == null ? List.of() : List.of(root), new AllowedNumberInputs.AllowedIntegerInputs.Unlimited());
            } else {
                throw new UnsupportedOperationException();
            }
        } else {
            newNode = new LiteralCommandNode(part, root == null ? List.of() : List.of(root));
        }

        if (!annotation.permission().isEmpty())
            newNode = newNode.withPermission(annotation.permission());

        return newNode;
    }

    public static @NotNull CommandNodeExecutor<?> createExecutor(@NotNull Object object, @NotNull String methodName, @NotNull Class<?> @NotNull ... parameterTypes) {
        try {
            Method method = object.getClass().getMethod(methodName, parameterTypes);
            return createExecutor(object, parameterTypes, method.getReturnType(), method);
        } catch (NoSuchMethodException e) {
            throw new IllegalArgumentException(e);
        }
    }

    private static @NotNull CommandNodeExecutor<?> createExecutor(@NotNull Object object, @NotNull Class<?> @NotNull [] parameterTypes, @NotNull Class<?> returnType, @NotNull Method method) {
        /*
        If empty -> error
        If CommandSender only -> pass only the command sender and check the return type
        If CommandContext only -> pass only the command context and check the return type
        If CommandSender + more parameters -> pass the command sender and the parameters and check the return type
        If CommandContext + more parameters -> pass the command context and the parameters and check the return type
        If only parameters -> pass the parameters and check the return type
        */

        if (parameterTypes.length == 0) //empty -> error
            throw new IllegalArgumentException();

        if (parameterTypes.length == 1) {
            if (parameterTypes[0].isAssignableFrom(CommandSender.class)) { //CommandSender only
                if (returnType.isAssignableFrom(CommandContext.class)) { //CommandContext return type
                    return new AbstractMethodCommandNodeExecutor.CommandSenderCommandContextExecutor<>(object, method);
                } else { //anything else return type
                    return new AbstractMethodCommandNodeExecutor.CommandSenderExecutor<>(object, method);
                }
            } else if (parameterTypes[0].isAssignableFrom(CommandContext.class)) { //CommandContext only
                if (returnType.isAssignableFrom(CommandContext.class)) { //CommandContext return type
                    return new AbstractMethodCommandNodeExecutor.CommandContextCommandContextExecutor<>(object, method);
                } else { //anything else return type
                    return new AbstractMethodCommandNodeExecutor.CommandContextExecutor<>(object, method);
                }
            }
        }

        if (parameterTypes[0].isAssignableFrom(CommandSender.class)) { //CommandSender + more arguments
            if (returnType.isAssignableFrom(CommandContext.class)) { //CommandContext return type
                return new AbstractMethodCommandNodeExecutor.CommandSenderAndArgumentsCommandContextExecutor<>(object, method);
            } else { //anything else return type
                return new AbstractMethodCommandNodeExecutor.CommandSenderAndArgumentsExecutor<>(object, method);
            }
        } else if (parameterTypes[0].isAssignableFrom(CommandContext.class)) { //CommandContext + more arguments
            if (returnType.isAssignableFrom(CommandContext.class)) { //CommandContext return type
                return new AbstractMethodCommandNodeExecutor.CommandContextAndArgumentsCommandContextExecutor<>(object, method);
            } else { //anything else return type
                return new AbstractMethodCommandNodeExecutor.CommandContextAndArgumentsExecutor<>(object, method);
            }
        } else {
            if (returnType.isAssignableFrom(CommandContext.class)) { //CommandContext return type
                return new AbstractMethodCommandNodeExecutor.ArgumentsCommandContextExecutor<>(object, method);
            } else { //anything else return type
                return new AbstractMethodCommandNodeExecutor.ArgumentsExecutor<>(object, method);
            }
        }
    }
}
