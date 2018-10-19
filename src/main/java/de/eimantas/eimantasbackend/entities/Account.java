package de.eimantas.eimantasbackend.entities;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@JsonIdentityInfo(
    generator = ObjectIdGenerators.PropertyGenerator.class,
    property = "id")
public class Account {

  @Id
  @GeneratedValue
  private Long id;
  private LocalDate createDate;
  private LocalDate expireDate;
  private LocalDate updateDate;
  private LocalDateTime lastProcessedTime;
  private long lastProcessedTransaction;
  private boolean active;
  private BigDecimal processedAmount;
  private BigDecimal amount;
  private boolean businessAccount;
  private String bank;
  private int processedCount;
  private @NonNull
  String name;
  private int expensesCount;
  private String userId;


  @PrePersist
  @PreUpdate
  public void updateAddressAssociation() {

  }

  @Override
  public String toString() {
    return "Account{" +
        "id=" + id +
        ", createDate=" + createDate +
        ", expireDate=" + expireDate +
        ", active=" + active +
        ", businessAccount=" + businessAccount +
        ", bank='" + bank + '\'' +
        ", name='" + name + '\'' +
        ", user=" + userId +
        '}';
  }


  public void increaseProcessedCount() {
    processedCount++;
  }

  public void addAmount(BigDecimal amountToAdd) {
    if (amount == null) {
      amount = BigDecimal.ZERO;
    }
    amount = amount.add(amountToAdd);
  }

  public void addProcessedAmount(BigDecimal amountToAdd) {
    if (processedAmount == null) {
      processedAmount = BigDecimal.ZERO;
    }
    processedAmount = processedAmount.add(amountToAdd);
  }
}
