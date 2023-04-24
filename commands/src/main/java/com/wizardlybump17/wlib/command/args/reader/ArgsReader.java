package com.wizardlybump17.wlib.command.args.reader;

import org.jetbrains.annotations.Nullable;

/**
 * Used to read and convert the args from string to the specified type
 * @param <T> which type the string will be converted to
 */
public abstract class ArgsReader<T> {

    /**
     * <p>The type that the string will be converted to.</p>
     * <p>If it is {@code null} then you should use the {@link com.wizardlybump17.wlib.command.args.ArgsReaderType} annotation on your parameter</p>
     * @return the type that the string will be converted to
     */
    @Nullable
    public abstract Class<T> getType();

    public abstract T read(String string) throws ArgsReaderException;
}
