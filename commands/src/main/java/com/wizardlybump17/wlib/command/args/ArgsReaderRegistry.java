package com.wizardlybump17.wlib.command.args;

import com.wizardlybump17.wlib.command.args.reader.*;

import java.util.HashMap;
import java.util.Map;

public class ArgsReaderRegistry {

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
    }

    private ArgsReaderRegistry() {
    }

    private final Map<Class<?>, ArgsReader<?>> readers = new HashMap<>();

    public void add(ArgsReader<?> reader) {
       readers.put(reader.getType(), reader);
    }

    public void remove(Class<?> type) {
        readers.remove(type);
    }

    public boolean has(Class<?> type) {
        return readers.containsKey(type);
    }

    public ArgsReader<?> get(Class<?> type) {
        return readers.get(type);
    }
}
