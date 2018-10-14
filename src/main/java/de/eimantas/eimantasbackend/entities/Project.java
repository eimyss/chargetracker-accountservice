package de.eimantas.eimantasbackend.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Project {

  @Id
  @GeneratedValue
  private Long id;
  private LocalDate createDate;
  private LocalDate expireDate;
  private LocalDate updateDate;
  private boolean active;
  private long refBankAccountId;
  private @NonNull
  String name;
  private BigDecimal rate;
  private String userId;


}
