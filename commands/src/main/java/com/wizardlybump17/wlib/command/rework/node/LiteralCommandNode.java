package com.wizardlybump17.wlib.command.rework.node;

import com.wizardlybump17.wlib.command.rework.executor.CommandExecutor;
import com.wizardlybump17.wlib.command.rework.input.LiteralAllowedInput;
import com.wizardlybump17.wlib.command.sender.CommandSender;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class LiteralCommandNode extends CommandNode<String> {

    public LiteralCommandNode(@NotNull String name, @NotNull List<CommandNode<?>> children, @Nullable CommandExecutor<String> executor) {
        super(name, children, new LiteralAllowedInput(name, false), executor);
    }

    public LiteralCommandNode(@NotNull String name, @NotNull List<CommandNode<?>> children) {
        super(name, children, new LiteralAllowedInput(name, false));
    }

    @Override
    public @NotNull LiteralAllowedInput getAllowedInputs() {
        return (LiteralAllowedInput) super.getAllowedInputs();
    }

    @Override
    public @NotNull String parse(@NotNull String input) {
        return input;
    }

    @Override
    public @NotNull List<String> getSuggestions(@NotNull CommandSender<?> sender, @NotNull List<Object> args, @NotNull String currentInput) {
        return List.of(getName());
    }
}
