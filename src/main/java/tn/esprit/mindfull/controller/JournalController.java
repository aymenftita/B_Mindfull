package tn.esprit.mindfull.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import tn.esprit.mindfull.Service.JournalService;
import tn.esprit.mindfull.dto.JournalRequest;
import tn.esprit.mindfull.model.Journal;
import tn.esprit.mindfull.model.Mood;

import java.nio.file.AccessDeniedException;
import java.util.List;

@RestController
@RequestMapping("/api/patient")
@RequiredArgsConstructor
public class JournalController {
    private final JournalService journalService;
   /* @PostMapping("journal/create")
    @PreAuthorize("hasRole('PATIENT')")
    public ResponseEntity<?> createJournalEntry(
            @Validated @RequestBody JournalRequest request,
            Authentication authentication
    ) {
       try {
            String username = authentication.getName();
            Journal createdJournal = journalService.createJournal(request, username);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdJournal);
        } catch (UsernameNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }*/
   @PostMapping("journal/create/{id}")
   @PreAuthorize("hasAuthority('PATIENT')")
   public ResponseEntity<?> createJournalEntry(
           @Validated @RequestBody JournalRequest request,
           Authentication authentication, @PathVariable Long id

   ) {
       try {
           Journal createdJournal = journalService.createJournal(request,authentication,id);
           return ResponseEntity.status(HttpStatus.CREATED).body(createdJournal);
       } catch (UsernameNotFoundException | AccessDeniedException e) {
           return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
       }
   }


    @GetMapping("journal/all")
    @PreAuthorize("hasAuthority('PATIENT')")
    public ResponseEntity<?> getAllJournalEntries(Authentication authentication) {
        try {
            String username = authentication.getName();
            List<Journal> journals = journalService.getAllJournals(username);
            return ResponseEntity.ok(journals);
        } catch (UsernameNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @GetMapping("journal/get/{id}")
    @PreAuthorize("hasAuthority('PATIENT')")
    public ResponseEntity<?> getJournalEntryById(
            @PathVariable Long id,
            Authentication authentication
    ) {
        try {
            Journal journal = journalService.getJournalById(id);
            return ResponseEntity.ok(journal);
        } catch (AccessDeniedException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
        } catch (UsernameNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @PutMapping("journal/update/{id}")
    @PreAuthorize("hasAuthority('PATIENT')")
    public ResponseEntity<?> updateJournalEntry(
            @PathVariable Long id,
            @Validated @RequestBody JournalRequest request,
            Authentication authentication
    ) {
        try {
            Journal updatedJournal = journalService.updateJournal(id, request,  authentication);
            return ResponseEntity.ok(updatedJournal);
        } catch (AccessDeniedException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
        } catch (UsernameNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @DeleteMapping("journal/delete/{id}")
    @PreAuthorize("hasAuthority('PATIENT')")
    public ResponseEntity<?> deleteJournalEntry(
            @PathVariable Long id,
            Authentication authentication
    ) {
        try {
            journalService.deleteJournal(id, authentication);
            return ResponseEntity.noContent().build();
        } catch (AccessDeniedException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
        } catch (UsernameNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @GetMapping("journal/filter/mood")
    @PreAuthorize("hasAuthority('PATIENT')")
    public ResponseEntity<?> getEntriesByMood(
            @RequestParam Mood mood,
            Authentication authentication
    ) {
        try {
            String username = authentication.getName();
            List<Journal> journals = journalService.getJournalsByMood(mood, username);
            return ResponseEntity.ok(journals);
        } catch (UsernameNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
    @GetMapping("journal/user/{userId}")
   // @PreAuthorize("hasAuthority('PATIENT')")
    public ResponseEntity<?> getJournalsByUserId(@PathVariable Long userId) {
        try {
            List<Journal> journals = journalService.getJournalsByUserId(userId);
            return ResponseEntity.ok(journals);
        } catch (UsernameNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
}