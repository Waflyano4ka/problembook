package diplom.problembook.controllers.api;

import diplom.problembook.models.Project;
import diplom.problembook.models.ProjectUser;
import diplom.problembook.models.Role;
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

import java.util.ArrayList;
import java.util.List;


@RestController
@RequestMapping("api/roles")
public class RoleRestController {
    private final RoleRepository roleRepository;
    private final ProjectRepository projectRepository;
    private final ProjectUserRepository projectUserRepository;

    @Autowired
    public RoleRestController(
            RoleRepository roleRepository, ProjectRepository projectRepository, ProjectUserRepository projectUserRepository) {
        this.roleRepository = roleRepository;
        this.projectRepository = projectRepository;
        this.projectUserRepository = projectUserRepository;
    }

    /**
     * Получение списка ролей
     *
     * @param user Данные аунтификации пользователя
     * @return Iterable<Role> Список ролей
     */
    @GetMapping
    public Iterable<Role> get(@AuthenticationPrincipal User user){
        return roleRepository.findAll();
    }

    /**
     * Изменение роли участника
     *
     * @param project Проект
     * @param request Данные о изменении роли
     * @param user Данные аунтификации пользователя
     * @return ResponseEntity Список измененных пользователей
     */
    @PutMapping("/{id}/change")
    public ResponseEntity changeRoles(@PathVariable(value = "id") Project project,
                                      @RequestBody String request,
                                      @AuthenticationPrincipal User user) {
        JSONArray data = new JSONObject(request).getJSONArray("data");
        if (project.getUser().getId().equals(user.getId())) {
            List<ProjectUser> projectUsers = projectUserRepository.findByProject(project);
            List<ProjectUser> changedProjectUsers = new ArrayList<ProjectUser>();
            for (int i = 0; i < data.length(); i++) {
                long projectUsersID = data.getJSONObject(i).getLong("projectUserID");
                String roleName = data.getJSONObject(i).getString("roleName");
                if (!roleName.equals("ADMIN")) {
                    try {
                        for (int j = 0; j < projectUsers.size(); j++) {
                            if (projectUsersID == projectUsers.get(j).getId()) {
                                if (projectUsers.get(j).ProjectUserNotDeleted()){
                                    ProjectUser changedProject = projectUserRepository.findById(projectUsersID).orElseThrow();
                                    changedProject.setRole(roleRepository.findByName(roleName).get(0));
                                    changedProjectUsers.add(projectUserRepository.save(changedProject));
                                }
                                else {
                                    return ResponseEntity
                                            .status(HttpStatus.BAD_REQUEST)
                                            .body("Пользователь не состоит в проекте");
                                }
                            }
                        }
                    }
                    catch (Error error) {
                        return ResponseEntity
                                .status(HttpStatus.BAD_REQUEST)
                                .body(error.getMessage());
                    }
                }
                else {
                    return ResponseEntity
                            .status(HttpStatus.BAD_REQUEST)
                            .body("Нельзя выдать роль Администратора");
                }
            }
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(changedProjectUsers);
        }
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body("Вы не являетесь владельцем данного проекта");
    }
}
