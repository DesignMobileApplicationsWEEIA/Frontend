package adm.virtualcampuswalk.models;

/**
 * Created by mariusz on 26.10.16.
 */

public class PhoneData {
    private double direction;
    private PhoneLocation phoneLocation;
    private String macAddress;

    public PhoneData(double direction, PhoneLocation phoneLocation, String macAddress) {
        this.direction = direction;
        this.phoneLocation = phoneLocation;
        this.macAddress = macAddress;
    }

    public double getDirection() {
        return direction;
    }

    public void setDirection(double direction) {
        this.direction = direction;
    }

    public PhoneLocation getPhoneLocation() {
        return phoneLocation;
    }

    public void setPhoneLocation(PhoneLocation phoneLocation) {
        this.phoneLocation = phoneLocation;
    }

    public String getMacAddress() {
        return macAddress;
    }

    public void setMacAddress(String macAddress) {
        this.macAddress = macAddress;
    }

    @Override
    public String toString() {
        return "PhoneData{" +
                "direction=" + direction +
                ", phoneLocation=" + phoneLocation +
                ", macAddress='" + macAddress + '\'' +
                '}';
    }
}