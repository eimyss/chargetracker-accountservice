package de.eimantas.eimantasbackend.service;

import de.eimantas.eimantasbackend.entities.Project;
import de.eimantas.eimantasbackend.entities.converter.EntitiesConverter;
import de.eimantas.eimantasbackend.repo.ProjectRepository;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.time.LocalDate;
import java.util.Optional;


@Service
public class ProjectService {

  @Inject
  SecurityService securityService;

  @Inject
  ProjectRepository projectRepository;

  @Inject
  EntitiesConverter converter;

  private final org.slf4j.Logger logger = LoggerFactory.getLogger(this.getClass());


  public Optional<Project> getProjectById(long id) {
    return projectRepository.findById(id);
  }

  public Project saveProject(Project project) {
    project.setCreateDate(LocalDate.now());
    return projectRepository.save(project);

  }
}
