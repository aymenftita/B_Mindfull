package tn.esprit.mindfull.Service;

import jakarta.transaction.Transactional;

import org.springframework.stereotype.Service;
import tn.esprit.mindfull.Respository.CoachingProgramRepository;
import tn.esprit.mindfull.Respository.ProgramContentRepository;
import tn.esprit.mindfull.entity.CoachingProgram;
import tn.esprit.mindfull.entity.ProgramContent;

import java.util.List;


@Service
public class ProgramContentServiceImpl implements IProgramContentService {
    private final ProgramContentRepository repository;
    private  final CoachingProgramRepository  coachingProgramRepository;
  //  private JInternalFrame updatedContent;

    public ProgramContentServiceImpl(ProgramContentRepository repository, CoachingProgramRepository coachingProgramRepository) {
        this.repository = repository;
        this.coachingProgramRepository = coachingProgramRepository;
    }

    @Override
    public List<ProgramContent> getAllContent() {
        return repository.findAll();
    }

    @Override
    public ProgramContent getContentById(Long id) {
        return repository.findById(id).orElse(null);
    }

    @Override
    public ProgramContent updateContent(Long contentId, ProgramContent programContent) {
        ProgramContent existingContent = repository.findById(contentId)
                .orElseThrow(() -> new IllegalArgumentException("Content not found"));

        // Mise à jour des attributs
        existingContent.setTitle(programContent.getTitle());
        existingContent.setContentType(programContent.getContentType());
        existingContent.setContentDesc(programContent.getContentDesc());
        existingContent.setMediaLink(programContent.getMediaLink());

        return repository.save(existingContent);
    }

    @Override
    @Transactional
    public ProgramContent saveContent(ProgramContent programContent) {
        if (programContent.getCoachingProgram() != null &&
                programContent.getCoachingProgram().getProgramId() != null) {

            CoachingProgram program = coachingProgramRepository.findById(
                            programContent.getCoachingProgram().getProgramId())
                    .orElseThrow(() -> new IllegalArgumentException("Programme non trouvé"));

            programContent.setCoachingProgram(program);
        }
        return repository.save(programContent);
    }
   
    @Override
    public void deleteContent(Long id) {
        repository.deleteById(id);
    }


}
