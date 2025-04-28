package tn.esprit.mindfull.Service.AppointmentsService;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import tn.esprit.mindfull.entity.Appointment.Appointment;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class ExcelService {

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public ByteArrayInputStream generateAppointmentsExcel(List<Appointment> appointments) throws IOException {
        try (Workbook workbook = new XSSFWorkbook();
             ByteArrayOutputStream out = new ByteArrayOutputStream()) {

            Sheet sheet = workbook.createSheet("Appointments");

            // Create header row
            Row headerRow = sheet.createRow(0);
            CellStyle headerStyle = createHeaderStyle(workbook);

            String[] headers = {
                    "ID", "Start Time", "End Time", "Status", "Video Status",
                    "Notes", "Patient ID", "Patient Name", "Professional ID", "Professional Name",
                    "Reminder Time", "Reminder Message"
            };

            for (int i = 0; i < headers.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(headers[i]);
                cell.setCellStyle(headerStyle);
                sheet.autoSizeColumn(i);
            }

            // Fill data rows
            int rowIndex = 1;
            for (Appointment appointment : appointments) {
                Row row = sheet.createRow(rowIndex++);

                row.createCell(0).setCellValue(appointment.getAppointmentId());

                if (appointment.getStartTime() != null) {
                    row.createCell(1).setCellValue(appointment.getStartTime().format(DATE_FORMATTER));
                }

                if (appointment.getEndTime() != null) {
                    row.createCell(2).setCellValue(appointment.getEndTime().format(DATE_FORMATTER));
                }

                if (appointment.getStatus() != null) {
                    row.createCell(3).setCellValue(appointment.getStatus().name());
                }

                if (appointment.getVideoStatus() != null) {
                    row.createCell(4).setCellValue(appointment.getVideoStatus().name());
                }

                row.createCell(5).setCellValue(appointment.getNotes() != null ? appointment.getNotes() : "");

                if (appointment.getPatient() != null) {
                    row.createCell(6).setCellValue(appointment.getPatient().getId());
                    row.createCell(7).setCellValue(appointment.getPatient().getFirstname() + " " +
                            appointment.getPatient().getLastname());
                }

                if (appointment.getProfessional() != null) {
                    row.createCell(8).setCellValue(appointment.getProfessional().getId());
                    row.createCell(9).setCellValue(appointment.getProfessional().getFirstname() + " " +
                            appointment.getProfessional().getLastname());
                }

                if (appointment.getReminderTime() != null) {
                    row.createCell(10).setCellValue(appointment.getReminderTime().format(DATE_FORMATTER));
                }

                row.createCell(11).setCellValue(appointment.getReminderMessage() != null ?
                        appointment.getReminderMessage() : "");
            }

            // Auto-size columns for better readability
            for (int i = 0; i < headers.length; i++) {
                sheet.autoSizeColumn(i);
            }

            workbook.write(out);
            return new ByteArrayInputStream(out.toByteArray());
        }
    }

    private CellStyle createHeaderStyle(Workbook workbook) {
        CellStyle headerStyle = workbook.createCellStyle();

        // Set background color
        headerStyle.setFillForegroundColor(IndexedColors.LIGHT_BLUE.getIndex());
        headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

        // Set font
        Font headerFont = workbook.createFont();
        headerFont.setBold(true);
        headerFont.setColor(IndexedColors.WHITE.getIndex());
        headerStyle.setFont(headerFont);

        // Set border
        headerStyle.setBorderBottom(BorderStyle.THIN);
        headerStyle.setBorderTop(BorderStyle.THIN);
        headerStyle.setBorderLeft(BorderStyle.THIN);
        headerStyle.setBorderRight(BorderStyle.THIN);

        return headerStyle;
    }
}