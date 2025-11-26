package com.wizardlybump17.wlib.command.extractor.method.factory.primitive;

import com.wizardlybump17.wlib.command.annotation.Command;
import com.wizardlybump17.wlib.command.input.primitive.number.*;
import com.wizardlybump17.wlib.command.node.CommandNode;
import com.wizardlybump17.wlib.command.node.primitive.number.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.List;

public class NumberMethodCommandNodeFactory extends PrimitiveMethodCommandNodeFactory {

    @Override
    public @NotNull NumberCommandNode<?> create(@NotNull Object object, @NotNull Method method, @NotNull Command commandAnnotation, @NotNull Parameter parameter, @NotNull String name, @Nullable CommandNode<?> root) {
        Class<?> type = parameter.getType();

        List<CommandNode<?>> children = root == null ? List.of() : List.of(root);

        if (type == byte.class || type == Byte.class)
            return new ByteCommandNode(name, children, AllowedByteInputs.unlimited());
        if (type == short.class || type == Short.class)
            return new ShortCommandNode(name, children, AllowedShortInputs.unlimited());
        if (type == int.class || type == Integer.class)
            return new IntegerCommandNode(name, children, AllowedIntegerInputs.unlimited());
        if (type == long.class || type == Long.class)
            return new LongCommandNode(name, children, AllowedLongInputs.unlimited());
        if (type == float.class || type == Float.class)
            return new FloatCommandNode(name, children, AllowedFloatInputs.unlimited());
        if (type == double.class || type == Double.class)
            return new DoubleCommandNode(name, children, AllowedDoubleInputs.unlimited());

        throw new IllegalArgumentException("Unsupported type. We accept only primitive numbers: " + type);
    }

    @Override
    public @NotNull Class<?> @NotNull [] getSupportedTypes() {
        return new Class<?>[] {
                byte.class, Byte.class,
                short.class, Short.class,
                int.class, Integer.class,
                long.class, Long.class,
                float.class, Float.class,
                double.class, Double.class
        };
    }
}
