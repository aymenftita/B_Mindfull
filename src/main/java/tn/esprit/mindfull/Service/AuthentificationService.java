package tn.esprit.mindfull.Service;


import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tn.esprit.mindfull.Respository.UserRepository.UserRepository;

@Service
@RequiredArgsConstructor
public class AuthentificationService {
    @Autowired
    private UserRepository userRepository;

}
