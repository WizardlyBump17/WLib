package com.wizardlybump17.wlib.command.args.reader;

import com.wizardlybump17.wlib.command.CommandSender;
import lombok.NonNull;

import java.util.List;

public class ByteReader extends ArgsReader<Byte> {

    public static final List<String> SUGGESTIONS = List.of("-128", "0", "127");
    public static final @NonNull List<Class<?>> TYPES = List.of(byte.class, Byte.class);

    @Override
    public Class<Byte> getType() {
        return byte.class;
    }

    @Override
    public @NonNull List<Class<?>> getTypes() {
        return TYPES;
    }

    @Override
    public Byte read(String string) throws ArgsReaderException {
        try {
            return Byte.parseByte(string);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    @Override
    public @NonNull List<@NonNull String> autoComplete(@NonNull CommandSender<?> sender, @NonNull String current) {
        return SUGGESTIONS;
    }
}
