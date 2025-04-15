package tn.esprit.mindfull.Respository;

import org.springframework.data.jpa.repository.JpaRepository;
import tn.esprit.mindfull.entity.User;

public interface UserRepository extends JpaRepository<User, Long> {
}
