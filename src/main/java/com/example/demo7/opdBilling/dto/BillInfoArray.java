package com.example.demo7.opdBilling.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;
@Getter
@Setter
public class BillInfoArray {

    List<PaymentDetails> paymentDetails;

    List<ServiceDetails> serviceDetails;

    private  String TotalPaidAmountInWord;

    private Boolean IsSelf;

    private Boolean IsCompany;

    private  Boolean Both;

}
