package tn.esprit.mindfull.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {
    @Autowired
    private UserService userService;

    // Create a new user
    @PostMapping
    public User createUser(@RequestBody User user) {
        return userService.createUser(user);
    }

    // Get a user by ID
    @GetMapping("/{id}")
    public User getUserById(@PathVariable Integer id) {
        return userService.getUserById(id);
    }

    // Get all users
    @GetMapping
    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }

    // Update a user
    @PutMapping("/{id}")
    public User updateUser(@PathVariable Integer id, @RequestBody User user) {
        user.setId(id); // Ensure the ID matches the path variable
        return userService.updateUser(user);
    }

    // Delete a user by ID
    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable Integer id) {
        userService.deleteUser(id);
    }
}