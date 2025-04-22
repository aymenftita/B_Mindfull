package tn.esprit.mindfull.Service.QuizzTestService;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tn.esprit.mindfull.Respository.QuizzTestRepository.QuestionResponseRepository;
import tn.esprit.mindfull.entity.Quizz_Test_Game.QuestionResponse;
import tn.esprit.mindfull.entity.Quizz_Test_Game.Quizz;


import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class QuestionResponseService {

    private final QuestionResponseRepository qrs;

    public List<QuestionResponse> getAllQuestionResponse() {
        return qrs.findAll();
    }

    public Optional<QuestionResponse> getQuestionResponse(long id) {return qrs.findById(id);}

    public QuestionResponse addQuestionResponse(QuestionResponse questionResponse) {
        return qrs.save(questionResponse);
    }

    public Optional<QuestionResponse> updateQuestionResponse(long id, QuestionResponse questionResponseUpdated) {

        return qrs.findById(id)
                .map(qr -> {
                            qr.setQuestionText(questionResponseUpdated.getQuestionText());
                            qr.setResponseText(questionResponseUpdated.getResponseText());
                            return qrs.save(qr);
                        }
                );
    }



    public void deleteQuestionResponse(long id) {
        qrs.deleteById(id);
    }
}
