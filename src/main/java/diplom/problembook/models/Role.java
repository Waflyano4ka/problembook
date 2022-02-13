package diplom.problembook.models;

import javax.persistence.*;
import java.util.Collection;

@Entity
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    @OneToMany(mappedBy = "role", fetch = FetchType.LAZY)
    private Collection<ProjectUser> projectUsers;

    public Role(String name) {
        this.name = name;
    }

    public Role() {

    }

    public Long getId() { return id; }

    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }

    public void setName(String name) { this.name = name; }

    public Collection<ProjectUser> getProjectUsers() { return projectUsers; }

    public void setProjectUsers(Collection<ProjectUser> projectUsers) { this.projectUsers = projectUsers; }
}
