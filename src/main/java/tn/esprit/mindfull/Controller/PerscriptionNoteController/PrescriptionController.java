package tn.esprit.mindfull.Controller.PerscriptionNoteController;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tn.esprit.mindfull.dto.PrescriptionNotedto.PrescriptionRequestDTO;
import tn.esprit.mindfull.entity.PerscriptionNote.Prescription;
import tn.esprit.mindfull.Service.PerscriptionNoteService.PrescriptionService;

import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping("/api/prescriptions")
@AllArgsConstructor
@CrossOrigin(origins = "http://localhost:4200")
public class PrescriptionController {

    private final PrescriptionService prescriptionService;

   /* @PostMapping
    public Prescription createPrescription(@RequestBody Prescription prescription) {
        return prescriptionService.createPrescription(prescription);
    }*/

    /*   @PostMapping(value = "/create", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Note> createNote(@RequestBody NoteDTO noteDTO) {
        Note note = noteService.createNote(noteDTO);
        return ResponseEntity.ok(note);
    }
*/
    @PostMapping(value = "/createpres", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Prescription> createPrescription(
            @Valid @RequestBody PrescriptionRequestDTO dto) {

        Prescription created = prescriptionService.createPrescription(dto);
        return ResponseEntity.ok((created));
    }

    @GetMapping
    public List<Prescription> getAllPrescriptions() {
        return prescriptionService.getAllPrescriptions();
    }

    @GetMapping("/getStatisticsGrowth")
    public HashMap<String, Float> getStatistics() {
        return prescriptionService.getStatistics();
    }

    @GetMapping("/getStatAgeGaps")
    public HashMap<String, Float> getMedicationStatistics() {
        return prescriptionService.getMedicationStatistics();
    }

    @GetMapping("/{id}")
    public Prescription getPrescriptionById(@PathVariable int id) {
        return prescriptionService.getPrescriptionById(id);
    }

    @DeleteMapping("/{id}")
    public void deletePrescription(@PathVariable int id) {
        prescriptionService.deletePrescription(id);
    }

    @PutMapping("/{id}")
    public Prescription updatePrescription(@PathVariable int id, @RequestBody Prescription updated) {
        return prescriptionService.updatePrescription(id, updated);
    }

    @GetMapping("/patientPrescription/{id}")
    public List<Prescription> getPatientPrescription(@PathVariable int id) {
        return prescriptionService.getPatientPrescription(id);
    }

}
