package com.wizardlybump17.wlib.command.args.reader;

public class DoubleArrayReader extends ArgsReader<double[]> {

    @Override
    public Class<double[]> getType() {
        return double[].class;
    }

    @Override
    public double[] read(String string) throws ArgsReaderException {
        try {
            final String[] strings = string.split(" ");

            double[] result = new double[strings.length];
            for (int i = 0; i < result.length; i++)
                result[i] = Double.parseDouble(strings[i]);
            return result;
        } catch (NumberFormatException e) {
            throw new ArgsReaderException("expected a double array in string form but got " + string);
        }
    }
}
