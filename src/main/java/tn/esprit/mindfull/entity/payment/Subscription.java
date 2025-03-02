package tn.esprit.mindfull.entity.payment;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Entity
@Data
@Getter
@Setter
public class Subscription {
    @Id
    private Long subscription_id;


}
