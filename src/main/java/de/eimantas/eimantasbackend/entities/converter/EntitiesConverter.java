package de.eimantas.eimantasbackend.entities.converter;

import de.eimantas.eimantasbackend.entities.Account;
import de.eimantas.eimantasbackend.entities.dto.AccountDTO;
import org.modelmapper.ModelMapper;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;


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


}
