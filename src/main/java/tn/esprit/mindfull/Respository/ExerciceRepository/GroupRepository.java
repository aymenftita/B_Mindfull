package tn.esprit.mindfull.Respository.ExerciceRepository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tn.esprit.mindfull.entity.exercise.Group;

@Repository
public interface GroupRepository extends JpaRepository<Group, Integer> {
}
