package tn.esprit.mindfull.Service.PerscriptionNoteService;

import tn.esprit.mindfull.entity.PerscriptionNote.User;
import tn.esprit.mindfull.entity.PerscriptionNote.Role;

import java.util.List;

public interface UserService {
    List<User> getAllUsers();
    User getUserById(int id);
    List<User> getUsersByRole(Role role);
    User saveUser(User user);
    User updateUser(int id, User user);
    void deleteUser(int id);
}
