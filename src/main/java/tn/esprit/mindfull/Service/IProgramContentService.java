package tn.esprit.mindfull.Service;

import jakarta.mail.MessagingException;
import tn.esprit.mindfull.entity.ProgramContent;

import java.util.List;

public interface IProgramContentService {
    List<ProgramContent> getAllContent();
    ProgramContent getContentById(Long id);
    ProgramContent updateContent(Long contentId, ProgramContent programContent);
    ProgramContent saveContent(ProgramContent programContent);
    void deleteContent(Long id);

   // ProgramContent save(ProgramContent programContent);


    // void addProgramContent(Long contentId)throws MessagingException;

    // Méthode pour ajouter un ProgramContent et envoyer un email au patient
    //void addProgramContent(Long contentId, Long userId)throws MessagingException;;
}
