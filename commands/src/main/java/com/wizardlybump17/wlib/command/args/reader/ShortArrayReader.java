package com.wizardlybump17.wlib.command.args.reader;

import java.util.Arrays;

public class ShortArrayReader extends ArgsReader<Short[]> {

    @Override
    public Class<Short[]> getType() {
        return Short[].class;
    }

    @Override
    public Short[] read(String string) throws ArgsReaderException {
        try {
            final String[] strings = string.split(" ");
            Short[] result = new Short[strings.length];
            for (int i = 0; i < result.length; i++)
                result[i] = Short.parseShort(strings[i]);
            return result;
        } catch (NumberFormatException e) {
            throw new ArgsReaderException("expected a short array in string form but got " + string);
        }
    }

    @Override
    public Short[] cast(Object[] original) {
        if (original.length == 0)
            return new Short[0];
        final Object o = original[0];
        if (o.getClass().isArray()) {
            Object[] fixed = new Object[original.length];
            for (int i = 0; i < original.length; i++)
                fixed[i] = ((Object[]) original[i])[0];
            original = fixed;
        }

        return Arrays.copyOf(original, original.length, Short[].class);
    }

    @Override
    public boolean isArray() {
        return true;
    }
}
