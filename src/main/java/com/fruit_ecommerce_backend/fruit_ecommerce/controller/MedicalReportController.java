package com.fruit_ecommerce_backend.fruit_ecommerce.controller;

import com.fruit_ecommerce_backend.fruit_ecommerce.entity.MedicalReport;
import com.fruit_ecommerce_backend.fruit_ecommerce.service.MedicalReportImportService;
import com.fruit_ecommerce_backend.fruit_ecommerce.service.MedicalReportService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

    
@RestController
@RequestMapping("/api/admin/reports")
@RequiredArgsConstructor
public class MedicalReportController {
	
	private final MedicalReportService service;
	private final MedicalReportImportService importService;
	
	/** Fetch all reports */
	@GetMapping
	public ResponseEntity<List<MedicalReport>> listReports() {
		return ResponseEntity.ok(service.listAll());
	}
	
	/** Create a new report */
	@PostMapping
	public ResponseEntity<MedicalReport> createReport(@RequestBody MedicalReport report) {
		return ResponseEntity.ok(service.save(report));
	}
	
	/** Update existing report */
	@PutMapping("/{id}")
	public ResponseEntity<MedicalReport> updateReport(@PathVariable Long id, @RequestBody MedicalReport updated) {
		return service.findById(id).map(existing -> {
			updated.setId(id);
			return ResponseEntity.ok(service.save(updated));
		}).orElse(ResponseEntity.notFound().build());
	}
	
	
	
	/** Delete report by ID */
	@DeleteMapping("/{id}")
	public ResponseEntity<?> deleteReport(@PathVariable Long id) {
		service.delete(id);
		return ResponseEntity.noContent().build();
	}
	
	/** Import Excel file into database */
	@PostMapping("/import")
	public ResponseEntity<String> importMedicalReports(@RequestParam("file") MultipartFile file) {
		File tempFile = null;
		try {
			// Save uploaded file temporarily
			tempFile = File.createTempFile("medical_reports_", ".xlsx");
			try (FileOutputStream fos = new FileOutputStream(tempFile)) {
				fos.write(file.getBytes());
			}
			
			// Call the import service
			importService.importExcelToDatabase(tempFile.getAbsolutePath());
			
			return ResponseEntity.ok("Excel data imported successfully!");
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("Failed to import Excel: " + e.getMessage());
		} finally {
			if (tempFile != null && tempFile.exists()) {
				tempFile.delete();
			}
		}
	}
	
	/** Download Excel template */
	@GetMapping("/template")
	public void downloadTemplate(HttpServletResponse response) {
		try (Workbook workbook = new XSSFWorkbook()) {
			Sheet sheet = workbook.createSheet("MedicalReports");
			
			String[] columns = {
					"id","age_range","body_part","bookingcutoff","center_name","color_name","component",
					"report_condition","container","department","department_name","disease","doctor_specialty",
					"fasting_time","gender","home_collection","investigation_name","lab","location_name",
					"package_name","parameters","pre_requisites","prescription","price","price_updated_on",
					"processingdays","quantity","report_deliverydays","report_tat","reportingcutoff",
					"sample_receive_tat","sample_type","sample_typename","specimen","sracutoff",
					"stability_refrigerated","stability_room","synonymous","temperature","test_code",
					"test_method","test_name","test_updated_on","report_usage"
			};
			
			// Create header row
			Row header = sheet.createRow(0);
			for (int i = 0; i < columns.length; i++) {
				header.createCell(i).setCellValue(columns[i]);
				sheet.autoSizeColumn(i);
			}
			
			// Response settings
			response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
			response.setHeader("Content-Disposition", "attachment; filename=medical_reports_template.xlsx");
			workbook.write(response.getOutputStream());
		} catch (Exception e) {
			throw new RuntimeException("Failed to generate template", e);
		}
	}
	
	/** Helper method to read cell value safely */
	private String getCellValue(Row row, int col) {
		if (row.getCell(col) == null) return "";
		Cell cell = row.getCell(col);
		if (cell.getCellType() == CellType.NUMERIC) {
			return String.valueOf(cell.getNumericCellValue());
		} else {
			return cell.toString().trim();
		}
	}
	
	/** Helper method to parse Double safely */
	private Double parseDoubleSafe(String val) {
		try {
			return Double.parseDouble(val);
		} catch (Exception e) {
			return null;
		}
	}
    
    
    @GetMapping("/search")
    public ResponseEntity<List<MedicalReport>> searchReports(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String disease,
            @RequestParam(required = false) String specialty) {

        List<MedicalReport> reports = service.listAll();

        List<MedicalReport> filtered = reports.stream()
                .filter(r -> {
                    boolean matchesKeyword = keyword == null || keyword.isEmpty() ||
                            (r.getTestName() != null && r.getTestName().toLowerCase().contains(keyword.toLowerCase())) ||
                            (r.getTestCode() != null && r.getTestCode().toLowerCase().contains(keyword.toLowerCase()));

                    boolean matchesDisease = disease == null || disease.isEmpty() ||
                            (r.getDisease() != null && r.getDisease().equalsIgnoreCase(disease));

                    boolean matchesSpecialty = specialty == null || specialty.isEmpty() ||
                            (r.getDoctorSpecialty() != null && r.getDoctorSpecialty().equalsIgnoreCase(specialty));

                    return matchesKeyword && matchesDisease && matchesSpecialty;
                })
                .toList();

        return ResponseEntity.ok(filtered);
    }

    
}
