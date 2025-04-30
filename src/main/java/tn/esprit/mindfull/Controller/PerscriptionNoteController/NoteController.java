package tn.esprit.mindfull.Controller.PerscriptionNoteController;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tn.esprit.mindfull.dto.PrescriptionNotedto.NoteDTO;
import tn.esprit.mindfull.dto.PrescriptionNotedto.NoteResponseDTO;
import tn.esprit.mindfull.entity.PerscriptionNote.Note;
import tn.esprit.mindfull.Service.PerscriptionNoteService.NoteService;
import tn.esprit.mindfull.entity.User.User;

import java.util.Collections;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/notes")
@AllArgsConstructor
@CrossOrigin(origins = "http://localhost:4200")
public class NoteController {

    private final NoteService noteService;

    @GetMapping
    public List<Note> getAllNotes() {
        return noteService.getAllNotes();
    }

    @GetMapping("/patientNotes/{idPatient}")
    public List<Note> getPatientNotes(@PathVariable int idPatient) {
        return noteService.getPatientNotes(idPatient);
    }

    @GetMapping("/{id}")
    public Note getNoteById(@PathVariable int id) {
        return noteService.getNoteById(id);
    }

    @PostMapping(value = "/create", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Note> createNote(@RequestBody NoteDTO noteDTO) {
        Note note = noteService.createNote(noteDTO);
        return ResponseEntity.ok(note);
    }

    @PutMapping("/{id}")
    public Note updateNote(@PathVariable int id, @RequestBody Note note) {
        return noteService.updateNote(id, note);
    }

    @DeleteMapping("/{id}")
    public void deleteNote(@PathVariable int id) {
        noteService.deleteNote(id);
    }

    @GetMapping("/summarize-ai/{patientId}")
    public ResponseEntity<Map<String, String>> summarizePatientNotesAI(@PathVariable int patientId) {
        try {
            System.out.println("hello");
            String description = noteService.summarizeWithAI(patientId);
            return ResponseEntity.ok(Map.of("message", description));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("message", "Erreur lors de la génération: " + e.getMessage()));
        }
    }
}