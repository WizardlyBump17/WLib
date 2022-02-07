package com.wizardlybump17.wlib.command.args.reader;

public class FloatReader extends ArgsReader<Float> {

    @Override
    public Class<Float> getType() {
        return float.class;
    }

    @Override
    public Float read(String string) throws ArgsReaderException {
        try {
            return Float.parseFloat(string);
        } catch (NumberFormatException e) {
            throw new ArgsReaderException("expected a float but got " + string);
        }
    }
}
