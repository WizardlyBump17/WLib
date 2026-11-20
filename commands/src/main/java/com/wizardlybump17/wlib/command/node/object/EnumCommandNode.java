package com.wizardlybump17.wlib.command.node.object;

import com.wizardlybump17.wlib.command.exception.InputParsingException;
import com.wizardlybump17.wlib.command.executor.CommandNodeExecutor;
import com.wizardlybump17.wlib.command.input.AllowedInputs;
import com.wizardlybump17.wlib.command.node.CommandNode;
import com.wizardlybump17.wlib.command.suggestion.Suggester;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class EnumCommandNode<E extends Enum<E>> extends CommandNode<E> {

    private final @NotNull Class<E> enumType;

    public EnumCommandNode(@NotNull String name, @NotNull List<CommandNode<?>> children, @NotNull AllowedInputs<E> allowedInputs, @Nullable Suggester<E> suggester, @Nullable CommandNodeExecutor<?> executor, @Nullable String permission, @NotNull Class<E> enumType) {
        super(name, children, allowedInputs, suggester, executor, permission);
        this.enumType = enumType;
    }

    @Override
    public @Nullable E parse(@NotNull String input) throws InputParsingException {
        try {
            return Enum.valueOf(enumType, input.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new InputParsingException("Could not parse as " + enumType.getSimpleName() + ": " + input, e);
        }
    }

    @Override
    public @NotNull CommandNode<E> withChildren(@NotNull List<CommandNode<?>> children) {
        return new EnumCommandNode<>(getName(), children, getAllowedInputs(), getSuggester(), getExecutor(), getPermission(), enumType);
    }

    @Override
    public @NotNull CommandNode<E> withExecutor(@Nullable CommandNodeExecutor<?> executor) {
        return new EnumCommandNode<>(getName(), getChildren(), getAllowedInputs(), getSuggester(), executor, getPermission(), enumType);
    }

    @Override
    public @NotNull CommandNode<E> withPermission(@Nullable String permission) {
        return new EnumCommandNode<>(getName(), getChildren(), getAllowedInputs(), getSuggester(), getExecutor(), permission, enumType);
    }

    public @NotNull Class<E> getEnumType() {
        return enumType;
    }
}
