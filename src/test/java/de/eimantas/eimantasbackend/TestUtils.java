package de.eimantas.eimantasbackend;

import de.eimantas.eimantasbackend.entities.Account;
import de.eimantas.eimantasbackend.entities.dto.AccountDTO;

import java.time.LocalDate;

public class TestUtils {


  public static Account getAccount() {
    Account acc = new Account();
    acc.setName("testname");
    acc.setBank("test");
    acc.setBusinessAccount(true);
    acc.setActive(true);
    acc.setCreateDate(LocalDate.now());
    return acc;
  }


  public static AccountDTO getAccountDTO() {
    AccountDTO acc = new AccountDTO();
    acc.setName("testname");
    acc.setBank("test");
    acc.setBusinessAccount(true);
    acc.setActive(true);
    acc.setCreateDate(LocalDate.now());
    return acc;
  }
}
