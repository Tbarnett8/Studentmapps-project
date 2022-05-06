package com.example.studentmaps;

public class LocationModel {

    private int id;
    private String Name;
    private String Type;
    private Double Latitude;
    private Double Longitude;

    //get and set methods

    public String getName() {
        return Name;
    }

    public void setName(String Name) {
        this.Name = Name;
    }

    public String getType() {
        return Type;
    }

    public void setType(String Type) {
        this.Type = Type;
    }

    public Double getLatitude() {
        return Latitude;
    }

    public void setLatitude(Double Latitude) {
        this.Latitude = Latitude;
    }

    public Double getLongitude() {
        return Longitude;
    }

    public void setLongitude(Double Longitude) {
        this.Longitude = Longitude;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    // constructor
    public LocationModel(String Name, String Type, Double Latitude, Double Longitude) {
        this.Name = Name;
        this.Type = Type;
        this.Latitude = Latitude;
        this.Longitude = Longitude;
    }

}
