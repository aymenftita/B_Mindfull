package tn.esprit.mindfull.dtos;

import tn.esprit.mindfull.entity.PatientNote;

import java.time.LocalDateTime;

// PatientNoteDto.java
public class PatientNoteDto {
    private Long noteId;
    private Long patientId;
    private String patientName;
    private Long doctorId;
    private String doctorName;
    private Long prescriptionId;
    private LocalDateTime creationDate;
    private String description;
    private LocalDateTime updateDate;

    public Long getNoteId() {
        return noteId;
    }
    public void setNoteId(Long noteId) {
        this.noteId = noteId;
    }

    public Long getDoctorId() {
        return doctorId;
    }
    public void setDoctorId(Long doctorId) {
        this.doctorId = doctorId;
    }
    public String getPatientName() {
        return patientName;
    }
    public void setPatientName(String patientName) {
        this.patientName = patientName;
    }
    public Long getPrescriptionId() {
        return prescriptionId;
    }
    public void setPrescriptionId(Long prescriptionId) {

        this.prescriptionId = prescriptionId;
    }

    public void setPatientId(Long patientId) {
        this.patientId = patientId;
    }

    public void setCreationDate(LocalDateTime creationDate) {
        this.creationDate = creationDate;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setDoctorName(String doctorName) {
        this.doctorName = doctorName;
    }

    public void setUpdateDate(LocalDateTime updateDate) {
        this.updateDate = updateDate;
    }

    public LocalDateTime getCreationDate() {
        return creationDate;
    }

    public LocalDateTime getUpdateDate() {
        return updateDate;
    }

    public Long getPatientId() {
        return patientId;
    }

    public String getDoctorName() {
        return doctorName;
    }

    public String getDescription() {
        return description;
    }

    public PatientNote toEntity() {
        PatientNote note = new PatientNote();
        note.setNoteId(this.noteId);
        note.setDescription(this.description);
        // Note: You'll need to fetch patient, doctor, and prescription entities
        // from repository if you need to set them
        return note;
    }



    // Constructor, getters, and setters
    public static PatientNoteDto fromEntity(PatientNote note) {
        PatientNoteDto dto = new PatientNoteDto();
        dto.setNoteId(note.getNoteId());
        if (note.getPatient() != null) {
            dto.setPatientId(note.getPatient().getUserId());
            dto.setPatientName(note.getPatient().getName());
        }
        if (note.getDoctor() != null) {
            dto.setDoctorId(note.getDoctor().getUserId());
            dto.setDoctorName(note.getDoctor().getName());
        }
        if (note.getPrescription() != null) {
            dto.setPrescriptionId(note.getPrescription().getPrescriptionId());
        }
        dto.setCreationDate(note.getCreationDate());
        dto.setDescription(note.getDescription());
        dto.setUpdateDate(note.getUpdateDate());
        return dto;
    }
}
