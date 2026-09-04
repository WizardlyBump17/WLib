package com.wizardlybump17.wlib.command.extractor.method.factory.string;

import com.wizardlybump17.wlib.command.annotation.Command;
import com.wizardlybump17.wlib.command.annotation.NonNullInput;
import com.wizardlybump17.wlib.command.extractor.method.factory.MethodCommandNodeFactory;
import com.wizardlybump17.wlib.command.input.string.AllowedStringInputs;
import com.wizardlybump17.wlib.command.node.CommandNode;
import com.wizardlybump17.wlib.command.node.string.StringCommandNode;
import com.wizardlybump17.wlib.command.suggestion.string.StringSuggester;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.List;

public class StringMethodCommandNodeFactory extends MethodCommandNodeFactory {

    @Override
    public @NotNull CommandNode<?> create(@NotNull Object object, @NotNull Method method, @NotNull Command commandAnnotation, @NotNull Parameter parameter, @NotNull String name, @Nullable CommandNode<?> root) {
        Class<?> type = parameter.getType();
        if (!isSupported(type))
            throw new IllegalArgumentException("Unsupported type. We accept only Strings: " + type);

        return new StringCommandNode(
                name,
                root == null ? List.of() : List.of(root),
                parameter.isAnnotationPresent(NonNullInput.class) ? AllowedStringInputs.anyNotNull() : AllowedStringInputs.anyNullable(),
                StringSuggester.any(),
                null,
                null
        );
    }

    @Override
    public @NotNull Class<?> @NotNull [] getSupportedTypes() {
        return new Class[] {String.class};
    }
}
