package dev.kyanbirb.world_casting.data;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.internal.Streams;
import com.google.gson.stream.JsonReader;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import java.util.function.BiConsumer;

public class WorldCastingLang {
    public static final Map<String, String> LANG_MAP = new HashMap<>();

    public static void provideLang(BiConsumer<String, String> consumer, String locale) {
        LANG_MAP.forEach(consumer);
        Map<String, String> lang = getLangMap(locale);
        lang.forEach(consumer);
    }

    private static Map<String, String> getLangMap(final String locale) {
        final String filepath = "datagen/lang/%s.json".formatted(locale);
        final JsonObject langObject = loadJsonResource(filepath).getAsJsonObject();

        final Map<String, String> langMap = new HashMap<>();
        flattenJson(langMap, langObject, null);
        return langMap;
    }

    private static JsonElement loadJson(InputStream inputStream) {
        try {
            JsonReader reader = new JsonReader(new BufferedReader(new InputStreamReader(inputStream)));
            reader.setLenient(true);
            JsonElement element = Streams.parse(reader);
            reader.close();
            inputStream.close();
            return element;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static JsonElement loadJsonResource(String filepath) {
        return loadJson(ClassLoader.getSystemResourceAsStream(filepath));
    }

    private static void flattenJson(Map<String, String> outputMap, JsonElement element, String currentPath) {
        if(element.isJsonPrimitive() && element.getAsJsonPrimitive().isString()) {
            String string = element.getAsJsonPrimitive().getAsString();
            outputMap.put(currentPath, string);
            return;
        }

        if(element.isJsonObject()) {
            JsonObject object = element.getAsJsonObject();
            for (String key : object.keySet()) {
                JsonElement value = object.get(key);
                String path;
                if(currentPath != null) {
                    String delimiter = currentPath.endsWith(":") ? "" : ".";
                    path = currentPath + delimiter + key;
                } else {
                    path = key;
                }

                flattenJson(outputMap, value, path);
            }
        } else if(element.isJsonArray() && currentPath != null) {
            JsonArray array = element.getAsJsonArray();
            for (int i = 0; i < array.size(); i++) {
                String path = currentPath + "_" + (i + 1);
                flattenJson(outputMap, array.get(i), path);
            }
        }
    }
}
