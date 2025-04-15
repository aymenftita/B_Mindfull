package tn.esprit.mindfull.service;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tn.esprit.mindfull.entity.User;
import tn.esprit.mindfull.entity.Role;
import tn.esprit.mindfull.Respository.UserRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    // Create or register a new user (doctor or patient)
    public User addUser(User user) {
        // Validate that the user role is set to either doctor or patient
        if (user.getRole() == null) {
            throw new IllegalArgumentException("Role must be either doctor or patient");
        }
        return userRepository.save(user);
    }

    // Get a list of all users
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    // Get a user by their ID
    public User getUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with ID: " + id));
    }

    // Update user details (including role)
    public User updateUser(Long id, User updatedUser) {
        User existingUser = getUserById(id);

        // You can also add role checks or any other logic here
        existingUser.setName(updatedUser.getName());
        existingUser.setRole(updatedUser.getRole());  // Update the role as well

        return userRepository.save(existingUser);
    }

    // Delete a user by their ID
    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }
}

