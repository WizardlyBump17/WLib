package com.wizardlybump17.wlib.command.extractor.method.factory.object;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.wizardlybump17.wlib.command.annotation.Command;
import com.wizardlybump17.wlib.command.annotation.NonNullInput;
import com.wizardlybump17.wlib.command.extractor.method.factory.MethodCommandNodeFactory;
import com.wizardlybump17.wlib.command.input.AllowedInputs;
import com.wizardlybump17.wlib.command.node.CommandNode;
import com.wizardlybump17.wlib.command.node.object.JsonElementCommandNode;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.List;

public class JsonElementMethodCommandNodeFactory extends MethodCommandNodeFactory {

    private final @NotNull Gson gson;

    public JsonElementMethodCommandNodeFactory(@NotNull Gson gson) {
        this.gson = gson;
    }

    @Override
    public @NotNull JsonElementCommandNode create(@NotNull Object object, @NotNull Method method, @NotNull Command commandAnnotation, @NotNull Parameter parameter, @NotNull String name, @Nullable CommandNode<?> root) {
        return new JsonElementCommandNode(
                name,
                root == null ? List.of() : List.of(root),
                parameter.isAnnotationPresent(NonNullInput.class) ? AllowedInputs.anyNotNull() : AllowedInputs.anyNullable(),
                null,
                null,
                null,
                gson
        );
    }

    @Override
    public @NotNull Class<?> @NotNull [] getSupportedTypes() {
        return new Class[] {JsonElement.class};
    }

    @Override
    public boolean isStrict() {
        return false;
    }

    public @NotNull Gson getGson() {
        return gson;
    }
}
