package com.wizardlybump17.wlib.command.args.reader;

import java.util.Arrays;

public class StringArrayReader extends ArgsReader<String[]> {

    @Override
    public Class<String[]> getType() {
        return String[].class;
    }

    @Override
    public String[] read(String string) {
        return string == null ? null : string.split(" ");
    }

    @Override
    public boolean isArray() {
        return true;
    }

    @Override
    public String[] cast(Object[] original) {
        if (original.length == 0)
            return new String[0];
        final Object o = original[0];
        if (o.getClass().isArray()) {
            Object[] fixed = new Object[original.length];
            for (int i = 0; i < original.length; i++)
                fixed[i] = ((Object[]) original[i])[0];
            original = fixed;
        }

        return Arrays.copyOf(original, original.length, String[].class);
    }
}
