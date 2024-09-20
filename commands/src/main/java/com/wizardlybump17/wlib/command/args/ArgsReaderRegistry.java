package com.wizardlybump17.wlib.command.args;

import com.wizardlybump17.wlib.command.args.reader.*;
import com.wizardlybump17.wlib.object.Registry;

/**
 * A registry for the {@link ArgsReader}.
 * <p>The key is the {@link ArgsReader#getClass()} and the value is the {@link ArgsReader}</p>
 */
public class ArgsReaderRegistry extends Registry<Class<? extends ArgsReader<?>>, ArgsReader<?>> {

    public static final ArgsReaderRegistry INSTANCE = new ArgsReaderRegistry();

    static {
        INSTANCE.add(new StringReader());
        INSTANCE.add(new ByteReader());
        INSTANCE.add(new ByteArrayReader());
        INSTANCE.add(new ShortReader());
        INSTANCE.add(new ShortArrayReader());
        INSTANCE.add(new IntegerReader());
        INSTANCE.add(new IntegerArrayReader());
        INSTANCE.add(new FloatReader());
        INSTANCE.add(new FloatArrayReader());
        INSTANCE.add(new DoubleReader());
        INSTANCE.add(new DoubleArrayReader());
        INSTANCE.add(new BigIntegerArgsReader());
        INSTANCE.add(new BigDecimalArgsReader());
        INSTANCE.add(new UUIDReader());
        INSTANCE.add(new BooleanReader());
        INSTANCE.add(new DateReader());
    }

    private ArgsReaderRegistry() {
    }

    @SuppressWarnings("unchecked")
    public void add(ArgsReader<?> reader) {
        put((Class<? extends ArgsReader<?>>) reader.getClass(), reader);
    }

    /**
     * Gets the first reader that can read the specified type
     * @param type the type
     * @return the reader
     */
    public ArgsReader<?> getReader(Class<?> type) {
        for (ArgsReader<?> reader : getMap().values())
            if (reader.getTypes().contains(type))
                return reader;
        return null;
    }
}
