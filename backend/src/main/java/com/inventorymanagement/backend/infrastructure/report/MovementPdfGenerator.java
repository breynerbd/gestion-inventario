package com.inventorymanagement.backend.infrastructure.report;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.List;

import com.inventorymanagement.backend.application.dto.movement.StockMovementResponseDTO;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.font.Standard14Fonts;
import org.springframework.stereotype.Component;
import java.util.ArrayList;
import java.time.format.DateTimeFormatter;

@Component
public class MovementPdfGenerator {

    public byte[] generate(List<StockMovementResponseDTO> movements){
        try (PDDocument document = new PDDocument(); ByteArrayOutputStream output = new ByteArrayOutputStream()) {
            PDPage page = new PDPage(PDRectangle.A4);
            document.addPage(page);

            try (PDPageContentStream content = new PDPageContentStream(document, page)){
                content.beginText();
                content.setFont(new PDType1Font(Standard14Fonts.FontName.HELVETICA_BOLD), 16);
                content.newLineAtOffset(40, 800);
                content.showText("Stockify - Movimientos de inventario");
                content.endText();
            }

            document.save(output);

            return output.toByteArray();

        }catch(IOException exception) {
            throw new UncheckedIOException("No se ha podido generar el pdf de movimientos", exception);
        }
    }

    private void drawText(PDPageContentStream content, String text, float x, float y,
                          float fontSize, boolean bold) throws IOException {

        var fontName = bold ? Standard14Fonts.FontName.HELVETICA_BOLD : Standard14Fonts.FontName.HELVETICA;

        content.beginText();
        content.setFont(new PDType1Font(fontName), fontSize);
        content.newLineAtOffset(x, y);
        content.showText(text);
        content.endText();
    }

    private List<String> wrapText(String text,
            float maxWidth,
            float fontSize) throws IOException {

        String value = text == null || text.isBlank() ? "-" : text.replaceAll("\\s+", " ").trim();

        var font = new PDType1Font(Standard14Fonts.FontName.HELVETICA);

        List<String> lines = new ArrayList<>();
        StringBuilder line = new StringBuilder();

        for (int i = 0; i < value.length(); i++) {
            String candidate = line.toString() + value.charAt(i);
            float width = font.getStringWidth(candidate) / 1000 * fontSize;

            if (width > maxWidth && !line.isEmpty()) {
                lines.add(line.toString());
                line.setLength(0);
            }

            line.append(value.charAt(i));
        }

        if (!line.isEmpty()) {
            lines.add(line.toString());
        }

        return lines;
    }
}