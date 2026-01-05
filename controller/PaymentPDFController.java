package com.banking.controller;

import com.banking.entity.Payment;
import com.banking.entity.User;
import com.banking.repository.PaymentRepository;
import com.banking.service.PaymentService;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import jakarta.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.io.ByteArrayOutputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Controller
public class PaymentPDFController {
	
    @Autowired
    private PaymentRepository paymentRepo;
    
    @Autowired
    private PaymentService paymentService;
    
    @GetMapping("/payment/pdf")
    public ResponseEntity<byte[]> generateUserPaymentPdf(HttpSession session) {

        User loggedUser = (User) session.getAttribute("loggedInUser");
        if (loggedUser == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }

        // Fetch only logged-in user payment history
        List<Payment> payments = paymentService.getPaymentsByUserId(loggedUser.getId());

        try {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            Document document = new Document();
            PdfWriter.getInstance(document, out);
            document.open();
            try {
            Image logo = Image.getInstance("src/main/resources/static/Bankinglogo.png"); 
            logo.scaleToFit(80, 80); // adjust size
            logo.setAbsolutePosition(document.right() - 80, document.top() - 60); 
            document.add(logo);
            }
            catch(Exception e) {
            	System.out.println("Logo not found: "+e.getMessage());
            }


            Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18);
            Paragraph title = new Paragraph("Payment Statement Report", titleFont);
            title.setAlignment(Element.ALIGN_CENTER);
            document.add(title);

            document.add(new Paragraph("User: " + loggedUser.getFullName()));
            document.add(new Paragraph("Email: " + loggedUser.getEmail()));
            document.add(new Paragraph("Generated On: " + LocalDateTime.now()));
            document.add(new Paragraph("------------------------------------------------------"));
            document.add(new Paragraph(" "));

            PdfPTable table = new PdfPTable(5);
            table.setWidthPercentage(100);
            table.addCell("Date");
            table.addCell("Transaction ID");
            table.addCell("Description");
            table.addCell("Amount (₹)");
            table.addCell("Status");

            for (Payment p : payments) {
                table.addCell(p.getPaymentDate().toString());
                table.addCell(p.getPaymentId());
                table.addCell(p.getDescription());
                table.addCell(String.valueOf(p.getAmount()));
                table.addCell(p.getStatus());
            }

            document.add(table);
            document.close();

            byte[] pdfBytes = out.toByteArray();

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            headers.setContentDispositionFormData("attachment", "payment_history.pdf");

            return new ResponseEntity<>(pdfBytes, headers, HttpStatus.OK);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

}
