package tn.esprit.mindfull.Service.PerscriptionNoteService;

import tn.esprit.mindfull.dto.PrescriptionNotedto.NoteDTO;
import tn.esprit.mindfull.entity.PerscriptionNote.Note;

import java.util.List;

public interface NoteService {
    List<Note> getAllNotes();
    List<Note> getPatientNotes(int id);
    Note getNoteById(int id);
    Note createNote(NoteDTO noteDTO);

    // NoteService.java

    Note updateNote(int id, Note note);
    void deleteNote(int id);
    String summarizeWithAI(int patientId);
}