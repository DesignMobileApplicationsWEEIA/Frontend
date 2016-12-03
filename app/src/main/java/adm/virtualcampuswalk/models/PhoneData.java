package adm.virtualcampuswalk.models;

/**
 * Created by mariusz on 26.10.16.
 */

public class PhoneData {
    private double direction;
    private PhoneLocation phoneLocation;
    private String mac;

    public PhoneData(double direction, PhoneLocation phoneLocation, String mac) {
        this.direction = direction;
        this.phoneLocation = phoneLocation;
        this.mac = mac;
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

    public String getMac() {
        return mac;
    }

    public void setMac(String mac) {
        this.mac = mac;
    }

    @Override
    public String toString() {
        return "PhoneData{" +
                "direction=" + direction +
                ", phoneLocation=" + phoneLocation +
                ", mac='" + mac + '\'' +
                '}';
    }
}
