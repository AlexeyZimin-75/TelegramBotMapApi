package org.example.apiMethods.KudaGo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class EventsResponse {
    private List<Event> results;

    public List<Event> getResults() { return results; }
}
