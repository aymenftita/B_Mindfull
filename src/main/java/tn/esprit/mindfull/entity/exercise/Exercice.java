package tn.esprit.mindfull.entity.exercise;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Entity
@Setter
@Data
public class Exercice {
    @Id
    private Long exrcice_id;


}
