package org.example.apiMethods.KudaGo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class DateRange {
    private Long start;
    private Long end;

    @JsonProperty("start_date")
    private String startDate;

    @JsonProperty("end_date")
    private String endDate;

    public Long getStart() { return start; }

    public Long getEnd() { return end; }

    public String getStartDate() { return startDate; }

    public String getEndDate() { return endDate; }
}