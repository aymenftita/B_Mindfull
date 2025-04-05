package tn.esprit.mindfull.Repository.AppointmentRepository;

import tn.esprit.mindfull.entity.Appointment.User; // Correct import
import tn.esprit.mindfull.entity.Appointment.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;
import tn.esprit.mindfull.entity.Appointment.UserRole;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Integer> {
    Optional<User> findByUserIdAndRole(Integer userId, UserRole role);
}