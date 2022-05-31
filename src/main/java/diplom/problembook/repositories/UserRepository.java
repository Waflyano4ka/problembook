package diplom.problembook.repositories;

import diplom.problembook.models.Task;
import diplom.problembook.models.User;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface UserRepository extends CrudRepository<User, String> {
    List<User> findByMobileToken(String token);
}
