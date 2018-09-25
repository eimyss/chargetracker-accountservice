package de.eimantas.eimantasbackend.entities;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import javax.persistence.*;
import java.time.LocalDate;

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
  private boolean active;
  private boolean businessAccount;
  private String bank;
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


}
