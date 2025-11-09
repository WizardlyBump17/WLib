package com.wizardlybump17.wlib.command.extractor.method;

import com.wizardlybump17.wlib.command.executor.CommandNodeExecutor;
import org.jetbrains.annotations.NotNull;

import java.lang.invoke.MethodHandle;
import java.lang.reflect.Method;

public sealed interface MethodCommandNodeExecutor<T> extends CommandNodeExecutor<T> permits AbstractMethodCommandNodeExecutor {

    @NotNull Object object();

    @NotNull MethodHandle methodHandle();

    @NotNull Method method();
}
