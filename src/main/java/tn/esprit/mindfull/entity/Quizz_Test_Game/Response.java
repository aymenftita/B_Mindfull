package tn.esprit.mindfull.entity.Quizz_Test_Game;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class Response {
    @Id
    private Long response_id;

    public void setResponse_id(Long responseId) {
        this.response_id = responseId;
    }

    public Long getResponse_id() {
        return response_id;
    }
}
