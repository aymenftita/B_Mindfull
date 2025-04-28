package tn.esprit.mindfull.Respository;

import tn.esprit.mindfull.entity.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<AppUser, Long> {

}