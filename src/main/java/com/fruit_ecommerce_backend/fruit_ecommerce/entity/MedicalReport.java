package com.fruit_ecommerce_backend.fruit_ecommerce.entity;


import jakarta.persistence.*;
import lombok.*;



import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import jakarta.persistence.*;
import lombok.*;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "medical_reports")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MedicalReport {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Basic Info
    @Lob
    @Column(name = "TestCode", columnDefinition = "TEXT")
    private String testCode;

    @Lob
    @Column(name = "LocationName", columnDefinition = "TEXT")
    private String locationName;

    @Lob
    @Column(name = "DepartmentName", columnDefinition = "TEXT")
    private String departmentName;

    @Lob
    @Column(name = "InvestigationName", columnDefinition = "TEXT")
    private String investigationName;

    @Lob
    @Column(name = "SampleTypename", columnDefinition = "TEXT")
    private String sampleTypeName;

    @Lob
    @Column(name = "Container", columnDefinition = "TEXT")
    private String container;

    @Lob
    @Column(name = "ColorName", columnDefinition = "TEXT")
    private String colorName;

    @Lob
    @Column(name = "bookingcutoff", columnDefinition = "TEXT")
    private String bookingCutoff;

    @Lob
    @Column(name = "sracutoff", columnDefinition = "TEXT")
    private String sracutoff;

    @Lob
    @Column(name = "reportingcutoff", columnDefinition = "TEXT")
    private String reportingCutoff;

    @Lob
    @Column(name = "Processingdays", columnDefinition = "TEXT")
    private String processingDays;

    @Lob
    @Column(name = "ReportDeliverydays", columnDefinition = "TEXT")
    private String reportDeliveryDays;

    // Main Test Data
    @Column(name = "center_name", length = 1000)
    private String centerName;

    @Lob
    @Column(name = "test_name", columnDefinition = "TEXT")
    private String testName;

    @Lob
    @Column(name = "test_method", columnDefinition = "TEXT")
    private String testMethod;

    @Lob
    @Column(name = "specimen", columnDefinition = "TEXT")
    private String specimen;

    @Column(name = "quantity", length = 1000)
    private String quantity;

    @Column(name = "temperature", length = 1000)
    private String temperature;

    @Lob
    @Column(name = "disease", columnDefinition = "TEXT")
    private String disease;

    @Lob
    @Column(name = "synonymous", columnDefinition = "TEXT")
    private String synonymous;

    @Column(name = "report_tat", length = 1000)
    private String reportTat;

    @Column(name = "home_collection", length = 1000)
    private String homeCollection;

    @Lob
    @Column(name = "age_range", columnDefinition = "TEXT")
    private String ageRange;

    @Lob
    @Column(name = "report_condition", columnDefinition = "TEXT")
    private String condition;

    @Column(name = "sample_type", length = 1000)
    private String sampleType;

    @Lob
    @Column(name = "prescription", columnDefinition = "TEXT")
    private String prescription;

    @Lob
    @Column(name = "parameters", columnDefinition = "TEXT")
    private String parameters;

    @Lob
    @Column(name = "report_usage", columnDefinition = "TEXT")
    private String usage;

    @Lob
    @Column(name = "component", columnDefinition = "TEXT")
    private String component;

    @Lob
    @Column(name = "pre_requisites", columnDefinition = "TEXT")
    private String preRequisites;

    @Lob
    @Column(name = "body_part", columnDefinition = "TEXT")
    private String bodyPart;

    @Column(name = "department", length = 1000)
    private String department;

    @Column(name = "doctor_specialty", length = 1000)
    private String doctorSpecialty;

    @Column(name = "sample_receive_tat", length = 1000)
    private String sampleReceiveTat;

    @Lob
    @Column(name = "package_name", columnDefinition = "TEXT")
    private String packageName;

    @Column(name = "gender", length = 1000)
    private String gender;

    @Column(name = "stability_room", length = 1000)
    private String stabilityRoom;

    @Column(name = "stability_refrigerated", length = 1000)
    private String stabilityRefrigerated;

    @Column(name = "fasting_time", length = 1000)
    private String fastingTime;

    @Column(name = "test_updated_on", length = 1000)
    private String testUpdatedOn;

    @Column(name = "price_updated_on", length = 1000)
    private String priceUpdatedOn;

    @Column(name = "price")
    private Double price;

    @Column(name = "lab", length = 1000)
    private String lab;
}


//@Entity
//@Table(name = "medical_reports")
//@Getter
//@Setter
//@NoArgsConstructor
//@AllArgsConstructor
//public class MedicalReport {
//
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Long id;
//
//    private String centerName;
//    private String testName;
//    private String testMethod;
//    private String specimen;
//    private String quantity;
//    private String temperature;
//    private String disease;
//    private String synonymous;
//    private String reportTat;
//    private String homeCollection;
//    private String ageRange;
//
//    @Column(name = "report_condition")   // ✅ avoid SQL reserved word
//    private String condition;
//
//    private String sampleType;
//    private String prescription;
//    private String parameters;
//
//    @Column(name = "report_usage")       // ✅ avoid SQL reserved word
//    private String usage;
//
//    private String component;
//    private String preRequisites;
//    private String bodyPart;
//    private String department;
//    private String doctorSpecialty;
//    private String sampleReceiveTat;
//
//    @Column(name = "package_name")
//    private String packageName;
//
//    private String gender;
//    private String stabilityRoom;
//    private String stabilityRefrigerated;
//    private String fastingTime;
//    private String testUpdatedOn;
//    private String priceUpdatedOn;
//    private Double price;
//    private String lab;
//}
