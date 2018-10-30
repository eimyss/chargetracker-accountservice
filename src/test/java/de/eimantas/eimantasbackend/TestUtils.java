package de.eimantas.eimantasbackend;

import de.eimantas.eimantasbackend.entities.Account;
import de.eimantas.eimantasbackend.entities.Project;
import de.eimantas.eimantasbackend.entities.dto.AccountDTO;

import java.math.BigDecimal;
import java.time.LocalDate;

public class TestUtils {


  public static final String USER_ID = "TSTSUSER_ID";

  public static Account getAccount() {
    Account acc = new Account();
    acc.setName("testname");
    acc.setBank("test");
    acc.setBusinessAccount(true);
    acc.setActive(true);
    acc.setUserId(USER_ID);
    acc.setCreateDate(LocalDate.now());
    return acc;
  }


  public static AccountDTO getAccountDTO() {
    AccountDTO acc = new AccountDTO();
    acc.setName("testname");
    acc.setBank("test");
    acc.setBusinessAccount(true);
    acc.setActive(true);
    acc.setUserId(USER_ID);
    acc.setCreateDate(LocalDate.now());
    return acc;
  }

  public static Project getProject() {
    Project project = new Project();
    project.setUserId("userId");
    project.setRate(new BigDecimal("85"));
    project.setRefBankAccountId(1L);
    project.setActive(true);
    project.setUserId(USER_ID);
    project.setName("Testing Mock");
    project.setCreateDate(LocalDate.now());
    return project;

  }
}
