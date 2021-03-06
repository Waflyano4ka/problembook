package diplom.problembook.repositories;

import diplom.problembook.models.Project;
import diplom.problembook.models.ProjectUser;
import diplom.problembook.models.User;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface ProjectRepository extends CrudRepository<Project, Long> {
    List<Project> findByKeyToConnect(String keyToConnect);
}