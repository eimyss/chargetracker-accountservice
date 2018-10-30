package de.eimantas.eimantasbackend.repo;

import de.eimantas.eimantasbackend.entities.Project;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;


public interface ProjectRepository extends CrudRepository<Project, Long> {

  List<Project> findAllByUserId(String user);

  Optional<Project> findByIdAndUserId(long id, String userIdFromPrincipal);
}