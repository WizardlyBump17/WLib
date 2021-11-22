package com.wizardlybump17.wlib.command.args.reader;

/**
 * Used to read and convert the args from string to the specified type
 * @param <T> which type the string will be converted to
 */
public abstract class ArgsReader<T> {

    public abstract Class<T> getType();
    public abstract T read(String string) throws ArgsReaderException;

    public boolean isArray() {
        return getType().isArray();
    }
}
