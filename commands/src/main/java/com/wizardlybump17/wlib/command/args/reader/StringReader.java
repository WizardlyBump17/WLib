package com.wizardlybump17.wlib.command.args.reader;

import com.wizardlybump17.wlib.command.CommandSender;
import lombok.NonNull;

import java.util.List;

public class StringReader extends ArgsReader<String> {

    public static final List<String> SUGGESTIONS = List.of("dummy", "text");

    @Override
    public Class<String> getType() {
        return String.class;
    }

    @Override
    public String read(String string) {
        return string;
    }

    @Override
    public @NonNull List<@NonNull String> autoComplete(@NonNull CommandSender<?> sender, @NonNull String current) {
        return SUGGESTIONS;
    }
}
