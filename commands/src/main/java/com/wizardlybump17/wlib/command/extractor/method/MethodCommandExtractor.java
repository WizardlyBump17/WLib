package com.wizardlybump17.wlib.command.extractor.method;

import com.wizardlybump17.wlib.command.Command;
import com.wizardlybump17.wlib.command.context.CommandContext;
import com.wizardlybump17.wlib.command.exception.extractor.method.MethodCommandNodeFactoryNotFoundException;
import com.wizardlybump17.wlib.command.executor.CommandNodeExecutor;
import com.wizardlybump17.wlib.command.extractor.CommandExtractor;
import com.wizardlybump17.wlib.command.extractor.method.executor.AbstractMethodCommandNodeExecutor;
import com.wizardlybump17.wlib.command.extractor.method.factory.MethodCommandNodeFactory;
import com.wizardlybump17.wlib.command.node.CommandNode;
import com.wizardlybump17.wlib.command.node.LiteralCommandNode;
import com.wizardlybump17.wlib.command.registry.MethodCommandNodeFactoryRegistry;
import com.wizardlybump17.wlib.command.result.CommandResult;
import com.wizardlybump17.wlib.command.sender.CommandSender;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.List;

public class MethodCommandExtractor implements CommandExtractor {

    private final @NotNull MethodCommandNodeFactoryRegistry factoryRegistry;

    public MethodCommandExtractor(@NotNull MethodCommandNodeFactoryRegistry factoryRegistry) {
        this.factoryRegistry = factoryRegistry;
    }

    @Override
    public boolean isAccepted(@NotNull Object object) {
        return true;
    }

    public @NotNull MethodCommandNodeFactoryRegistry getFactoryRegistry() {
        return factoryRegistry;
    }

    @Override
    public @NotNull List<Command> extract(@NotNull Object object) throws MethodCommandNodeFactoryNotFoundException {
        List<Command> commands = new ArrayList<>();

        Class<?> clazz = object.getClass();

        for (Method method : clazz.getMethods()) {
            com.wizardlybump17.wlib.command.annotation.Command annotation = method.getAnnotation(com.wizardlybump17.wlib.command.annotation.Command.class);
            if (annotation == null)
                continue;

            Parameter[] parameters = method.getParameters();
            Class<?> returnType = method.getReturnType();

            String[] commandParts = annotation.value().split(" ");

            CommandNode<?> root = null;

            int parameterIndex = parameters.length - 1;
            for (int i = commandParts.length - 1; i >= 0; i--) {
                String part = commandParts[i];

                CommandNode<?> oldRoot = root;
                root = createNode(factoryRegistry, part, parameters, parameterIndex, root, annotation, object, method);
                if (!(root instanceof LiteralCommandNode))
                    parameterIndex--;

                if (oldRoot == null)
                    root = root.withExecutor(createExecutor(object, parameters, returnType, method));
            }

            if (root == null)
                throw new IllegalArgumentException();

            commands.add(new Command((LiteralCommandNode) root));
        }

        return commands;
    }

    private static @NotNull CommandNode<?> createNode(@NotNull MethodCommandNodeFactoryRegistry factoryRegistry, @NotNull String part, @NotNull Parameter @NotNull [] parameters, int parameterIndex, @Nullable CommandNode<?> root, @NotNull com.wizardlybump17.wlib.command.annotation.Command annotation, @NotNull Object object, @NotNull Method method) throws MethodCommandNodeFactoryNotFoundException {
        CommandNode<?> newNode;

        boolean argument = part.charAt(0) == '<' && part.charAt(part.length() - 1) == '>';
        if (argument)
            part = part.substring(1, part.length() - 1);

        if (parameterIndex < 0) {
            newNode = new LiteralCommandNode(part, root == null ? List.of() : List.of(root));
        } else {
            if (argument) {
                Parameter parameter = parameters[parameterIndex];
                Class<?> parameterType = parameter.getType();

                MethodCommandNodeFactory factory = factoryRegistry.getFactory(parameterType);
                if (factory == null)
                    throw new MethodCommandNodeFactoryNotFoundException("MethodCommandNodeFactory not found for the parameter " + parameter);

                newNode = factory.create(object, method, annotation, parameter, part, root);
            } else {
                newNode = new LiteralCommandNode(part, root == null ? List.of() : List.of(root));
            }
        }

        if (!annotation.permission().isEmpty())
            newNode = newNode.withPermission(annotation.permission());

        return newNode;
    }

    public static @NotNull CommandNodeExecutor<?> createExecutor(@NotNull Object object, @NotNull String methodName, @NotNull Class<?> @NotNull ... parameterTypes) {
        try {
            Method method = object.getClass().getMethod(methodName, parameterTypes);
            return createExecutor(object, method.getParameters(), method.getReturnType(), method);
        } catch (NoSuchMethodException e) {
            throw new IllegalArgumentException(e);
        }
    }

    private static @NotNull CommandNodeExecutor<?> createExecutor(@NotNull Object object, @NotNull Parameter @NotNull [] parameters, @NotNull Class<?> returnType, @NotNull Method method) {
        /*
        If empty -> check the return type
        If CommandSender only -> pass only the command sender and check the return type
        If CommandContext only -> pass only the command context and check the return type
        If CommandSender + more parameters -> pass the command sender and the parameters and check the return type
        If CommandContext + more parameters -> pass the command context and the parameters and check the return type
        If only parameters -> pass the parameters and check the return type
        */

        if (parameters.length == 0) { //no parameters
            if (returnType.isAssignableFrom(CommandResult.class)) { //CommandResult return type
                return new AbstractMethodCommandNodeExecutor.NoArgumentsCommandResultExecutor<>(object, method);
            } else {
                return new AbstractMethodCommandNodeExecutor.NoArgumentsExecutor<>(object, method);
            }
        }

        Parameter firstParameter = parameters[0];
        Class<?> firstParameterType = firstParameter.getType();

        if (parameters.length == 1) {
            if (firstParameterType.isAssignableFrom(CommandSender.class)) { //CommandSender only
                if (returnType.isAssignableFrom(CommandResult.class)) { //CommandResult return type
                    return new AbstractMethodCommandNodeExecutor.CommandSenderCommandResultExecutor<>(object, method);
                } else { //anything else return type
                    return new AbstractMethodCommandNodeExecutor.CommandSenderExecutor<>(object, method);
                }
            } else if (firstParameterType.isAssignableFrom(CommandContext.class)) { //CommandContext only
                if (returnType.isAssignableFrom(CommandResult.class)) { //CommandResult return type
                    return new AbstractMethodCommandNodeExecutor.CommandContextCommandResultExecutor<>(object, method);
                } else { //anything else return type
                    return new AbstractMethodCommandNodeExecutor.CommandContextExecutor<>(object, method);
                }
            }
        }

        if (firstParameterType.isAssignableFrom(CommandSender.class)) { //CommandSender + more arguments
            if (returnType.isAssignableFrom(CommandResult.class)) { //CommandResult return type
                return new AbstractMethodCommandNodeExecutor.CommandSenderAndArgumentsCommandResultExecutor<>(object, method);
            } else { //anything else return type
                return new AbstractMethodCommandNodeExecutor.CommandSenderAndArgumentsExecutor<>(object, method);
            }
        } else if (firstParameterType.isAssignableFrom(CommandContext.class)) { //CommandContext + more arguments
            if (returnType.isAssignableFrom(CommandResult.class)) { //CommandResult return type
                return new AbstractMethodCommandNodeExecutor.CommandContextAndArgumentsCommandResultExecutor<>(object, method);
            } else { //anything else return type
                return new AbstractMethodCommandNodeExecutor.CommandContextAndArgumentsExecutor<>(object, method);
            }
        } else {
            if (returnType.isAssignableFrom(CommandResult.class)) { //CommandResult return type
                return new AbstractMethodCommandNodeExecutor.ArgumentsCommandResultExecutor<>(object, method);
            } else { //anything else return type
                return new AbstractMethodCommandNodeExecutor.ArgumentsExecutor<>(object, method);
            }
        }
    }
}
