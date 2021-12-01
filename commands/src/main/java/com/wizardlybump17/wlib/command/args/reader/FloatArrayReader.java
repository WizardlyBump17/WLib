package com.wizardlybump17.wlib.command.args.reader;

public class FloatArrayReader extends ArrayReader<Float[]> {

    @Override
    public Class<Float[]> getType() {
        return Float[].class;
    }

    @Override
    public Float[] read(String string) throws ArgsReaderException {
        try {
            final String[] strings = split(string);
            if (strings == null)
                throw new NumberFormatException();

            Float[] result = new Float[strings.length];
            for (int i = 0; i < result.length; i++)
                result[i] = Float.parseFloat(strings[i]);
            return result;
        } catch (NumberFormatException e) {
            throw new ArgsReaderException(e, "expected a float array in string form but got " + string);
        }
    }
}
