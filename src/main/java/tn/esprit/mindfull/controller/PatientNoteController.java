package tn.esprit.mindfull.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tn.esprit.mindfull.dtos.PatientNoteDto;
import tn.esprit.mindfull.service.PatientNoteService;

import java.util.List;

@RestController
@RequestMapping("/api/")
@RequiredArgsConstructor
public class PatientNoteController {
    private final PatientNoteService patientNoteService;

    @PostMapping
    public ResponseEntity<PatientNoteDto> addNote(@RequestBody PatientNoteDto noteDto) {
        return ResponseEntity.ok(patientNoteService.addNote(noteDto));
    }

    @GetMapping
    public ResponseEntity<List<PatientNoteDto>> getAllNotes() {
        return ResponseEntity.ok(patientNoteService.getAllNotes());
    }

    @GetMapping("/{id}")
    public ResponseEntity<PatientNoteDto> getNoteById(@PathVariable Long id) {
        return ResponseEntity.ok(patientNoteService.getNoteById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<PatientNoteDto> updateNote(
            @PathVariable Long id,
            @RequestBody PatientNoteDto noteDto) {
        return ResponseEntity.ok(patientNoteService.updateNote(id, noteDto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteNote(@PathVariable Long id) {
        patientNoteService.deleteNote(id);
        return ResponseEntity.noContent().build();
    }
}