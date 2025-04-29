package tn.esprit.mindfull.Service.PerscriptionNoteService;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import org.apache.commons.lang3.StringEscapeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;
import tn.esprit.mindfull.entity.PerscriptionNote.Note;

import tn.esprit.mindfull.Repository.PerscriptionNoteRepository.NoteRepository;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

import java.util.concurrent.TimeUnit;

@Service
public class NoteServiceImpl implements NoteService {
    @Autowired
    private  NoteRepository noteRepository;

    @Autowired
    private  UserRepository userRepository;
    private  MailService mailService;
    private final RestTemplate restTemplate = new RestTemplate();
    private final ObjectMapper mapper = new ObjectMapper();
    @Value("${openrouter.api.key}")
    private String OPENROUTER_API_KEY;

    private static final String OPENROUTER_API_URL = "https://openrouter.ai/api/v1/chat/completions";

    @Autowired
    private WebClient.Builder webClientBuilder;

    @Override
    public List<Note> getAllNotes() {
        return noteRepository.findAll();
    }

    @Override
    public List<Note> getPatientNotes(int patientId) {
        return noteRepository.findByPatientId(patientId);
    }

    @Override
    public Note getNoteById(int id) {
        return noteRepository.findById(id).orElseThrow(() -> new RuntimeException("Note not found"));
    }

    @Override
    public Note createNote(Note note) {
        note.setCreationDate(LocalDate.now());
        return noteRepository.save(note);
    }
    @Override
    @Transactional
    public Note updateNote(int id, Note updatedNote) {
        Note note = getNoteById(id);
        // update fields
        note.setDiagnosis(updatedNote.getDiagnosis());
        note.setNotes(updatedNote.getNotes());
        note.setGuidance(updatedNote.getGuidance());
        note.setExpirationDate(updatedNote.getExpirationDate());
        note.setUpdateDate(updatedNote.getUpdateDate());
        return noteRepository.save(note);
    }

    @Override
    public void deleteNote(int id) {
        noteRepository.deleteById(id);
    }

    // @Scheduled(cron = "0 0 8 * * ?")
   // @Scheduled(cron = "*/30 * * * * ?")
    public void executeDailyTask() {
        List<Note> listHackathons = noteRepository.findAll();

        Date currentDate = new Date();
        Date nextDay = new Date(currentDate.getTime() + TimeUnit.DAYS.toMillis(1));


        for (Note note : listHackathons) {
            LocalDate localDate = note.getExpirationDate();  // Assuming this returns a LocalDate
            Date startTime = Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
            // calculate the difference in days
            long diffInMillies = startTime.getTime() - nextDay.getTime();
            long daysLeftUntilForum = TimeUnit.DAYS.convert(diffInMillies, TimeUnit.MILLISECONDS);

            if (daysLeftUntilForum <= 2) {
               this.mailService.sendEmail(note.getPatient().getEmail(), "Expiration de la note", note.getDiagnosis());
            }
        }
    }

    public String summarizePatientNotes(int patientId) {
        // fetch notes for the given patient
        List<Note> patientNotes = noteRepository.findByPatientId(patientId);

        // create a StringBuilder to accumulate the summary
        StringBuilder summary = new StringBuilder();

        // loop through all the notes for the patient
        for (Note note : patientNotes) {
            // add diagnosis and notes to the summary
            summary.append("Diagnosis: ").append(note.getDiagnosis()).append("\n");
            summary.append("Notes: ").append(note.getNotes()).append("\n\n");
        }

        // return the summary as a String
        return summary.toString();
    }

    public String summarizeWithAI(int patientId) {
        // retrieve patient notes
        List<Note> patientNotes = noteRepository.findByPatientId(patientId);
        String prompt = buildPrompt(patientNotes);

        // escape special characters to ensure valid JSON
        String escapedPrompt = StringEscapeUtils.escapeJson(prompt);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "Bearer " + OPENROUTER_API_KEY);
        headers.set("User-Agent", "Mindfull (romdhaninour96@gmail.com)");

        // construct the request body with the escaped prompt
        String requestBody = """
    {
     "model": "mistralai/mistral-7b-instruct",
      "messages": [
        {"role": "system", "content": "You are a helpful assistant that summarizes medical notes of a patient."},
        {"role": "user", "content": "%s"}
      ]
    }
    """.formatted(escapedPrompt);  // Use the escaped prompt here

        HttpEntity<String> httpEntity = new HttpEntity<>(requestBody, headers);

        try {
            // send the request and get the response
            ResponseEntity<String> response = restTemplate.postForEntity(OPENROUTER_API_URL, httpEntity, String.class);

            // check if the response is successful
            if (response.getStatusCode().is2xxSuccessful()) {
                JsonNode json = mapper.readTree(response.getBody());

                // make sure the response contains the expected structure before accessing it
                if (json.has("choices") && json.get("choices").isArray() && json.get("choices").size() > 0) {
                    String content = json.at("/choices/0/message/content").asText();
                    return String.join(" ", content.split("\\n"));
                } else {
                    throw new RuntimeException("Unexpected API response structure.");
                }
            } else {
                throw new RuntimeException("API Error: " + response.getStatusCode() + " - " + response.getBody());
            }
        } catch (Exception e) {
            throw new RuntimeException("Error during description generation: " + e.getMessage(), e);
        }
    }

    private String buildPrompt(List<Note> patientNotes) {
        StringBuilder promptBuilder = new StringBuilder();

        promptBuilder.append("You are a medical assistant. Summarize the following patient notes in a clear and concise manner.\n");
        promptBuilder.append("Use bullet points or short sentences when appropriate. Avoid repetition and include only the most relevant information.\n");
        promptBuilder.append("Structure the summary in the following sections:\n");
        promptBuilder.append("1. Diagnosis: Concise overview of the patient's condition(s).\n");
        promptBuilder.append("2. Key Observations: Main points from the doctor's notes.\n");
        promptBuilder.append("3. Medical Advice: Brief summary of guidance or next steps given by the doctor.\n\n");

        promptBuilder.append("Patient Notes:\n");

        for (Note note : patientNotes) {
            promptBuilder.append("- Diagnosis: ").append(note.getDiagnosis()).append("\n");
            promptBuilder.append("- Notes: ").append(note.getNotes()).append("\n");
            promptBuilder.append("- Guidance: ").append(note.getGuidance()).append("\n\n");
        }

        return promptBuilder.toString();
    }


}