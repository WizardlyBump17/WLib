package com.wizardlybump17.wlib.command.args.reader;

public class BooleanReader extends ArgsReader<Boolean> {

    @Override
    public Class<Boolean> getType() {
        return boolean.class;
    }

    @Override
    public Boolean read(String string) throws ArgsReaderException {
        if (string.equalsIgnoreCase("true"))
            return true;
        if (string.equalsIgnoreCase("false"))
            return false;
        throw new ArgsReaderException(new IllegalArgumentException(), "expected a boolean but got " + string);
    }
}
