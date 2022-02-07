package com.wizardlybump17.wlib.command.args.reader;

import java.util.UUID;

public class UUIDReader extends ArgsReader<UUID> {

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
}
