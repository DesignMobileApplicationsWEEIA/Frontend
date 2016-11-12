package adm.virtualcampuswalk.models;

import java.util.Arrays;

/**
 * Created by mariusz on 26.10.16.
 */

public class Faculty {
    private int id;
    private String name;
    private String shortName;
    private int logoId;
    private Logo logo;

    public Faculty(int id, String name, String shortName, int logoId, Logo logo) {
        this.id = id;
        this.name = name;
        this.shortName = shortName;
        this.logoId = logoId;
        this.logo = logo;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getShortName() {
        return shortName;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName;
    }

    public int getLogoId() {
        return logoId;
    }

    public void setLogoId(int logoId) {
        this.logoId = logoId;
    }

    public Logo getLogo() {
        return logo;
    }

    public void setLogo(Logo logo) {
        this.logo = logo;
    }

    @Override
    public String toString() {
        return "Faculty{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", shortName='" + shortName + '\'' +
                ", logoId=" + logoId +
                ", logo=" + logo +
                '}';
    }
}
