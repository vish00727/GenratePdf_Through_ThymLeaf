package com.example.demo7.billingtemplate.pdfDTO;

import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PdfDto {

    private Integer srNo;

    private String service;

    private String doctorName;

    private Integer quantity;

    private Double rate;

    private Double total;
}
