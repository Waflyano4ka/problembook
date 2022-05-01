package diplom.problembook.controllers.api;

import diplom.problembook.helpers.ProjectHelper;
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

import java.util.ArrayList;
import java.util.List;

import static diplom.problembook.helpers.ProjectHelper.checkUser;

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

    /**
     * Проверка возможности просмотра пользователем конкретного проекта
     *
     * @param project Данные проекта
     * @param user Данные аунтификации пользователя
     * @return ResponseEntity Данные о проекте
     */
    @GetMapping("/{id}")
    public ResponseEntity getProject(@PathVariable(value = "id") Project project, @AuthenticationPrincipal User user){
        if (checkUser(user, project, projectUserRepository.findByUser(user)))
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(project);
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body("Нет доступа к этому проекту");
    }

    /**
     * Получение участников проекта
     *
     * @param project Проект
     * @param user Данные аунтификации пользователя
     * @return ResponseEntity Участники проекта
     */
    @GetMapping("/{id}/members")
    public ResponseEntity members(@PathVariable(value = "id") Project project, @AuthenticationPrincipal User user){
        if (checkUser(user, project, projectUserRepository.findByUser(user))){
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(projectUserRepository.findByProject(project).stream().filter(ProjectUser::ProjectUserNotDeleted));
        }
        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body("Нет доступа к этому проекту");
    }

    /**
     * Блокировка пользователя
     *
     * @param project Данные проекта
     * @param projectUser Данные участника
     * @param user Данные аунтификации пользователя
     * @return ResponseEntity Измененное значение участника
     */
    @GetMapping("/{idProject}/access/{idMember}")
    public ResponseEntity changeAccess(@PathVariable(value = "idProject") Project project,
                                      @PathVariable(value = "idMember") ProjectUser projectUser,
                                      @AuthenticationPrincipal User user) {
        ResponseEntity response = ProjectHelper.CheckUserRoleAndChangedMemberInProject(project, user, projectUser);
        if (response.getStatusCode().equals(HttpStatus.OK)) {
            projectUser.setAccess(!projectUser.getAccess());

            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(projectUserRepository.save(projectUser));
        }
        return response;
    }

    /**
     * Удаление пользователя из проекта
     *
     * @param project Проект
     * @param projectUser Данные участника
     * @param user Данные аунтификации пользователя
     * @return ResponseEntity Измененное значение участника
     */
    @DeleteMapping("/{idProject}/kick/{idMember}")
    public ResponseEntity kickMember(@PathVariable(value = "idProject") Project project,
                                       @PathVariable(value = "idMember") ProjectUser projectUser,
                                       @AuthenticationPrincipal User user) {
        ResponseEntity response = ProjectHelper.CheckUserRoleAndChangedMemberInProject(project, user, projectUser);
        if (response.getStatusCode().equals(HttpStatus.OK)) {
            projectUser.setDeleted(true);
            projectUserRepository.save(projectUser);

            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(projectUser);
        }
        return response;
    }
}