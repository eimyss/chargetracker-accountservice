package de.eimantas.eimantasbackend.entities;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@JsonIdentityInfo(
    generator = ObjectIdGenerators.PropertyGenerator.class,
    property = "id")
public class AccountHistory {

  @Id
  @GeneratedValue
  private Long id;
  private @NonNull long refAccountId;
  private LocalDateTime createDate;
  private LocalDateTime lastProcessedTime;
  private BigDecimal amount;
  private String userId;

  public AccountHistory createHistory(Account account) {
    this.amount = account.getAmount();
    this.userId = account.getUserId();
    return this;
  }
}
