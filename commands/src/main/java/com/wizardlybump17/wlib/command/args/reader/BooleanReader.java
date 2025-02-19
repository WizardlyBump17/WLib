package com.wizardlybump17.wlib.command.args.reader;

import com.wizardlybump17.wlib.command.sender.CommandSender;
import lombok.NonNull;

import java.util.List;

public class BooleanReader extends ArgsReader<Boolean> {

    public static final @NonNull List<Class<?>> TYPES = List.of(boolean.class, Boolean.class);
    public static final List<String> SUGGESTIONS = List.of("true", "false");

    @Override
    public Class<Boolean> getType() {
        return boolean.class;
    }

    @Override
    public @NonNull List<Class<?>> getTypes() {
        return TYPES;
    }

    @Override
    public Boolean read(String string) throws ArgsReaderException {
        if (string.equalsIgnoreCase("true"))
            return true;
        if (string.equalsIgnoreCase("false"))
            return false;

        return null;
    }

    @Override
    public @NonNull List<@NonNull String> autoComplete(@NonNull CommandSender<?> sender, @NonNull String current) {
        return SUGGESTIONS;
    }
}
