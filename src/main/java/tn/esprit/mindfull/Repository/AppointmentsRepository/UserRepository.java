package tn.esprit.mindfull.Repository.AppointmentsRepository;

import tn.esprit.mindfull.entity.Appointment.User; // Correct import
import tn.esprit.mindfull.entity.Appointment.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Integer> {
    Optional<User> findByUserIdAndRole(Integer userId, UserRole role);
    List<User> findByRoleIn(List<UserRole> roles);

}