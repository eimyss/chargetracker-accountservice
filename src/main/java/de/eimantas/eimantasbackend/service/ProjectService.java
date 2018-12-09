package de.eimantas.eimantasbackend.service;

import de.eimantas.eimantasbackend.entities.AddressProject;
import de.eimantas.eimantasbackend.entities.Project;
import de.eimantas.eimantasbackend.entities.converter.EntitiesConverter;
import de.eimantas.eimantasbackend.repo.AddressRepository;
import de.eimantas.eimantasbackend.repo.ProjectRepository;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;
import javax.inject.Inject;
import org.keycloak.adapters.springsecurity.token.KeycloakAuthenticationToken;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;


@Service
public class ProjectService {

  @Inject
  SecurityService securityService;

  @Inject
  AddressRepository addressRepository;

  @Inject
  ProjectRepository projectRepository;

  @Inject
  EntitiesConverter converter;

  private final org.slf4j.Logger logger = LoggerFactory.getLogger(this.getClass());


  public Optional<Project> getProjectById(long id, KeycloakAuthenticationToken token) {
    return projectRepository.findByIdAndUserId(id, securityService.getUserIdFromPrincipal(token));
  }

  public Project saveProject(Project project) {
    project.setCreateDate(LocalDate.now());
    return projectRepository.save(project);

  }

  public List<Project> findAll(KeycloakAuthenticationToken token) {
    String user = securityService.getUserIdFromPrincipal(token);
    logger.info("getting all projects by user id: " + user);
    return projectRepository.findAllByUserId(user);

  }

  public Project saveProjectInTest(Project project) {
    return projectRepository.save(project);
  }

  public Optional<AddressProject> getAddressByProjectId(long projectId,
      KeycloakAuthenticationToken principal) {

    Optional<Project> project = projectRepository
        .findByIdAndUserId(projectId, securityService.getUserIdFromPrincipal(principal));
    logger.info("Searching address for id: " + projectId);
    if (project.isPresent()) {
      logger.debug("found Project for address request: " + project);
      long addressId = project.get().getAddressId();
      return addressRepository
          .findByIdAndUserId(addressId, securityService.getUserIdFromPrincipal(principal));

    }
    return Optional.empty();
  }

  public List<AddressProject> getProjectAddresses(KeycloakAuthenticationToken principal) {
    return addressRepository.findByUserId(securityService.getUserIdFromPrincipal(principal));

  }

  public Optional<AddressProject> getAddressById(long id, KeycloakAuthenticationToken principal) {
    return addressRepository
        .findByIdAndUserId(id, securityService.getUserIdFromPrincipal(principal));
  }
}
