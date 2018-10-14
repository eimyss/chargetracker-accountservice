package de.eimantas.eimantasbackend.repo;

import de.eimantas.eimantasbackend.entities.Project;
import org.springframework.data.repository.CrudRepository;


public interface ProjectRepository extends CrudRepository<Project, Long> {

}