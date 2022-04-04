package diplom.problembook.repositories;

import diplom.problembook.models.ProjectUser;
import diplom.problembook.models.TaskUser;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface TaskUserRepository extends CrudRepository<TaskUser, Long> {
    List<TaskUser> findByReader(ProjectUser projectUser);
}
