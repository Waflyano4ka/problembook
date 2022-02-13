package diplom.problembook.models;

import javax.persistence.*;

@Entity
public class ProjectUser {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(optional = false)
    private Role role;
    @ManyToOne(optional = false)
    private User user;
    @ManyToOne(optional = false)
    private Project project;
    private Boolean liked;
    private Boolean access;

    public ProjectUser(Role role, User user, Project project, Boolean liked, Boolean access) {
        this.role = role;
        this.user = user;
        this.project = project;
        this.liked = liked;
        this.access = access;
    }

    public ProjectUser() {

    }

    public Long getId() { return id; }

    public void setId(Long id) { this.id = id; }

    public Role getRole() { return role; }

    public void setRole(Role role) { this.role = role; }

    public User getUser() { return user; }

    public void setUser(User user) { this.user = user; }

    public Project getProject() { return project; }

    public void setProject(Project project) { this.project = project; }

    public Boolean getLiked() { return liked; }

    public void setLiked(Boolean liked) { this.liked = liked; }

    public Boolean getAccess() { return access; }

    public void setAccess(Boolean access) { this.access = access; }
}
