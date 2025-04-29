package tn.esprit.mindfull.Repository.GroupRepository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tn.esprit.mindfull.entity.Group.Program;

@Repository
public interface ProgramRepository extends JpaRepository<Program, Long> {
    Program findByTitleContainingIgnoreCase(String title);
}