package com.wizardlybump17.wlib.command.args.reader;

import java.util.Arrays;

public class ByteArrayReader extends ArgsReader<Byte[]> {

    @Override
    public Class<Byte[]> getType() {
        return Byte[].class;
    }

    @Override
    public Byte[] read(String string) throws ArgsReaderException {
        try {
            final String[] strings = string.split(" ");
            Byte[] result = new Byte[strings.length];
            for (int i = 0; i < result.length; i++)
                result[i] = Byte.parseByte(strings[i]);
            return result;
        } catch (NumberFormatException e) {
            throw new ArgsReaderException("expected a byte array in string form but got " + string);
        }
    }

    @Override
    public Byte[] cast(Object[] original) {
        if (original.length == 0)
            return new Byte[0];
        final Object o = original[0];
        if (o.getClass().isArray()) {
            Object[] fixed = new Object[original.length];
            for (int i = 0; i < original.length; i++)
                fixed[i] = ((Object[]) original[i])[0];
            original = fixed;
        }

        return Arrays.copyOf(original, original.length, Byte[].class);
    }

    @Override
    public boolean isArray() {
        return true;
    }
}
