package tn.esprit.mindfull.Service;


import jakarta.annotation.Nullable;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import tn.esprit.mindfull.Respository.UserRepository;
import tn.esprit.mindfull.configuration.JwtUtils;
import tn.esprit.mindfull.dto.UserRegistrationRequest;
import tn.esprit.mindfull.dto.UserUpdateRequest;
import tn.esprit.mindfull.model.AppRole;
import tn.esprit.mindfull.model.User;

import java.net.ContentHandler;
import java.nio.file.AccessDeniedException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.*;

@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtils jwtUtils; // Inject JWT utility class
    private final EmailService emailService;


    public String registerUser(UserRegistrationRequest registrationRequest) {
        // Check for duplicate username
        if (userRepository.findByUsername(registrationRequest.getUsername()).isPresent()) {
            throw new IllegalArgumentException("Username is already taken.");
        }

        // Check for duplicate email
        if (userRepository.findByEmail(registrationRequest.getEmail()).isPresent()) {
            throw new IllegalArgumentException("Email is already registered.");
        }

        User user = new User();
        user.setUsername(registrationRequest.getUsername());
        user.setFirstname(registrationRequest.getFirstName());
        user.setLastname(registrationRequest.getLastName());
        user.setEmail(registrationRequest.getEmail());
        user.setPassword(passwordEncoder.encode(registrationRequest.getPassword()));
        user.setRole(registrationRequest.getRole().toSpringRole()); // Ensure role is prefixed with "ROLE_"

        userRepository.save(user);

        // Return a token for the newly registered user
        return jwtUtils.generateToken(user);
    }
    // New loginUser method
    public String loginUser(String username, String password) {
        // Load user from the database
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));

        // Validate password
        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new BadCredentialsException("Invalid password");
        }

        // Invalidate previous session (if any)
        user.setSessionToken(null);
        userRepository.save(user);

        // Generate new JWT token
        String token = jwtUtils.generateToken(user);

        // Store new session token
        user.setSessionToken(token);
        userRepository.save(user);

        return token;
    }
    public boolean isSessionValid(String username, String token) {
        Optional<User> userOpt = userRepository.findByUsername(username);
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            return token.equals(user.getSessionToken()) && !jwtUtils.isTokenExpired(token);
        }
        return false;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));
    }

    public Optional<User> loadUserById(Long id) {
        return userRepository.findById(id);
    }
    public List<User> getAllUsers() throws AccessDeniedException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUserRole = authentication.getAuthorities().iterator().next().getAuthority();


        if (currentUserRole.equals("ROLE_ADMIN") || currentUserRole.equals("ROLE_DOCTOR") ) {
            return userRepository.findAll();}
        else
        {
            throw new AccessDeniedException("Unauthorized role. Only ADMIN or DOCTOR can get users."); // General error
        }
    }


  public String deleteUser(Long userId) throws AccessDeniedException {
        // Get the current authenticated user's role
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUserRole = authentication.getAuthorities().iterator().next().getAuthority();

        // Fetch the target user to delete
        User targetUser = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        // Check permissions and return messages
        if (currentUserRole.equals("ROLE_ADMIN")) {
            userRepository.deleteById(userId);
            return "User deleted successfully."; // Success message for ADMIN
        } else if (currentUserRole.equals("ROLE_DOCTOR")) {
            if (targetUser.getRole().equals("ROLE_PATIENT")) {
                userRepository.deleteById(userId);
                return "Patient deleted successfully."; // Success message for DOCTOR
            } else {
                throw new AccessDeniedException("Doctors can only delete patients."); // Error for non-patient
            }
        } else {
            throw new AccessDeniedException("Unauthorized role. Only ADMIN or DOCTOR can delete users."); // General error
        }
    }

   public User updateUser(String username, UserUpdateRequest updateRequest, @Nullable AppRole newRole) throws AccessDeniedException {
        // Get the current authenticated user
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUsername = authentication.getName();
        String currentUserRole = authentication.getAuthorities().iterator().next().getAuthority();

        // Fetch the target user
        User targetUser = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));

        // Authorization check
      //  if (!currentUserRole.equals("ROLE_ADMIN") && !currentUserRole.equals("ROLE_DOCTOR") && !currentUsername.equals(username)) {
         //   throw new AccessDeniedException("You can only update your own account");
       // }

        // Update basic fields (non-admin users cannot change role)
        if (updateRequest.getEmail() != null) {
            targetUser.setEmail(updateRequest.getEmail());
        }
        if (updateRequest.getFirstName() != null) {
            targetUser.setFirstname(updateRequest.getFirstName());
        }
        if (updateRequest.getLastName() != null) {
            targetUser.setLastname(updateRequest.getLastName());
        }
        if (updateRequest.getPassword() != null) {
            targetUser.setPassword(passwordEncoder.encode(updateRequest.getPassword())); // Encode new password
        }
        if (updateRequest.getUsername() != null) {
            targetUser.setUsername(updateRequest.getUsername());
        }

        // Only admins can update roles via the newRole parameter
        if (currentUserRole.equals("ROLE_ADMIN") && newRole != null) {
            targetUser.setRole(newRole.toSpringRole()); // Use newRole, not updateRequest
        }

        return userRepository.save(targetUser);
    }

    public void initiatePasswordReset(String email) {
        User user = (User) userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email));

        String resetToken = UUID.randomUUID().toString();
        user.setResetToken(resetToken);
        user.setResetTokenExpiry(Date.from(Instant.now().plus(15, ChronoUnit.MINUTES)));

        userRepository.save(user);

        try {
            emailService.sendPasswordResetEmail(user.getEmail(), resetToken);
        } catch (MessagingException e) {
            throw new RuntimeException("Failed to send reset email", e);
        }
    }
    public void resetPassword(String token, String newPassword) {
        User user = (User) userRepository.findByResetToken(token)
                .orElseThrow(() -> new IllegalArgumentException("Invalid reset token"));

        if (user.getResetTokenExpiry().before(new Date())) {
            throw new IllegalArgumentException("Reset token has expired");
        }

        user.setPassword(passwordEncoder.encode(newPassword));
        user.setResetToken(null);
        user.setResetTokenExpiry(null);

        userRepository.save(user);
    }


  /* @Transactional
   public User updateUser(String currentUsername, UserUpdateRequest updateRequest, AppRole newRole) {
       User targetUser = userRepository.findByUsername(currentUsername)
               .orElseThrow(() -> new UsernameNotFoundException("User not found: " + currentUsername));

       // Handle username change first
       if (updateRequest.getUsername() != null && !updateRequest.getUsername().equals(currentUsername)) {
           if (userRepository.existsByUsername(updateRequest.getUsername())) {
               throw new IllegalArgumentException("Username already exists");
           }
           targetUser.setUsername(updateRequest.getUsername());
       }

       // Update other fields
       if (updateRequest.getEmail() != null) {
           targetUser.setEmail(updateRequest.getEmail());
       }
       if (updateRequest.getFirstName() != null) {
           targetUser.setFirstname(updateRequest.getFirstName());
       }
       if (updateRequest.getLastName() != null) {
           targetUser.setLastname(updateRequest.getLastName());
       }
       if (updateRequest.getPassword() != null) {
           targetUser.setPassword(passwordEncoder.encode(updateRequest.getPassword()));
       }
       if (newRole != null) {
           targetUser.setRole("ROLE_" + newRole.name());
       }

       return userRepository.save(targetUser);
   }*/
    // In UserService.java

    public Map<String, Integer> getUserStats() {
        Map<String, Integer> stats = new HashMap<>();
        stats.put("totalUsers", (int) userRepository.count());
        stats.put("activeUsers", (int) userRepository.countByAccountStatus("ACTIVE"));
        stats.put("inactiveUsers", (int) userRepository.countByAccountStatus("INACTIVE"));
        stats.put("doctors", (int) userRepository.countByRole("ROLE_DOCTOR"));
        stats.put("patients", (int) userRepository.countByRole("ROLE_PATIENT"));
        return stats;
    }

    public int countUsers() {
        return (int) userRepository.count();
    }

    public int countByAccountStatus(String status) {
        return (int) userRepository.countByAccountStatus(status);
    }

    public int countDoctors() {
        return (int) userRepository.countByRole("ROLE_DOCTOR");
    }

    public int countPatients() {
        return (int) userRepository.countByRole("ROLE_PATIENT");
    }
    public List<User> searchUsers(String query) {
        return userRepository.findByUsernameContainingOrEmailContainingOrFirstnameContainingOrLastnameContaining(
                query, query, query, query);
    }

}