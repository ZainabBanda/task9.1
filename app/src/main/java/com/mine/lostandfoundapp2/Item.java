package com.mine.lostandfoundapp2;

public class Item {
    private final int    id;
    private final String name;
    private final String description;
    private final String dateReported;
    private final String location;
    private final String phone;
    private final String status;
    private final double latitude;    // new
    private final double longitude;   // new

    public Item(int id,
                String name,
                String description,
                String dateReported,
                String location,
                String phone,
                String status,
                double latitude,
                double longitude) {
        this.id           = id;
        this.name         = name;
        this.description  = description;
        this.dateReported = dateReported;
        this.location     = location;
        this.phone        = phone;
        this.status       = status;
        this.latitude     = latitude;
        this.longitude    = longitude;
    }

    // existing getters...
    public int    getId()            { return id; }
    public String getName()          { return name; }
    public String getDescription()   { return description; }
    public String getDateReported()  { return dateReported; }
    public String getLocation()      { return location; }
    public String getPhone()         { return phone; }
    public String getStatus()        { return status; }

    // new getters for MapActivity
    public double getLatitude()      { return latitude; }
    public double getLongitude()     { return longitude; }
}
