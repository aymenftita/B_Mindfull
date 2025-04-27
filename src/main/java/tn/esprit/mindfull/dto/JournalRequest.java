package tn.esprit.mindfull.dto;

import lombok.Data;
import tn.esprit.mindfull.entity.User.Mood;

@Data
public class JournalRequest {
    private String title;

    private String content;

    private Mood mood;
}