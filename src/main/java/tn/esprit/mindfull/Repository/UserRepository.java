package tn.esprit.mindfull.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import tn.esprit.mindfull.entity.Role;
import tn.esprit.mindfull.entity.User;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long>, JpaSpecificationExecutor<User>
{    Optional<User> findByUsername(String username);

    Optional<Object> findByEmail(String email);

    boolean existsByUsername(String username);

    Optional<User> findByResetToken(String token);
    long countByRole(Role role);
    List<User> findAllByRole(Role role);

    long countByAccountStatus(String accountStatus);
    Optional<User> findBySessionToken(String sessionToken);
    List<User> findByUsernameContainingOrEmailContainingOrFirstnameContainingOrLastnameContaining(
            String username, String email, String firstname, String lastname);
    @Query("SELECT FUNCTION('DATE', u.createdAt) AS regDate, COUNT(u) " +
            "FROM User u GROUP BY FUNCTION('DATE', u.createdAt) " )
    List<Object[]> countRegistrationsByDate();

    @Query("SELECT DISTINCT u.primaryCarePhysician FROM User u WHERE u.primaryCarePhysician IS NOT NULL")
    List<String> findAllDoctors();

    // Find all patients by the doctor's name
    @Query("SELECT u FROM User u WHERE u.primaryCarePhysician = :doctorName")
    List<User> findPatientsByDoctorName(@Param("doctorName") String doctorName);
}

