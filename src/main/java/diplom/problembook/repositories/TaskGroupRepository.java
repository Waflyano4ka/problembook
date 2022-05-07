package diplom.problembook.repositories;

import diplom.problembook.models.TaskGroup;
import diplom.problembook.models.Project;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface TaskGroupRepository extends CrudRepository<TaskGroup, Long> {
    List<TaskGroup> findByProject(Project project);
    List<TaskGroup> findByName(String name);
}
