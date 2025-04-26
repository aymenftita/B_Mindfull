package tn.esprit.mindfull.user;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Integer> {
    Optional<User> findByFirstName(String firstName);
    List<User> findByRoleIn(List<UserRole> roles);

}
