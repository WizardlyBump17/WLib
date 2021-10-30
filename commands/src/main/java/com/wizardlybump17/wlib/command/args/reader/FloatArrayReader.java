package com.wizardlybump17.wlib.command.args.reader;

import java.util.Arrays;

public class FloatArrayReader extends ArgsReader<Float[]> {

    @Override
    public Class<Float[]> getType() {
        return Float[].class;
    }

    @Override
    public Float[] read(String string) throws ArgsReaderException {
        try {
            final String[] strings = string.split(" ");
            Float[] result = new Float[strings.length];
            for (int i = 0; i < result.length; i++)
                result[i] = Float.parseFloat(strings[i]);
            return result;
        } catch (NumberFormatException e) {
            throw new ArgsReaderException("expected a float array in string form but got " + string);
        }
    }

    @Override
    public Float[] cast(Object[] original) {
        if (original.length == 0)
            return new Float[0];
        final Object o = original[0];
        if (o.getClass().isArray()) {
            Object[] fixed = new Object[original.length];
            for (int i = 0; i < original.length; i++)
                fixed[i] = ((Object[]) original[i])[0];
            original = fixed;
        }

        return Arrays.copyOf(original, original.length, Float[].class);
    }

    @Override
    public boolean isArray() {
        return true;
    }
}
