package tn.esprit.mindfull.dto;


import lombok.Data;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

@Data
public class UserUpdateRequest {
    private String username;
    private String password;

    private String firstName;

    private String lastName;

    private String email;
    private String avatarUrl;
    private String primaryCarePhysician;
    private Date birth;
    private String workingHours;
    private String contactNumber;
    private List<String> specializations;
    private Integer experienceYears;


}
