package com.wizardlybump17.wlib.command.args.reader;

public class ShortArrayReader extends ArrayReader<Short[]> {

    @Override
    public Class<Short[]> getType() {
        return Short[].class;
    }

    @Override
    public Short[] read(String string) throws ArgsReaderException {
        try {
            final String[] strings = split(string);
            if (strings == null)
                throw new NumberFormatException();

            Short[] result = new Short[strings.length];
            for (int i = 0; i < result.length; i++)
                result[i] = Short.parseShort(strings[i]);
            return result;
        } catch (NumberFormatException e) {
            throw new ArgsReaderException("expected a short array in string form but got " + string);
        }
    }
}
