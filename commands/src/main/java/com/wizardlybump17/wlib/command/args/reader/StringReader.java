package com.wizardlybump17.wlib.command.args.reader;

public class StringReader extends ArgsReader<String> {

    @Override
    public Class<String> getType() {
        return String.class;
    }

    @Override
    public String read(String string) {
        return string;
    }
}
