package de.eimantas.eimantasbackend.entities.dto;

import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.time.LocalDate;


@AllArgsConstructor
@NoArgsConstructor
@Data
@ApiModel(description = "")
public class AccountDTO {


  @Id
  @GeneratedValue
  private Long id;
  private LocalDate createDate;
  private LocalDate expireDate;
  private LocalDate updateDate;
  private boolean active;
  private boolean businessAccount;
  private String bank;
  private String name;
  private int expensescount;
  private String userId;


}

