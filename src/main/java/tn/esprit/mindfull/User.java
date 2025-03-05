package tn.esprit.mindfull;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
@Entity
@Table(name = "user")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Column(name = "birth")
    private Instant birth;

    @Column(name = "calender_id")
    private Integer calenderId;

    @Column(name = "email")
    private String email;

    @Column(name = "lastname")
    private String lastname;

    @Column(name = "name")
    private String name;

    @Column(name = "password")
    private String password;

    @Column(name = "reference")
    private Integer reference;

    @Lob
    @Column(name = "role")
    private String role;

}