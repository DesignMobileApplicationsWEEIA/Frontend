package adm.virtualcampuswalk.models;

import java.util.List;

/**
 * Created by mariusz on 26.10.16.
 */

public class Building {
    private int id;
    private String name;
    private String address;
    private String description;
    private List<Faculty> faculties;
    private List<Place> places;
    private boolean visited;

    public Building(int id, String name, String address, String description, List<Faculty> faculties, List<Place> places, boolean visited) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.description = description;
        this.faculties = faculties;
        this.places = places;
        this.visited = visited;
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

    public List<Place> getPlaces() {
        return places;
    }

    public void setPlaces(List<Place> places) {
        this.places = places;
    }

    public boolean isVisited() {
        return visited;
    }

    public void setVisited(boolean visited) {
        this.visited = visited;
    }

    @Override
    public String toString() {
        return "Building{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", address='" + address + '\'' +
                ", description='" + description + '\'' +
                ", faculties=" + faculties +
                ", places=" + places +
                ", visited=" + visited +
                '}';
    }
}