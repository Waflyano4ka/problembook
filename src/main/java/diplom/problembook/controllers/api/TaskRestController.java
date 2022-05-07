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
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import static diplom.problembook.helpers.ProjectHelper.getMember;

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

    /**
     * Изменение ежедневной повестки
     *
     * @param project Проект
     * @param request Текст пвестки
     * @param user Данные аунтификации пользователя
     * @return ResponseEntity данные проекта
     */
    @PutMapping("/{id}/daily")
    public ResponseEntity changeDailyMessage(@PathVariable(value = "id") Project project,
                                      @RequestBody String request,
                                      @AuthenticationPrincipal User user) {
        ProjectUser member = getMember(user, projectUserRepository.findByProject(project));
        if (project.getActive()) {
            if(member != null){
                Role role = member.getRole();
                if (role.getName().equals("CREATOR") || role.getName().equals("REDACTOR")){
                    project.setDailyMessage(new JSONObject(request).getString("data"));

                    return ResponseEntity
                            .status(HttpStatus.OK)
                            .body(projectRepository.save(project));
                }
                return ResponseEntity
                        .status(HttpStatus.BAD_REQUEST)
                        .body("У вас недостаточно прав для добавления задания");
            }
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body("У вас нет доступа к этому проекту");
        }
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body("Проект находится в архиве");
    }

    @GetMapping("/{id}")
    public ResponseEntity getTasks(@PathVariable(value = "id") Project project,
                                   @AuthenticationPrincipal User user) {
        for (ProjectUser mem : projectUserRepository.findByProject(project)){
            if (mem.getUser().getId().equals(user.getId())){
                List<TaskGroup> groups = taskGroupRepository.findByProject(project);
                List<HashMap<Object, Object>> data = new ArrayList<HashMap<Object, Object>>();
                for (TaskGroup group : groups) {
                    List<Task> tasks;
                    if (mem.getRole().getName().equals("READER")) {
                        tasks = taskRepository.findByTaskGroup(group).stream().filter(t -> t.checkUser(user, taskUserRepository.findByTask(t))).toList();
                    }
                    else {
                        tasks = taskRepository.findByTaskGroup(group);
                    }

                    HashMap<Object, Object> content = new HashMap<>();
                    content.put("name", group.getName());
                    content.put("tasks", tasks);
                    data.add(content);
                }
                return ResponseEntity
                        .status(HttpStatus.OK)
                        .body(data);
            }
        }
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body("У вас нет доступа к этому проекту");
    }

    /**
     * Добавление задания
     *
     * @param project проект
     * @param request Данные задания
     * @param user Данные аунтификации пользователя
     * @return ResponseEntity Задание
     */
    @PutMapping("/{id}")
    public ResponseEntity taskAdd(@PathVariable(value = "id") Project project,
                                  @RequestBody String request,
                                  @AuthenticationPrincipal User user) {
        JSONObject data = new JSONObject(request);
        ProjectUser member = getMember(user, projectUserRepository.findByProject(project));
        if (data.getString("name").isBlank()) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body("Название задания не может быть пустым");
        }
        if (project.getActive()) {
            if (member != null) {
                Role role = member.getRole();

                if (role.getName().equals("CREATOR") || role.getName().equals("REDACTOR")) {
                    Task task = new Task();
                    task.setArchive(false);
                    task.setAuthor(user);
                    task.setProject(project);
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
                    task.setDatetime(LocalDateTime.parse(data.getString("datetime"), formatter));
                    task.setCreateDatetime(LocalDateTime.now());
                    task.setName(data.getString("name"));

                    task.setDescription(data.getString("description"));
                    task.setEnableTime(data.getBoolean("enableTime"));

                    if (data.getBoolean("groupEnable")) {
                        JSONObject group =  data.getJSONObject("group");
                        task.setTaskGroup(taskGroupRepository.findById(group.getLong("id")).orElseThrow());
                    }
                    else {
                        TaskGroup invisibleGroup;

                        List<TaskGroup> res = taskGroupRepository.findByProject(project).stream().filter(TaskGroup::getInvisible).toList();
                        if (res.size() > 0) {
                            invisibleGroup = res.get(0);
                            task.setTaskGroup(invisibleGroup);
                        }
                        else {
                            invisibleGroup = new TaskGroup();
                            invisibleGroup.setProject(project);
                            invisibleGroup.setName("DEFAULT");
                            invisibleGroup.setInvisible(true);

                            task.setTaskGroup(taskGroupRepository.save(invisibleGroup));
                        }
                    }

                    taskRepository.save(task);

                    for (Object mem : data.getJSONArray("members")) {
                        taskUserRepository.save(new TaskUser(false, projectUserRepository.findById(Long.parseLong(mem.toString())).orElseThrow(), task));
                    }

                    return ResponseEntity
                            .status(HttpStatus.OK)
                            .body(task);
                }
                return ResponseEntity
                        .status(HttpStatus.BAD_REQUEST)
                        .body("У вас недостаточно прав для добавления задания");
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
