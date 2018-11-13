package de.eimantas.eimantasbackend.helpers;

import de.eimantas.eimantasbackend.entities.AccountHistory;

import java.time.LocalDateTime;

public class EntityHelper {
  public static AccountHistory createHistory(long accountId) {

    AccountHistory history = new AccountHistory();
    history.setRefAccountId(accountId);
    history.setCreateDate(LocalDateTime.now());
    return history;
  }
}
