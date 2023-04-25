package com.wizardlybump17.wlib.command.reader;

import com.google.gson.Gson;
import com.wizardlybump17.wlib.command.args.reader.ArgsReader;

import java.util.Map;

public class MapJsonArgsReader extends ArgsReader<Map<String, Object>> {

    private final Gson gson = new Gson();

    @Override
    public Class<Map<String, Object>> getType() {
        return null;
    }

    @SuppressWarnings("unchecked")
    @Override
    public Map<String, Object> read(String string) {
        return gson.fromJson(string, Map.class);
    }
}
