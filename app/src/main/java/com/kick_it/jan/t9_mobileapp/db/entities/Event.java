package com.kick_it.jan.t9_mobileapp.db.entities;

/*
 * Created by taraszaika on 03.01.18.
 * new Event
 */

public class Event {

    private String objectId;

    private double placeLatitude, placeLongitude;
    private long dateAndTime;
    private int maxPlayersNumber;
    private String description;

    public Event(double placeLatitude, double placeLongitude, long dateAndTime, int maxPlayersNumber, String description) {
        this("", placeLatitude, placeLongitude, dateAndTime, maxPlayersNumber, description);
    }

    public Event(String objectId, double placeLatitude, double placeLongitude, long dateAndTimeFinal, int maxPlayersNumber, String description) {
        setObjectId(objectId);
        setPlaceLatitude(placeLatitude);
        setPlaceLongitude(placeLongitude);
        setDateAndTime(dateAndTimeFinal);
        setDescription(description);
    }

    //setter

    public void setObjectId(String objectId) {
        this.objectId = objectId == null? "" : objectId;
    }

    public void setPlaceLatitude(double placeLatitude) {
        this.placeLatitude = placeLatitude;
    }

    public void setPlaceLongitude(double placeLongitude) {
        this.placeLongitude = placeLongitude;
    }

    public void setDateAndTime(long dateAndTime) {
        this.dateAndTime = dateAndTime == 0 ? -1 : dateAndTime;
    }

    public void setMaaxPlayersNumber(int maxPlayersNumber) {
        this.maxPlayersNumber = maxPlayersNumber == 0? -1 : maxPlayersNumber;
    }

    public void setDescription(String description) {
        this.description = description == null ? "" : description;
    }

    //getter

    public String getObjectId() {
        return objectId;
    }

    public double getPlaceLatitude() {
        return placeLatitude;
    }

    public double getPlaceLongitude() {
        return placeLongitude;
    }

    public long getDateAndTime() {
        return dateAndTime;
    }

    public int getMaaxPlayersNumber() {
        return maxPlayersNumber;
    }

    public String getDescription() {
        return description;
    }

}
