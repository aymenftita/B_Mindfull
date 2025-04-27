package tn.esprit.mindfull.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "journal_entry")
public class Journal {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String content;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Mood mood;
    private LocalDateTime entryDate;


    @ElementCollection
    private List<String> tags = new ArrayList<>();

    private boolean favorite;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
}