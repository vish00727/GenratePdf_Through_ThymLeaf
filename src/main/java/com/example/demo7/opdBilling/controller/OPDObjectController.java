package com.example.demo7.opdBilling.controller;

import com.example.demo7.opdBilling.dto.*;
import jakarta.servlet.http.HttpServletResponse;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.xhtmlrenderer.pdf.ITextRenderer;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class OPDObjectController {

    @Autowired
    private TemplateEngine templateEngine;

    @GetMapping("/getMap")
    public RootDto getMaps() throws IOException, InterruptedException {
         HttpClient client = HttpClient.newHttpClient();
         HttpRequest request = HttpRequest.newBuilder()
         .uri(URI.create("http://localhost:8080/api/css/Static.json"))
         .build();
         HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
         JSONObject jsonObject = new JSONObject(response.body());
         RootDto root = new RootDto();
         List<BillInfoArray> billInfoArrayList = new ArrayList<>();
         JSONObject patientObj = (JSONObject) jsonObject.get("patientDetails");
         PatientDetails patientDetails = new PatientDetails();
         patientDetails.setPatientName(patientObj.getString("patientName"));
         patientDetails.setUhid(patientObj.getString("uhid"));
         patientDetails.setMobileNumber(patientObj.getString("mobileNumber"));
         patientDetails.setPatientAge(patientObj.getInt("patientAge"));
         patientDetails.setPatientCategory(patientObj.getString("patientCategory"));
         patientDetails.setDepartmentName(patientObj.getString("departmentName"));
         patientDetails.setGender(patientObj.getString("gender"));
         patientDetails.setBillNumber(patientObj.getString("billNumber"));
         patientDetails.setBillDate(patientObj.getString("billDate"));
         patientDetails.setReferalDoctor(patientObj.getString("referalDoctor"));
         patientDetails.setCompanyName(patientObj.getString("companyName"));
         patientDetails.setDesignation(patientObj.optString("designation", " "));
         patientDetails.setRelation(patientObj.optString("relation"," "));
         patientDetails.setMedicalCardNumber(patientObj.optString("medicalCardNumber"," "));
         patientDetails.setTariffDespansary(patientObj.optString("tariffDespansary",  " "));
         patientDetails.setRailwayDepartment(patientObj.optString("railwayDepartment", " "));
         patientDetails.setStation(patientObj.optString("station"," " ));

        root.setPatientDetails(patientDetails);

        JSONArray resultArray = (JSONArray) jsonObject.get("BillInfoArray");

        for (int i = 0; i < resultArray.length(); i++) {
            JSONObject obj = resultArray.getJSONObject(i);
            System.out.println("obj"+obj);
            List<PaymentDetails> paymentDetailsList = new ArrayList<>();
            if (obj.has("paymentDetails")) {
                JSONArray paymentDetailsArray = (JSONArray) obj.get("paymentDetails");
                for (int j = 0; j < paymentDetailsArray.length(); j++) {
                    JSONObject paymentObj = paymentDetailsArray.getJSONObject(j);
                    PaymentDetails paymentDetails = new PaymentDetails();
                    paymentDetails.setAmount(paymentObj.getDouble("amount"));
                    paymentDetails.setReferenceNumber(paymentObj.getString("referenceNumber"));
                    paymentDetails.setPaymentType(paymentObj.getString("paymentType"));
                    paymentDetails.setReceiptNumber(paymentObj.getString("receiptNumber"));
                    paymentDetails.setReceiptDate(paymentObj.getString("receiptDate"));
                    paymentDetailsList.add(paymentDetails);
                }
            }
            List<ServiceDetails> serviceDetailsList = new ArrayList<>();
            if (obj.has("serviceDetails")) {
                JSONArray serviceDetailsArray = (JSONArray) obj.get("serviceDetails");
                for (int k = 0; k < serviceDetailsArray.length(); k++) {
                    JSONObject serviceObj = serviceDetailsArray.getJSONObject(k);
                    ServiceDetails serviceDetails = new ServiceDetails();
                    serviceDetails.setServiceName(serviceObj.getString("serviceName"));
                    serviceDetails.setDoctorName(serviceObj.getString("doctorName"));
                    serviceDetails.setQty(serviceObj.getInt("qty"));
                    serviceDetails.setBillType(serviceObj.get("billType") != null ? serviceObj.get("billType").toString() : null);
                    serviceDetails.setServiceRate(serviceObj.get("serviceRate") != null ?Double.parseDouble(serviceObj.get("serviceRate").toString()):0.00 );
                    serviceDetails.setTotalAmount(serviceObj.get("totalAmount") != null ?Double.parseDouble(serviceObj.get("totalAmount").toString()):0.00 );
                    serviceDetails.setTotalBillAmount(serviceObj.get("totalBillAmount") != null ?Double.parseDouble(serviceObj.get("totalBillAmount").toString()):0.00 );
                    serviceDetails.setPaidAmount(serviceObj.get("paidAmount") != null ?Double.parseDouble(serviceObj.get("paidAmount").toString()):0.00 );
                    try{
                        serviceDetails.setOutstandingAmount(serviceObj.get("outstandingAmount") != null ?Double.parseDouble(serviceObj.get("outstandingAmount").toString()) : 0.00);
                    }catch (Exception  e){
                        e.printStackTrace();
                        serviceDetails.setOutstandingAmount(0.00);
                    }
                    serviceDetailsList.add(serviceDetails);
                }
            }

            BillInfoArray billInfoArray = new BillInfoArray();
            billInfoArray.setTotalPaidAmountInWord(obj.getString("totalPaidAmountInWord"));
            billInfoArray.setIsSelf(obj.getBoolean("isSelf"));
            billInfoArray.setIsCompany(obj.getBoolean("isCompany"));
            billInfoArray.setBoth(obj.getBoolean("both"));
            billInfoArray.setPaymentDetails(paymentDetailsList);
            billInfoArray.setServiceDetails(serviceDetailsList);

            billInfoArrayList.add(billInfoArray);

        }

        root.setBillInfoArray(billInfoArrayList);
        return root;
    }
       @GetMapping("generatePdf/OpdObjectBill")
       public void generateOpdObjectPdf(HttpServletResponse response) {
            try(OutputStream os=new ByteArrayOutputStream()){
            RootDto data= getMaps();
            Map<String,Object> map = new HashMap<>();
            map.put("patientDetails",data.getPatientDetails());
            map.put("billInfoArray",data.getBillInfoArray());
            String templateName="OPDBilling";
            Context context=new Context();
            context.setVariables(map);
            String htmlContent= templateEngine.process(templateName,context);
            ITextRenderer renderer=new ITextRenderer();
            renderer.setDocumentFromString(htmlContent);
            renderer.layout();
            renderer.createPDF(os);
            response.setContentType("application/pdf");
            response.setHeader("Content-Disposition", "attachment; filename=OpdObjectBill.pdf");
            response.getOutputStream().write(((ByteArrayOutputStream) os).toByteArray());
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }
}