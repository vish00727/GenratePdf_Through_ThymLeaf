package com.example.demo7.billingtemplate.entity;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PDFUserInfo {
    @Id
    private  Long id;

    private Integer srNo;

    private String service;

    private String doctorName;

    private Integer quantity;

    private Double rate;

    private Double total;
}
