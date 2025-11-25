package com.wizardlybump17.wlib.command.node.primitive;

import com.wizardlybump17.wlib.command.exception.InputParsingException;
import com.wizardlybump17.wlib.command.executor.CommandNodeExecutor;
import com.wizardlybump17.wlib.command.input.primitive.AllowedCharacterInputs;
import com.wizardlybump17.wlib.command.node.CommandNode;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class CharacterCommandNode extends AbstractPrimitiveCommandNode<Character> {

    public CharacterCommandNode(@NotNull String name, @NotNull List<CommandNode<?>> children, @NotNull AllowedCharacterInputs allowedInputs, @Nullable CommandNodeExecutor<?> executor, @Nullable String permission) {
        super(name, children, allowedInputs, executor, permission);
    }

    public CharacterCommandNode(@NotNull String name, @NotNull AllowedCharacterInputs allowedInputs, @Nullable CommandNodeExecutor<?> executor, @Nullable String permission) {
        super(name, allowedInputs, executor, permission);
    }

    public CharacterCommandNode(@NotNull String name, @NotNull List<CommandNode<?>> children, @NotNull AllowedCharacterInputs allowedInputs, @Nullable String permission) {
        super(name, children, allowedInputs, permission);
    }

    public CharacterCommandNode(@NotNull String name, @NotNull AllowedCharacterInputs allowedInputs, @Nullable String permission) {
        super(name, allowedInputs, permission);
    }

    public CharacterCommandNode(@NotNull String name, @NotNull AllowedCharacterInputs allowedInputs, @Nullable CommandNodeExecutor<?> executor) {
        super(name, allowedInputs, executor);
    }

    public CharacterCommandNode(@NotNull String name, @NotNull List<CommandNode<?>> children, @NotNull AllowedCharacterInputs allowedInputs) {
        super(name, children, allowedInputs);
    }

    public CharacterCommandNode(@NotNull String name, @NotNull AllowedCharacterInputs allowedInputs) {
        super(name, allowedInputs);
    }

    @Override
    public @NotNull AllowedCharacterInputs getAllowedInputs() {
        return (AllowedCharacterInputs) super.getAllowedInputs();
    }

    @Override
    public @Nullable Character parse(@NotNull String input) throws InputParsingException {
        if (input.length() != 1)
            throw new InputParsingException("Invalid input length. Expected exactly one char: " + input);
        return input.charAt(0);
    }

    @Override
    public @NotNull CharacterCommandNode withChildren(@NotNull List<CommandNode<?>> children) {
        return new CharacterCommandNode(getName(), children, getAllowedInputs(), getExecutor(), getPermission());
    }

    @Override
    public @NotNull CharacterCommandNode withExecutor(@Nullable CommandNodeExecutor<?> executor) {
        return new CharacterCommandNode(getName(), getChildren(), getAllowedInputs(), executor, getPermission());
    }

    @Override
    public @NotNull CharacterCommandNode withPermission(@Nullable String permission) {
        return new CharacterCommandNode(getName(), getChildren(), getAllowedInputs(), getExecutor(), permission);
    }
}
