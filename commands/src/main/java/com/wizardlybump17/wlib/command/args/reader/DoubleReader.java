package com.wizardlybump17.wlib.command.args.reader;

import com.wizardlybump17.wlib.command.CommandSender;
import lombok.NonNull;

import java.util.List;

public class DoubleReader extends ArgsReader<Double> {

    public static final List<String> SUGGESTIONS = List.of("1", "1.5", "10", "10.5");
    public static final @NonNull List<Class<?>> TYPES = List.of(double.class, Double.class);

    @Override
    public Class<Double> getType() {
        return double.class;
    }

    @Override
    public @NonNull List<Class<?>> getTypes() {
        return TYPES;
    }

    @Override
    public Double read(String string) throws ArgsReaderException {
        try {
            return Double.parseDouble(string);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    @Override
    public @NonNull List<@NonNull String> autoComplete(@NonNull CommandSender<?> sender, @NonNull String current) {
        return SUGGESTIONS;
    }
}
