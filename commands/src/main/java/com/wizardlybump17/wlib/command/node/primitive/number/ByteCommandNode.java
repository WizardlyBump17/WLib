package com.wizardlybump17.wlib.command.node.primitive.number;

import com.wizardlybump17.wlib.command.exception.InputParsingException;
import com.wizardlybump17.wlib.command.executor.CommandNodeExecutor;
import com.wizardlybump17.wlib.command.input.AllowedInputs;
import com.wizardlybump17.wlib.command.node.CommandNode;
import com.wizardlybump17.wlib.command.node.primitive.PrimitiveCommandNode;
import com.wizardlybump17.wlib.command.suggestion.Suggester;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class ByteCommandNode extends NumberCommandNode<Byte> implements PrimitiveCommandNode {

    public ByteCommandNode(@NotNull String name, @NotNull List<CommandNode<?>> children, @NotNull AllowedInputs<Byte> allowedInputs, @Nullable Suggester<Byte> suggester, @Nullable CommandNodeExecutor<?> executor, @Nullable String permission) {
        super(name, children, allowedInputs, suggester, executor, permission);
    }

    @Override
    public @NotNull Byte parse(@NotNull String input) throws InputParsingException {
        try {
            return Byte.parseByte(input);
        } catch (NumberFormatException e) {
            throw new InputParsingException("Could not parse as byte: " + input, e);
        }
    }

    @Override
    public @NotNull ByteCommandNode withChildren(@NotNull List<CommandNode<?>> children) {
        return new ByteCommandNode(getName(), children, getAllowedInputs(), getSuggester(), getExecutor(), getPermission());
    }

    @Override
    public @NotNull ByteCommandNode withExecutor(@Nullable CommandNodeExecutor<?> executor) {
        return new ByteCommandNode(getName(), getChildren(), getAllowedInputs(), getSuggester(), executor, getPermission());
    }

    @Override
    public @NotNull ByteCommandNode withPermission(@Nullable String permission) {
        return new ByteCommandNode(getName(), getChildren(), getAllowedInputs(), getSuggester(), getExecutor(), permission);
    }
}
