package pl.polsl.skarbonka.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.polsl.skarbonka.model.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
}
