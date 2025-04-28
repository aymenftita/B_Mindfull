package tn.esprit.mindfull.entity;


import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "user", uniqueConstraints = {
        @UniqueConstraint(columnNames = "username"),
        @UniqueConstraint(columnNames = "email")
})
public class User implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    //common attributes
    private Long id;
    private String firstname;
    private String lastname;
    private  String username;
    private String email;
    private String password;
    @Column(name = "account_status")
    private String accountStatus;
    private String avatarUrl;
    @Column(unique = true)
    private String resetToken;
    private Date resetTokenExpiry;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;
    private String sessionToken;
    @Column(
            name = "created_at",
            nullable = false,
            updatable = false,
            columnDefinition = "DATETIME(6) DEFAULT CURRENT_TIMESTAMP"
    )
    @CreationTimestamp
    private LocalDateTime createdAt;








    //  patient attributes
    private Date birth;
    private Date lastSessionDate;
    private String primaryCarePhysician;
    private Date nextAppointment;


//doctor attributes
    private String workingHours;
    private String contactNumber;
    private String specializations;
    private String experienceYears;


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(role.name())); // Directly use role name
    }
    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public String getUsername() {
        return this.username;
    }


    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    public void setRole(Role role) {
        this.role = role;
        if (role != null) {
            System.out.println("Role assigned: " + role.toString());
        } else {
            System.out.println("Warning: Tried to assign a null role!");
        }
    }

    public String getResetToken() { return resetToken; }
    public void setResetToken(String resetToken) { this.resetToken = resetToken; }

    public Date getResetTokenExpiry() { return resetTokenExpiry; }
    public void setResetTokenExpiry(Date resetTokenExpiry) {
        this.resetTokenExpiry = resetTokenExpiry;
    }

    public String getAccountStatus() {
        return accountStatus;
    }

    public void setAccountStatus(String accountStatus) {
        this.accountStatus = accountStatus;
    }
    public String getSessionToken() {
        return sessionToken;
    }

    public void setSessionToken(String sessionToken) {
        this.sessionToken = sessionToken;
    }

}

