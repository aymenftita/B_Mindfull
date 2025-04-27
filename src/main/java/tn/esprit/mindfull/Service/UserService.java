package tn.esprit.mindfull.Service;


import jakarta.annotation.Nullable;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
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
import tn.esprit.mindfull.model.Role;
import tn.esprit.mindfull.model.User;

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
        user.setRole(registrationRequest.getRole());

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
    // In UserService.java (getAllUsers method)
    public List<User> getAllUsers() throws AccessDeniedException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUserAuthority = authentication.getAuthorities().iterator().next().getAuthority();

        if (currentUserAuthority.equals("ADMIN") || currentUserAuthority.equals("DOCTOR")) {
            return userRepository.findAll();
        } else {
            throw new AccessDeniedException("Unauthorized role. Only ADMIN or DOCTOR can get users.");
        }
    }


  public String deleteUser(Long userId) throws AccessDeniedException {
        // Get the current authenticated user's role
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUserRole = authentication.getAuthorities().iterator().next().getAuthority();

        // Fetch the target user to delete
        User targetUser = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
      System.out.println(currentUserRole);
        // Check permissions and return messages
        if (currentUserRole.equals("ADMIN")) {
            userRepository.deleteById(userId);
            return "User deleted successfully."; // Success message for ADMIN
        } else
            if (currentUserRole.equals("DOCTOR")) {
            System.out.println("hi");

                System.out.println(targetUser.getRole());
                userRepository.deleteById(userId);
                return "Patient deleted successfully.";
        } else {
            throw new AccessDeniedException("Unauthorized role. Only ADMIN or DOCTOR can delete users."); // General error
        }
    }

   public User updateUser(String username, UserUpdateRequest updateRequest, @Nullable Role newRole) throws AccessDeniedException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUserRole = authentication.getAuthorities().iterator().next().getAuthority();

        User targetUser = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));



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
           targetUser.setPassword(passwordEncoder.encode(updateRequest.getPassword()));
       }
       if (updateRequest.getUsername() != null) {
           targetUser.setUsername(updateRequest.getUsername());
       }

       if (updateRequest.getAvatarUrl() != null) {
           targetUser.setAvatarUrl(updateRequest.getAvatarUrl());
       }

       if (updateRequest.getPrimaryCarePhysician() != null) {
           targetUser.setPrimaryCarePhysician(updateRequest.getPrimaryCarePhysician());
       }

       if (updateRequest.getBirth() != null) {
           targetUser.setBirth(updateRequest.getBirth());
       }
       if (updateRequest.getWorkingHours() != null) {
           targetUser.setWorkingHours(updateRequest.getWorkingHours());
       }
       if (updateRequest.getContactNumber() != null) {
           targetUser.setContactNumber(updateRequest.getContactNumber());
       }
       if (updateRequest.getSpecializations() != null) {
           targetUser.setSpecializations(updateRequest.getSpecializations().toString());
       }
       if (updateRequest.getExperienceYears() != null) {
           targetUser.setExperienceYears(String.valueOf(updateRequest.getExperienceYears()));
       }

        // Only admins can update roles via the newRole parameter
        if (currentUserRole.equals("ADMIN") && newRole != null) {
            targetUser.setRole(newRole);
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




    public Map<String, Integer> getUserStats() {
        Map<String, Integer> stats = new HashMap<>();
        stats.put("totalUsers", (int) userRepository.count());
        stats.put("activeUsers", (int) userRepository.countByAccountStatus("ACTIVE"));
        stats.put("inactiveUsers", (int) userRepository.countByAccountStatus("INACTIVE"));
        stats.put("doctors", (int) userRepository.countByRole(Role.DOCTOR));
        stats.put("patients", (int) userRepository.countByRole(Role.PATIENT));
        return stats;
    }

    public int countUsers() {
        return (int) userRepository.count();
    }

    public int countByAccountStatus(String status) {
        return (int) userRepository.countByAccountStatus(status);
    }

    public int countDoctors() {
        return (int) userRepository.countByRole(Role.DOCTOR);
    }

    public int countPatients() {
        return (int) userRepository.countByRole(Role.PATIENT);
    }
    public List<User> searchUsers(String query) {
        return userRepository.findByUsernameContainingOrEmailContainingOrFirstnameContainingOrLastnameContaining(
                query, query, query, query);
    }

}