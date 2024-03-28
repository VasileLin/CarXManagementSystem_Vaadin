package com.vmoon.carx.utils;

import com.vmoon.carx.dto.CashGridDto;
import com.vmoon.carx.dto.CustomerDto;
import com.vmoon.carx.dto.EmployerDto;
import com.vmoon.carx.dto.ServiceDto;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Component;

import java.time.format.DateTimeFormatter;
import java.util.List;

@Component
public class ExcelWorkBooks {

    public static Workbook createEmployersExcelWorkBook(List<EmployerDto> employers) {
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Employers");

        Row headerRow = sheet.createRow(0);
        String[] columnNames = {"Full Name", "Date of Birth", "Email", "Address", "Phone", "Role"};

        CellStyle style = workbook.createCellStyle();
        style.setFillBackgroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        style.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());

        for (int i = 0; i<columnNames.length; i++) {
            headerRow.createCell(i).setCellValue(columnNames[i]);
            headerRow.getCell(i).setCellStyle(style);
        }

        int rowNum = 1;

        for (EmployerDto employee : employers) {
            Row row = sheet.createRow(rowNum++);
            row.createCell(0).setCellValue(employee.getFullName());
            row.createCell(1).setCellValue(employee.getDateOfBirth().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
            row.createCell(2).setCellValue(employee.getEmail());
            row.createCell(3).setCellValue(employee.getAddress());
            row.createCell(4).setCellValue(employee.getPhone());
            row.createCell(5).setCellValue(employee.getRole().getName());
        }

        for (int i = 0; i < columnNames.length; i++) {
            sheet.autoSizeColumn(i);
        }

        return workbook;
    }

    public static Workbook createCustomersExcelWorkBook(List<CustomerDto> customers) {
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Customers");

        Row headerRow = sheet.createRow(0);
        String[] columnNames = {"Full Name", "Phone", "Email", "Car Model", "Car Number"};

        CellStyle style = workbook.createCellStyle();
        style.setFillBackgroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        style.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());

        for (int i = 0; i<columnNames.length; i++) {
            headerRow.createCell(i).setCellValue(columnNames[i]);
            headerRow.getCell(i).setCellStyle(style);
        }

        int rowNum = 1;
        for (CustomerDto customer : customers) {
            Row row = sheet.createRow(rowNum++);
            row.createCell(0).setCellValue(customer.getName());
            row.createCell(1).setCellValue(customer.getPhone());
            row.createCell(2).setCellValue(customer.getEmail());
            row.createCell(3).setCellValue(customer.getCarNumber());
            row.createCell(4).setCellValue(customer.getCarNumber());
        }

        for (int i = 0; i < columnNames.length; i++) {
            sheet.autoSizeColumn(i);
        }

        return workbook;
    }


    public static Workbook createServicesExcelWorkBook(List<ServiceDto> services) {
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Services");

        Row headerRow = sheet.createRow(0);
        String[] columnNames = {"Service Name", "Price"};

        CellStyle style = workbook.createCellStyle();
        style.setFillBackgroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        style.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());

        for (int i = 0; i<columnNames.length; i++) {
            headerRow.createCell(i).setCellValue(columnNames[i]);
            headerRow.getCell(i).setCellStyle(style);
        }

        int rowNum = 1;
        for (ServiceDto service : services) {
            Row row = sheet.createRow(rowNum++);
            row.createCell(0).setCellValue(service.getName());
            row.createCell(1).setCellValue(service.getPrice());
        }

        for (int i = 0; i < columnNames.length; i++) {
            sheet.autoSizeColumn(i);
        }

        return workbook;
    }


    public static Workbook createTransactionsExcelWorkBook(List<CashGridDto> cashList) {
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Transactions");

        Row headerRow = sheet.createRow(0);
        String[] columnNames = {"Transaction Number", "Total Price", "Date", "Details"};

        CellStyle style = workbook.createCellStyle();
        style.setFillBackgroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        style.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());

        for (int i = 0; i<columnNames.length; i++) {
            headerRow.createCell(i).setCellValue(columnNames[i]);
            headerRow.getCell(i).setCellStyle(style);
        }

        int rowNum = 1;
        for (CashGridDto cash : cashList) {
            Row row = sheet.createRow(rowNum++);
            row.createCell(0).setCellValue(cash.getTransactionNo());
            row.createCell(1).setCellValue(cash.getPrice());
            row.createCell(2).setCellValue(cash.getDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
            row.createCell(3).setCellValue(cash.getDetails());
        }

        for (int i = 0; i < columnNames.length; i++) {
            sheet.autoSizeColumn(i);
        }

        return workbook;
    }
}
