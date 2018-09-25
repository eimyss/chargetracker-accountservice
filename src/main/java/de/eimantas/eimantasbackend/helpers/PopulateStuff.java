package de.eimantas.eimantasbackend.helpers;

import de.eimantas.eimantasbackend.entities.Account;

import java.time.LocalDate;

public class PopulateStuff {


  public static Account getAccount() {
    Account acc = new Account();
    acc.setName("testname");
    acc.setBank("test");
    acc.setBusinessAccount(true);
    acc.setActive(true);
    acc.setCreateDate(LocalDate.now());
    return acc;
  }


}
