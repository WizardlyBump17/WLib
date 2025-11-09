package com.wizardlybump17.wlib.command.extractor.method;

import com.wizardlybump17.wlib.command.context.CommandContext;
import com.wizardlybump17.wlib.command.result.CommandResult;
import org.jetbrains.annotations.NotNull;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

abstract sealed class AbstractMethodCommandNodeExecutor<T> implements MethodCommandNodeExecutor<T> {

    private final @NotNull Object object;
    private final @NotNull MethodHandle methodHandle;
    private final @NotNull Method method;

    public AbstractMethodCommandNodeExecutor(@NotNull Object object, @NotNull Method method) {
        this.object = object;
        try {
            this.methodHandle = MethodHandles.publicLookup().findVirtual(object.getClass(), method.getName(), MethodType.methodType(method.getReturnType(), method.getParameterTypes()));
        } catch (NoSuchMethodException | IllegalAccessException e) {
            throw new IllegalArgumentException(e);
        }
        this.method = method;
    }

    @Override
    public @NotNull Object object() {
        return object;
    }

    @Override
    public @NotNull MethodHandle methodHandle() {
        return methodHandle;
    }

    @Override
    public @NotNull Method method() {
        return method;
    }

    @Override
    public boolean equals(Object object1) {
        if (object1 == null || getClass() != object1.getClass())
            return false;
        AbstractMethodCommandNodeExecutor<?> that = (AbstractMethodCommandNodeExecutor<?>) object1;
        return Objects.equals(object, that.object)
                && Objects.equals(method, that.method);
    }

    @Override
    public int hashCode() {
        return Objects.hash(object, method);
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "{" +
                "object=" + object +
                ", methodHandle=" + methodHandle +
                ", method=" + method +
                '}';
    }

    static final class CommandSenderCommandContextExecutor<T> extends AbstractMethodCommandNodeExecutor<T> {

        public CommandSenderCommandContextExecutor(@NotNull Object object, @NotNull Method method) {
            super(object, method);
        }

        @SuppressWarnings("unchecked")
        @Override
        public @NotNull CommandResult<T> execute(@NotNull CommandContext context) {
            try {
                List<Object> parameters = new ArrayList<>();
                parameters.add(object());
                parameters.add(context.sender());
                return (CommandResult<T>) methodHandle().invokeWithArguments(parameters);
            } catch (Throwable throwable) {
                return CommandResult.exceptionally(context, throwable);
            }
        }
    }

    static final class CommandSenderExecutor<T> extends AbstractMethodCommandNodeExecutor<T> {

        public CommandSenderExecutor(@NotNull Object object, @NotNull Method method) {
            super(object, method);
        }

        @SuppressWarnings("unchecked")
        @Override
        public @NotNull CommandResult<T> execute(@NotNull CommandContext context) {
            try {
                List<Object> parameters = new ArrayList<>();
                parameters.add(object());
                parameters.add(context.sender());
                return (CommandResult<T>) CommandResult.successful(context, methodHandle().invokeWithArguments(parameters));
            } catch (Throwable throwable) {
                return CommandResult.exceptionally(context, throwable);
            }
        }
    }

    static final class CommandContextCommandContextExecutor<T> extends AbstractMethodCommandNodeExecutor<T> {

        public CommandContextCommandContextExecutor(@NotNull Object object, @NotNull Method method) {
            super(object, method);
        }

        @SuppressWarnings("unchecked")
        @Override
        public @NotNull CommandResult<T> execute(@NotNull CommandContext context) {
            try {
                List<Object> parameters = new ArrayList<>();
                parameters.add(object());
                parameters.add(context);
                return (CommandResult<T>) methodHandle().invokeWithArguments(parameters);
            } catch (Throwable throwable) {
                return CommandResult.exceptionally(context, throwable);
            }
        }
    }

    static final class CommandContextExecutor<T> extends AbstractMethodCommandNodeExecutor<T> {

        public CommandContextExecutor(@NotNull Object object, @NotNull Method method) {
            super(object, method);
        }

        @SuppressWarnings("unchecked")
        @Override
        public @NotNull CommandResult<T> execute(@NotNull CommandContext context) {
            try {
                List<Object> parameters = new ArrayList<>();
                parameters.add(object());
                parameters.add(context);
                return (CommandResult<T>) CommandResult.successful(context, methodHandle().invokeWithArguments(parameters));
            } catch (Throwable throwable) {
                return CommandResult.exceptionally(context, throwable);
            }
        }
    }

    static final class CommandSenderAndArgumentsCommandContextExecutor<T> extends AbstractMethodCommandNodeExecutor<T> {

        public CommandSenderAndArgumentsCommandContextExecutor(@NotNull Object object, @NotNull Method method) {
            super(object, method);
        }

        @SuppressWarnings("unchecked")
        @Override
        public @NotNull CommandResult<T> execute(@NotNull CommandContext context) {
            try {
                List<Object> parameters = new ArrayList<>();
                parameters.add(object());
                parameters.add(context.sender());
                context.arguments().getArguments().forEach((nodeName, argument) -> parameters.add(argument.data()));
                return (CommandResult<T>) methodHandle().invokeWithArguments(parameters);
            } catch (Throwable throwable) {
                return CommandResult.exceptionally(context, throwable);
            }
        }
    }

    static final class CommandSenderAndArgumentsExecutor<T> extends AbstractMethodCommandNodeExecutor<T> {

        public CommandSenderAndArgumentsExecutor(@NotNull Object object, @NotNull Method method) {
            super(object, method);
        }

        @SuppressWarnings("unchecked")
        @Override
        public @NotNull CommandResult<T> execute(@NotNull CommandContext context) {
            try {
                List<Object> parameters = new ArrayList<>();
                parameters.add(object());
                parameters.add(context.sender());
                context.arguments().getArguments().forEach((nodeName, argument) -> parameters.add(argument.data()));
                return (CommandResult<T>) CommandResult.successful(context, methodHandle().invokeWithArguments(parameters));
            } catch (Throwable throwable) {
                return CommandResult.exceptionally(context, throwable);
            }
        }
    }

    static final class CommandContextAndArgumentsCommandContextExecutor<T> extends AbstractMethodCommandNodeExecutor<T> {

        public CommandContextAndArgumentsCommandContextExecutor(@NotNull Object object, @NotNull Method method) {
            super(object, method);
        }

        @SuppressWarnings("unchecked")
        @Override
        public @NotNull CommandResult<T> execute(@NotNull CommandContext context) {
            try {
                List<Object> parameters = new ArrayList<>();
                parameters.add(object());
                parameters.add(context);
                context.arguments().getArguments().forEach((nodeName, argument) -> parameters.add(argument.data()));
                return (CommandResult<T>) methodHandle().invokeWithArguments(parameters);
            } catch (Throwable throwable) {
                return CommandResult.exceptionally(context, throwable);
            }
        }
    }

    static final class CommandContextAndArgumentsExecutor<T> extends AbstractMethodCommandNodeExecutor<T> {

        public CommandContextAndArgumentsExecutor(@NotNull Object object, @NotNull Method method) {
            super(object, method);
        }

        @SuppressWarnings("unchecked")
        @Override
        public @NotNull CommandResult<T> execute(@NotNull CommandContext context) {
            try {
                List<Object> parameters = new ArrayList<>();
                parameters.add(object());
                parameters.add(context);
                context.arguments().getArguments().forEach((nodeName, argument) -> parameters.add(argument.data()));
                return (CommandResult<T>) CommandResult.successful(context, methodHandle().invokeWithArguments(parameters));
            } catch (Throwable throwable) {
                return CommandResult.exceptionally(context, throwable);
            }
        }
    }

    static final class ArgumentsCommandContextExecutor<T> extends AbstractMethodCommandNodeExecutor<T> {

        public ArgumentsCommandContextExecutor(@NotNull Object object, @NotNull Method method) {
            super(object, method);
        }

        @SuppressWarnings("unchecked")
        @Override
        public @NotNull CommandResult<T> execute(@NotNull CommandContext context) {
            try {
                List<Object> parameters = new ArrayList<>();
                parameters.add(object());
                context.arguments().getArguments().forEach((nodeName, argument) -> parameters.add(argument.data()));
                return (CommandResult<T>) methodHandle().invokeWithArguments(parameters);
            } catch (Throwable throwable) {
                return CommandResult.exceptionally(context, throwable);
            }
        }
    }

    static final class ArgumentsExecutor<T> extends AbstractMethodCommandNodeExecutor<T> {

        public ArgumentsExecutor(@NotNull Object object, @NotNull Method method) {
            super(object, method);
        }

        @SuppressWarnings("unchecked")
        @Override
        public @NotNull CommandResult<T> execute(@NotNull CommandContext context) {
            try {
                List<Object> parameters = new ArrayList<>();
                parameters.add(object());
                context.arguments().getArguments().forEach((nodeName, argument) -> parameters.add(argument.data()));
                return (CommandResult<T>) CommandResult.successful(context, methodHandle().invokeWithArguments(parameters));
            } catch (Throwable throwable) {
                return CommandResult.exceptionally(context, throwable);
            }
        }
    }
}
