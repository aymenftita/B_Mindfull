package tn.esprit.mindfull.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import tn.esprit.mindfull.Service.AiDoctorNoteService;

@RestController
@RequestMapping("/api/notes")
@CrossOrigin(origins = "http://localhost:4200") // Allow Angular dev server
public class DoctorNoteController {

    @Autowired
    private AiDoctorNoteService aiService;

    @PostMapping("/generate")
    public String generateNote(@RequestBody String summary) {
        return aiService.generateDoctorNote(summary);
    }
}
