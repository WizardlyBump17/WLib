package com.wizardlybump17.wlib.command.rework.node;

import com.wizardlybump17.wlib.command.rework.node.input.AllowedNumberInputs;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Optional;

public class IntegerCommandNode extends CommandNode<Integer> {

    public IntegerCommandNode(@NotNull String name, @NotNull List<CommandNode<?>> children, @NotNull AllowedNumberInputs.AllowedIntegerInputs allowedInputs) {
        super(name, children, allowedInputs);
    }

    @Override
    public @NotNull AllowedNumberInputs.AllowedIntegerInputs getAllowedInputs() {
        return (AllowedNumberInputs.AllowedIntegerInputs) super.getAllowedInputs();
    }

    @Override
    public @NotNull Optional<Integer> parse(@NotNull String input) {
        try {
            int integer = Integer.parseInt(input);
            if (getAllowedInputs().isAllowed(integer))
                return Optional.of(integer);
            return Optional.empty();
        } catch (NumberFormatException e) {
            return Optional.empty();
        }
    }
}
