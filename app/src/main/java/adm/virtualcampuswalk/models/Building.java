package adm.virtualcampuswalk.models;

import java.util.List;

/**
 * Created by mariusz on 26.10.16.
 */

public class Building {
    private String name;
    private String address;
    private String description;
    private List<Faculty> faculties;

    public Building(String name, String address, String description, List<Faculty> faculties) {
        this.name = name;
        this.address = address;
        this.description = description;
        this.faculties = faculties;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<Faculty> getFaculties() {
        return faculties;
    }

    public void setFaculties(List<Faculty> faculties) {
        this.faculties = faculties;
    }

    @Override
    public String toString() {
        return "Building{" +
                "name='" + name + '\'' +
                ", address='" + address + '\'' +
                ", description='" + description + '\'' +
                ", faculties=" + faculties +
                '}';
    }
}
