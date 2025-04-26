package tn.esprit.mindfull.Respository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import tn.esprit.mindfull.model.User;

import java.net.ContentHandler;
import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long>, JpaSpecificationExecutor<User>
{    Optional<User> findByUsername(String username);

    Optional<Object> findByEmail(String email);

    boolean existsByUsername(String username);

    Optional<User> findByResetToken(String token);
    long countByRole(String role);
    long countByAccountStatus(String accountStatus);
    Optional<User> findBySessionToken(String sessionToken);
    List<User> findByUsernameContainingOrEmailContainingOrFirstnameContainingOrLastnameContaining(
            String username, String email, String firstname, String lastname);

}

