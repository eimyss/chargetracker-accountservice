package de.eimantas.eimantasbackend.service;

import de.eimantas.eimantasbackend.controller.exceptions.NonExistingEntityException;
import de.eimantas.eimantasbackend.entities.Account;
import de.eimantas.eimantasbackend.entities.converter.EntitiesConverter;
import de.eimantas.eimantasbackend.entities.dto.AccountDTO;
import de.eimantas.eimantasbackend.repo.AccountRepository;
import org.keycloak.adapters.springsecurity.token.KeycloakAuthenticationToken;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.security.Principal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;


@Service
public class AccountService {

  @Inject
  SecurityService securityService;

  @Inject
  AccountRepository accountRepository;

  @Inject
  EntitiesConverter converter;

  private final org.slf4j.Logger logger = LoggerFactory.getLogger(this.getClass());
  private int monthsGoBack = 6;


  public Optional<Account> getAccountById(long accountId, Principal authentication) {
    return accountRepository.findById(accountId);
  }


  public Account saveAccount(AccountDTO dto, KeycloakAuthenticationToken authentication) throws NonExistingEntityException {

    if (dto == null) {
      throw new IllegalArgumentException("Account passed is null");
    }

    if (authentication == null) {
      throw new SecurityException("account cannot be null");
    }

    Account acc = converter.getAccountDTO(dto);
    return saveAccount(acc, authentication);

  }


  public Account saveAccount(Account acc, KeycloakAuthenticationToken authentication) throws NonExistingEntityException {

    if (acc == null) {
      throw new IllegalArgumentException("Account passed is null");
    }

    if (authentication == null) {
      throw new SecurityException("account cannot be null");
    }

    String usr = securityService.getUserIdFromPrincipal(authentication);

    if (acc.getId() != null) {

      if (!accountRepository.existsById(acc.getId())) {
        logger.warn("Account with id: " + acc.getId() + " doesnt exist!");
        throw new NonExistingEntityException("There is no Account for update with id: " + acc.getId());
      }

      logger.info("Account '" + acc.getId() + "'is existing, setting only update date");
      acc.setUpdateDate(LocalDate.now());
    } else {
      logger.info("Account '" + acc.getName() + "' is new, setting create date");
      acc.setCreateDate(LocalDate.now());
    }

    acc.setUserId(usr);


    return accountRepository.save(acc);

  }


  public Stream<Account> getAccountsByUserId(String userid) {
    // TODO check auth
    return accountRepository.findByUserId(userid);
  }

  public List<AccountDTO> getAccountDtoByUserId(String userId) {

    Stream<Account> accountStream = getAccountsByUserId(userId);
    return accountStream.map(acc -> converter.getAccountDTO(acc)).collect(Collectors.toList());
  }


  public boolean isPresentAccount(Long accountId) {
    return accountRepository.existsById(accountId);
  }

  public List<Long> getAccountIds() {
    return accountRepository.getAllIds();
  }
}
