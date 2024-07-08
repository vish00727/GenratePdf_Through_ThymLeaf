package com.example.demo7.billingtemplate.service;
import com.lowagie.text.DocumentException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.xhtmlrenderer.pdf.ITextRenderer;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Map;

@Service
public class PdfGeneratorService {
    @Autowired
    private TemplateEngine templateEngine;

        public ByteArrayOutputStream pdfPrescription(String html, Map<String, Object> map) throws DocumentException, IOException {
        Context context = new Context();
        context.setVariables(map);
        String htmlString = templateEngine.process(html,context);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ITextRenderer renderer = new ITextRenderer();
        renderer.setDocumentFromString(htmlString);
        renderer.layout();
        renderer.createPDF(outputStream);
        return outputStream;
    }
}

