package com.wizardlybump17.wlib.command.args.reader;

public class FloatArrayReader extends ArgsReader<float[]> {

    @Override
    public Class<float[]> getType() {
        return float[].class;
    }

    @Override
    public float[] read(String string) throws ArgsReaderException {
        try {
            final String[] strings = string.split(" ");

            float[] result = new float[strings.length];
            for (int i = 0; i < result.length; i++)
                result[i] = Float.parseFloat(strings[i]);
            return result;
        } catch (NumberFormatException e) {
            throw new ArgsReaderException("expected a float array in string form but got " + string);
        }
    }
}
