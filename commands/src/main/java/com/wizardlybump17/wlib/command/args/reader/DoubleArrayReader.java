package com.wizardlybump17.wlib.command.args.reader;

import java.util.Arrays;

public class DoubleArrayReader extends ArgsReader<Double[]> {

    @Override
    public Class<Double[]> getType() {
        return Double[].class;
    }

    @Override
    public Double[] read(String string) throws ArgsReaderException {
        try {
            final String[] strings = string.split(" ");
            Double[] result = new Double[strings.length];
            for (int i = 0; i < result.length; i++)
                result[i] = Double.parseDouble(strings[i]);
            return result;
        } catch (NumberFormatException e) {
            throw new ArgsReaderException("expected a double array in string form but got " + string);
        }
    }

    @Override
    public Double[] cast(Object[] original) {
        if (original.length == 0)
            return new Double[0];
        final Object o = original[0];
        if (o.getClass().isArray()) {
            Object[] fixed = new Object[original.length];
            for (int i = 0; i < original.length; i++)
                fixed[i] = ((Object[]) original[i])[0];
            original = fixed;
        }

        return Arrays.copyOf(original, original.length, Double[].class);
    }

    @Override
    public boolean isArray() {
        return true;
    }
}
