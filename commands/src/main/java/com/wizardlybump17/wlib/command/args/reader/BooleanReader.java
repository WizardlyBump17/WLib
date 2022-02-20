package com.wizardlybump17.wlib.command.args.reader;

import java.util.HashSet;
import java.util.Set;

public class BooleanReader extends ArgsReader<Boolean> {

    private static final Set<String> TRUE = new HashSet<>();
    private static final Set<String> FALSE = new HashSet<>();

    static {
        TRUE.add("true");
        TRUE.add("t");
        TRUE.add("yes");
        TRUE.add("y");
        TRUE.add("on");
        TRUE.add("1");
        TRUE.add("-y");

        FALSE.add("false");
        FALSE.add("f");
        FALSE.add("no");
        FALSE.add("n");
        FALSE.add("off");
        FALSE.add("0");
        FALSE.add("-n");
    }

    @Override
    public Class<Boolean> getType() {
        return boolean.class;
    }

    @Override
    public Boolean read(String string) throws ArgsReaderException {
        if (TRUE.contains(string.toLowerCase()))
            return true;
        if (FALSE.contains(string.toLowerCase()))
            return false;
        throw new ArgsReaderException("expected a boolean but got " + string);
    }
}
