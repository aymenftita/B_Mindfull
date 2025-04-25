package tn.esprit.mindfull.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    public User getCurrentUser() {
        int staticUserId = 1;
        User existingUser = userRepository.findById(staticUserId).orElse(null);
        if (existingUser != null) {
            return existingUser;
        }

        // Create the user only if it doesn't already exist
        User staticUser = new User();
        staticUser.setName("Chiha");
        staticUser.setLastname("DevUser");
        staticUser.setEmail("chiha@example.com");
        staticUser.setRole("user");
        return userRepository.save(staticUser); // Let the DB assign the ID
    }



    // Create a new user
    public User createUser(User user) {
        return userRepository.save(user);
    }

    // Get a user by ID
    public User getUserById(Integer id) {
        return userRepository.findById(id).orElse(null);
    }

    // Get all users
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    // Update a user
    public User updateUser(User user) {
        return userRepository.save(user);
    }

    // Delete a user by ID
    public void deleteUser(Integer id) {
        userRepository.deleteById(id);
    }
}
