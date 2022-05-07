package diplom.problembook.models;

import javax.persistence.*;
import java.util.Collection;

@Entity
public class TaskGroup {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private Boolean invisible;
    @ManyToOne(optional = false)
    private Project project;

    public TaskGroup(String name, Project project) {
        this.name = name;
        this.project = project;
    }

    public TaskGroup() {

    }

    public Long getId() { return id; }

    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }

    public void setName(String name) { this.name = name; }

    public Boolean getInvisible() {
        if (invisible != null)
            return invisible;
        return false;
    }

    public void setInvisible(Boolean invisible) { this.invisible = invisible; }

    public Project getProject() { return project; }

    public void setProject(Project project) { this.project = project; }

    public Boolean checkName(String name) {
        return this.name.equals(name);
    }

    public Boolean canSee() {
        if (invisible != null)
            return !invisible;
        return true;
    }
}
