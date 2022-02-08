package diplom.problembook.models;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Project {
    @Id
    private String id;
    private String name;
    private String color;
    private String keyToConnect;
    private Boolean active;

    public Project(String name, String color, String keyToConnect, Boolean active) {
        this.name = name;
        this.color = color;
        this.keyToConnect = keyToConnect;
        this.active = active;
    }

    public Project() {

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getKeyToConnect() {
        return keyToConnect;
    }

    public void setKeyToConnect(String keyToConnect) {
        this.keyToConnect = keyToConnect;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }
}
