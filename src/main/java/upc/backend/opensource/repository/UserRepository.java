package upc.backend.opensource.repository;

import upc.backend.opensource.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    // Buscar usuario por email
    Optional<User> findByEmail(String email);
    Boolean existsByEmail(String email);
}