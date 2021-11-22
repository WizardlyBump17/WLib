package com.wizardlybump17.wlib.command.args.reader;

public class StringArrayReader extends ArrayReader<String[]> {

    @Override
    public Class<String[]> getType() {
        return String[].class;
    }

    @Override
    public String[] read(String string) throws ArgsReaderException {
        String[] split = split(string);
        if (split == null)
            throw new ArgsReaderException("expected a string array but got " + string);
        return split;
    }
}
