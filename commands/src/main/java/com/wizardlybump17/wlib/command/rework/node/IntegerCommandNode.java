package com.wizardlybump17.wlib.command.rework.node;

import com.wizardlybump17.wlib.command.rework.exception.InputParsingException;
import com.wizardlybump17.wlib.command.rework.executor.CommandNodeExecutor;
import com.wizardlybump17.wlib.command.rework.input.AllowedNumberInputs;
import com.wizardlybump17.wlib.command.rework.input.RangedAllowedInputs;
import com.wizardlybump17.wlib.command.sender.CommandSender;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.stream.IntStream;

public class IntegerCommandNode extends CommandNode<Integer> {

    public IntegerCommandNode(@NotNull String name, @NotNull List<CommandNode<?>> children, @NotNull AllowedNumberInputs.AllowedIntegerInputs allowedInputs, @Nullable CommandNodeExecutor<?> executor, @Nullable String permission) {
        super(name, children, allowedInputs, executor, permission);
    }

    public IntegerCommandNode(@NotNull String name, @NotNull AllowedNumberInputs.AllowedIntegerInputs allowedInputs, @Nullable CommandNodeExecutor<?> executor, @Nullable String permission) {
        this(name, List.of(), allowedInputs, executor, permission);
    }

    public IntegerCommandNode(@NotNull String name, @NotNull List<CommandNode<?>> children, @NotNull AllowedNumberInputs.AllowedIntegerInputs allowedInputs, @Nullable String permission) {
        this(name, children, allowedInputs, null, permission);
    }

    public IntegerCommandNode(@NotNull String name, @NotNull AllowedNumberInputs.AllowedIntegerInputs allowedInputs, @Nullable String permission) {
        this(name, List.of(), allowedInputs, null, permission);
    }

    public IntegerCommandNode(@NotNull String name, @NotNull List<CommandNode<?>> children, @NotNull AllowedNumberInputs.AllowedIntegerInputs allowedInputs, @Nullable CommandNodeExecutor<?> executor) {
        super(name, children, allowedInputs, executor, null);
    }

    public IntegerCommandNode(@NotNull String name, @NotNull AllowedNumberInputs.AllowedIntegerInputs allowedInputs, @Nullable CommandNodeExecutor<?> executor) {
        this(name, List.of(), allowedInputs, executor, null);
    }

    public IntegerCommandNode(@NotNull String name, @NotNull List<CommandNode<?>> children, @NotNull AllowedNumberInputs.AllowedIntegerInputs allowedInputs) {
        this(name, children, allowedInputs, null, null);
    }

    public IntegerCommandNode(@NotNull String name, @NotNull AllowedNumberInputs.AllowedIntegerInputs allowedInputs) {
        this(name, List.of(), allowedInputs, null, null);
    }

    @Override
    public @NotNull AllowedNumberInputs.AllowedIntegerInputs getAllowedInputs() {
        return (AllowedNumberInputs.AllowedIntegerInputs) super.getAllowedInputs();
    }

    @Override
    public @NotNull Integer parse(@NotNull String input) throws InputParsingException {
        try {
            return Integer.parseInt(input);
        } catch (NumberFormatException e) {
            throw new InputParsingException("Could not parse as int: " + input, e);
        }
    }

    @Override
    public @NotNull List<Integer> getSuggestions(@NotNull CommandSender<?> sender, @NotNull List<String> args, @NotNull String currentInput) {
        return switch (getAllowedInputs()) {
            case RangedAllowedInputs<?> ranged -> {
                int from = (int) ranged.from();
                int to = (int) ranged.to();

                if (to - from < 5)
                    yield IntStream.rangeClosed(from, to).boxed().toList();

                int fourth = (to - from) / 4;
                yield List.of(from, from + fourth, from + fourth * 2, from + fourth * 3, to);
            }
            case AllowedNumberInputs.AllowedIntegerInputs.SingleValue singleValue -> List.of(singleValue.value());
            case AllowedNumberInputs.AllowedIntegerInputs.ValuesList valuesList -> {
                List<Integer> list = valuesList.values();
                if (list.size() < 5)
                    yield list;

                int fourth = list.size() / 4;
                yield List.of(
                        list.get(0),
                        list.get(fourth),
                        list.get(fourth * 2),
                        list.get(fourth * 3),
                        list.get(list.size() - 1)
                );
            }
            default -> List.of(-100, -10, 0, 10, 100);
        };
    }
}
