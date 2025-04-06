package tn.esprit.mindfull.Respository;

import tn.esprit.mindfull.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}