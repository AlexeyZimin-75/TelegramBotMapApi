package org.example.apiMethods.YandexAPI;

import org.example.apiMethods.YandexMapsAPI.YandexMapsRepository;
import org.example.apiMethods.YandexMapsAPI.YandexMapsService;
import org.example.apiMethods.YandexSchedulesApi.YandexSchedulesRepository;
import org.example.apiMethods.YandexSchedulesApi.YandexSchedulesService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class YandexAPITest {

    @Mock
    private YandexMapsRepository yandexMapsRepository;

    @Mock
    private YandexSchedulesRepository yandexSchedulesRepository;

   // Тесты для Яндекс.Карт
    @Test
    void getCityWithCords() throws Exception {
        YandexMapsService service = new YandexMapsService(yandexMapsRepository, "fake-api-key");

        String mockJsonResponse = """
        {"response":{"GeoObjectCollection":{"metaDataProperty":{"GeocoderResponseMetaData":{"Point":{"pos":"60.615579 56.828606"},"boundedBy":{"Envelope":{"lowerCorner":"60.365583 56.577769","upperCorner":"60.865568 57.077768"}},"request":"60.615579,56.828606","results":"5","found":"5"}},"featureMember":[{"GeoObject":{"metaDataProperty":{"GeocoderMetaData":{"precision":"other","text":"Россия, Свердловская область, Екатеринбург","kind":"locality","Address":{"country_code":"RU","formatted":"Россия, Свердловская область, Екатеринбург","Components":[{"kind":"country","name":"Россия"},{"kind":"province","name":"Уральский федеральный округ"},{"kind":"province","name":"Свердловская область"},{"kind":"area","name":"муниципальное образование Екатеринбург"},{"kind":"locality","name":"Екатеринбург"}]},"AddressDetails":{"Country":{"AddressLine":"Россия, Свердловская область, Екатеринбург","CountryNameCode":"RU","CountryName":"Россия","AdministrativeArea":{"AdministrativeAreaName":"Свердловская область","SubAdministrativeArea":{"SubAdministrativeAreaName":"муниципальное образование Екатеринбург","Locality":{"LocalityName":"Екатеринбург"}}}}}}},"name":"Екатеринбург","description":"Свердловская область, Россия","boundedBy":{"Envelope":{"lowerCorner":"60.006832 56.593795","upperCorner":"60.943299 56.982695"}},"uri":"ymapsbm1://geo?data=Cgg1MzE2NjUzNxJP0KDQvtGB0YHQuNGPLCDQodCy0LXRgNC00LvQvtCy0YHQutCw0Y8g0L7QsdC70LDRgdGC0YwsINCV0LrQsNGC0LXRgNC40L3QsdGD0YDQsyIKDftjckIViVljQg,,","Point":{"pos":"60.597636 56.837435"}}},{"GeoObject":{"metaDataProperty":{"GeocoderMetaData":{"precision":"other","text":"Россия, Свердловская область, Екатеринбург, садоводческое некоммерческое товарищество Металлург","kind":"locality","Address":{"country_code":"RU","formatted":"Россия, Свердловская область, Екатеринбург, садоводческое некоммерческое товарищество Металлург","Components":[{"kind":"country","name":"Россия"},{"kind":"province","name":"Уральский федеральный округ"},{"kind":"province","name":"Свердловская область"},{"kind":"area","name":"муниципальное образование Екатеринбург"},{"kind":"locality","name":"Екатеринбург"},{"kind":"locality","name":"садоводческое некоммерческое товарищество Металлург"}]},"AddressDetails":{"Country":{"AddressLine":"Россия, Свердловская область, Екатеринбург, садоводческое некоммерческое товарищество Металлург","CountryNameCode":"RU","CountryName":"Россия","AdministrativeArea":{"AdministrativeAreaName":"Свердловская область","SubAdministrativeArea":{"SubAdministrativeAreaName":"муниципальное образование Екатеринбург","Locality":{"LocalityName":"Екатеринбург","DependentLocality":{"DependentLocalityName":"садоводческое некоммерческое товарищество Металлург"}}}}}}}},"name":"садоводческое некоммерческое товарищество Металлург","description":"Екатеринбург, Свердловская область, Россия","boundedBy":{"Envelope":{"lowerCorner":"60.634377 56.803302","upperCorner":"60.639255 56.805087"}},"uri":"ymapsbm1://geo?data=CgozODAyNzMyNzQwErQB0KDQvtGB0YHQuNGPLCDQodCy0LXRgNC00LvQvtCy0YHQutCw0Y8g0L7QsdC70LDRgdGC0YwsINCV0LrQsNGC0LXRgNC40L3QsdGD0YDQsywg0YHQsNC00L7QstC-0LTRh9C10YHQutC-0LUg0L3QtdC60L7QvNC80LXRgNGH0LXRgdC60L7QtSDRgtC-0LLQsNGA0LjRidC10YHRgtCy0L4g0JzQtdGC0LDQu9C70YPRgNCzIgoNi4xyQhV8N2NC","Point":{"pos":"60.637252 56.804185"}}},{"GeoObject":{"metaDataProperty":{"GeocoderMetaData":{"precision":"other","text":"Россия, Свердловская область, Екатеринбург, сельскохозяйственный производственный кооператив Энергетик-3","kind":"locality","Address":{"country_code":"RU","formatted":"Россия, Свердловская область, Екатеринбург, сельскохозяйственный производственный кооператив Энергетик-3","Components":[{"kind":"country","name":"Россия"},{"kind":"province","name":"Уральский федеральный округ"},{"kind":"province","name":"Свердловская область"},{"kind":"area","name":"муниципальное образование Екатеринбург"},{"kind":"locality","name":"Екатеринбург"},{"kind":"locality","name":"сельскохозяйственный производственный кооператив Энергетик-3"}]},"AddressDetails":{"Country":{"AddressLine":"Россия, Свердловская область, Екатеринбург, сельскохозяйственный производственный кооператив Энергетик-3","CountryNameCode":"RU","CountryName":"Россия","AdministrativeArea":{"AdministrativeAreaName":"Свердловская область","SubAdministrativeArea":{"SubAdministrativeAreaName":"муниципальное образование Екатеринбург","Locality":{"LocalityName":"Екатеринбург","DependentLocality":{"DependentLocalityName":"сельскохозяйственный производственный кооператив Энергетик-3"}}}}}}}},"name":"сельскохозяйственный производственный кооператив Энергетик-3","description":"Екатеринбург, Свердловская область, Россия","boundedBy":{"Envelope":{"lowerCorner":"60.665351 56.821306","upperCorner":"60.66819 56.822661"}},"uri":"ymapsbm1://geo?data=Cgo0MTE1NzEzOTYyEsQB0KDQvtGB0YHQuNGPLCDQodCy0LXRgNC00LvQvtCy0YHQutCw0Y8g0L7QsdC70LDRgdGC0YwsINCV0LrQsNGC0LXRgNC40L3QsdGD0YDQsywg0YHQtdC70YzRgdC60L7RhdC-0LfRj9C50YHRgtCy0LXQvdC90YvQuSDQv9GA0L7QuNC30LLQvtC00YHRgtCy0LXQvdC90YvQuSDQutC-0L7Qv9C10YDQsNGC0LjQsiDQrdC90LXRgNCz0LXRgtC40LotMyIKDQqrckIVtEljQg,,","Point":{"pos":"60.667031 56.821976"}}},{"GeoObject":{"metaDataProperty":{"GeocoderMetaData":{"precision":"other","text":"Россия, Свердловская область, Екатеринбург, садовое товарищество Лесной-2","kind":"locality","Address":{"country_code":"RU","formatted":"Россия, Свердловская область, Екатеринбург, садовое товарищество Лесной-2","Components":[{"kind":"country","name":"Россия"},{"kind":"province","name":"Уральский федеральный округ"},{"kind":"province","name":"Свердловская область"},{"kind":"area","name":"муниципальное образование Екатеринбург"},{"kind":"locality","name":"Екатеринбург"},{"kind":"locality","name":"садовое товарищество Лесной-2"}]},"AddressDetails":{"Country":{"AddressLine":"Россия, Свердловская область, Екатеринбург, садовое товарищество Лесной-2","CountryNameCode":"RU","CountryName":"Россия","AdministrativeArea":{"AdministrativeAreaName":"Свердловская область","SubAdministrativeArea":{"SubAdministrativeAreaName":"муниципальное образование Екатеринбург","Locality":{"LocalityName":"Екатеринбург","DependentLocality":{"DependentLocalityName":"садовое товарищество Лесной-2"}}}}}}}},"name":"садовое товарищество Лесной-2","description":"Екатеринбург, Свердловская область, Россия","boundedBy":{"Envelope":{"lowerCorner":"60.595579 56.792715","upperCorner":"60.598966 56.797807"}},"uri":"ymapsbm1://geo?data=CgoxNjg2NzE3MjU3EocB0KDQvtGB0YHQuNGPLCDQodCy0LXRgNC00LvQvtCy0YHQutCw0Y8g0L7QsdC70LDRgdGC0YwsINCV0LrQsNGC0LXRgNC40L3QsdGD0YDQsywg0YHQsNC00L7QstC-0LUg0YLQvtCy0LDRgNC40YnQtdGB0YLQstC-INCb0LXRgdC90L7QuS0yIgoN0GNyQhUBL2NC","Point":{"pos":"60.597474 56.795904"}}},{"GeoObject":{"metaDataProperty":{"GeocoderMetaData":{"precision":"other","text":"Россия, Свердловская область, Екатеринбург, СНТ Механизатор","kind":"locality","Address":{"country_code":"RU","formatted":"Россия, Свердловская область, Екатеринбург, СНТ Механизатор","Components":[{"kind":"country","name":"Россия"},{"kind":"province","name":"Уральский федеральный округ"},{"kind":"province","name":"Свердловская область"},{"kind":"area","name":"муниципальное образование Екатеринбург"},{"kind":"locality","name":"Екатеринбург"},{"kind":"locality","name":"СНТ Механизатор"}]},"AddressDetails":{"Country":{"AddressLine":"Россия, Свердловская область, Екатеринбург, СНТ Механизатор","CountryNameCode":"RU","CountryName":"Россия","AdministrativeArea":{"AdministrativeAreaName":"Свердловская область","SubAdministrativeArea":{"SubAdministrativeAreaName":"муниципальное образование Екатеринбург","Locality":{"LocalityName":"Екатеринбург","DependentLocality":{"DependentLocalityName":"СНТ Механизатор"}}}}}}}},"name":"СНТ Механизатор","description":"Екатеринбург, Свердловская область, Россия","boundedBy":{"Envelope":{"lowerCorner":"60.641591 56.795254","upperCorner":"60.644223 56.797043"}},"uri":"ymapsbm1://geo?data=CgoxNDk1NzgzMDg2Em7QoNC-0YHRgdC40Y8sINCh0LLQtdGA0LTQu9C-0LLRgdC60LDRjyDQvtCx0LvQsNGB0YLRjCwg0JXQutCw0YLQtdGA0LjQvdCx0YPRgNCzLCDQodCd0KIg0JzQtdGF0LDQvdC40LfQsNGC0L7RgCIKDSuSckIVIy9jQg,,","Point":{"pos":"60.642741 56.796032"}}}]}}}
        """;

        when(yandexMapsRepository.sendGeocodeRequest(eq("60.761076,56.76987"), anyString()))
                .thenReturn(mockJsonResponse);

        String answer = service.getCityName(60.761076, 56.769870);
        assertEquals("Россия, Свердловская область, Екатеринбург", answer);
    }

    @Test
    void getCityWithCityName() throws Exception {
        YandexMapsService service = new YandexMapsService(yandexMapsRepository, "fake-api-key");

        String mockJsonResponse = """
       {"response":{"GeoObjectCollection":{"metaDataProperty":{"GeocoderResponseMetaData":{"boundedBy":{"Envelope":{"lowerCorner":"-0.250001 -0.250003","upperCorner":"0.250001 0.250003"}},"request":"Симферополь","results":"5","found":"5"}},"featureMember":[{"GeoObject":{"metaDataProperty":{"GeocoderMetaData":{"precision":"other","text":"Россия, Республика Крым, Симферополь","kind":"locality","Address":{"country_code":"RU","formatted":"Россия, Республика Крым, Симферополь","Components":[{"kind":"country","name":"Россия"},{"kind":"province","name":"Южный федеральный округ"},{"kind":"province","name":"Республика Крым"},{"kind":"area","name":"городской округ Симферополь"},{"kind":"locality","name":"Симферополь"}]},"AddressDetails":{"Country":{"AddressLine":"Россия, Республика Крым, Симферополь","CountryNameCode":"RU","CountryName":"Россия","AdministrativeArea":{"AdministrativeAreaName":"Республика Крым","SubAdministrativeArea":{"SubAdministrativeAreaName":"городской округ Симферополь","Locality":{"LocalityName":"Симферополь"}}}}}}},"name":"Симферополь","description":"Республика Крым, Россия","boundedBy":{"Envelope":{"lowerCorner":"34.028372 44.89091","upperCorner":"34.199213 45.004334"}},"uri":"ymapsbm1://geo?data=CgoxNDQ0NDI4NDY5EkPQoNC-0YHRgdC40Y8sINCg0LXRgdC_0YPQsdC70LjQutCwINCa0YDRi9C8LCDQodC40LzRhNC10YDQvtC_0L7Qu9GMIgoNvGYIQhX_yjNC","Point":{"pos":"34.100327 44.948237"}}},{"GeoObject":{"metaDataProperty":{"GeocoderMetaData":{"precision":"other","text":"Казахстан, Костанайская область, Карасуский район, Люблинский сельский округ, село Симферополь","kind":"locality","Address":{"country_code":"KZ","formatted":"Казахстан, Костанайская область, Карасуский район, Люблинский сельский округ, село Симферополь","Components":[{"kind":"country","name":"Казахстан"},{"kind":"province","name":"Костанайская область"},{"kind":"area","name":"Карасуский район"},{"kind":"area","name":"Люблинский сельский округ"},{"kind":"locality","name":"село Симферополь"}]},"AddressDetails":{"Country":{"AddressLine":"Казахстан, Костанайская область, Карасуский район, Люблинский сельский округ, село Симферополь","CountryNameCode":"KZ","CountryName":"Казахстан","AdministrativeArea":{"AdministrativeAreaName":"Костанайская область","SubAdministrativeArea":{"SubAdministrativeAreaName":"Карасуский район","Locality":{"LocalityName":"село Симферополь"}}}}}},"ReferencesMetaData":{"references":[{"id":"390000000","scope":"kz:kato"}]}},"name":"село Симферополь","description":"Люблинский сельский округ, Карасуский район, Костанайская область, Казахстан","boundedBy":{"Envelope":{"lowerCorner":"65.360639 52.426538","upperCorner":"65.375658 52.443641"}},"uri":"ymapsbm1://geo?data=CgoxNTA4NTU2MTU4EpkB0prQsNC30LDSm9GB0YLQsNC9LCDSmtC-0YHRgtCw0L3QsNC5INC-0LHQu9GL0YHRiywg0prQsNGA0LDRgdGDINCw0YPQtNCw0L3Riywg0JvRjtCx0LvQuNC9INCw0YPRi9C70LTRi9KbINC-0LrRgNGD0LPRliwg0KHQuNC80YTQtdGA0L7Qv9C-0LvRjCDQsNGD0YvQu9GLIgoNZ7yCQhUjvVFC","Point":{"pos":"65.367969 52.434703"}}},{"GeoObject":{"metaDataProperty":{"GeocoderMetaData":{"precision":"street","text":"Россия, Р-280 Новороссия","kind":"street","Address":{"country_code":"RU","formatted":"Россия, Р-280 Новороссия","Components":[{"kind":"country","name":"Россия"},{"kind":"street","name":"Р-280 Новороссия"}]},"AddressDetails":{"Country":{"AddressLine":"Россия, Р-280 Новороссия","CountryNameCode":"RU","CountryName":"Россия","Thoroughfare":{"ThoroughfareName":"Р-280 Новороссия"}}}}},"name":"Р-280 Новороссия","description":"Россия","boundedBy":{"Envelope":{"lowerCorner":"34.047461 45.041929","upperCorner":"39.62095 47.32238"}},"uri":"ymapsbm1://geo?data=Cgo1MTY4OTM1OTg2EinQoNC-0YHRgdC40Y8sINCgLTI4MCDQndC-0LLQvtGA0L7RgdGB0LjRjyIKDXDyGEIVr6M8Qg,,","Point":{"pos":"38.236754 47.159844"}}},{"GeoObject":{"metaDataProperty":{"GeocoderMetaData":{"precision":"other","text":"Россия, Республика Крым, аэропорт Симферополь имени И.К. Айвазовского","kind":"airport","Address":{"country_code":"RU","formatted":"Россия, Республика Крым, аэропорт Симферополь имени И.К. Айвазовского","Components":[{"kind":"country","name":"Россия"},{"kind":"province","name":"Южный федеральный округ"},{"kind":"province","name":"Республика Крым"},{"kind":"airport","name":"аэропорт Симферополь имени И.К. Айвазовского"}]},"AddressDetails":{"Country":{"AddressLine":"Россия, Республика Крым, аэропорт Симферополь имени И.К. Айвазовского","CountryNameCode":"RU","CountryName":"Россия","AdministrativeArea":{"AdministrativeAreaName":"Республика Крым","Locality":{"DependentLocality":{"DependentLocalityName":"аэропорт Симферополь имени И.К. Айвазовского"}}}}}}},"name":"аэропорт Симферополь имени И.К. Айвазовского","description":"Республика Крым, Россия","boundedBy":{"Envelope":{"lowerCorner":"33.964025 44.999497","upperCorner":"33.998628 45.076991"}},"uri":"ymapsbm1://geo?data=CgoxNDQ1Mjk3MTQ4En_QoNC-0YHRgdC40Y8sINCg0LXRgdC_0YPQsdC70LjQutCwINCa0YDRi9C8LCDQsNGN0YDQvtC_0L7RgNGCINCh0LjQvNGE0LXRgNC-0L_QvtC70Ywg0LjQvNC10L3QuCDQmC7Qmi4g0JDQudCy0LDQt9C-0LLRgdC60L7Qs9C-IgoNg-wHQhUcJTRC","Point":{"pos":"33.980968 45.036241"}}},{"GeoObject":{"metaDataProperty":{"GeocoderMetaData":{"precision":"street","text":"Россия, A-291 Таврида","kind":"street","Address":{"country_code":"RU","formatted":"Россия, A-291 Таврида","Components":[{"kind":"country","name":"Россия"},{"kind":"street","name":"A-291 Таврида"}]},"AddressDetails":{"Country":{"AddressLine":"Россия, A-291 Таврида","CountryNameCode":"RU","CountryName":"Россия","Thoroughfare":{"ThoroughfareName":"A-291 Таврида"}}}}},"name":"A-291 Таврида","description":"Россия","boundedBy":{"Envelope":{"lowerCorner":"33.661293 44.609173","upperCorner":"36.365159 45.32806"}},"uri":"ymapsbm1://geo?data=CgoxOTQ1NTI4ODUxEiLQoNC-0YHRgdC40Y8sIEEtMjkxINCi0LDQstGA0LjQtNCwIgoNwfQLQhUhXDRC","Point":{"pos":"34.989021 45.089973"}}}]}}}
       """;

        when(yandexMapsRepository.sendGeocodeRequest(eq("Симферополь"), anyString()))
                .thenReturn(mockJsonResponse);

        String answer = service.getCityName("Симферополь");
        assertEquals("Россия, Республика Крым, Симферополь", answer);
    }

    @Test
    void getCityWithWrongCityName() throws Exception {
        YandexMapsService service = new YandexMapsService(yandexMapsRepository, "fake-api-key");

        String mockJsonResponse = """
        {
            "response": {
                "GeoObjectCollection": {
                    "featureMember": []
                }
            }
        }
        """;

        when(yandexMapsRepository.sendGeocodeRequest(eq("вылрплоыврпловы"), anyString()))
                .thenReturn(mockJsonResponse);

        String answer = service.getCityName("вылрплоыврпловы");
        assertEquals("", answer);
    }

    @Test
    void getLandmarksWithCity() throws Exception {
        YandexMapsService service = new YandexMapsService(yandexMapsRepository, "fake-api-key");

        String mockJsonResponse = """
        {"suggest_reqid":"1765037746360375-449252753-sgxyky7gftxyu3vs","results":[{"title":{"text":"Екатеринбургский музей изобразительных искусств","hl":[{"begin":0,"end":12}]},"subtitle":{"text":"Музей · Свердловская область, Екатеринбург, улица Воеводина, 5","hl":[{"begin":8,"end":20},{"begin":21,"end":28},{"begin":30,"end":42}]},"tags":["business","museum","landmark"],"distance":{"value":8283243.774,"text":"8283.24 км"}},{"title":{"text":"Шарташские каменные палатки"},"subtitle":{"text":"Горная вершина · Свердловская область, Екатеринбург, Шарташские каменные палатки","hl":[{"begin":17,"end":29},{"begin":30,"end":37},{"begin":39,"end":51}]},"tags":["business","mountain","landmark"],"distance":{"value":8287797.554,"text":"8287.80 км"}},{"title":{"text":"Дом Н. И. Севастьянова"},"subtitle":{"text":"Достопримечательность · Свердловская область, Екатеринбург, проспект Ленина, 35","hl":[{"begin":0,"end":21,"type":"MISPRINT"},{"begin":24,"end":36},{"begin":37,"end":44},{"begin":46,"end":58}]},"tags":["business","landmark"],"distance":{"value":8283579.418,"text":"8283.58 км"}},{"title":{"text":"Плотинка"},"subtitle":{"text":"Достопримечательность · Свердловская область, Екатеринбург, проспект Ленина","hl":[{"begin":0,"end":21,"type":"MISPRINT"},{"begin":24,"end":36},{"begin":37,"end":44},{"begin":46,"end":58}]},"tags":["business","landmark","memorable event"],"distance":{"value":8283392.882,"text":"8283.39 км"}},{"title":{"text":"Нулевой километр"},"subtitle":{"text":"Достопримечательность · Свердловская область, Екатеринбург, проспект Ленина, 39","hl":[{"begin":0,"end":21,"type":"MISPRINT"},{"begin":24,"end":36},{"begin":37,"end":44},{"begin":46,"end":58}]},"tags":["business","landmark"],"distance":{"value":8283709.828,"text":"8283.71 км"}},{"title":{"text":"Культурно-просветительский Центр Эрмитаж-Урал"},"subtitle":{"text":"Музей · Свердловская область, Екатеринбург, улица Вайнера, 11","hl":[{"begin":8,"end":20},{"begin":21,"end":28},{"begin":30,"end":42}]},"tags":["business","museum","landmark"],"distance":{"value":8282753.518,"text":"8282.75 км"}}]}
        """;

        when(yandexMapsRepository.sendSuggestRequest(
                eq("Достопримечательности Россия, Свердловская область, Екатеринбург"),
                anyString()))
                .thenReturn(mockJsonResponse);

        String answer = service.getLandmarks("Россия, Свердловская область, Екатеринбург");
        assertEquals("""
Екатеринбургский музей изобразительных искусств
Шарташские каменные палатки
Дом Н. И. Севастьянова
Плотинка
Нулевой километр
Культурно-просветительский Центр Эрмитаж-Урал""", answer);
    }

    @Test
    void getLandmarksWithWrongCity() throws Exception {
        YandexMapsService service = new YandexMapsService(yandexMapsRepository, "fake-api-key");

        String mockJsonResponse = """
        {
            "results": []
        }
        """;

        when(yandexMapsRepository.sendSuggestRequest(
                eq("Достопримечательности fihj=hksfdsk"),
                anyString()))
                .thenReturn(mockJsonResponse);

        String answer = service.getLandmarks("fihj=hksfdsk");
        assertEquals("", answer);
    }

    // Тесты для Яндекс.Расписаний

    @Test
    void getBusSchedule() throws Exception {
        YandexSchedulesService service = new YandexSchedulesService(yandexSchedulesRepository, "fake-api-key");


        LocalDate testDate = LocalDate.of(2024, 11, 23);

        String mockCityCodeEkb = """
        [
            null,
            [
                ["c54", "Екатеринбург", "г. Екатеринбург", "yekaterinburg"],
                ["s9635954", "Екатеринбург, Южный автовокзал", "авт.вкз.", "ekaterinburg-yuzhniy"]
            ]
        ]
        """;

        when(yandexSchedulesRepository.sendCityCode("Екатеринбург"))
                .thenReturn(mockCityCodeEkb);

        String mockCityCodeTmn = """
        [
            null,
            [
                ["c55", "Тюмень", "г. Тюмень", "tyumen"],
                ["s9635955", "Тюмень, автовокзал", "авт.вкз.", "tyumen-bus-station"]
            ]
        ]
        """;

        when(yandexSchedulesRepository.sendCityCode("Тюмень"))
                .thenReturn(mockCityCodeTmn);

        String mockScheduleResponse = """
        {
            "segments": [{
                "from": {"title": "Екатеринбург, Южный автовокзал"},
                "to": {"title": "Тюмень, автовокзал"},
                "departure": "2024-11-23T18:45:00+05:00",
                "arrival": "2024-11-24T00:10:00+05:00",
                "thread": {
                    "carrier": {"title": "ООО \\"ТК Лига\\""}
                },
                "tickets_info": {
                    "places": [{"price": {"whole": 1840}}]
                }
            }]
        }
        """;

        when(yandexSchedulesRepository.getSchedule(
                eq("s9635954"),
                eq("s9635955"),
                eq("2024-11-23"),
                anyString()))
                .thenReturn(mockScheduleResponse);

        String answer = service.findBusRoutes("Екатеринбург", "Тюмень", testDate);
        assertEquals("Рейс: Екатеринбург, Южный автовокзал -> Тюмень, автовокзал\n" +
                "Отправление: 23.11 18:45\n" +
                "Прибытие: 24.11 00:10\n" +
                "Перевозчик: ООО \"ТК Лига\"\n" +
                "Цена: 1840 руб.", answer);
    }

    @Test
    void getNullBusSchedule() throws Exception {
        YandexSchedulesService service = new YandexSchedulesService(yandexSchedulesRepository, "fake-api-key");

        LocalDate testDate = LocalDate.now();

        String mockEmptyResponse = "[null, []]";

        when(yandexSchedulesRepository.sendCityCode("adasfgsdgs"))
                .thenReturn(mockEmptyResponse);

        String answer = service.findBusRoutes("adasfgsdgs", "adasfgsdgs", testDate);
        assertEquals("Возможно вы ввели неправильное название населенного пункта или его не существует в базе", answer);
    }

    //Тесты парсинга

    @Test
    void getCityCode() {
        YandexSchedulesService service = new YandexSchedulesService(yandexSchedulesRepository, "fake-api-key");

        String testJson = """
        [
            null,
            [
                ["c54", "Екатеринбург", "г. Екатеринбург, Свердловская область", "yekaterinburg"],
                ["s9600370", "Кольцово", "а/п Кольцово, Екатеринбург, Свердловская область", "yekaterinburg-koltsovo"],
                ["s9635954", "Екатеринбург, Южный автовокзал", "авт.вкз. Екатеринбург, Южный автовокзал, Екатеринбург", "ekaterinburg-yuzhniy"],
                ["s9635953", "Екатеринбург, Северный автовокзал", "авт.вкз. Екатеринбург, Северный автовокзал, Екатеринбург", "yekaterinburg-northern-bus-terminal"],
                ["s9607404", "Екатеринбург-Пасс.", "вкз. Екатеринбург-Пасс., Екатеринбург", "ekaterinburg"]
            ]
        ]
        """;

        String answer = service.parseCityCode(testJson);
        assertEquals("s9635954", answer);
    }

    @Test
    void testEmptyJsonReturnsNull() {
        YandexSchedulesService service = new YandexSchedulesService(yandexSchedulesRepository, "fake-api-key");

        String emptyJson = "[null, []]";
        String result = service.parseCityCode(emptyJson);
        assertNull(result);
    }

    @Test
    void testNoBusStationsReturnsNull() {
        YandexSchedulesService service = new YandexSchedulesService(yandexSchedulesRepository, "fake-api-key");

        String noBusStationsJson = """
        [
            null,
            [
                ["c54", "Екатеринбург", "г. Екатеринбург", "yekaterinburg"],
                ["s9600370", "Кольцово", "а/п Кольцово", "yekaterinburg-koltsovo"]
            ]
        ]
        """;

        String result = service.parseCityCode(noBusStationsJson);
        assertNull(result);
    }
}
