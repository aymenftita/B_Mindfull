package tn.esprit.mindfull.entity.forum;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Entity
@Data
@Getter
@Setter
public class Post {
    @Id
    private Long post_id;


}
