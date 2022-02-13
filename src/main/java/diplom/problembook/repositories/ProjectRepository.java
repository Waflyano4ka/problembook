package diplom.problembook.repositories;

import diplom.problembook.models.Project;
import diplom.problembook.models.User;
import org.springframework.data.repository.CrudRepository;

public interface ProjectRepository extends CrudRepository<Project, Long> {
}
