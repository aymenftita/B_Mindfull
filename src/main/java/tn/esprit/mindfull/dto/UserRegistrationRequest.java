package tn.esprit.mindfull.dto;

import lombok.Data;
import org.antlr.v4.runtime.misc.NotNull;
import tn.esprit.mindfull.model.Role;

@Data
public class UserRegistrationRequest {

    private String username;
    private String password;

    private String firstName;

    private String lastName;


    private String email;

   // private LocalDate birth;
   @NotNull
    private Role role;
}