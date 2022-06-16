package com.wizardlybump17.wlib.command.args.reader;

public class StringReader extends ArgsReader<String> {

    @Override
    public Class<String> getType() {
        return String.class;
    }

    @Override
    public String read(String string) {
        string = string.replace("\\\"", "\"");
        if (string.startsWith("\"") && string.endsWith("\""))
            return string.substring(1, string.length() - 1);
        return string;
    }
}
