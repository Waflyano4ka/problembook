package diplom.problembook.repositories;

import diplom.problembook.models.Role;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface RoleRepository extends CrudRepository<Role, Long> {
    List<Role> findByName(String name);
}
