package org.example.apiMethods;

import com.google.gson.*;

import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class JsonExtractor {
    private JsonExtractor() {
    }

    //Метод для получения достопримечательностей
    //вычисляет есть ли в tags "landmark" и если есть, то добавляет этот объект
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


//    Метод для получения города из координат
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

    public static String extractBusShedules(String json) {

        JsonObject root = JsonParser.parseString(json).getAsJsonObject();
        JsonArray segments = root.getAsJsonArray("segments");

        if (segments.size() == 0) {
            return "Рейсов не найдено";
        }

        for (int i = 0; i < segments.size(); i++) {
            JsonObject segment = segments.get(i).getAsJsonObject();

            String from = segment.getAsJsonObject("from")
                    .get("title").getAsString();
            String to = segment.getAsJsonObject("to")
                    .get("title").getAsString();

            OffsetDateTime departure = OffsetDateTime.parse(
                    segment.get("departure").getAsString()
            );
            OffsetDateTime arrival = OffsetDateTime.parse(
                    segment.get("arrival").getAsString()
            );

            String carrier = segment.getAsJsonObject("thread")
                    .getAsJsonObject("carrier")
                    .get("title").getAsString();

            int price;
            if (segment.has("tickets_info") && !segment.get("tickets_info").isJsonNull()) {
                price = segment.getAsJsonObject("tickets_info")
                        .getAsJsonArray("places")
                        .get(0).getAsJsonObject()
                        .getAsJsonObject("price")
                        .get("whole").getAsInt();
            } else {
                continue;
            }
            return String.format(
                    "Рейс: %s -> %s\nОтправление: %s\nПрибытие: %s\nПеревозчик: %s\nЦена: %d руб.",
                    from, to,
                    departure.format(DateTimeFormatter.ofPattern("dd.MM HH:mm")),
                    arrival.format(DateTimeFormatter.ofPattern("dd.MM HH:mm")),
                    carrier, price
            );
        }
        return "Рейсов нет";
    }

    public static List<String> extractBusStationCodes(String json) {
        Gson gson = new Gson();
        List<String> stationCodes = new ArrayList<>();

        JsonArray mainArray = gson.fromJson(json, JsonArray.class);

        if (mainArray.size() < 2) {
            return stationCodes;
        }

        JsonElement secondElement = mainArray.get(1);
        if (!secondElement.isJsonArray()) {
            return stationCodes;
        }

        JsonArray stations = secondElement.getAsJsonArray();

        for (JsonElement stationElement : stations) {
            if (!stationElement.isJsonArray()) {
                continue;
            }

            JsonArray station = stationElement.getAsJsonArray();

            if (station.size() < 3) {
                continue;
            }

            String description = station.get(2).getAsString();

            if (description.contains("авт.")) {
                String stationCode = station.get(0).getAsString();
                stationCodes.add(stationCode);
            }
        }

        return stationCodes;
    }

    public static String extractFirstBusStationCode(String json) {
        List<String> codes = extractBusStationCodes(json);
        return codes.isEmpty() ? null : codes.get(0);
    }
}
