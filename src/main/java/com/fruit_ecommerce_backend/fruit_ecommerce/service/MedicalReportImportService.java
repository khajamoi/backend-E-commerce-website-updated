package com.fruit_ecommerce_backend.fruit_ecommerce.service;

import java.io.FileInputStream;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Types;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import javax.sql.DataSource;
import org.apache.poi.ss.usermodel.Sheet;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MedicalReportImportService {

    @Autowired
    
    private DataSource dataSource;

    public void importExcelToDatabase(String excelFilePath) throws Exception {
        try (Connection conn = dataSource.getConnection();
             Workbook workbook = new XSSFWorkbook(new FileInputStream(excelFilePath))) {

            Sheet sheet = workbook.getSheetAt(0);

            if (sheet.getPhysicalNumberOfRows() < 2) {
                throw new RuntimeException("Excel sheet does not contain any data.");
            }

            // === Step 1: Get table column names dynamically ===
            Set<String> tableColumns = new HashSet<>();
            DatabaseMetaData metaData = conn.getMetaData();
            try (ResultSet rs = metaData.getColumns(null, null, "medical_reports", null)) {
                while (rs.next()) {
                    tableColumns.add(rs.getString("COLUMN_NAME").toLowerCase());
                }
            }

            // === Step 2: Read Excel header row ===
            Row headerRow = sheet.getRow(0);
            Map<Integer, String> excelToDbColumnMap = new HashMap<>();

            for (int i = 0; i < headerRow.getLastCellNum(); i++) {
                String excelHeader = headerRow.getCell(i).getStringCellValue().trim().toLowerCase();
                if (tableColumns.contains(excelHeader)) {
                    excelToDbColumnMap.put(i, excelHeader); // Map Excel index -> DB column
                }
            }

            if (excelToDbColumnMap.isEmpty()) {
                throw new RuntimeException("No matching columns found between Excel and database table.");
            }

            // === Step 3: Build dynamic SQL ===
            String columnNames = String.join(", ", excelToDbColumnMap.values());
            String placeholders = excelToDbColumnMap.values().stream()
                    .map(col -> "?")
                    .collect(Collectors.joining(", "));

            String sql = "INSERT INTO medical_reports (" + columnNames + ") VALUES (" + placeholders + ")";
            System.out.println("Generated SQL: " + sql);

            // === Step 4: Insert rows ===
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                conn.setAutoCommit(false);

                for (int rowIndex = 1; rowIndex <= sheet.getLastRowNum(); rowIndex++) {
                    Row dataRow = sheet.getRow(rowIndex);
                    if (dataRow == null) continue;

                    int paramIndex = 1;
                    for (int colIndex = 0; colIndex < headerRow.getLastCellNum(); colIndex++) {
                        if (!excelToDbColumnMap.containsKey(colIndex)) continue;

                        Cell cell = dataRow.getCell(colIndex);
                        if (cell == null) {
                            pstmt.setNull(paramIndex++, Types.NULL);
                            continue;
                        }

                        switch (cell.getCellType()) {
                            case STRING:
                                pstmt.setString(paramIndex++, cell.getStringCellValue().trim());
                                break;
                            case NUMERIC:
                                if (DateUtil.isCellDateFormatted(cell)) {
                                    pstmt.setString(paramIndex++, cell.getDateCellValue().toString());
                                } else {
                                    pstmt.setDouble(paramIndex++, cell.getNumericCellValue());
                                }
                                break;
                            case BOOLEAN:
                                pstmt.setBoolean(paramIndex++, cell.getBooleanCellValue());
                                break;
                            case BLANK:
                                pstmt.setNull(paramIndex++, Types.NULL);
                                break;
                            default:
                                pstmt.setString(paramIndex++, cell.toString());
                                break;
                        }
                    }

                    pstmt.addBatch();

                    if (rowIndex % 100 == 0) { // Batch size
                        pstmt.executeBatch();
                    }
                }

                pstmt.executeBatch();
                conn.commit();
            }
        }
    }
}
