package tn.esprit.mindfull.dto.PrescriptionNotedto;

public class MedicationDTO {
    private String medicationName;

    private String directions;


    private Integer duration;


    public String getMedicationName() {
        return  this.medicationName;
    }

    public String getDirections() {
        return  this.directions;
    }

    public int getDuration() {
        return this.duration;
    }
}



