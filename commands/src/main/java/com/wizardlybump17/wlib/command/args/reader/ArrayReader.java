package com.wizardlybump17.wlib.command.args.reader;

public abstract class ArrayReader<T> extends ArgsReader<T> {

    public String[] split(String string) {
        if (!string.startsWith("\"") && !string.endsWith("\""))
            return null;

        return string.substring(1, string.length() - 1).replace("\\\"", "\"").split(" ");
    }
}
