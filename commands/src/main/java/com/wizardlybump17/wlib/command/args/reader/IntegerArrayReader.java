package com.wizardlybump17.wlib.command.args.reader;

import java.util.Arrays;

public class IntegerArrayReader extends ArgsReader<Integer[]> {

    @Override
    public Class<Integer[]> getType() {
        return Integer[].class;
    }

    @Override
    public Integer[] read(String string) throws ArgsReaderException {
        try {
            final String[] strings = string.split(" ");
            Integer[] result = new Integer[strings.length];
            for (int i = 0; i < result.length; i++)
                result[i] = Integer.parseInt(strings[i]);
            return result;
        } catch (NumberFormatException e) {
            throw new ArgsReaderException("expected a int array in string form but got " + string);
        }
    }

    @Override
    public Integer[] cast(Object[] original) {
        if (original.length == 0)
            return new Integer[0];
        final Object o = original[0];
        if (o.getClass().isArray()) {
            Object[] fixed = new Object[original.length];
            for (int i = 0; i < original.length; i++)
                fixed[i] = ((Object[]) original[i])[0];
            original = fixed;
        }

        return Arrays.copyOf(original, original.length, Integer[].class);
    }

    @Override
    public boolean isArray() {
        return true;
    }
}
