package tn.esprit.mindfull.Repository.UserRepository;

import org.springframework.data.jpa.repository.JpaRepository;
import tn.esprit.mindfull.entity.User.Journal;
import tn.esprit.mindfull.entity.User.Mood;
import tn.esprit.mindfull.entity.User.User;

import java.util.List;

public interface JournalRepository extends JpaRepository<Journal, Long> {
    List<Journal> findByUser(User user);
    List<Journal> findByUserAndMood(User user, Mood mood);
    List<Journal> findByUser_Username(String username);
}