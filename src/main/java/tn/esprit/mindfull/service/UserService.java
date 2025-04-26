package tn.esprit.mindfull.service;

import tn.esprit.mindfull.entity.User;
import tn.esprit.mindfull.entity.Role;

import java.util.List;

public interface UserService {
    List<User> getAllUsers();
    User getUserById(int id);
    List<User> getUsersByRole(Role role);
    User saveUser(User user);
    User updateUser(int id, User user);
    void deleteUser(int id);
}
