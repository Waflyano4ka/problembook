package diplom.problembook.controllers.api;

import diplom.problembook.models.Project;
import diplom.problembook.models.ProjectUser;
import diplom.problembook.models.User;
import diplom.problembook.repositories.ProjectRepository;
import diplom.problembook.repositories.ProjectUserRepository;
import diplom.problembook.repositories.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/project")
public class ProjectRestController {
    private final ProjectRepository projectRepository;
    private final ProjectUserRepository projectUserRepository;
    private final RoleRepository roleRepository;

    @Autowired
    public ProjectRestController(
            ProjectRepository projectRepository,
            ProjectUserRepository projectUserRepository,
            RoleRepository roleRepository) {
        this.projectRepository = projectRepository;
        this.projectUserRepository = projectUserRepository;
        this.roleRepository = roleRepository;
    }

    private Boolean checkUser(User user, Project project) {
        for (ProjectUser projectUser : projectUserRepository.findByUser(user)){
            if (projectUser.getProject() == project)
                return projectUser.getAccess();
        };
        return false;
    }

    @GetMapping("/{id}")
    public ResponseEntity get(@PathVariable(value = "id") Project project, @AuthenticationPrincipal User user){
        if (checkUser(user, project))
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(project);
        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body("Нет доступа к этому проекту");
    }

    @GetMapping("/{id}/members")
    public ResponseEntity members(@PathVariable(value = "id") Project project, @AuthenticationPrincipal User user){
        if (checkUser(user, project)){
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(projectUserRepository.findByProject(project));
        }
        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body("Нет доступа к этому проекту");
    }

    @GetMapping("/{idProject}/access/{idMember}")
    public ResponseEntity changeAccess(@PathVariable(value = "idProject") Project project,
                                      @PathVariable(value = "idMember") ProjectUser projectUser,
                                      @AuthenticationPrincipal User user) {
        if (project.getUser().getId().equals(user.getId())) {
            if (project.getId().equals(projectUser.getProject().getId())) {
                projectUser.setAccess(!projectUser.getAccess());

                return ResponseEntity
                        .status(HttpStatus.OK)
                        .body(projectUserRepository.save(projectUser));
            }
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body("Пользователь не состоит в вашем проекте");
        }
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body("Вы не являетесь владельцем данного проекта");
    }

    @DeleteMapping("/{idProject}/kick/{idMember}")
    public ResponseEntity kickMember(@PathVariable(value = "idProject") Project project,
                                       @PathVariable(value = "idMember") ProjectUser projectUser,
                                       @AuthenticationPrincipal User user) {
        if (project.getUser().getId().equals(user.getId())) {
            if (project.getId().equals(projectUser.getProject().getId())) {
                projectUserRepository.delete(projectUser);

                return ResponseEntity
                        .status(HttpStatus.OK)
                        .body(projectUser);
            }
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body("Пользователь не состоит в вашем проекте");
        }
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body("Вы не являетесь владельцем данного проекта");
    }
}
