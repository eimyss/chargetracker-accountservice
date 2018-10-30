package de.eimantas.eimantasbackend.controller;

import de.eimantas.eimantasbackend.controller.exceptions.ErrorDesc;
import de.eimantas.eimantasbackend.controller.exceptions.NonExistingEntityException;
import de.eimantas.eimantasbackend.entities.Project;
import de.eimantas.eimantasbackend.entities.converter.EntitiesConverter;
import de.eimantas.eimantasbackend.entities.dto.ProjectDTO;
import de.eimantas.eimantasbackend.service.ProjectService;
import de.eimantas.eimantasbackend.service.SecurityService;
import org.keycloak.adapters.springsecurity.token.KeycloakAuthenticationToken;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import java.security.Principal;
import java.util.List;
import java.util.Optional;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping(value = "/project")
public class ProjectController {
  private final org.slf4j.Logger logger = LoggerFactory.getLogger(this.getClass());


  @Inject
  private ProjectService projectService;

  @Inject
  private EntitiesConverter entitiesConverter;

  @Inject
  private SecurityService securityService;

  @Inject
  private EntitiesConverter converter;

  public ProjectController() {

  }


  @GetMapping(value = "/get/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
  @CrossOrigin(origins = "*")
  public ProjectDTO getProject(Principal principal, @PathVariable long id) throws NonExistingEntityException {

    Optional<Project> project = projectService.getProjectById(id, (KeycloakAuthenticationToken) principal);
    logger.info("Getting project with id: " + id);

    if (!project.isPresent()) {
      throw new NonExistingEntityException("Project for id '" + id + "' cannot be found!");
    }
    return entitiesConverter.getProjectDTO(project.get());

  }

  @GetMapping(value = "/get/all}", produces = MediaType.APPLICATION_JSON_VALUE)
  @CrossOrigin(origins = "*")
  public List<ProjectDTO> getProject(Principal principal) {

    KeycloakAuthenticationToken user = (KeycloakAuthenticationToken) principal;
    List project = projectService.findAll(user);
    logger.info("Getting all projects with size : " + project.size());

    return entitiesConverter.getProjectDTO(project);

  }


  @PostMapping("/save")
  @Transactional
  @CrossOrigin(origins = "*")
  public ProjectDTO saveAccount(Principal principal, @RequestBody ProjectDTO projectDTO) throws NonExistingEntityException {

    logger.info("creating project: " + projectDTO.toString());
    KeycloakAuthenticationToken user = (KeycloakAuthenticationToken) principal;

    Project converted = converter.getProjectFromDTO(projectDTO);
    converted.setUserId(securityService.getUserIdFromPrincipal(user));
    Project project = projectService.saveProject(converted);
    logger.info("Project created: " + project.toString());
    return converter.getProjectDTO(project);

  }


  @ExceptionHandler(NonExistingEntityException.class)
  @ResponseStatus(value = HttpStatus.BAD_REQUEST)
  public @ResponseBody
  ErrorDesc handleException(NonExistingEntityException e) {
    return new ErrorDesc(e.getMessage());
  }


}


