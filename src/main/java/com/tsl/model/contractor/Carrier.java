package com.tsl.model.contractor;

import com.tsl.model.invoice.CarrierInvoice;
import com.tsl.model.order.ForwardingOrder;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Carrier extends Contractor{
    @OneToMany(mappedBy = "carrier")
    private List<ForwardingOrder> orders = new ArrayList<>();
    @OneToMany(mappedBy = "carrier")
    private List<CarrierInvoice> carrierInvoices = new ArrayList<>();
    private LocalDate insuranceExpirationDate;
    private LocalDate licenceExpirationDate;

}
