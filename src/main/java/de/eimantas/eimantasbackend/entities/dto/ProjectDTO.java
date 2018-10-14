package de.eimantas.eimantasbackend.entities.dto;

import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import java.math.BigDecimal;
import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Data
@ApiModel(description = "")
public class ProjectDTO {

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
