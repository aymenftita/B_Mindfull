package tn.esprit.mindfull.entity.chat;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Entity
@Data
@Getter
@Setter
public class Chat {
    @Id
    private Long chat_id;


}
