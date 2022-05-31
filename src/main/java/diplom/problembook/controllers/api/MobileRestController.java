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

import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("api/mobile")
public class MobileRestController {
    private final UserRepository userRepository;
    private final ProjectUserRepository projectUserRepository;
    private final TaskRepository taskRepository;
    private final TaskUserRepository taskUserRepository;
    private final TaskGroupRepository taskGroupRepository;

    @Autowired
    public MobileRestController(
            UserRepository userRepository,
            ProjectUserRepository projectUserRepository,
            TaskRepository taskRepository,
            TaskUserRepository taskUserRepository,
            TaskGroupRepository taskGroupRepository) {
        this.userRepository = userRepository;
        this.projectUserRepository = projectUserRepository;
        this.taskRepository = taskRepository;
        this.taskUserRepository = taskUserRepository;
        this.taskGroupRepository = taskGroupRepository;
    }

    @GetMapping("/token")
    public ResponseEntity getTokenTest() {
        List<String> tokens = new ArrayList<>();
        userRepository.findAll().forEach(user -> tokens.add(user.getMobileToken()));

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(tokens);
    }

    @GetMapping("/authorization/{id}")
    public ResponseEntity authorization(@PathVariable(value = "id") String token) {
        List<User> user = userRepository.findByMobileToken(token);
        if (user.size() == 1) {
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(user);
        }
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body("Пользователь не найден");
    }

    @GetMapping("/projects/{id}")
    public ResponseEntity getProjects(@PathVariable(value = "id") String token) {
        List<User> user = userRepository.findByMobileToken(token);
        List<Project> projects = new ArrayList<>();
        if (user.size() == 1) {
            List<ProjectUser> projectUsers = new ArrayList<>();
            projectUsers = projectUserRepository
                    .findByUser(user.get(0))
                    .stream()
                    .filter(ProjectUser::getAccess)
                    .filter(ProjectUser::ProjectUserNotDeleted)
                    .filter(ProjectUser::ProjectNotArchived)
                    .sorted(Comparator.comparing(ProjectUser::ProjectName))
                    .sorted(Comparator.comparingInt(ProjectUser::ProjectUnliked))
                    .collect(Collectors.toList());
            projectUsers.forEach(pu -> projects.add(pu.getProject()));
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(projects);
        }
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body("Ошибка");
    }

    @GetMapping("/archive/{id}")
    public ResponseEntity getArchive(@PathVariable(value = "id") String token) {
        List<User> user = userRepository.findByMobileToken(token);
        List<Project> projects = new ArrayList<>();
        if (user.size() == 1) {
            List<ProjectUser> projectUsers = new ArrayList<>();
            projectUsers = projectUserRepository
                    .findByUser(user.get(0))
                    .stream()
                    .filter(ProjectUser::getAccess)
                    .filter(ProjectUser::ProjectUserNotDeleted)
                    .filter(ProjectUser::ProjectArchived)
                    .sorted(Comparator.comparing(ProjectUser::ProjectName))
                    .sorted(Comparator.comparingInt(ProjectUser::ProjectUnliked))
                    .collect(Collectors.toList());
            projectUsers.forEach(pu -> projects.add(pu.getProject()));
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(projects);
        }
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body("Ошибка");
    }

    @GetMapping("/tasks/{idProject}/{id}")
    public ResponseEntity getTasks(@PathVariable(value = "idProject") Project project,
                                   @PathVariable(value = "id") String token) {
        List<User> users = userRepository.findByMobileToken(token);
        User user = users.get(0);
        for (ProjectUser mem : projectUserRepository.findByProject(project)){
            if (mem.getUser().getId().equals(user.getId())){
                List<TaskGroup> groups = taskGroupRepository.findByProject(project);
                List<Task> tasks = new ArrayList<>();
                List<HashMap<Object, Object>> content = new ArrayList<>();
                for (TaskGroup group : groups) {
                    if (mem.getRole().getName().equals("READER")) {
                        tasks = taskRepository.findByTaskGroup(group).stream().filter(t -> t.checkUser(user, taskUserRepository.findByTask(t))).toList();
                    }
                    else {
                        tasks = taskRepository.findByTaskGroup(group);
                    }
                    tasks = tasks.stream().filter(t -> !t.getArchive()).toList();

                    for (Task t : tasks) {
                        HashMap<Object, Object> task = new HashMap<>();
                        task.put("id", t.getId());
                        task.put("name", t.getName());
                        task.put("description", t.getDescription());
                        task.put("enableTime", t.getEnableTime());
                        task.put("archive", t.getArchive());
                        task.put("datetime", t.getDatetime());
                        task.put("createDatetime", t.getCreateDatetime());
                        task.put("editDatetime", t.getEditDatetime());
                        task.put("author", t.getAuthor());
                        content.add(task);
                    }
                }
                return ResponseEntity
                        .status(HttpStatus.OK)
                        .body(content);
            }
        }
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body("У вас нет доступа к этому проекту");
    }
}
