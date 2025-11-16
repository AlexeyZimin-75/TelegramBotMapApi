package org.example.apiMethods.KudaGo;

import org.junit.jupiter.api.Test;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class KudaGoClientTest {
    private final KudaGoClient kudaGoClient = new KudaGoClient();

    @Test
    void WhenValidMoscowRequestWeMustGetEventsList() {
        List<Event> events = kudaGoClient.returnEvents("msk", "1701388800", "1701993600");

        assertNotNull(events);

    }

    @Test
    void WhenValidSpbRequestWeMustGetEventsList() {
        List<Event> events = kudaGoClient.returnEvents("spb", "1701388800", "1701993600");

        assertNotNull(events);

    }

    @Test
    void WhenSameStartAndEndDate() {
        List<Event> events = kudaGoClient.returnEvents("msk", "1701388800", "1701388800");

        assertNotNull(events);
    }

    @Test
    void checkEventsStructure() {
        List<Event> events = kudaGoClient.returnEvents("msk", "1701388800", "1701993600");

        assertNotNull(events);
        if (!events.isEmpty()) {
            Event firstEvent = events.get(0);
            assertNotNull(firstEvent);

            if (firstEvent.getTitle() != null) {
                assertFalse(firstEvent.getTitle().trim().isEmpty());
            }
        }
    }

    @Test
    void CheckTimeOfExecute() {
        long startTime = System.currentTimeMillis();

        List<Event> events = kudaGoClient.returnEvents("msk", "1701388800", "1701993600");

        long endTime = System.currentTimeMillis();
        long duration = endTime - startTime;

        assertNotNull(events);
        assertTrue(duration < 15000, "Запрос должен выполняться менее 15 секунд. Выполнено за: " + duration + "мс");
        System.out.println("Время выполнения: " + duration + "мс");
    }
}