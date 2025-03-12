package tn.esprit.mindfull;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

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
