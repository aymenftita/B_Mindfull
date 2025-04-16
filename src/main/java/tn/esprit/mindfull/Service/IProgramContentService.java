package tn.esprit.mindfull.Service;

import tn.esprit.mindfull.entity.ProgramContent;

import java.util.List;

public interface IProgramContentService {
    List<ProgramContent> getAllContent();
    ProgramContent getContentById(Long id);
    ProgramContent updateContent(Long contentId, ProgramContent programContent);
    ProgramContent saveContent(ProgramContent content);
    void deleteContent(Long id);


}
