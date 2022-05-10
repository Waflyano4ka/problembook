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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping("api/task")
public class TaskRestController {
    private final ProjectUserRepository projectUserRepository;
    private final ProjectRepository projectRepository;
    private final TaskRepository taskRepository;
    private final TaskUserRepository taskUserRepository;
    private final TaskGroupRepository taskGroupRepository;

    @Autowired
    public TaskRestController(
            ProjectRepository projectRepository,
            ProjectUserRepository projectUserRepository,
            TaskRepository taskRepository,
            TaskUserRepository taskUserRepository,
            TaskGroupRepository taskGroupRepository) {
        this.projectUserRepository = projectUserRepository;
        this.projectRepository = projectRepository;
        this.taskRepository = taskRepository;
        this.taskUserRepository = taskUserRepository;
        this.taskGroupRepository = taskGroupRepository;
    }

    @GetMapping("/{id}")
    public ResponseEntity getTask(@PathVariable(value = "id") Task task,
                                   @AuthenticationPrincipal User user) {
        if (task.getArchive()) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body("Задачи не существует");
        }
        List<ProjectUser> members = projectUserRepository.findByProject(task.getProject()).stream().filter(m -> m.getUser().getId().equals(user.getId())).toList();
        if (members.size() > 0){
            ProjectUser mem = members.get(0);
            HashMap<Object, Object> content = new HashMap<>();
            if (mem.getRole().getName().equals("READER")) {
                List<TaskUser> taskUsers = taskUserRepository.findByTask(task);
                for (TaskUser tu : taskUsers) {
                    if (tu.getReader().getUser().getId().equals(user.getId())){

                        content.put("task", task);
                        content.put("role", mem.getRole());
                        content.put("taskUsers", tu);

                        return ResponseEntity
                                .status(HttpStatus.OK)
                                .body(content);
                    }
                }
            }
            else {
                content.put("task", task);
                content.put("role", mem.getRole());
                content.put("taskUsers", taskUserRepository.findByTask(task));

                return ResponseEntity
                        .status(HttpStatus.OK)
                        .body(content);
            }
        }
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body("У вас нет доступа к этой задаче");
    }

    /**
     * Удаление задачи
     *
     * @param task задача
     * @param user Данные аунтификации пользователя
     * @return ResponseEntity Задача, которую удалили
     */
    @GetMapping("/{id}/delete")
    public ResponseEntity deleteTask(@PathVariable(value = "id") Task task,
                                  @AuthenticationPrincipal User user) {
        if (!task.getProject().getActive()){
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body("Проект находится в архиве");
        }
        List<ProjectUser> members = projectUserRepository.findByProject(task.getProject()).stream().filter(m -> m.getUser().getId().equals(user.getId())).toList();
        if (members.size() > 0){
            ProjectUser mem = members.get(0);
            if (!mem.getRole().getName().equals("READER")) {
                task.setArchive(true);

                return ResponseEntity
                        .status(HttpStatus.OK)
                        .body(taskRepository.save(task));
            }
        }
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body("У вас нет доступа к этой задаче");
    }

    @PutMapping("/{id}/edit")
    public ResponseEntity editTask(@PathVariable(value = "id") Task task,
                                   @RequestBody String request,
                                   @AuthenticationPrincipal User user) {
        JSONObject data = new JSONObject(request);
        if (!task.getProject().getActive()){
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body("Проект находится в архиве");
        }
        List<ProjectUser> members = projectUserRepository.findByProject(task.getProject()).stream().filter(m -> m.getUser().getId().equals(user.getId())).toList();
        if (members.size() > 0){
            ProjectUser mem = members.get(0);
            if (!mem.getRole().getName().equals("READER")) {

                task.setName(data.getString("name"));
                task.setDescription(data.getString("description"));
                task.setEditDatetime(LocalDateTime.now());

                return ResponseEntity
                        .status(HttpStatus.OK)
                        .body(taskRepository.save(task));
            }
        }
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body("У вас нет доступа к этой задаче");
    }
}
