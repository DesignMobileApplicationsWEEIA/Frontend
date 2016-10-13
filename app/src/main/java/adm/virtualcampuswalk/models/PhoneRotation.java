package adm.virtualcampuswalk.models;

/**
 * Created by mariusz on 13.10.16.
 */

public class PhoneRotation {
    private double azimuth;
    private double pitch;
    private double roll;

    public PhoneRotation() {
        this.azimuth = 0;
        this.pitch = 0;
        this.roll = 0;
    }

    public PhoneRotation(double azimuth, double pitch, double roll) {
        this.azimuth = azimuth;
        this.pitch = pitch;
        this.roll = roll;
    }

    public double getAzimuth() {
        return azimuth;
    }

    public void setAzimuth(double azimuth) {
        this.azimuth = azimuth;
    }

    public double getPitch() {
        return pitch;
    }

    public void setPitch(double pitch) {
        this.pitch = pitch;
    }

    public double getRoll() {
        return roll;
    }

    public void setRoll(double roll) {
        this.roll = roll;
    }

    @Override
    public String toString() {
        return "PhoneRotation{" +
                "azimuth=" + azimuth +
                ", pitch=" + pitch +
                ", roll=" + roll +
                '}';
    }
}
