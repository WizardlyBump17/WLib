package com.wizardlybump17.wlib.command.args.reader;

public class ShortReader extends ArgsReader<Short> {

    @Override
    public Class<Short> getType() {
        return short.class;
    }

    @Override
    public Short read(String string) throws ArgsReaderException {
        try {
            return Short.parseShort(string);
        } catch (NumberFormatException e) {
            throw new ArgsReaderException("expected a short but got " + string);
        }
    }
}
