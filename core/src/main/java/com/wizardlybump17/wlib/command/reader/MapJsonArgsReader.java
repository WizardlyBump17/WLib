package com.wizardlybump17.wlib.command.reader;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.wizardlybump17.wlib.command.args.reader.ArgsReader;
import com.wizardlybump17.wlib.command.args.reader.ArgsReaderException;
import com.wizardlybump17.wlib.command.sender.CommandSender;
import lombok.NonNull;

import java.util.List;
import java.util.Map;

public class MapJsonArgsReader extends ArgsReader<Map<String, Object>> {

    public static final List<String> SUGGESTIONS = List.of("{key=value,key2=value2}");

    private final Gson gson = new Gson();

    @Override
    public Class<Map<String, Object>> getType() {
        return null;
    }

    @SuppressWarnings("unchecked")
    @Override
    public Map<String, Object> read(String string) throws ArgsReaderException {
        try {
            return gson.fromJson(string, Map.class);
        } catch (JsonSyntaxException exception) {
            throw new ArgsReaderException("expected a valid JSON", exception);
        }
    }

    @Override
    public @NonNull List<@NonNull String> autoComplete(@NonNull CommandSender<?> sender, @NonNull String current) {
        return SUGGESTIONS;
    }
}
