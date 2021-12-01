package com.wizardlybump17.wlib.command.args.reader;

public class DoubleReader extends ArgsReader<Double> {

    @Override
    public Class<Double> getType() {
        return double.class;
    }

    @Override
    public Double read(String string) throws ArgsReaderException {
        try {
            return Double.parseDouble(string);
        } catch (NumberFormatException e) {
            throw new ArgsReaderException(e, "expected a double but got " + string);
        }
    }
}
