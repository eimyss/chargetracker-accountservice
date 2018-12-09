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
public class AddressProject {

  @Id
  @GeneratedValue
  private Long id;
  private LocalDate createDate;
  private boolean active;
  private String city;
  private String streetName;
  private @NonNull
  String name;
  private String houseNumber;
  private String postalCode;
  private String userId;
  private double distanceFromHome;

}
