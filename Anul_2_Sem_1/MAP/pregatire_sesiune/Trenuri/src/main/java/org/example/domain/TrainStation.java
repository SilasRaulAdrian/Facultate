package org.example.domain;

public class TrainStation {
    private final int id;
    private final String trainId;
    private final String departureCityId;
    private final String destinationCityId;

    public TrainStation(int id, String trainId, String departureCityId, String destinationCityId) {
        this.id = id;
        this.trainId = trainId;
        this.departureCityId = departureCityId;
        this.destinationCityId = destinationCityId;
    }

    public int getId() { return id; }
    public String getTrainId() { return trainId; }
    public String getDepartureCityId() { return departureCityId; }
    public String getDestinationCityId() { return destinationCityId; }
}
