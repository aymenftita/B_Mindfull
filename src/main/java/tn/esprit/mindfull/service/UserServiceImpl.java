package tn.esprit.mindfull.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import tn.esprit.mindfull.entity.User;
import tn.esprit.mindfull.entity.Role;
import tn.esprit.mindfull.repository.UserRepository;

import java.util.List;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public User getUserById(int id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with id " + id));
    }

    @Override
    public List<User> getUsersByRole(Role role) {
        return userRepository.findByRole(role);
    }

    @Override
    public User saveUser(User user) {
        return userRepository.save(user);
    }

    @Override
    public User updateUser(int id, User user) {
        User existing = userRepository.findById(id).orElse(null);
        if (existing != null) {
            existing.setFullName(user.getFullName());
            existing.setEmail(user.getEmail());
            existing.setRole(user.getRole());
            return userRepository.save(existing);
        }
        return null;
    }

    @Override
    public void deleteUser(int id) {
        userRepository.deleteById(id);
    }


}
