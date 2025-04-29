package tn.esprit.mindfull.Service.CoachingService;

import jakarta.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import tn.esprit.mindfull.Repository.CoachingRepository.CoachingProgramRepository;
import tn.esprit.mindfull.Repository.CoachingRepository.ProgramContentRepository;
import tn.esprit.mindfull.Repository.UserRepository;
import tn.esprit.mindfull.entity.Coaching.CoachingProgram;
import tn.esprit.mindfull.entity.Coaching.ProgramContent;

import java.util.List;


@Service
public class ProgramContentServiceImpl implements IProgramContentService {
    private final ProgramContentRepository repository;
    private  final CoachingProgramRepository  coachingProgramRepository;

    private final UserRepository userRepository;

    @Autowired
    private ProgramContentRepository programContentRepository;
    @Autowired
    private EmailService emailService;


  //  private JInternalFrame updatedContent;

    public ProgramContentServiceImpl(ProgramContentRepository repository, CoachingProgramRepository coachingProgramRepository, UserRepository userRepository) {
        this.repository = repository;
        this.coachingProgramRepository = coachingProgramRepository;
        this.userRepository = userRepository;
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

        // Réattacher l'entité CoachingProgram si nécessaire
        if (programContent.getCoachingProgram() != null) {
            CoachingProgram program = coachingProgramRepository.findById(
                            programContent.getCoachingProgram().getProgramId())
                    .orElseThrow(() -> new IllegalArgumentException("Coaching Program not found"));
            existingContent.setCoachingProgram(program);
        }

        // Mise à jour des autres champs
        existingContent.setTitle(programContent.getTitle());
        existingContent.setContentType(programContent.getContentType());
        existingContent.setContentDesc(programContent.getContentDesc());
        existingContent.setMediaLink(programContent.getMediaLink());

        return repository.save(existingContent);
    }

    @Transactional
    public ProgramContent saveContent(ProgramContent programContent) {
        if (programContent.getCoachingProgram() != null &&
                programContent.getCoachingProgram().getProgramId() != null) {
            // Récupérer l'entité persistante depuis la base de données
            CoachingProgram program = coachingProgramRepository.findById(
                            programContent.getCoachingProgram().getProgramId())
                    .orElseThrow(() -> new IllegalArgumentException("Programme non trouvé"));
            programContent.setCoachingProgram(program); // Associer l'entité persistante
        }
        return repository.save(programContent);
    }



    @Override
    public void deleteContent(Long id) {
        repository.deleteById(id);
    }

    public Page<ProgramContent> getPaginatedContents(Long programId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return programContentRepository.findByCoachingProgram_Id(programId, pageable);
    }

}








