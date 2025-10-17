package com.wizardlybump17.wlib.command.rework.node;

import com.wizardlybump17.wlib.command.rework.node.input.LiteralAllowedInput;
import com.wizardlybump17.wlib.command.sender.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class LiteralCommandNode extends CommandNode<String> {

    public static final @NotNull String EMPTY_STRING = "";

    public LiteralCommandNode(@NotNull String name, @NotNull List<CommandNode<?>> children) {
        super(name, children, new LiteralAllowedInput(name, false));
    }

    @Override
    public @NotNull LiteralAllowedInput getAllowedInputs() {
        return (LiteralAllowedInput) super.getAllowedInputs();
    }

    @Override
    public @NotNull ParseResult<String> parse(@NotNull String input) {
        return ParseResult.emptySuccess();
    }

    @Override
    public @NotNull List<String> getSuggestions(@NotNull CommandSender<?> sender, @NotNull List<Object> args, @NotNull String currentInput) {
        return List.of(getName());
    }
}
