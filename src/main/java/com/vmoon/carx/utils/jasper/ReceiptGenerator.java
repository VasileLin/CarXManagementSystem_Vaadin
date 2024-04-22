package com.vmoon.carx.utils.jasper;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.server.StreamRegistration;
import com.vaadin.flow.server.StreamResource;
import com.vaadin.flow.server.VaadinSession;
import net.sf.jasperreports.engine.*;
import org.springframework.stereotype.Component;

import java.io.*;
import java.util.Map;

@Component
public class ReceiptGenerator {

    public static void cashReceiptGenerator(Map<String, Object> parameters, File pathDirectory) {
        try {
            InputStream reportStream = ReceiptGenerator.class.getResourceAsStream("/jasper/carXReceipt.jrxml");
            JasperReport jasperReport = JasperCompileManager.compileReport(reportStream);
            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, new JREmptyDataSource());

            byte[] pdfContent = JasperExportManager.exportReportToPdf(jasperPrint);

            if (!pathDirectory.exists()) {
               boolean isCreated = pathDirectory.mkdirs();

               if (!isCreated) {
                   Notification.show("Failed to save receipt: ", 5000, Notification.Position.MIDDLE);
               }
            }

            String transactionNo = parameters.get("transactionNo").toString();
            File outputFile = new File(pathDirectory, transactionNo+ ".pdf");

            try (FileOutputStream fos = new FileOutputStream(outputFile)) {
                fos.write(pdfContent);
            }

            StreamResource resource = new StreamResource("receipt.pdf", () -> new ByteArrayInputStream(pdfContent));
            resource.setContentType("application/pdf");
            resource.setCacheTime(0);

            StreamRegistration registration = VaadinSession.getCurrent().getResourceRegistry().registerResource(resource);
            String url = registration.getResourceUri().toString();

            UI.getCurrent().getPage().open(url, "_blank");

        } catch (JRException | IOException e) {
            Notification.show("Failed to generate the report: " + e.getMessage(), 5000, Notification.Position.MIDDLE);
        }
    }
}
