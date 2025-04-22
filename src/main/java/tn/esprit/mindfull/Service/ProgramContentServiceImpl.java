package tn.esprit.mindfull.Service;

import jakarta.mail.MessagingException;
import jakarta.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tn.esprit.mindfull.Respository.CoachingProgramRepository;
import tn.esprit.mindfull.Respository.ProgramContentRepository;
import tn.esprit.mindfull.Respository.UserRepository;
import tn.esprit.mindfull.entity.CoachingProgram;
import tn.esprit.mindfull.entity.ProgramContent;
import tn.esprit.mindfull.entity.Role;
import tn.esprit.mindfull.entity.User;

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
            return programContentRepository.save(programContent);  // Assure-toi que l'ID est généré après save.
        }

   
    @Override
    public void deleteContent(Long id) {
        repository.deleteById(id);
    }

    //@Override
    //public ProgramContent save(ProgramContent programContent) {
      //  return programContentRepository.save(programContent);
 //   }

  /*  @Override
    public void addProgramContent(Long contentId) throws MessagingException {

    }*/


    // Méthode pour ajouter un ProgramContent et envoyer un email au patient
   /*@Override
    public void addProgramContent(Long contentId, Long userId) throws MessagingException {
        ProgramContent content = programContentRepository.findById(contentId)
                .orElseThrow(() -> new RuntimeException("Contenu non trouvé"));

        for (User user : content.getUsers()) {
            if (user.getRole() == Role.PATIENT) {
                String email = user.getEmail();
                String nom = user.getFullName(); // ou getUsername() si tu n’as pas de champ "nom"
                String subject = "Vous avez été ajouté à un nouveau programme";
                String body = "Bonjour " + nom + ",\n\n" +
                        "Vous avez été ajouté à un nouveau contenu de programme : " + content.getTitle() + "\n\n" +
                        "Bonne chance !";

                emailService.sendEmail(email, subject, body);
            }
        }*/
    }








