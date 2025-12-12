package com.wizardlybump17.wlib.command.node.primitive;

import com.wizardlybump17.wlib.command.exception.InputParsingException;
import com.wizardlybump17.wlib.command.executor.CommandNodeExecutor;
import com.wizardlybump17.wlib.command.input.primitive.AllowedCharacterInputs;
import com.wizardlybump17.wlib.command.node.CommandNode;
import com.wizardlybump17.wlib.command.suggestion.primitive.CharacterSuggester;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Unmodifiable;

import java.util.List;

public class CharacterCommandNode extends AbstractPrimitiveCommandNode<Character> {

    public CharacterCommandNode(@NotNull String name, @NotNull @Unmodifiable List<CommandNode<?>> children, @NotNull AllowedCharacterInputs allowedInputs, @Nullable CharacterSuggester suggester, @Nullable CommandNodeExecutor<?> executor, @Nullable String permission) {
        super(name, children, allowedInputs, suggester, executor, permission);
    }

    @Override
    public @NotNull AllowedCharacterInputs getAllowedInputs() {
        return (AllowedCharacterInputs) super.getAllowedInputs();
    }

    @Override
    public @Nullable CharacterSuggester getSuggester() {
        return (CharacterSuggester) super.getSuggester();
    }

    @Override
    public @Nullable Character parse(@NotNull String input) throws InputParsingException {
        if (input.length() != 1)
            throw new InputParsingException("Invalid input length. Expected exactly one char: " + input);
        return input.charAt(0);
    }

    @Override
    public @NotNull CharacterCommandNode withChildren(@NotNull List<CommandNode<?>> children) {
        return new CharacterCommandNode(getName(), children, getAllowedInputs(), getSuggester(), getExecutor(), getPermission());
    }

    @Override
    public @NotNull CharacterCommandNode withExecutor(@Nullable CommandNodeExecutor<?> executor) {
        return new CharacterCommandNode(getName(), getChildren(), getAllowedInputs(), getSuggester(), executor, getPermission());
    }

    @Override
    public @NotNull CharacterCommandNode withPermission(@Nullable String permission) {
        return new CharacterCommandNode(getName(), getChildren(), getAllowedInputs(), getSuggester(), getExecutor(), permission);
    }
}
