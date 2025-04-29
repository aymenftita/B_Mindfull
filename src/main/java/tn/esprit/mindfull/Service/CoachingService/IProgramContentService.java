package tn.esprit.mindfull.Service.CoachingService;

import org.springframework.data.domain.Page;
import tn.esprit.mindfull.entity.Coaching.ProgramContent;

import java.util.List;

public interface IProgramContentService {
    List<ProgramContent> getAllContent();
    ProgramContent getContentById(Long id);
    ProgramContent updateContent(Long contentId, ProgramContent programContent);
    ProgramContent saveContent(ProgramContent programContent);
    void deleteContent(Long id);

    Page<ProgramContent> getPaginatedContents(Long id, int page, int size);


}
