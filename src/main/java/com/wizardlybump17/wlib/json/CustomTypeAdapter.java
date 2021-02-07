package com.wizardlybump17.wlib.json;

import com.google.gson.*;
import com.google.gson.internal.LinkedTreeMap;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class CustomTypeAdapter extends TypeAdapter<Object> {

    public static final Gson GSON;

    static {
        CustomTypeAdapter adapter = new CustomTypeAdapter();
        GSON = new GsonBuilder()
                .registerTypeAdapter(Map.class, adapter)
                .registerTypeAdapter(List.class, adapter)
                .create();
    }

    private final TypeAdapter<Object> delegate = new Gson().getAdapter(Object.class);

    @Override
    public void write(JsonWriter out, Object value) throws IOException {
        delegate.write(out, value);
    }

    @Override
    public Object read(JsonReader in) throws IOException {
        JsonToken token = in.peek();
        switch (token) {
            case BEGIN_ARRAY: {
                List<Object> list = new ArrayList<>();
                in.beginArray();
                while (in.hasNext()) list.add(read(in));
                in.endArray();
                return list;
            }

            case BEGIN_OBJECT: {
                Map<String, Object> map = new LinkedTreeMap<>();
                in.beginObject();
                while (in.hasNext()) map.put(in.nextName(), read(in));
                in.endObject();
                return map;
            }

            case STRING: return in.nextString();

            case NUMBER: {
                String n = in.nextString();
                if (isByte(n)) return Byte.parseByte(n);
                if (isShort(n)) return Short.parseShort(n);
                if (isInteger(n)) return Integer.parseInt(n);
                if (isLong(n)) return Long.parseLong(n);
                if (isFloat(n)) return Float.parseFloat(n);
                if (isDouble(n)) return Double.parseDouble(n);
                if (isBigInteger(n)) return new BigInteger(n);
                return new BigDecimal(n);
            }

            case BOOLEAN: return in.nextBoolean();

            case NULL: {
                in.nextNull();
                return null;
            }

            default: throw new IllegalStateException();
        }
    }

    private static boolean isByte(String string) {
        try {
            Byte.parseByte(string);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    private static boolean isShort(String string) {
        try {
            Short.parseShort(string);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    private static boolean isInteger(String string) {
        try {
            Integer.parseInt(string);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    private static boolean isLong(String string) {
        try {
            Long.parseLong(string);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    private static boolean isFloat(String string) {
        try {
            Float.parseFloat(string);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    private static boolean isDouble(String string) {
        try {
            Double.parseDouble(string);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    private static boolean isBigInteger(String string) {
        try {
            new BigInteger(string);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    private static boolean isBigDecimal(String string) {
        try {
            new BigDecimal(string);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
}
