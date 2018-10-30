package de.eimantas.eimantasbackend.helpers;

import de.eimantas.eimantasbackend.entities.Account;

import java.time.LocalDate;

public class PopulateStuff {

  public static final String TEST_USER_ID = "9a204126-12b9-4efe-9d9b-3808aba51ba3";

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
