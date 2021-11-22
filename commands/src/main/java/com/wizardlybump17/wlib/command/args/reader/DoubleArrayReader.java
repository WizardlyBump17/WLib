package com.wizardlybump17.wlib.command.args.reader;

public class DoubleArrayReader extends ArrayReader<Double[]> {

    @Override
    public Class<Double[]> getType() {
        return Double[].class;
    }

    @Override
    public Double[] read(String string) throws ArgsReaderException {
        try {
            final String[] strings = split(string);
            if (strings == null)
                throw new NumberFormatException();

            Double[] result = new Double[strings.length];
            for (int i = 0; i < result.length; i++)
                result[i] = Double.parseDouble(strings[i]);
            return result;
        } catch (NumberFormatException e) {
            throw new ArgsReaderException("expected a double array in string form but got " + string);
        }
    }
}
