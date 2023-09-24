package com.wizardlybump17.wlib.command.args.reader;

import com.wizardlybump17.wlib.command.CommandSender;
import lombok.NonNull;

import java.util.List;

public class BooleanReader extends ArgsReader<Boolean> {

    public static final List<String> SUGGESTIONS = List.of("true", "false");

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

        throw new ArgsReaderException("expected a boolean but got " + string);
    }

    @Override
    public @NonNull List<@NonNull String> autoComplete(@NonNull CommandSender<?> sender, @NonNull String current) {
        return SUGGESTIONS;
    }
}
