package org.example.service;

public class UserData {
    private String currentCity;
    private String destinationCity;
    private String departureDate;
    private String arrivalDate;

    public String getCurrentCity() { return currentCity; }
    public void setCurrentCity(String currentCity) { this.currentCity = currentCity; }

    public String getDestinationCity() { return destinationCity; }
    public void setDestinationCity(String destinationCity) { this.destinationCity = destinationCity; }

    public String getDepartureDate() { return departureDate; }
    public void setDepartureDate(String departureDate) {this.departureDate = departureDate;}

    public String getArrivalDate() { return arrivalDate; }
    public void setArrivalDate(String arrivalDate) {this.arrivalDate = arrivalDate;}
}
