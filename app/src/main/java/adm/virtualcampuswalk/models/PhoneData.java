package adm.virtualcampuswalk.models;

/**
 * Created by mariusz on 26.10.16.
 */

public class PhoneData {
    private double direction;
    private PhoneLocation phoneLocation;

    public PhoneData(double direction, PhoneLocation phoneLocation) {
        this.direction = direction;
        this.phoneLocation = phoneLocation;
    }

    public PhoneLocation getPhoneLocation() {
        return phoneLocation;
    }

    public void setPhoneLocation(PhoneLocation phoneLocation) {
        this.phoneLocation = phoneLocation;
    }

    public double getDirection() {
        return direction;
    }

    public void setDirection(double direction) {
        this.direction = direction;
    }

    @Override
    public String toString() {
        return "PhoneData{" +
                "direction=" + direction +
                ", phoneLocation=" + phoneLocation +
                '}';
    }
}
