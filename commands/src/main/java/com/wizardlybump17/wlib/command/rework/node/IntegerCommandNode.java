package com.wizardlybump17.wlib.command.rework.node;

import com.wizardlybump17.wlib.command.rework.node.input.AllowedNumberInputs;
import com.wizardlybump17.wlib.command.rework.node.input.RangedAllowedInputs;
import com.wizardlybump17.wlib.command.sender.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;

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

    @Override
    public @NotNull List<Integer> getSuggestions(@NotNull CommandSender<?> sender, @NotNull List<Object> args, @NotNull String currentInput) {
        if (!currentInput.isEmpty())
            return List.of();

        AllowedNumberInputs.AllowedIntegerInputs allowedInputs = getAllowedInputs();

        if (allowedInputs instanceof RangedAllowedInputs<?> ranged) {
            int from = (int) ranged.from();
            int to = (int) ranged.to();

            if (to - from < 5)
                return IntStream.rangeClosed(from, to).boxed().toList();

            int fourth = (to - from) / 4;
            return List.of(from, from + fourth, from + fourth * 2, from + fourth * 3, to);
        }

        return List.of(-100, -10, 0, 10, 100);
    }
}
