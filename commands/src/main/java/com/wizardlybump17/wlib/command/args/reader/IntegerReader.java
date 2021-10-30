package com.wizardlybump17.wlib.command.args.reader;

public class IntegerReader extends ArgsReader<Integer> {

    @Override
    public Class<Integer> getType() {
        return int.class;
    }

    @Override
    public Integer read(String string) throws ArgsReaderException {
        try {
            return Integer.parseInt(string);
        } catch (NumberFormatException e) {
            throw new ArgsReaderException("expected an int but got " + string);
        }
    }
}
