package diplom.problembook.models;

import javax.persistence.*;

@Entity
public class TaskUser {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Boolean complete;
    @ManyToOne(optional = true)
    private ProjectUser reader;
    @ManyToOne(optional = true)
    private Task task;

    public TaskUser(Boolean complete, ProjectUser reader, Task task) {
        this.complete = complete;
        this.reader = reader;
        this.task = task;
    }

    public TaskUser() {

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Boolean getComplete() {
        return complete;
    }

    public void setComplete(Boolean complete) {
        this.complete = complete;
    }

    public ProjectUser getReader() {
        return reader;
    }

    public void setReader(ProjectUser reader) {
        this.reader = reader;
    }

    public Task getTask() {
        return task;
    }

    public void setTask(Task task) {
        this.task = task;
    }
}
