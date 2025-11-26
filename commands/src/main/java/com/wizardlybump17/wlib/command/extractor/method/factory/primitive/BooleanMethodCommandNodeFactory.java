package com.wizardlybump17.wlib.command.extractor.method.factory.primitive;

import com.wizardlybump17.wlib.command.annotation.Command;
import com.wizardlybump17.wlib.command.input.primitive.AllowedBooleanInputs;
import com.wizardlybump17.wlib.command.node.CommandNode;
import com.wizardlybump17.wlib.command.node.primitive.BooleanCommandNode;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.List;

public class BooleanMethodCommandNodeFactory extends PrimitiveMethodCommandNodeFactory {

    @Override
    public @NotNull BooleanCommandNode create(@NotNull Object object, @NotNull Method method, @NotNull Command commandAnnotation, @NotNull Parameter parameter, @NotNull String name, @Nullable CommandNode<?> root) {
        Class<?> type = parameter.getType();
        if (isSupported(type))
            throw new IllegalArgumentException("Unsupported type. We only accept booleans: " + type);

        return new BooleanCommandNode(
                name,
                root == null ? List.of() : List.of(root),
                AllowedBooleanInputs.anyNotNull(),
                null,
                null
        );
    }

    @Override
    public @NotNull Class<?> @NotNull [] getSupportedTypes() {
        return new Class[] {boolean.class, Boolean.class};
    }
}
