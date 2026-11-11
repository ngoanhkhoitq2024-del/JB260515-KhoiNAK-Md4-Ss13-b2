package re.edu.identityservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import re.edu.identityservice.entity.User;

import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {
    boolean existsByUsername(String username);

    User findByUsername(String username);
}
