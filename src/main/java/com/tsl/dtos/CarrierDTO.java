package com.tsl.dtos;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
public class CarrierDTO {
    private Long id;
    private String fullName;
    private String shortName;
    private String vatNumber;
    private String description;
    private Integer termOfPayment;
    private LocalDate insuranceExpirationDate;
    private LocalDate licenceExpirationDate;
}
