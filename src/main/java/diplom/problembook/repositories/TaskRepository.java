package diplom.problembook.repositories;

import diplom.problembook.models.Project;
import diplom.problembook.models.Task;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface TaskRepository extends CrudRepository<Task, Long> {
    List<Task> findByProject(Project project);
}
