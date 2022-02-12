package diplom.problembook.models;

import javax.persistence.*;
import java.util.Collection;

@Entity
public class Project {
    @Id
    private String id;
    private String name;
    private String color;
    private String keyToConnect;
    private Boolean active;
    @ManyToOne(optional = false)
    private User creator;
    @OneToMany(mappedBy = "project", fetch = FetchType.LAZY)
    private Collection<ProjectUser> projectUsers;

    public Project(String name, String color, String keyToConnect, Boolean active) {
        this.name = name;
        this.color = color;
        this.keyToConnect = keyToConnect;
        this.active = active;
    }

    public Project() {

    }

    public String getId() { return id; }

    public void setId(String id) { this.id = id; }

    public String getName() { return name; }

    public void setName(String name) { this.name = name; }

    public String getColor() { return color; }

    public void setColor(String color) { this.color = color; }

    public String getKeyToConnect() { return keyToConnect; }

    public void setKeyToConnect(String keyToConnect) { this.keyToConnect = keyToConnect; }

    public Boolean getActive() { return active; }

    public void setActive(Boolean active) { this.active = active;}

    public User getCreator() { return creator; }

    public void setCreator(User creator) { this.creator = creator; }

    public Collection<ProjectUser> getProjectUsers() { return projectUsers; }

    public void setProjectUsers(Collection<ProjectUser> projectUsers) { this.projectUsers = projectUsers; }
}