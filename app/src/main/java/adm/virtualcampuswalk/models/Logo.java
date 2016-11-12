package adm.virtualcampuswalk.models;

/**
 * Created by mariusz on 12.11.16.
 */

public class Logo {
    private int id;
    private String fileName;
    private String contentType;
    private String content;
    private int buildingId;
    private Building building;

    public Logo(int id, String fileName, String contentType, String content, int buildingId, Building building) {
        this.id = id;
        this.fileName = fileName;
        this.contentType = contentType;
        this.content = content;
        this.buildingId = buildingId;
        this.building = building;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getBuildingId() {
        return buildingId;
    }

    public void setBuildingId(int buildingId) {
        this.buildingId = buildingId;
    }

    public Building getBuilding() {
        return building;
    }

    public void setBuilding(Building building) {
        this.building = building;
    }

    @Override
    public String toString() {
        return "Logo{" +
                "id=" + id +
                ", fileName='" + fileName + '\'' +
                ", contentType='" + contentType + '\'' +
                ", content='" + content + '\'' +
                ", buildingId=" + buildingId +
                ", building=" + building +
                '}';
    }
}
