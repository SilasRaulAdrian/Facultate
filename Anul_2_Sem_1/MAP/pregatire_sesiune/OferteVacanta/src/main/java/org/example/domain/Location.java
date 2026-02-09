package org.example.domain;

public class Location {
    private final long locationId;
    private final String locationName;

    public Location(long locationId, String locationName) {
        this.locationId = locationId;
        this.locationName = locationName;
    }

    public long getLocationId() { return locationId; }
    public String getLocationName() { return locationName; }

    @Override public String toString() { return locationName; }
}
