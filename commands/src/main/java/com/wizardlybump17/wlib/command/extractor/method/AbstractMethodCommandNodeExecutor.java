package com.wizardlybump17.wlib.command.extractor.method;

import com.wizardlybump17.wlib.command.context.CommandContext;
import com.wizardlybump17.wlib.command.result.CommandResult;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@ApiStatus.Internal
public abstract sealed class AbstractMethodCommandNodeExecutor<T> implements MethodCommandNodeExecutor<T> {

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

    @ApiStatus.Internal
    public static final class CommandSenderCommandResultExecutor<T> extends AbstractMethodCommandNodeExecutor<T> {

        public CommandSenderCommandResultExecutor(@NotNull Object object, @NotNull Method method) {
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

    @ApiStatus.Internal
    public static final class CommandSenderExecutor<T> extends AbstractMethodCommandNodeExecutor<T> {

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

    @ApiStatus.Internal
    public static final class CommandContextCommandResultExecutor<T> extends AbstractMethodCommandNodeExecutor<T> {

        public CommandContextCommandResultExecutor(@NotNull Object object, @NotNull Method method) {
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

    @ApiStatus.Internal
    public static final class CommandContextExecutor<T> extends AbstractMethodCommandNodeExecutor<T> {

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

    @ApiStatus.Internal
    public static final class CommandSenderAndArgumentsCommandResultExecutor<T> extends AbstractMethodCommandNodeExecutor<T> {

        public CommandSenderAndArgumentsCommandResultExecutor(@NotNull Object object, @NotNull Method method) {
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

    @ApiStatus.Internal
    public static final class CommandSenderAndArgumentsExecutor<T> extends AbstractMethodCommandNodeExecutor<T> {

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

    @ApiStatus.Internal
    public static final class CommandContextAndArgumentsCommandResultExecutor<T> extends AbstractMethodCommandNodeExecutor<T> {

        public CommandContextAndArgumentsCommandResultExecutor(@NotNull Object object, @NotNull Method method) {
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

    @ApiStatus.Internal
    public static final class CommandContextAndArgumentsExecutor<T> extends AbstractMethodCommandNodeExecutor<T> {

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

    @ApiStatus.Internal
    public static final class ArgumentsCommandResultExecutor<T> extends AbstractMethodCommandNodeExecutor<T> {

        public ArgumentsCommandResultExecutor(@NotNull Object object, @NotNull Method method) {
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

    @ApiStatus.Internal
    public static final class ArgumentsExecutor<T> extends AbstractMethodCommandNodeExecutor<T> {

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

    @ApiStatus.Internal
    public static final class NoArgumentsExecutor<T> extends AbstractMethodCommandNodeExecutor<T> {

        public NoArgumentsExecutor(@NotNull Object object, @NotNull Method method) {
            super(object, method);
        }

        @SuppressWarnings("unchecked")
        @Override
        public @NotNull CommandResult<T> execute(@NotNull CommandContext context) {
            try {
                List<Object> parameters = new ArrayList<>();
                parameters.add(object());
                return (CommandResult<T>) CommandResult.successful(context, methodHandle().invokeWithArguments(parameters));
            } catch (Throwable throwable) {
                return CommandResult.exceptionally(context, throwable);
            }
        }
    }

    @ApiStatus.Internal
    public static final class NoArgumentsCommandResultExecutor<T> extends AbstractMethodCommandNodeExecutor<T> {

        public NoArgumentsCommandResultExecutor(@NotNull Object object, @NotNull Method method) {
            super(object, method);
        }

        @SuppressWarnings("unchecked")
        @Override
        public @NotNull CommandResult<T> execute(@NotNull CommandContext context) {
            try {
                List<Object> parameters = new ArrayList<>();
                parameters.add(object());
                return (CommandResult<T>) methodHandle().invokeWithArguments(parameters);
            } catch (Throwable throwable) {
                return CommandResult.exceptionally(context, throwable);
            }
        }
    }
}
