package diplom.problembook.controllers.api;

import diplom.problembook.models.Project;
import diplom.problembook.models.ProjectUser;
import diplom.problembook.models.User;
import diplom.problembook.repositories.ProjectRepository;
import diplom.problembook.repositories.ProjectUserRepository;
import diplom.problembook.repositories.RoleRepository;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping("api/projects")
public class ProjectsRestController {
    private final ProjectRepository projectRepository;
    private final ProjectUserRepository projectUserRepository;
    private final RoleRepository roleRepository;

    @Autowired
    public ProjectsRestController(
            ProjectRepository projectRepository,
            ProjectUserRepository projectUserRepository,
            RoleRepository roleRepository) {
        this.projectRepository = projectRepository;
        this.projectUserRepository = projectUserRepository;
        this.roleRepository = roleRepository;
    }

    /**
     * Этот метод передает все проекты к которым есть доспу у пользователя
     *
     * @param user Данные аунтификации пользователя
     * @return Iterable<ProjectUser> Лист проектов
     */
    @GetMapping
    public Iterable<ProjectUser> get(@AuthenticationPrincipal User user){
        return projectUserRepository.findByUser(user).stream().filter(ProjectUser::getAccess).filter(ProjectUser::ProjectUserNotDeleted).toList();
    }

    /**
     * Этот метод создает или изменяет участника-проекта к которому хочет присоединиться пользователь
     *
     * @param keyToConnect Ключ подключения
     * @param user Данные аунтификации пользователя
     * @return ResponseEntity Созданный или измененный данные участника-проекта
     */
    @GetMapping("/{keyToConnect}")
    public ResponseEntity joinToProject(@PathVariable(value = "keyToConnect") String keyToConnect, @AuthenticationPrincipal User user){
        List<Project> projects = projectRepository.findByKeyToConnect(keyToConnect);

        if (projects.size() == 1) {
            for (ProjectUser projectsUser : projectUserRepository.findByUser(user)) {
                if (projectsUser.getProject() == projects.get(0)){
                    if (!projectsUser.getAccess()) {
                        return ResponseEntity
                                .status(HttpStatus.BAD_REQUEST)
                                .body("Вы заблокированы в этом проекте");
                    }
                    if (projectsUser.ProjectUserNotDeleted())
                        return ResponseEntity
                                .status(HttpStatus.BAD_REQUEST)
                                .body("Вы уже есть в этом проекте");
                    else {
                        projectsUser.setDeleted(false);
                        projectsUser.setRole(roleRepository.findByName("READER").get(0));
                        return ResponseEntity
                                .status(HttpStatus.OK)
                                .body(projectUserRepository.save(projectsUser));
                    }
                }
            }

            ProjectUser projectUser = new ProjectUser(
                    roleRepository.findByName("READER").get(0),
                    user,
                    projects.get(0),
                    false,
                    true
            );

            return ResponseEntity
                    .status(HttpStatus.CREATED)
                    .body(projectUserRepository.save(projectUser));
        }

        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body("Ключ недействиетельный либо, указан неверно");
    }

    /**
     * Добавление проекта в избранные
     *
     * @param projectUser Участник
     * @return ProjectUser Возращает измененное значение участника
     */
    @GetMapping("/{id}/like")
    public ProjectUser like(@PathVariable(value = "id") ProjectUser projectUser){
        projectUser.setLiked(!projectUser.getLiked());
        return projectUserRepository.save(projectUser);
    }

    /**
     * Покинуть проект
     *
     * @param projectUser Участник
     * @param user Данные аунтификации пользователя
     * @return ResponseEntity Возвращает измененное значение участника
     */
    @GetMapping("/{id}/leave")
    public ResponseEntity leave(@PathVariable(value = "id") ProjectUser projectUser, @AuthenticationPrincipal User user){
        if (!projectUser.getProject().getUser().getId().equals(user.getId())) {
            projectUser.setDeleted(true);

            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(projectUserRepository.save(projectUser));
        }
        else {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body("Вы являетесь владельцем данного проекта и не можете его покинуть");
        }
    }

    /**
     * Архивация проекта
     *
     * @param projectUser Участник
     * @param user Данные аунтификации пользователя
     * @return ResponseEntity врзвращает измененное значение проекта
     */
    @GetMapping("/{id}/archive")
    public ResponseEntity archive(@PathVariable(value = "id") ProjectUser projectUser, @AuthenticationPrincipal User user){
        Project project = projectUser.getProject();

        if (project.getUser().getId().equals(user.getId())) {
            project.setActive(!project.getActive());
            projectRepository.save(project);

            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(projectUserRepository.findById(projectUser.getId()).orElseThrow());
        }
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body("Вы не являетесь владельцем данного проекта");
    }

    /**
     * Создание нового проекта
     *
     * @param project Данные проекта
     * @param user Данные аунтификации пользователя
     * @return ProjectUser Возвращает значение участника
     */
    @PostMapping
    public ResponseEntity create(@RequestBody Project project, @AuthenticationPrincipal User user) {
        project.setUser(user);
        projectRepository.save(project);

        ProjectUser projectUser = new ProjectUser(
                roleRepository.findByName("CREATOR").get(0),
                user,
                project,
                false,
                true
        );

        if (project.getName().isBlank()) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body("Название проекта пустое или состоит из пробелов");
        }

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(projectUserRepository.save(projectUser));
    }

    /**
     * Обновление данных проекта
     *
     * @param project Проект
     * @param request Изменяемые значения
     * @param user Данные аунтификации пользователя
     * @return ResponseEntity Возвращает измененный проект
     */
    @PutMapping("/{id}")
    public ResponseEntity updateProject(@PathVariable(value = "id") Project project,
                                        @RequestBody String request,
                                        @AuthenticationPrincipal User user) {
        JSONObject data = new JSONObject(request);
        if (project.getUser().getId().equals(user.getId())) {

            if (data.getString("name").isBlank()) {
                return ResponseEntity
                        .status(HttpStatus.BAD_REQUEST)
                        .body("Название проекта пустое или состоит из пробелов");
            }

            project.setName(data.getString("name"));
            project.setColor(data.getString("color"));
            project.setKeyToConnect(data.getString("keyToConnect"));
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(projectRepository.save(project));
        }
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body("Вы не являетесь владельцем данного проекта");
    }
}
