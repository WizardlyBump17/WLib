package com.wizardlybump17.wlib.command.extractor.method.factory;

import com.wizardlybump17.wlib.command.annotation.Command;
import com.wizardlybump17.wlib.command.node.CommandNode;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;

public abstract class MethodCommandNodeFactory {

    public abstract @NotNull CommandNode<?> create(@NotNull Object object, @NotNull Method method, @NotNull Command commandAnnotation, @NotNull Parameter parameter, @NotNull String name, @Nullable CommandNode<?> root);
}
