package de.eimantas.eimantasbackend.entities.converter;

import de.eimantas.eimantasbackend.entities.Account;
import de.eimantas.eimantasbackend.entities.Project;
import de.eimantas.eimantasbackend.entities.dto.AccountDTO;
import de.eimantas.eimantasbackend.entities.dto.ProjectDTO;
import org.modelmapper.ModelMapper;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


public class EntitiesConverter {

  @Autowired
  private ModelMapper modelMapper;


  private final org.slf4j.Logger logger = LoggerFactory.getLogger(this.getClass());


  public AccountDTO getAccountDTO(Optional<Account> accountOptional) {

    if (accountOptional.isPresent()) {
      Account account = accountOptional.get();
      AccountDTO postDto = modelMapper.<AccountDTO>map(account, AccountDTO.class);
      return postDto;

    }
    logger.info("account is null");
    return null;

  }

  public AccountDTO getAccountDTO(Account acc) {
    AccountDTO postDto = modelMapper.<AccountDTO>map(acc, AccountDTO.class);
    return postDto;
  }

  public Account getAccountDTO(AccountDTO dto) {
    Account postDto = modelMapper.map(dto, Account.class);
    return postDto;

  }


  public ProjectDTO getProjectDTO(Project project) {
    ProjectDTO postDto = modelMapper.map(project, ProjectDTO.class);
    return postDto;

  }


  public Project getProjectFromDTO(ProjectDTO project) {
    Project postDto = modelMapper.map(project, Project.class);
    return postDto;

  }

  public List<ProjectDTO> getProjectDTO(List project) {
    return (List<ProjectDTO>) project.stream().map(p -> modelMapper.map(p, ProjectDTO.class)).collect(Collectors.toList());
  }
}
