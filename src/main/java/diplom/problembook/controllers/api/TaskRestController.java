package diplom.problembook.controllers.api;

import diplom.problembook.models.*;
import diplom.problembook.repositories.*;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("api/task")
public class TaskRestController {
    private final ProjectUserRepository projectUserRepository;
    private final ProjectRepository projectRepository;
    private final TaskRepository taskRepository;
    private final TaskUserRepository taskUserRepository;

    @Autowired
    public TaskRestController(
            ProjectRepository projectRepository,
            ProjectUserRepository projectUserRepository,
            TaskRepository taskRepository,
            TaskUserRepository taskUserRepository) {
        this.projectUserRepository = projectUserRepository;
        this.projectRepository = projectRepository;
        this.taskRepository = taskRepository;
        this.taskUserRepository = taskUserRepository;
    }

    @GetMapping("/{id}")
    public ResponseEntity getTasks(@PathVariable(value = "id") Project project,
                                   @AuthenticationPrincipal User user) {
        for (ProjectUser mem : projectUserRepository.findByProject(project)){
            if (mem.getUser().getId().equals(user.getId())){
                if (mem.getRole().getName().equals("READER")) {
                    List<Task> tasks = new ArrayList<Task>();
                    for (TaskUser taskUser : taskUserRepository.findByReader(mem)){
                        if (taskUser.getReader().getUser().getId().equals(user.getId())){
                            tasks.add(taskUser.getTask());
                        }
                    }
                    return ResponseEntity
                            .status(HttpStatus.OK)
                            .body(tasks);
                }
                return ResponseEntity
                        .status(HttpStatus.OK)
                        .body(taskRepository.findByProject(project));
            }
        }
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body("У вас нет доступа к этому проекту");
    }

    @PutMapping("/{id}")
    public ResponseEntity taskAdd(@PathVariable(value = "id") Project project,
                                  @RequestBody String request,
                                  @AuthenticationPrincipal User user) {
        JSONObject data = new JSONObject(request);
        Role role = new Role();
        for (ProjectUser mem : projectUserRepository.findByProject(project)){
            if (mem.getUser().getId().equals(user.getId())){
                role = mem.getRole();
            }
        }

        if (role.getName().equals("CREATOR") || role.getName().equals("REDACTOR")){
            Task task = new Task();
            task.setArchive(false);
            task.setAuthor(user);
            task.setProject(project);
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
            task.setDatetime(LocalDateTime.parse(data.getString("datetime"),formatter));
            task.setName(data.getString("name"));

            task.setDescription(data.getString("description"));
            task.setEnableTime(data.getBoolean("enableTime"));

            taskRepository.save(task);

            for (Object mem : data.getJSONArray("members")){
                taskUserRepository.save(new TaskUser(false, projectUserRepository.findById(Long.parseLong(mem.toString())).orElseThrow(), task));
            }

            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(task);
        }
        System.out.println(role.getName());
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body("У вас недостаточно прав для добавления задания");
    }
}