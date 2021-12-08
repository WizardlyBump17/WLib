package com.wizardlybump17.wlib.command.args;

import com.wizardlybump17.wlib.command.args.reader.*;
import com.wizardlybump17.wlib.object.Registry;

public class ArgsReaderRegistry extends Registry<Class<?>, ArgsReader<?>> {

    public static final ArgsReaderRegistry INSTANCE = new ArgsReaderRegistry();

    static {
        INSTANCE.add(new StringReader());
        INSTANCE.add(new StringArrayReader());
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
        INSTANCE.add(new UUIDReader());
        INSTANCE.add(new BooleanReader());
    }

    private ArgsReaderRegistry() {
    }

    public void add(ArgsReader<?> reader) {
        put(reader.getType(), reader);
    }
}
