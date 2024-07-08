package com.example.demo7.opdBilling.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ServiceDetails {

    private String serviceName;

    private String doctorName;

    private int qty;

    private String billType;

    private Double totalBillAmount;

    private Double outstandingAmount;

    private  Double paidAmount;

    private Double ServiceRate;

    private Double TotalAmount;

}
