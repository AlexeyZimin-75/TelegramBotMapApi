package org.example.apiMethods.KudaGo;



import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Event {
    private String title;
    private Place place;
    private List<DateRange> dates;
    private String price;

    @JsonProperty("site_url")
    private String siteUrl;

    private List<String> categories;

    public void setPlace(Place place) {
        this.place = place;
    }

    public void setDates(List<DateRange> dates) {
        this.dates = dates;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public void setSiteUrl(String siteUrl) {
        this.siteUrl = siteUrl;
    }

    public void setCategories(List<String> categories) {
        this.categories = categories;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTitle() { return title; }

    public Place getPlace() { return place; }

    public List<DateRange> getDates() { return dates; }

    public String getPrice() { return price; }

    public String getSiteUrl() { return siteUrl; }

    public List<String> getCategories() { return categories; }
}