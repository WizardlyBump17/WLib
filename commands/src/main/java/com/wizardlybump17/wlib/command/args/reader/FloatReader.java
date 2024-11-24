package com.wizardlybump17.wlib.command.args.reader;

import com.wizardlybump17.wlib.command.sender.CommandSender;
import lombok.NonNull;

import java.util.List;

public class FloatReader extends ArgsReader<Float> {

    public static final List<String> SUGGESTIONS = List.of("1", "1.5", "10", "10.5");
    public static final @NonNull List<Class<?>> TYPES = List.of(float.class, Float.class);

    @Override
    public Class<Float> getType() {
        return float.class;
    }

    @Override
    public @NonNull List<Class<?>> getTypes() {
        return TYPES;
    }

    @Override
    public Float read(String string) throws ArgsReaderException {
        try {
            return Float.parseFloat(string);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    @Override
    public @NonNull List<@NonNull String> autoComplete(@NonNull CommandSender<?> sender, @NonNull String current) {
        return SUGGESTIONS;
    }
}
