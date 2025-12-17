package com.wizardlybump17.wlib.command.node;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonSyntaxException;
import com.wizardlybump17.wlib.command.exception.InputParsingException;
import com.wizardlybump17.wlib.command.executor.CommandNodeExecutor;
import com.wizardlybump17.wlib.command.input.AllowedInputs;
import com.wizardlybump17.wlib.command.suggestion.Suggester;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class JsonElementCommandNode extends CommandNode<JsonElement> {

    private final @NotNull Gson gson;

    public JsonElementCommandNode(@NotNull String name, @NotNull List<CommandNode<?>> children, @NotNull AllowedInputs<JsonElement> allowedInputs, @Nullable Suggester<JsonElement> suggester, @Nullable CommandNodeExecutor<?> executor, @Nullable String permission, @NotNull Gson gson) {
        super(name, children, allowedInputs, suggester, executor, permission);
        this.gson = gson;
    }

    @Override
    public @Nullable JsonElement parse(@NotNull String input) throws InputParsingException {
        try {
            return gson.fromJson(input, JsonElement.class);
        } catch (JsonSyntaxException e) {
            throw new InputParsingException("Could not parse as JsonElement: " + input, e);
        }
    }

    @Override
    public @NotNull CommandNode<JsonElement> withChildren(@NotNull List<CommandNode<?>> children) {
        return new JsonElementCommandNode(getName(), children, getAllowedInputs(), getSuggester(), getExecutor(), getPermission(), gson);
    }

    @Override
    public @NotNull CommandNode<JsonElement> withExecutor(@Nullable CommandNodeExecutor<?> executor) {
        return new JsonElementCommandNode(getName(), getChildren(), getAllowedInputs(), getSuggester(), executor, getPermission(), gson);
    }

    @Override
    public @NotNull CommandNode<JsonElement> withPermission(@Nullable String permission) {
        return new JsonElementCommandNode(getName(), getChildren(), getAllowedInputs(), getSuggester(), getExecutor(), permission, gson);
    }
}
