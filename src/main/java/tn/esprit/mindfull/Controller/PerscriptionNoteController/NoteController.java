package tn.esprit.mindfull.Controller.PerscriptionNoteController;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tn.esprit.mindfull.entity.PerscriptionNote.Note;
import tn.esprit.mindfull.Service.PerscriptionNoteService.NoteService;

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

    @PostMapping
    public Note createNote(@RequestBody Note note) {
        return noteService.createNote(note);
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
            String description = noteService.summarizeWithAI(patientId);
            return ResponseEntity.ok(Map.of("message", description));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("message", "Erreur lors de la génération: " + e.getMessage()));
        }
    }
}