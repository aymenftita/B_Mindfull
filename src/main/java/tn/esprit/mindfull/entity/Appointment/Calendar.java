package tn.esprit.mindfull.entity.Appointment;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import tn.esprit.mindfull.user.User;

import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Calendar {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer calendarId;

    @OneToOne
    @JoinColumn(name = "user_id", unique = true)
    @JsonManagedReference
    @EqualsAndHashCode.Exclude  // Exclude from hashCode and equals
    @ToString.Exclude           // Exclude from toString
    private User owner;

    @OneToMany(
            mappedBy = "calendar",
            cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REMOVE}, // Explicitly include REMOVE
            orphanRemoval = true
    )
    @JsonIgnoreProperties("calendar")
    private List<Appointment> appointments = new ArrayList<>();

    @ElementCollection
    @CollectionTable(name = "calendar_events", joinColumns = @JoinColumn(name = "calendar_id"))
    @MapKeyColumn(name = "event_time")
    @Column(name = "event_description")
    @org.hibernate.annotations.Cascade(org.hibernate.annotations.CascadeType.DELETE_ORPHAN)
    private Map<LocalDateTime, String> events = new HashMap<>();
}
