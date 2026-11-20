package com.wizardlybump17.wlib.command.extractor.method.factory.object;

import com.wizardlybump17.wlib.command.annotation.Command;
import com.wizardlybump17.wlib.command.extractor.method.factory.MethodCommandNodeFactory;
import com.wizardlybump17.wlib.command.input.AllowedInputs;
import com.wizardlybump17.wlib.command.node.CommandNode;
import com.wizardlybump17.wlib.command.node.object.EnumCommandNode;
import com.wizardlybump17.wlib.command.suggestion.object.EnumSuggester;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.List;

public class EnumMethodCommandNodeFactory extends MethodCommandNodeFactory {

    @SuppressWarnings("unchecked")
    @Override
    public @NotNull EnumCommandNode<?> create(@NotNull Object object, @NotNull Method method, @NotNull Command commandAnnotation, @NotNull Parameter parameter, @NotNull String name, @Nullable CommandNode<?> root) {
        Class<Enum> type = (Class<Enum>) parameter.getType();

        return new EnumCommandNode<>(
                name,
                root == null ? List.of() : List.of(root),
                AllowedInputs.anyNotNull(),
                EnumSuggester.any(type),
                null,
                null,
                type
        );
    }

    @Override
    public @NotNull Class<?> @NotNull [] getSupportedTypes() {
        return new Class[] {Enum.class};
    }

    @Override
    public boolean isStrict() {
        return false;
    }
}
