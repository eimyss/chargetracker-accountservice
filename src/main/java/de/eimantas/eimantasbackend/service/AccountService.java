package de.eimantas.eimantasbackend.service;

import de.eimantas.eimantasbackend.controller.exceptions.NonExistingEntityException;
import de.eimantas.eimantasbackend.entities.Account;
import de.eimantas.eimantasbackend.entities.converter.EntitiesConverter;
import de.eimantas.eimantasbackend.entities.dto.AccountDTO;
import de.eimantas.eimantasbackend.messaging.ExpensesSender;
import de.eimantas.eimantasbackend.repo.AccountRepository;
import org.json.JSONException;
import org.json.JSONObject;
import org.keycloak.adapters.springsecurity.token.KeycloakAuthenticationToken;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
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

  @Inject
  ExpensesSender expensesSender;

  private final org.slf4j.Logger logger = LoggerFactory.getLogger(this.getClass());


  public Optional<Account> getAccountById(long accountId, KeycloakAuthenticationToken authentication) {
    return accountRepository.findByIdAndUserId(accountId, securityService.getUserIdFromPrincipal(authentication));
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


  public Stream<Account> getAccountsByUserId(KeycloakAuthenticationToken authentication) {
    return accountRepository.findByUserId(securityService.getUserIdFromPrincipal(authentication));
  }

  public List<AccountDTO> getAccountDtoByUserId(KeycloakAuthenticationToken authentication) {

    Stream<Account> accountStream = getAccountsByUserId(authentication);
    return accountStream.map(acc -> converter.getAccountDTO(acc)).collect(Collectors.toList());
  }


  private boolean isPresentAccount(Long accountId) {
    return accountRepository.existsById(accountId);
  }

  public List<Long> getAccountIds(KeycloakAuthenticationToken authentication) {
    return accountRepository.getAllIds(securityService.getUserIdFromPrincipal(authentication));
  }


  @Transactional
  public void processTransaction(JSONObject json) {

    int accountId = parseInt(json, "accountId");
    int entityId = parseInt(json, "refEntityId");
    logger.info("processing transaction with id: " + accountId);

    Optional<Account> acc = accountRepository.findById((long) accountId);
    int transactionId = parseInt(json, "id");
    if (acc.isPresent()) {
      processAccountFromTransaction(acc.get(), json);
      try {
        JSONObject jsonNotify = new JSONObject();
        jsonNotify.put("accountId", accountId);
        jsonNotify.put("transactionId", transactionId);
        jsonNotify.put("refEntityId", entityId);
        expensesSender.notifyAddedExpense(jsonNotify);
      } catch (JSONException e) {
        logger.error("cannot create json for notification", e);
        e.printStackTrace();
      }

    } else {
      logger.error("Transaction with id : " + transactionId + " does not have acount with id: " + accountId);
    }


  }

  private void processAccountFromTransaction(Account account, JSONObject json) {

    account.increaseProcessedCount();
    account.setLastProcessedTransaction(parseInt(json, "id"));
    account.setLastProcessedTime(LocalDateTime.now());
    BigDecimal processedAmount = parseBigDecimal(json, "amountAfter");
    BigDecimal amount = parseBigDecimal(json, "amountBefore");
    account.addAmount(amount);
    account.addProcessedAmount(processedAmount);
    accountRepository.save(account);

  }

  private BigDecimal parseBigDecimal(JSONObject json, String key) {
    try {
      return new BigDecimal(json.getString(key));
    } catch (JSONException e) {
      logger.info("error getting  int for key ", e);
      e.printStackTrace();
    }
    return BigDecimal.ZERO;
  }


  private int parseInt(JSONObject json, String key) {
    try {
      return json.getInt(key);
    } catch (JSONException e) {
      logger.info("error getting  int for key ", e);
      e.printStackTrace();
    }
    return 0;
  }



  // TODO after initial release it should be made more nice
  public Account saveAccountInTest(Account acc) {
    return accountRepository.save(acc);
  }
}
