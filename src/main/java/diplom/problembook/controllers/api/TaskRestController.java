package diplom.problembook.controllers.api;

import diplom.problembook.models.*;
import diplom.problembook.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
            if (mem.getRole().getName().equals("READER")) {
                List<TaskUser> taskUsers = taskUserRepository.findByTask(task);
                for (TaskUser tu : taskUsers) {
                    if (tu.getReader().getUser().getId().equals(user.getId())){
                        return ResponseEntity
                                .status(HttpStatus.OK)
                                .body(task);
                    }
                }
            }
            else {
                return ResponseEntity
                        .status(HttpStatus.OK)
                        .body(task);
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
}
