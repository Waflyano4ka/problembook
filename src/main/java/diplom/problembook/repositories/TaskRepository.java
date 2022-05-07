package diplom.problembook.repositories;

import diplom.problembook.models.Project;
import diplom.problembook.models.Task;
import diplom.problembook.models.TaskGroup;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface TaskRepository extends CrudRepository<Task, Long> {
    List<Task> findByProject(Project project);
    List<Task> findByTaskGroup(TaskGroup taskGroup);
}
