package de.eimantas.eimantasbackend.controller;

import de.eimantas.eimantasbackend.controller.exceptions.BadRequestException;
import de.eimantas.eimantasbackend.controller.exceptions.ErrorDesc;
import de.eimantas.eimantasbackend.controller.exceptions.NonExistingEntityException;
import de.eimantas.eimantasbackend.entities.Account;
import de.eimantas.eimantasbackend.entities.AccountHistory;
import de.eimantas.eimantasbackend.entities.converter.EntitiesConverter;
import de.eimantas.eimantasbackend.entities.dto.AccountDTO;
import de.eimantas.eimantasbackend.service.AccountService;
import de.eimantas.eimantasbackend.service.SecurityService;
import org.keycloak.adapters.springsecurity.token.KeycloakAuthenticationToken;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import java.security.Principal;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping(value = "/account")
public class AccountController {
  private final org.slf4j.Logger logger = LoggerFactory.getLogger(this.getClass());


  @Inject
  private AccountService accountService;

  @Inject
  private EntitiesConverter entitiesConverter;

  @Inject
  private SecurityService securityService;

  @Inject
  private EntitiesConverter converter;

  public AccountController() {

  }


  @GetMapping(value = "/get/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
  @CrossOrigin(origins = "*")
  public AccountDTO getAccountById(Principal principal, @PathVariable long id) throws NonExistingEntityException {

    KeycloakAuthenticationToken user = (KeycloakAuthenticationToken) principal;

    Optional<Account> acc = accountService.getAccountById(id, user);

    if (!acc.isPresent()) {
      throw new NonExistingEntityException("Account for id '" + id + "' cannot be found!");
    }

    return entitiesConverter.getAccountDTO(acc.get());

  }

  @GetMapping(value = "/list", produces = MediaType.APPLICATION_JSON_VALUE)
  @CrossOrigin(origins = "*")
  @Transactional
  public List<AccountDTO> getAccountsList(Principal principal) throws BadRequestException {

    if (principal == null) {
      throw new BadRequestException("Auth cannot be null");
    }

    List<AccountDTO> dtos = accountService.getAccountDtoByUserId((KeycloakAuthenticationToken) principal);

    logger.info("returning account list size: " + dtos.size());
    return dtos;

  }

  @GetMapping(value = "/list/id", produces = MediaType.APPLICATION_JSON_VALUE)
  @CrossOrigin(origins = "*")
  @Transactional
  public Collection<Long> getAccountsListIds(Principal principal) {

    List<Long> dtos = accountService.getAccountIds((KeycloakAuthenticationToken) principal);
    logger.info("returning account list size: " + dtos.size());
    return dtos;

  }


  @GetMapping(value = "/history/get/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
  @CrossOrigin(origins = "*")
  public List<AccountHistory> getAccountHistory(Principal principal, @PathVariable long id) {
    List<AccountHistory> list = accountService.getAccountHistoryByAccId(id, (KeycloakAuthenticationToken) principal);
    logger.info("returning size history for acc " + id + " Size: " + list.size());
    return list;
  }


  @GetMapping(value = "/history/list", produces = MediaType.APPLICATION_JSON_VALUE)
  @CrossOrigin(origins = "*")
  @Transactional
  public List<AccountHistory> getAllAccountHistories(Principal principal) throws BadRequestException {
    List<AccountHistory> dtos = accountService.getAllAccountsHistories((KeycloakAuthenticationToken) principal);
    logger.info("returning account list full history size: " + dtos.size());
    return dtos;
  }

  @PostMapping("/save")
  @Transactional
  @CrossOrigin(origins = "*")
  public AccountDTO saveAccount(Principal principal, @RequestBody AccountDTO account) throws NonExistingEntityException {

    logger.info("creating account: " + account.toString());
    KeycloakAuthenticationToken user = (KeycloakAuthenticationToken) principal;
    Account acc = accountService.saveAccount(converter.getAccountDTO(account), user);
    logger.info("Account created: " + acc.toString());
    return converter.getAccountDTO(acc);

  }

  @PutMapping("/save")
  @Transactional
  @CrossOrigin(origins = "*")
  public AccountDTO updateAccount(Principal principal, @RequestBody AccountDTO account) throws NonExistingEntityException {

    logger.info("updating account: " + account.toString());

    KeycloakAuthenticationToken user = (KeycloakAuthenticationToken) principal;
    Account acc = accountService.saveAccount(converter.getAccountDTO(account), user);
    logger.info("Account updated: " + acc.toString());
    return converter.getAccountDTO(acc);

  }


  @ExceptionHandler(NonExistingEntityException.class)
  @ResponseStatus(value = HttpStatus.BAD_REQUEST)
  public @ResponseBody
  ErrorDesc handleException(NonExistingEntityException e) {
    return new ErrorDesc(e.getMessage());
  }


}


