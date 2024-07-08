package com.example.demo7.opdBilling.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;
@Getter
@Setter
public class RootDto {

    private PatientDetails patientDetails;

    List<BillInfoArray> billInfoArray;
}
