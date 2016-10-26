package adm.virtualcampuswalk.models;

import java.util.Arrays;

/**
 * Created by mariusz on 26.10.16.
 */

public class Faculty {
    private String shortName;
    private String name;
    private byte[] logo;

    public Faculty(String shortName, String name, byte[] logo) {
        this.shortName = shortName;
        this.name = name;
        this.logo = logo;
    }

    public String getShortName() {
        return shortName;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public byte[] getLogo() {
        return logo;
    }

    public void setLogo(byte[] logo) {
        this.logo = logo;
    }

    @Override
    public String toString() {
        return "Faculty{" +
                "shortName='" + shortName + '\'' +
                ", name='" + name + '\'' +
                ", logo=" + Arrays.toString(logo) +
                '}';
    }
}
