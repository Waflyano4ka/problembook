package diplom.problembook.controllers.api;

import diplom.problembook.models.*;
import diplom.problembook.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static diplom.problembook.helpers.ProjectHelper.getMember;

@RestController
@RequestMapping("api/group")
public class TaskGroupRestController {
    private final ProjectUserRepository projectUserRepository;
    private final ProjectRepository projectRepository;
    private final TaskGroupRepository taskGroupRepository;
    private final TaskRepository taskRepository;

    @Autowired
    public TaskGroupRestController(
            ProjectRepository projectRepository,
            ProjectUserRepository projectUserRepository,
            TaskGroupRepository taskGroupRepository,
            TaskRepository taskRepository) {
        this.projectUserRepository = projectUserRepository;
        this.projectRepository = projectRepository;
        this.taskGroupRepository = taskGroupRepository;
        this.taskRepository = taskRepository;
    }

    /**
     * Получение списка групп
     *
     * @param project Проект
     * @param user Данные аунтификации пользователя
     * @return ResponseEntity Список групп
     */
    @GetMapping("/{id}")
    public ResponseEntity getAllGroups(@PathVariable(value = "id") Project project,
                                   @AuthenticationPrincipal User user) {
        ProjectUser member = getMember(user, projectUserRepository.findByProject(project));
        if (project.getActive()) {
            if(member != null){
                Role role = member.getRole();
                if (role.getName().equals("CREATOR") || role.getName().equals("REDACTOR")){
                    return ResponseEntity
                            .status(HttpStatus.OK)
                            .body(taskGroupRepository.findByProject(project).stream().filter(TaskGroup::canSee));
                }
                return ResponseEntity
                        .status(HttpStatus.BAD_REQUEST)
                        .body("У вас недостаточно прав для добавления новой группы");
            }
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body("У вас нет доступа к этому проекту");
        }
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body("Проект находится в архиве");
    }

    @GetMapping("/{ProjectId}/delete/{GroupId}")
    public ResponseEntity getAllGroups(@PathVariable(value = "ProjectId") Project project,
                                       @PathVariable(value = "GroupId") TaskGroup group,
                                       @AuthenticationPrincipal User user) {
        ProjectUser member = getMember(user, projectUserRepository.findByProject(project));
        if (project.getActive()) {
            if(member != null){
                Role role = member.getRole();
                if (role.getName().equals("CREATOR") || role.getName().equals("REDACTOR")){

                    List<Task> tasks = taskRepository.findByTaskGroup(group);
                    if (tasks.size() > 0) {
                        List<TaskGroup> res = taskGroupRepository.findByProject(project).stream().filter(TaskGroup::getInvisible).toList();

                        TaskGroup invisibleGroup;
                        if (res.size() > 0) {
                            invisibleGroup = res.get(0);
                        }
                        else {
                            invisibleGroup = new TaskGroup();
                            invisibleGroup.setProject(project);
                            invisibleGroup.setName("DEFAULT");
                            invisibleGroup.setInvisible(true);

                            taskGroupRepository.save(invisibleGroup);
                        }

                        for (Task t : tasks) {
                            t.setTaskGroup(invisibleGroup);
                        }
                    }

                    if (!group.canSee()) {
                        return ResponseEntity
                                .status(HttpStatus.BAD_REQUEST)
                                .body("Невозможно сделать эту группу невидимой");
                    }

                    taskGroupRepository.delete(group);
                    return ResponseEntity
                            .status(HttpStatus.OK)
                            .body(group);
                }
                return ResponseEntity
                        .status(HttpStatus.BAD_REQUEST)
                        .body("У вас недостаточно прав для добавления новой группы");
            }
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body("У вас нет доступа к этому проекту");
        }
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body("Проект находится в архиве");
    }

    /**
     * Создание группы
     *
     * @param project Проект
     * @param group Название группы
     * @param user Данные аунтификации пользователя
     * @return ResponseEntity Группа
     */
    @PostMapping("/{id}")
    public ResponseEntity addGroup(@PathVariable(value = "id") Project project,
                                   @RequestBody TaskGroup group,
                                   @AuthenticationPrincipal User user) {
        ProjectUser member = getMember(user, projectUserRepository.findByProject(project));
        if (project.getActive()) {
            if(member != null){
                Role role = member.getRole();
                if (role.getName().equals("CREATOR") || role.getName().equals("REDACTOR")){
                    group.setProject(project);
                    if (group.getName().isBlank()) {
                        return ResponseEntity
                                .status(HttpStatus.BAD_REQUEST)
                                .body("Название группы пустое или состоит из пробелов");
                    }
                    List<TaskGroup> res = taskGroupRepository.findByProject(project).stream().filter(g -> g.getName().equals(group.getName())).toList();
                    if (res.size() > 0) {
                        return ResponseEntity
                                .status(HttpStatus.BAD_REQUEST)
                                .body("Группа с таким именем уже существует");
                    }
                    return ResponseEntity
                            .status(HttpStatus.OK)
                            .body(taskGroupRepository.save(group));
                }
                return ResponseEntity
                        .status(HttpStatus.BAD_REQUEST)
                        .body("У вас недостаточно прав для добавления новой группы");
            }
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body("У вас нет доступа к этому проекту");
        }
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body("Проект находится в архиве");
    }

    /**
     * Изменение названия группы
     *
     * @param project Проект
     * @param group Группа
     * @param user Данные аунтификации пользователя
     * @return ResponseEntity Группа с изменными значениями
     */
    @PutMapping("/{id}")
    public ResponseEntity editGroup(@PathVariable(value = "id") Project project,
                                   @RequestBody TaskGroup group,
                                   @AuthenticationPrincipal User user) {
        ProjectUser member = getMember(user, projectUserRepository.findByProject(project));
        if (project.getActive()) {
            if(member != null){
                Role role = member.getRole();
                if (role.getName().equals("CREATOR") || role.getName().equals("REDACTOR")){
                    if (!group.canSee()) {
                        return ResponseEntity
                                .status(HttpStatus.BAD_REQUEST)
                                .body("Невозможно сделать эту группу невидимой");
                    }
                    if (group.getName().isBlank()) {
                        return ResponseEntity
                                .status(HttpStatus.BAD_REQUEST)
                                .body("Название группы пустое или состоит из пробелов");
                    }
                    List<TaskGroup> res = taskGroupRepository.findByProject(project).stream().filter(g -> g.getName().equals(group.getName())).toList();
                    if (res.size() > 0) {
                        if (!res.get(0).getId().equals(group.getId())){
                            return ResponseEntity
                                    .status(HttpStatus.BAD_REQUEST)
                                    .body("Группа с таким именем уже существует");
                        }
                    }
                    return ResponseEntity
                            .status(HttpStatus.OK)
                            .body(taskGroupRepository.save(group));
                }
                return ResponseEntity
                        .status(HttpStatus.BAD_REQUEST)
                        .body("У вас недостаточно прав для добавления новой группы");
            }
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body("У вас нет доступа к этому проекту");
        }
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body("Проект находится в архиве");
    }
}
