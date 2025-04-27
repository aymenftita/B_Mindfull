package tn.esprit.mindfull.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tn.esprit.mindfull.entity.Journal;
import tn.esprit.mindfull.entity.Mood;
import tn.esprit.mindfull.entity.User;

import java.util.List;

public interface JournalRepository extends JpaRepository<Journal, Long> {
    List<Journal> findByUser(User user);
    List<Journal> findByUserAndMood(User user, Mood mood);
    List<Journal> findByUser_Username(String username);
}