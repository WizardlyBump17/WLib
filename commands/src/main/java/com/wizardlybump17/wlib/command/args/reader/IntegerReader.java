package com.wizardlybump17.wlib.command.args.reader;

import com.wizardlybump17.wlib.command.CommandSender;
import lombok.NonNull;

import java.util.List;

public class IntegerReader extends ArgsReader<Integer> {

    public static final List<String> SUGGESTIONS = List.of("1", "10", "100");

    @Override
    public Class<Integer> getType() {
        return int.class;
    }

    @Override
    public Integer read(String string) throws ArgsReaderException {
        try {
            return Integer.parseInt(string);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    @Override
    public @NonNull List<@NonNull String> autoComplete(@NonNull CommandSender<?> sender, @NonNull String current) {
        return SUGGESTIONS;
    }
}
