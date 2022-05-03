package diplom.problembook.models;

import javax.persistence.*;

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

    public Boolean getInvisible() { return invisible; }

    public void setInvisible(Boolean invisible) { this.invisible = invisible; }

    public Project getProject() { return project; }

    public void setProject(Project project) { this.project = project; }



    public Boolean canSee() {
        if (invisible != null)
            return !invisible;
        return true;
    }
}
