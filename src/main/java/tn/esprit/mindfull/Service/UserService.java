package tn.esprit.mindfull.Service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tn.esprit.mindfull.Respository.UserRepository;
import tn.esprit.mindfull.entity.User;
import tn.esprit.mindfull.entity.Role;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public List<User> getAllCoachs() {
        return userRepository.findByRole(Role.valueOf(String.valueOf(Role.COACH)));
    }

    public User addUser(User user) {
        return userRepository.save(user);
    }

    public List<User> findUserByRole(String role) {
        Role roleEnum = Role.valueOf(role);  // Convertir la chaîne en énumération
        return userRepository.findByRole(roleEnum);  // Passer l'énumération Role à la méthode
    }


}
