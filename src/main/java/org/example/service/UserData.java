package org.example.service;

public class UserData {
    private String currentCity;
    private String destinationCity;
    private String dateStartOfTravel;
    private String dateEndOfTravel;

    public String getCurrentCity() { return currentCity; }
    public void setCurrentCity(String currentCity) { this.currentCity = currentCity; }

    public String getDestinationCity() { return destinationCity; }
    public void setDestinationCity(String destinationCity) { this.destinationCity = destinationCity; }
}