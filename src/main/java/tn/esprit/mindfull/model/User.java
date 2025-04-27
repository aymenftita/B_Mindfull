package tn.esprit.mindfull.model;


import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

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
    @Column(name = "role", nullable = false)
    private String role;
    private String sessionToken;







    //  patient attributes
    private Date birth;
    private Date lastSessionDate;
    private String primaryCarePhysician;
    private Date nextAppointment;


//doctor attributes
    private String workingHours;
    private String contactNumber;
    private String Specializations;
    private String experienceYears;


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // Return the role as a Spring Security authority
        return List.of(new SimpleGrantedAuthority(role));
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public String getUsername() {
        return this.username; // Verify this is correctly implemented
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

    public void setRole(String role) {
        this.role = role.startsWith("ROLE_") ? role : "ROLE_" + role;
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

