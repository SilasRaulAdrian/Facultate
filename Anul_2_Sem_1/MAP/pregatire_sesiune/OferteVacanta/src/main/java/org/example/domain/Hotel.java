package org.example.domain;

public class Hotel {
    private final long hotelId;
    private final long locationId;
    private final String hotelName;
    private final int noRooms;
    private final double pricePerNight;
    private final String type; // family/teenagers/oldpeople

    public Hotel(long hotelId, long locationId, String hotelName, int noRooms, double pricePerNight, String type) {
        this.hotelId = hotelId;
        this.locationId = locationId;
        this.hotelName = hotelName;
        this.noRooms = noRooms;
        this.pricePerNight = pricePerNight;
        this.type = type;
    }

    public long getHotelId() { return hotelId; }
    public long getLocationId() { return locationId; }
    public String getHotelName() { return hotelName; }
    public int getNoRooms() { return noRooms; }
    public double getPricePerNight() { return pricePerNight; }
    public String getType() { return type; }

    @Override
    public String toString() {
        return hotelName;
    }
}
