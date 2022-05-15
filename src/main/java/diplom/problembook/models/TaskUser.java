package diplom.problembook.models;

import com.fasterxml.jackson.annotation.JsonFormat;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
public class TaskUser {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Boolean complete;
    private Boolean enableFile;
    private String filename;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy HH:mm")
    private LocalDateTime datetime;
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

    public Boolean getEnableFile() { return enableFile; }

    public void setEnableFile(Boolean enableFile) { this.enableFile = enableFile; }

    public String getFilename() { return filename; }

    public void setFilename(String filename) { this.filename = filename; }

    public LocalDateTime getDatetime() { return datetime; }

    public void setDatetime(LocalDateTime datetime) { this.datetime = datetime;}

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
