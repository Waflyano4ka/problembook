package diplom.problembook.models;

import com.fasterxml.jackson.annotation.JsonFormat;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

@Entity
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String description;
    private Boolean enableTime;
    private Boolean archive;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy HH:mm")
    private LocalDateTime datetime;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy HH:mm")
    private LocalDateTime createDatetime;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy HH:mm")
    private LocalDateTime editDatetime;
    @ManyToOne(optional = true)
    private User author;
    @ManyToOne(optional = true)
    private TaskGroup taskGroup;
    @ManyToOne(optional = true)
    private Project project;

    public Task(String name, String description, Boolean enableTime, Boolean archive, LocalDateTime datetime, User author, TaskGroup taskGroup, Project project) {
        this.name = name;
        this.description = description;
        this.enableTime = enableTime;
        this.archive = archive;
        this.datetime = datetime;
        this.createDatetime = LocalDateTime.now();
        this.author = author;
        this.taskGroup = taskGroup;
        this.project = project;
    }

    public Task() {

    }

    public Long getId() { return id; }

    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }

    public void setName(String name) { this.name = name; }

    public String getDescription() { return description; }

    public void setDescription(String description) { this.description = description; }

    public Boolean getEnableTime() { return enableTime; }

    public void setEnableTime(Boolean enableTime) { this.enableTime = enableTime; }

    public Boolean getArchive() { return archive; }

    public void setArchive(Boolean archive) { this.archive = archive; }

    public LocalDateTime getDatetime() { return datetime; }

    public LocalDateTime getCreateDatetime() { return createDatetime; }

    public void setCreateDatetime(LocalDateTime createDatetime) { this.createDatetime = createDatetime; }

    public void setDatetime(LocalDateTime datetime) { this.datetime = datetime; }

    public LocalDateTime getEditDatetime() { return editDatetime; }

    public void setEditDatetime(LocalDateTime editDatetime) { this.editDatetime = editDatetime; }

    public User getAuthor() { return author; }

    public void setAuthor(User author) { this.author = author; }

    public TaskGroup getTaskGroup() { return taskGroup; }

    public void setTaskGroup(TaskGroup taskGroup) { this.taskGroup = taskGroup; }

    public Project getProject() { return project; }

    public void setProject(Project project) { this.project = project; }

    public Boolean checkUser(User user, List<TaskUser> taskUsers) {
        for (TaskUser u : taskUsers) {
            if (u.getReader().getUser().getId().equals(user.getId()))
                return true;
        }
        return false;
    }
}
