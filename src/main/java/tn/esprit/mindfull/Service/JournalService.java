package tn.esprit.mindfull.Service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import tn.esprit.mindfull.Repository.JournalRepository;
import tn.esprit.mindfull.Repository.UserRepository;
import tn.esprit.mindfull.dto.JournalRequest;
import tn.esprit.mindfull.entity.Journal;
import tn.esprit.mindfull.entity.Mood;
import tn.esprit.mindfull.entity.User;

import java.nio.file.AccessDeniedException;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class JournalService {

    private final JournalRepository journalRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    // Create Journal Entry
    public Journal createJournal(JournalRequest request, Authentication authentication ,Long userId) throws AccessDeniedException {

        String currentUserRole = authentication.getAuthorities().iterator().next().getAuthority();

        // Fetch the target user to delete
        User patient = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        if(!currentUserRole.equals("PATIENT")) {
          throw new AccessDeniedException("Only patients can create journal entries");
       }

        Journal journal = new Journal();
        journal.setTitle(request.getTitle());
        journal.setContent(request.getContent());
        journal.setMood(request.getMood());
        journal.setEntryDate(LocalDateTime.now());
        journal.setUser(patient);

        return journalRepository.save(journal);
    }

    // Get All Entries for Patient
    public List<Journal> getAllJournals(String username) {
        User patient = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        return journalRepository.findByUser(patient);
    }

    // Get Single Entry
    public Journal getJournalById(Long id) throws AccessDeniedException {
        Journal journal = journalRepository.findById(id)
                .orElseThrow(() -> new UsernameNotFoundException("Journal entry not found"));



        return journal;
    }

    // Update Journal Entry
    public Journal updateJournal(Long id, JournalRequest request, Authentication authentication) throws AccessDeniedException {



        String currentUserRole = authentication.getAuthorities().iterator().next().getAuthority();

        if(!currentUserRole.equals("PATIENT")) {
            throw new AccessDeniedException("Only patients can create journal entries");
        }

        Journal existingJournal = getJournalById(id);

        if(request.getTitle() != null) {
            existingJournal.setTitle(request.getTitle());
        }
        if(request.getContent() != null) {
            existingJournal.setContent(request.getContent());
        }
        if(request.getMood() != null) {
            existingJournal.setMood(request.getMood());
        }

        return journalRepository.save(existingJournal);
    }

    // Delete Journal Entry
    public void deleteJournal(Long id, Authentication authentication) throws AccessDeniedException {
        String currentUserRole = authentication.getAuthorities().iterator().next().getAuthority();

        if(!currentUserRole.equals("PATIENT")) {
            throw new AccessDeniedException("Only patients can create journal entries");
        }
        Journal journal = getJournalById(id);
        journalRepository.delete(journal);
    }

    // Optional: Get Entries by Mood
    public List<Journal> getJournalsByMood(Mood mood, String username) {
        User patient = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        return journalRepository.findByUserAndMood(patient, mood);
    }
    public List<Journal> getJournalsByUserId(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        return journalRepository.findByUser(user);
    }


}