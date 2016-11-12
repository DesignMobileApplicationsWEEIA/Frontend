package adm.virtualcampuswalk.models;

/**
 * Created by mariusz on 12.11.16.
 */

public class Place {
    private int id;
    private double latitude;
    private double longitude;
    private Building building;
    private int buildingId;

    public Place(int id, double latitude, double longitude, Building building, int buildingId) {
        this.id = id;
        this.latitude = latitude;
        this.longitude = longitude;
        this.building = building;
        this.buildingId = buildingId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public Building getBuilding() {
        return building;
    }

    public void setBuilding(Building building) {
        this.building = building;
    }

    public int getBuildingId() {
        return buildingId;
    }

    public void setBuildingId(int buildingId) {
        this.buildingId = buildingId;
    }

    @Override
    public String toString() {
        return "Place{" +
                "id=" + id +
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                ", building=" + building +
                ", buildingId=" + buildingId +
                '}';
    }
}
