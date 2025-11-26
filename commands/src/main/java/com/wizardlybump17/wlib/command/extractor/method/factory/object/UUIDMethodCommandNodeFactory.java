package com.wizardlybump17.wlib.command.extractor.method.factory.object;

import com.wizardlybump17.wlib.command.annotation.Command;
import com.wizardlybump17.wlib.command.extractor.method.factory.MethodCommandNodeFactory;
import com.wizardlybump17.wlib.command.input.object.AllowedUUIDInputs;
import com.wizardlybump17.wlib.command.node.CommandNode;
import com.wizardlybump17.wlib.command.node.object.UUIDCommandNode;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.List;
import java.util.UUID;

public class UUIDMethodCommandNodeFactory extends MethodCommandNodeFactory {

    @Override
    public @NotNull CommandNode<?> create(@NotNull Object object, @NotNull Method method, @NotNull Command commandAnnotation, @NotNull Parameter parameter, @NotNull String name, @Nullable CommandNode<?> root) {
        if (!isSupported(parameter.getType()))
            throw new IllegalArgumentException("Unsupported type. We only accept UUIDs: " + parameter);

        return new UUIDCommandNode(
                name,
                root == null ? List.of() : List.of(root),
                AllowedUUIDInputs.anyNotNull(),
                null,
                null
        );
    }

    @Override
    public @NotNull Class<?> @NotNull [] getSupportedTypes() {
        return new Class[] {UUID.class};
    }
}
