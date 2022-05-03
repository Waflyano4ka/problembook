package diplom.problembook.models;

import javax.persistence.*;
import java.util.Collection;

@Entity
public class Project {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String color;
    private String keyToConnect;
    private String dailyMessage;
    private Boolean active;
    @ManyToOne(optional = true)
    private User user;

    public Project(String name, String color, String keyToConnect, Boolean active) {
        this.name = name;
        this.color = color;
        this.keyToConnect = keyToConnect;
        this.active = active;
    }

    public Project() {

    }

    public Long getId() { return id; }

    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }

    public void setName(String name) { this.name = name; }

    public String getColor() { return color; }

    public void setColor(String color) { this.color = color; }

    public String getKeyToConnect() { return keyToConnect; }

    public void setKeyToConnect(String keyToConnect) { this.keyToConnect = keyToConnect; }

    public String getDailyMessage() { return dailyMessage; }

    public void setDailyMessage(String dailyMessage) { this.dailyMessage = dailyMessage; }

    public Boolean getActive() { return active; }

    public void setActive(Boolean active) { this.active = active;}

    public User getUser() { return user; }

    public void setUser(User user) { this.user = user; }
}
