package tn.esprit.mindfull.entity.User;

public enum AppRole {
    ADMIN, PATIENT, DOCTOR, COACH;

    public String toSpringRole() {
        return "ROLE_" + this.name();
    }
}
