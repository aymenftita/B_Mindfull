package tn.esprit.mindfull.Respository;

import org.springframework.data.jpa.repository.JpaRepository;
import tn.esprit.mindfull.model.Journal;
import tn.esprit.mindfull.model.Mood;
import tn.esprit.mindfull.model.User;

import java.util.List;

public interface JournalRepository extends JpaRepository<Journal, Long> {
    List<Journal> findByUser(User user);
    List<Journal> findByUserAndMood(User user, Mood mood);
    List<Journal> findByUser_Username(String username);
}