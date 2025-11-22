package org.example.apiMethods;


import org.example.apiMethods.YandexApi.JsonExtractor;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class JsonExtractorTest {

    @Test
    void testNullOrEmptyJson() {
        //Для extractLandmarkTexts
        assertEquals("", JsonExtractor.extractLandmarkTexts(null));
        assertEquals("", JsonExtractor.extractLandmarkTexts(""));
        assertEquals("", JsonExtractor.extractLandmarkTexts("   "));

        //Для extractFormattedAddress
        assertEquals("", JsonExtractor.extractFormattedAddress(null));
        assertEquals("", JsonExtractor.extractFormattedAddress(""));
        assertEquals("", JsonExtractor.extractFormattedAddress("   "));
    }

    @Test
    void testNoResultsOrTags() {
        //Для extractLandmarkTexts
        String jsonNoResults = "{}";
        assertEquals("", JsonExtractor.extractLandmarkTexts(jsonNoResults));

        String jsonNoTags = "{\"results\": [{\"title\": {\"text\": \"Test\"}}]}";
        assertEquals("", JsonExtractor.extractLandmarkTexts(jsonNoTags));

    }

    @Test
    void testWithLandmark() {
        String jsonWithLandmark = "{\"results\": [{\"tags\": [\"landmark\"], \"title\": {\"text\": \"Эйфелева башня\"}}]}";
        assertEquals("Эйфелева башня", JsonExtractor.extractLandmarkTexts(jsonWithLandmark));
    }

    @Test
    void testWithRealLandmarkData() {
        String json = "{\"results\": [{\"title\": {\"text\": \"Шарташские каменные палатки\"},\"subtitle\": {\"text\": \"Горная вершина · Свердловская область, Екатеринбург, Шарташские каменные палатки\",\"hl\": [{\"begin\": 17,\"end\": 29},{\"begin\": 30,\"end\": 37},{\"begin\": 39,\"end\": 51}]},\"tags\": [\"business\",\"mountain\",\"landmark\"],\"distance\": {\"value\": 475543.1861,\"text\": \"475.54 км\"}}]}";
        assertEquals("Шарташские каменные палатки", JsonExtractor.extractLandmarkTexts(json));
    }

    //Для extractFormattedAddress
    @Test
    void testNoFeatureMember() {
        String jsonNoFeature = "{\"response\": {\"GeoObjectCollection\": {}}}";
        assertEquals("", JsonExtractor.extractFormattedAddress(jsonNoFeature));
    }

    @Test
    void testEmptyFeatureMember() {
        String json = """
    {"response":{"GeoObjectCollection":{"featureMember":[{"GeoObject":{"metaDataProperty":{"GeocoderMetaData":{"Address":{"formatted":"Россия, Свердловская область, Екатеринбург"}}}}}]}}}
    """;
        assertEquals("Россия, Свердловская область, Екатеринбург", JsonExtractor.extractFormattedAddress(json));
    }

    @Test
    void testMissingNestedObjects() {
        String jsonMissingGeoObject = """
                {"response":{"GeoObjectCollection":{"featureMember":[{"noGeoObject": true}]}}""";
        assertEquals("", JsonExtractor.extractFormattedAddress(jsonMissingGeoObject));
    }

    @Test
    void testBlankAddress() {
        String jsonBlank = "{\"response\":{\"GeoObjectCollection\":{\"featureMember\":[{\"GeoObject\":{\"metaDataProperty\":{\"GeocoderMetaData\":{\"Address\":{\"formatted\":\"   \"}}}}}]}}";
        assertEquals("", JsonExtractor.extractFormattedAddress(jsonBlank));
    }

}