package com.wizardlybump17.wlib.command.extractor.method.factory.primitive;

import com.wizardlybump17.wlib.command.annotation.Command;
import com.wizardlybump17.wlib.command.input.primitive.AllowedCharacterInputs;
import com.wizardlybump17.wlib.command.node.CommandNode;
import com.wizardlybump17.wlib.command.node.primitive.AbstractPrimitiveCommandNode;
import com.wizardlybump17.wlib.command.node.primitive.CharacterCommandNode;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.List;

public class CharacterMethodCommandNodeFactory extends PrimitiveMethodCommandNodeFactory {

    @Override
    public @NotNull AbstractPrimitiveCommandNode<?> create(@NotNull Object object, @NotNull Method method, @NotNull Command commandAnnotation, @NotNull Parameter parameter, @NotNull String name, @Nullable CommandNode<?> root) {
        Class<?> type = parameter.getType();

        if (type != char.class && type != Character.class)
            throw new IllegalArgumentException("Unsupported type. We only accept chars: " + type);

        return new CharacterCommandNode(
                name,
                root == null ? List.of() : List.of(root),
                AllowedCharacterInputs.anyNotNull(),
                null,
                null
        );
    }

    @Override
    public @NotNull Class<?> @NotNull [] getSupportedTypes() {
        return new Class<?>[] {char.class, Character.class};
    }
}
