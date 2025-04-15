package tn.esprit.mindfull.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tn.esprit.mindfull.dtos.PatientNoteDto;
import tn.esprit.mindfull.entity.PatientNote;
import tn.esprit.mindfull.Respository.PatientNoteRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PatientNoteService {
    private final PatientNoteRepository patientNoteRepository;

    public PatientNoteDto addNote(PatientNoteDto noteDto) {
        PatientNote note = new PatientNote();
        // Map DTO to entity (you might need to fetch related entities)
        note.setDescription(noteDto.getDescription());
        // Set other fields as needed

        PatientNote savedNote = patientNoteRepository.save(note);
        return PatientNoteDto.fromEntity(savedNote);
    }


    public List<PatientNoteDto> getAllNotes() {
        return patientNoteRepository.findAll().stream()
                .map(PatientNoteDto::fromEntity)
                .collect(Collectors.toList());
    }

    public PatientNoteDto getNoteById(Long id) {
        return patientNoteRepository.findById(id)
                .map(PatientNoteDto::fromEntity)
                .orElseThrow(() -> new RuntimeException("Note not found"));
    }

    public PatientNoteDto updateNote(Long id, PatientNoteDto noteDto) {
        PatientNote existingNote = patientNoteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Note not found"));

        existingNote.setDescription(noteDto.getDescription());
        // Update other fields as needed

        PatientNote updatedNote = patientNoteRepository.save(existingNote);
        return PatientNoteDto.fromEntity(updatedNote);
    }

    public void deleteNote(Long id) {
        patientNoteRepository.deleteById(id);
    }
}