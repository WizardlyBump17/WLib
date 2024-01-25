package com.wizardlybump17.wlib.command.args.reader;

import com.wizardlybump17.wlib.command.CommandSender;
import lombok.NonNull;

import java.util.List;

public class ShortReader extends ArgsReader<Short> {

    public static final List<String> SUGGESTIONS = List.of("-32768", "0", "32767");

    @Override
    public Class<Short> getType() {
        return short.class;
    }

    @Override
    public Short read(String string) throws ArgsReaderException {
        try {
            return Short.parseShort(string);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    @Override
    public @NonNull List<@NonNull String> autoComplete(@NonNull CommandSender<?> sender, @NonNull String current) {
        return SUGGESTIONS;
    }
}
