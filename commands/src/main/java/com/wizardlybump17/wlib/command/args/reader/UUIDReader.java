package com.wizardlybump17.wlib.command.args.reader;

import com.wizardlybump17.wlib.command.CommandSender;
import lombok.NonNull;

import java.util.List;
import java.util.UUID;

public class UUIDReader extends ArgsReader<UUID> {

    public static final List<String> SUGGESTIONS = List.of("00000000-0000-0000-0000-000000000000");

    @Override
    public Class<UUID> getType() {
        return UUID.class;
    }

    @Override
    public UUID read(String string) throws ArgsReaderException {
        try {
            return UUID.fromString(string);
        } catch (IllegalArgumentException e) {
            throw new ArgsReaderException("expected an uuid in string form but got " + string);
        }
    }

    @Override
    public @NonNull List<@NonNull String> autoComplete(@NonNull CommandSender<?> sender, @NonNull String current) {
        return SUGGESTIONS;
    }
}
