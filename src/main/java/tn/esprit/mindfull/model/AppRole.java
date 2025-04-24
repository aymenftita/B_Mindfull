package tn.esprit.mindfull.model;

public enum AppRole {
    ADMIN, PATIENT, DOCTOR, COACH;

    public String toSpringRole() {
        return "ROLE_" + this.name();
    }
}
