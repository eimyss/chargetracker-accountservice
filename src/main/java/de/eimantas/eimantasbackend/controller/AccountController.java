package de.eimantas.eimantasbackend.controller;

import de.eimantas.eimantasbackend.controller.exceptions.ErrorDesc;
import de.eimantas.eimantasbackend.controller.exceptions.NonExistingEntityException;
import de.eimantas.eimantasbackend.entities.Account;
import de.eimantas.eimantasbackend.entities.converter.EntitiesConverter;
import de.eimantas.eimantasbackend.entities.dto.AccountDTO;
import de.eimantas.eimantasbackend.service.AccountService;
import de.eimantas.eimantasbackend.service.SecurityService;
import org.keycloak.adapters.springsecurity.token.KeycloakAuthenticationToken;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
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
  public ResponseEntity<List<AccountDTO>> getAccountsList(Principal principal) {


    if (principal == null) {
      return new ResponseEntity<List<AccountDTO>>(HttpStatus.FORBIDDEN);
    }

    logger.info("Principal: " + principal.toString());
    KeycloakAuthenticationToken user = (KeycloakAuthenticationToken) principal;
    logger.info("name direclty:" + user.getName());

    List<AccountDTO> dtos = accountService.getAccountDtoByUserId(securityService.getUserIdFromPrincipal(user));

    logger.info("returning account list size: " + dtos.size());
    return new ResponseEntity<List<AccountDTO>>(dtos, HttpStatus.OK);

  }

  @GetMapping(value = "/list/id", produces = MediaType.APPLICATION_JSON_VALUE)
  @CrossOrigin(origins = "*")
  @Transactional
  public Collection<Long> getAccountsListIds(Principal principal) {

    logger.info("Principal: " + principal.toString());
    KeycloakAuthenticationToken user = (KeycloakAuthenticationToken) principal;


    List<Long> dtos = accountService.getAccountIds();

    logger.info("returning account list size: " + dtos.size());
    return dtos;

  }


  @PostMapping("/save")
  @Transactional
  @CrossOrigin(origins = "*")
  public ResponseEntity<AccountDTO> saveAccount(Principal principal, @RequestBody AccountDTO account) throws NonExistingEntityException {

    logger.info("creating account: " + account.toString());

    if (principal == null) {
      return new ResponseEntity(HttpStatus.FORBIDDEN);
    }

    if (account == null) {
      logger.info("passed account dto is null");
      return new ResponseEntity(HttpStatus.BAD_REQUEST);
    }

    if (account.getId() != null) {
      logger.info("passed account is with ID, please use PUT");
      return new ResponseEntity(HttpStatus.BAD_REQUEST);
    }

    KeycloakAuthenticationToken user = (KeycloakAuthenticationToken) principal;
    Account acc = accountService.saveAccount(account, user);
    logger.info("Account created: " + acc.toString());
    return new ResponseEntity<AccountDTO>(converter.getAccountDTO(acc), HttpStatus.CREATED);

  }

  @PutMapping("/save")
  @Transactional
  @CrossOrigin(origins = "*")
  public ResponseEntity<AccountDTO> updateAccount(Principal principal, @RequestBody AccountDTO account) throws NonExistingEntityException {

    logger.info("updating account: " + account.toString());

    if (principal == null) {
      return new ResponseEntity(HttpStatus.FORBIDDEN);
    }

    if (account == null) {
      logger.info("passed account dto is null");
      return new ResponseEntity(HttpStatus.BAD_REQUEST);
    }

    if (account.getId() == null) {
      logger.info("passed account ID is not null (please use post)");
      return new ResponseEntity(HttpStatus.BAD_REQUEST);
    }

    KeycloakAuthenticationToken user = (KeycloakAuthenticationToken) principal;
    Account acc = accountService.saveAccount(account, user);
    logger.info("Account updated: " + acc.toString());
    return new ResponseEntity<AccountDTO>(converter.getAccountDTO(acc), HttpStatus.OK);

  }


  @ExceptionHandler(NonExistingEntityException.class)
  @ResponseStatus(value = HttpStatus.BAD_REQUEST)
  public @ResponseBody
  ErrorDesc handleException(NonExistingEntityException e) {
    return new ErrorDesc(e.getMessage());
  }


}


