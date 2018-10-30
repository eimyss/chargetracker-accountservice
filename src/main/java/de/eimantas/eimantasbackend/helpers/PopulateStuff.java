package de.eimantas.eimantasbackend.helpers;

import de.eimantas.eimantasbackend.entities.Account;

import java.time.LocalDate;

public class PopulateStuff {


  public static final String TEST_USER_ID = "ee9fb974-c2c2-45f8-b60e-c22d9f00273f";

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
