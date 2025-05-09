package tn.esprit.mindfull.dto.PrescriptionNotedto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDate;

public class NoteDTO {

    private String diagnosis;

    private String notes;

    private String guidance;

    @JsonProperty("expirationDate")
    private LocalDate expirationDate;

    @JsonProperty("patient_id") // Map snake_case to camelCase
    private Long patientId;


    @JsonProperty("coach_id") // Optional, if frontend sends coach_id
    private Long coachId;

    // Getters and Setters
    public String getDiagnosis() { return diagnosis; }
    public void setDiagnosis(String diagnosis) { this.diagnosis = diagnosis; }

    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }

    public String getGuidance() { return guidance; }
    public void setGuidance(String guidance) { this.guidance = guidance; }

    public LocalDate getExpirationDate() { return expirationDate; }
    public void setExpirationDate(LocalDate expirationDate) { this.expirationDate = expirationDate; }

    public Long getPatientId() { return patientId; }
    public void setPatientId(Long patientId) { this.patientId = patientId; }






    public Long getCoachId() { return coachId; }
    public void setCoachId(Long coachId) { this.coachId = coachId; }
}