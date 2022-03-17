package diplom.problembook.repositories;

import diplom.problembook.models.Project;
import diplom.problembook.models.ProjectUser;
import diplom.problembook.models.User;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface ProjectUserRepository extends CrudRepository<ProjectUser, Long> {
    List<ProjectUser> findByUser(User user);
    List<ProjectUser> findByProject(Project project);
}
