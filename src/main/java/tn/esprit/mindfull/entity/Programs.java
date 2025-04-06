package tn.esprit.mindfull.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class Programs {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getMoodTarget() {
        return moodTarget;
    }

    public void setMoodTarget(String moodTarget) {
        this.moodTarget = moodTarget;
    }

    private String name;
    private String description;
    private String category;
    private String moodTarget;
}