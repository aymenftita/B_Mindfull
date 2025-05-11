package tn.esprit.mindfull.Controller.AppointmentsController;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import tn.esprit.mindfull.Service.AppointmentsService.AiDoctorNoteService;

@RestController
@RequestMapping("/api/notes")
public class DoctorNoteController {

    @Autowired
    private AiDoctorNoteService aiService;

    @PostMapping("/generate")
    public String generateNote(@RequestBody String summary) {
        return aiService.generateDoctorNote(summary);
    }
}
