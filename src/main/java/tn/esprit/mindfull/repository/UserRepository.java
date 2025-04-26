package tn.esprit.mindfull.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tn.esprit.mindfull.entity.User;
import tn.esprit.mindfull.entity.Role; // ✅ This is required


import java.util.Optional;
import java.util.List;

public interface UserRepository extends JpaRepository<User, Integer> {
    Optional<User> findByEmail(String email);

    List<User> findByRole(Role role);
}
