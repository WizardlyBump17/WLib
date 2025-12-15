package com.wizardlybump17.wlib.command.context;

import com.wizardlybump17.wlib.command.Command;
import com.wizardlybump17.wlib.command.node.CommandNode;
import com.wizardlybump17.wlib.command.sender.CommandSender;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Unmodifiable;

import java.util.*;

public record CommandContext(@NotNull Command command, @NotNull CommandSender<?> sender, @NotNull CommandNodeArguments arguments, int lastInputIndex, @NotNull CommandNode<?> lastNode) {

    public record CommandNodeArgument<T>(@NotNull CommandNode<T> node, @NotNull String input, @Nullable T data) {
    }

    public static final class CommandNodeArguments {

        private final @NotNull @Unmodifiable Map<String, CommandNodeArgument<?>> arguments;

        public CommandNodeArguments(@NotNull List<CommandNodeArgument<?>> arguments) {
            Map<String, CommandNodeArgument<?>> argumentsMap = new LinkedHashMap<>();
            for (CommandNodeArgument<?> argument : arguments)
                argumentsMap.put(argument.node().getName(), argument);
            this.arguments = Collections.unmodifiableMap(argumentsMap);
        }

        public boolean hasArgument(@NotNull String key) {
            return arguments.containsKey(key);
        }

        @SuppressWarnings("unchecked")
        public <T> @NotNull Optional<CommandNodeArgument<T>> getArgument(@NotNull String key) {
            return Optional.ofNullable((CommandNodeArgument<T>) arguments.get(key));
        }

        @SuppressWarnings("unchecked")
        public <T> @NotNull Optional<T> getArgumentData(@NotNull String key) {
            return (Optional<T>) getArgument(key).map(CommandNodeArgument::data);
        }

        public @NotNull @Unmodifiable Map<String, CommandNodeArgument<?>> getArguments() {
            return arguments;
        }

        @Override
        public String toString() {
            return "CommandNodeArguments{" +
                    "arguments=" + arguments +
                    '}';
        }

        @Override
        public boolean equals(Object o) {
            if (o == null || getClass() != o.getClass())
                return false;
            CommandNodeArguments arguments1 = (CommandNodeArguments) o;
            return Objects.equals(arguments, arguments1.arguments);
        }

        @Override
        public int hashCode() {
            return Objects.hash(arguments);
        }
    }
}
