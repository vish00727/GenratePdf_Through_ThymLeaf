package com.example.demo7.opdBilling.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PaymentDetails {

    private Double Amount;

    private String ReferenceNumber;

    private  String PaymentType;

    private String ReceiptNumber;

    private  String ReceiptDate;

}
