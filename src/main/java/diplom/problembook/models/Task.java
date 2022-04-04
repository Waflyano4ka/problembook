package diplom.problembook.models;

import com.fasterxml.jackson.annotation.JsonFormat;

import javax.persistence.*;
import java.time.LocalDateTime;

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
    @ManyToOne(optional = true)
    private User author;
    @ManyToOne(optional = true)
    private Project project;

    public Task(String name, String description, Boolean enableTime, Boolean archive, LocalDateTime datetime, User author, Project project) {
        this.name = name;
        this.description = description;
        this.enableTime = enableTime;
        this.archive = archive;
        this.datetime = datetime;
        this.author = author;
        this.project = project;
    }

    public Task() {

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Boolean getEnableTime() {
        return enableTime;
    }

    public void setEnableTime(Boolean enableTime) {
        this.enableTime = enableTime;
    }

    public Boolean getArchive() {
        return archive;
    }

    public void setArchive(Boolean archive) {
        this.archive = archive;
    }

    public LocalDateTime getDatetime() {
        return datetime;
    }

    public void setDatetime(LocalDateTime datetime) {
        this.datetime = datetime;
    }

    public User getAuthor() {
        return author;
    }

    public void setAuthor(User author) {
        this.author = author;
    }

    public Project getProject() {
        return project;
    }

    public void setProject(Project project) {
        this.project = project;
    }
}
