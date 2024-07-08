package com.example.demo7.billingtemplate.controller;

import com.example.demo7.billingtemplate.service.PdfGeneratorService;
import com.example.demo7.billingtemplate.entity.PDFUserInfo;
import com.example.demo7.billingtemplate.pdfDTO.PdfDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class PdfController {
@Autowired
private PdfGeneratorService pdfGeneratorService;

@GetMapping("/PDF")
public ResponseEntity<?> generatePdf(@RequestBody List<PdfDto> prescriptionDtoList) {
        try {
            String htmlContent = "BillingTemplate";
            Map<String, Object> map = new HashMap<>();
            List<PDFUserInfo> prescriptions = new ArrayList<>();
            prescriptionDtoList.forEach(data -> {
                PDFUserInfo pui = new PDFUserInfo();
                pui.setSrNo(data.getSrNo());
                pui.setService(data.getService());
                pui.setQuantity(data.getQuantity());
                pui.setDoctorName(data.getDoctorName());
                pui.setRate(data.getRate());
                pui.setTotal(data.getTotal());
                prescriptions.add(pui);
            });

            map.put("users", prescriptions);
            ByteArrayOutputStream outputStream = pdfGeneratorService.pdfPrescription(htmlContent, map);
            byte[] pdfByte = outputStream.toByteArray();
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            headers.setContentDispositionFormData("filename", "billingTemplate.pdf");
            return new ResponseEntity<>(pdfByte, headers, HttpStatus.OK);
            } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(("Error Generating Pdf: " + e.getMessage()).getBytes(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}