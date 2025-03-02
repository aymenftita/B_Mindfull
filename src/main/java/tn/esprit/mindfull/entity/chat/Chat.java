package tn.esprit.mindfull.entity.chat;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;

@Entity
@Data
public class Chat {
    @Id
    private Long chat_id;

    public void setChat_id(Long chatId) {
        this.chat_id = chatId;
    }

    public Long getChat_id() {
        return chat_id;
    }
}
