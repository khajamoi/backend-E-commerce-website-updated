package com.fruit_ecommerce_backend.fruit_ecommerce.dto;

import lombok.Data;

@Data
public class MedicalReportDTO {
    private Long id;
    private String centerName;
    private String testName;
    private String testMethod;
    private String specimen;
    private String quantity;
    private String temperature;
    private String disease;
    private String synonymous;
    private String reportTat;
    private String homeCollection;
    private String ageRange;
    private String condition;
    private String sampleType;
    private String prescription;
    private String parameters;
    private String usage;
    private String component;
    private String preRequisites;
    private String bodyPart;
    private String department;
    private String doctorSpecialty;
    private String sampleReceiveTat;
    private String packageName;
    private String gender;
    private String stabilityRoom;
    private String stabilityRefrigerated;
    private String fastingTime;
    private String testUpdatedOn;
    private String priceUpdatedOn;
    private double price;
    private String lab;
}
