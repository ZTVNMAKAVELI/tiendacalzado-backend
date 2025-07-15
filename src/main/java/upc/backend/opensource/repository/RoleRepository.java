package upc.backend.opensource.repository;

import upc.backend.opensource.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;


public interface RoleRepository extends JpaRepository<Role, Long> {
    // rol por su nombre
    Optional<Role> findByName(String name);
}