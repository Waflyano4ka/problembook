package diplom.problembook.models;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.util.Collection;

@Entity
public class Role {
    @Id
    private String id;
    private String name;
    @OneToMany(mappedBy = "role", fetch = FetchType.LAZY)
    private Collection<ProjectUser> projectUsers;

    public Role(String name) {
        this.name = name;
    }

    public Role() {

    }

    public String getId() { return id; }

    public void setId(String id) { this.id = id; }

    public String getName() { return name; }

    public void setName(String name) { this.name = name; }

    public Collection<ProjectUser> getProjectUsers() { return projectUsers; }

    public void setProjectUsers(Collection<ProjectUser> projectUsers) { this.projectUsers = projectUsers; }
}
