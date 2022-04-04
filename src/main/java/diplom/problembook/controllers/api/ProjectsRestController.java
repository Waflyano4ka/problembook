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

    @GetMapping
    public Iterable<ProjectUser> get(@AuthenticationPrincipal User user){
        return projectUserRepository.findByUser(user).stream().filter(ProjectUser::getAccess).toList();
    }

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
                    return ResponseEntity
                            .status(HttpStatus.BAD_REQUEST)
                            .body("Вы уже есть в этом проекте");
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

    @GetMapping("/{id}/like")
    public ProjectUser like(@PathVariable(value = "id") ProjectUser projectUser){
        projectUser.setLiked(!projectUser.getLiked());
        return projectUserRepository.save(projectUser);
    }

    @GetMapping("/{id}/leave")
    public ResponseEntity leave(@PathVariable(value = "id") ProjectUser projectUser, @AuthenticationPrincipal User user){
        if (!projectUser.getProject().getUser().getId().equals(user.getId())) {
            projectUserRepository.delete(projectUser);

            HashMap<Object, Object> data = new HashMap<>();
            data.put("id", projectUser.getId());
            data.put("name", projectUser.getProject().getName());

            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(data);
        }
        else {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body("Вы являетесь владельцем данного проекта и не можете его покинуть");
        }
    }

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
        else {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body("Вы не являетесь владельцем данного проекта");
        }
    }

    @PostMapping
    public ProjectUser create(@RequestBody Project project, @AuthenticationPrincipal User user) {
        project.setUser(user);
        projectRepository.save(project);

        ProjectUser projectUser = new ProjectUser(
                roleRepository.findByName("CREATOR").get(0),
                user,
                project,
                false,
                true
        );

        return projectUserRepository.save(projectUser);
    }
    @PutMapping("/{id}")
    public ResponseEntity updateProject(@PathVariable(value = "id") Project project,
                                        @RequestBody String request,
                                        @AuthenticationPrincipal User user) {
        JSONObject data = new JSONObject(request);
        if (project.getUser().getId().equals(user.getId())) {
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
