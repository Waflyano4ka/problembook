package diplom.problembook.helpers;

import diplom.problembook.models.Project;
import diplom.problembook.models.ProjectUser;
import diplom.problembook.models.User;
import diplom.problembook.repositories.ProjectRepository;
import diplom.problembook.repositories.ProjectUserRepository;
import diplom.problembook.repositories.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

public final class ProjectHelper {

    /**
     * Различные проверки пользователя перед удалением или блокировкой
     *
     * @param project Проект
     * @param user Данные аунтификации пользователя
     * @param member Участник
     * @return ResponseEntity Ошибки если они есть
     */
    public static ResponseEntity CheckUserRoleAndChangedMemberInProject(Project project, User user, ProjectUser member) {
        if (project.getUser().getId().equals(user.getId())) {
            if (project.getId().equals(member.getProject().getId())) {
                if (member.ProjectUserNotDeleted()) {
                    return ResponseEntity
                            .status(HttpStatus.OK)
                            .body("");
                }
            }
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body("Пользователь не состоит в вашем проекте");
        }
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body("Вы не являетесь владельцем данного проекта");
    }

    /**
     * Проверка возможности просмотра пользователем данного проекта
     *
     * @param user Данные аунтификации пользователя
     * @param project Проект
     * @param members Участник
     * @return Boolean Статус возможности просмотра
     */
    public static Boolean checkUser(User user, Project project, List<ProjectUser> members) {
        for (ProjectUser projectUser : members){
            if (projectUser.getProject() == project)
                if (projectUser.getUser().getId().equals(user.getId()) && projectUser.ProjectUserNotDeleted())
                    return projectUser.getAccess();
        };
        return false;
    }

    /**
     * Получения данных пользователя
     *
     * @param user Данные аунтификации пользователя
     * @param members Участник
     * @return ProjectUser Данные пользователя
     */
    public static ProjectUser getMember(User user, List<ProjectUser> members) {
        for (ProjectUser mem : members){
            if (mem.getUser().getId().equals(user.getId())){
                return mem;
            }
        }

        return null;
    }
}
