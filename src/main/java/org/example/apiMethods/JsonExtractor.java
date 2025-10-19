package org.example.apiMethods;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class JsonExtractor {
    private JsonExtractor() {
    }

    //Метод для получения достопримечательностей
    //Вычисляет есть ли в tags "landmark" и если есть, то добавляет этот объект
    public static String extractLandmarkTexts(String json) {
        if (json == null || json.isBlank()) {
            return "";
        }

        JsonElement rootEl;
        try {
            rootEl = JsonParser.parseString(json);
        } catch (Exception ex) {
            return "";
        }
        if (!rootEl.isJsonObject()) {
            return "";
        }

        JsonObject root = rootEl.getAsJsonObject();
        if (!root.has("results") || !root.get("results").isJsonArray()) {
            return "";
        }

        JsonArray results = root.getAsJsonArray("results");
        StringBuilder out = new StringBuilder();

        for (JsonElement e : results) {
            if (!e.isJsonObject()) continue;
            JsonObject obj = e.getAsJsonObject();

            if (!obj.has("tags") || !obj.get("tags").isJsonArray()) continue;
            if (!containsTag(obj.getAsJsonArray("tags"), "landmark")) continue;

            if (obj.has("title") && obj.get("title").isJsonObject()) {
                JsonObject title = obj.getAsJsonObject("title");
                if (title.has("text") && title.get("text").isJsonPrimitive()) {
                    String name = title.get("text").getAsString();
                    if (name != null && !name.isBlank()) {
                        if (!out.isEmpty()) out.append('\n');
                        out.append(name);
                    }
                }
            }
        }

        return out.toString();
    }


    //Метод для получения города из координат
    public static String extractFormattedAddress(String json) {
        if (json == null || json.isBlank()) return "";
        JsonElement rootEl;
        try {
            rootEl = JsonParser.parseString(json);
        } catch (Exception ex) {
            return "";
        }

        if (!rootEl.isJsonObject()) return "";
        JsonObject root = rootEl.getAsJsonObject();

        JsonObject response = getObj(root, "response");
        JsonObject goc = getObj(response, "GeoObjectCollection");
        JsonArray featureMember = getArr(goc, "featureMember");
        if (featureMember == null || featureMember.isEmpty()) return "";

        JsonElement fmEl = featureMember.get(0);
        if (!fmEl.isJsonObject()) return "";
        JsonObject fm0 = fmEl.getAsJsonObject();

        JsonObject geoObject = getObj(fm0, "GeoObject");
        JsonObject mdp = getObj(geoObject, "metaDataProperty");
        JsonObject geocoderMeta = getObj(mdp, "GeocoderMetaData");
        JsonObject address = getObj(geocoderMeta, "Address");

        String formatted = getString(address, "formatted");
        if (!formatted.isBlank()) return formatted;

        JsonObject addressDetails = getObj(geocoderMeta, "AddressDetails");
        JsonObject country = getObj(addressDetails, "Country");
        String addressLine = getString(country, "AddressLine");
        if (!addressLine.isBlank()) return addressLine;

        return "";
    }


    //Вспомогательные методы для работы с JSON'ами
    private static boolean containsTag(JsonArray tags, String needleLowerCase) {
        for (JsonElement t : tags) {
            if (t.isJsonPrimitive()) {
                String v = t.getAsJsonPrimitive().getAsString();
                if (v != null && v.equalsIgnoreCase(needleLowerCase)) {
                    return true;
                }
            }
        }
        return false;
    }

    private static JsonObject getObj(JsonObject parent, String key) {
        if (parent == null || !parent.has(key) || !parent.get(key).isJsonObject()) return null;
        return parent.getAsJsonObject(key);
    }

    private static JsonArray getArr(JsonObject parent, String key) {
        if (parent == null || !parent.has(key) || !parent.get(key).isJsonArray()) return null;
        return parent.getAsJsonArray(key);
    }

    private static String getString(JsonObject parent, String key) {
        if (parent == null || !parent.has(key) || !parent.get(key).isJsonPrimitive()) return "";
        try {
            String v = parent.get(key).getAsString();
            return v == null ? "" : v;
        } catch (Exception e) {
            return "";
        }
    }
}
