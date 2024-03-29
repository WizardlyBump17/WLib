package com.wizardlybump17.wlib.command.args.reader;

public class ShortArrayReader extends ArgsReader<short[]> {

    @Override
    public Class<short[]> getType() {
        return short[].class;
    }

    @Override
    public short[] read(String string) throws ArgsReaderException {
        try {
            final String[] strings = string.split(" ");

            short[] result = new short[strings.length];
            for (int i = 0; i < result.length; i++)
                result[i] = Short.parseShort(strings[i]);
            return result;
        } catch (NumberFormatException e) {
            throw new ArgsReaderException("expected a short array in string form but got " + string);
        }
    }
}
