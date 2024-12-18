package com.h.impl;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;
import org.springframework.stereotype.Service;
import java.text.DecimalFormat;
import com.h.entity.detalleOrden;
import com.h.service.IPDFService;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@Service
public class PDFServiceImpl implements IPDFService {

    public byte[] generateStyledPdf(List<detalleOrden> detalleOrden, String numeroOrden, String nombreUsuario, String fechaCompra) throws IOException {
        try (PDDocument document = new PDDocument();
             ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {

            PDPage page = new PDPage(PDRectangle.A4);
            document.addPage(page);

            float margin = 50;
            float yStart = page.getMediaBox().getHeight() - 100; // Margen superior
            float tableWidth = page.getMediaBox().getWidth() - 2 * margin;
            float yPosition = yStart;

            // Ruta del logo en el classpath
            String imagePath = "/static/imagenes/logos/logo.png";
            File tempImageFile = null;

            try (PDPageContentStream contentStream = new PDPageContentStream(document, page)) {

                // Cargar la imagen como recurso del classpath
                InputStream imageStream = getClass().getResourceAsStream(imagePath);
                if (imageStream == null) {
                    throw new IllegalArgumentException("No se pudo encontrar la imagen en la ruta: " + imagePath);
                }

                // Crear un archivo temporal para la imagen
                tempImageFile = File.createTempFile("logo", ".png");
                try (FileOutputStream outStream = new FileOutputStream(tempImageFile)) {
                    byte[] buffer = new byte[1024];
                    int bytesRead;
                    while ((bytesRead = imageStream.read(buffer)) != -1) {
                        outStream.write(buffer, 0, bytesRead);
                    }
                }

                // Cargar la imagen como PDImageXObject
                PDImageXObject logo = PDImageXObject.createFromFile(tempImageFile.getAbsolutePath(), document);
                float logoWidth = 100;
                float logoHeight = 100;

                // Dibujar el logo
                contentStream.drawImage(logo, margin, yStart, logoWidth, logoHeight);

                yPosition -= 60;

                // Encabezado con texto
                contentStream.beginText();
                contentStream.setFont(PDType1Font.HELVETICA_BOLD, 16);
                contentStream.newLineAtOffset(margin, yPosition);
                contentStream.showText("VIRTUAL SHOP"); // Nombre de la empresa
                contentStream.endText();

                yPosition -= 30;
                contentStream.beginText();
                contentStream.setFont(PDType1Font.HELVETICA, 12);
                contentStream.newLineAtOffset(margin, yPosition);
                contentStream.showText("Nombre: " + nombreUsuario);
                contentStream.newLineAtOffset(0, -15);
                contentStream.showText("Factura No.: " + numeroOrden);
                contentStream.newLineAtOffset(0, -15);
                contentStream.showText("Fecha: " + fechaCompra);
                contentStream.endText();

                // Separador horizontal
                contentStream.setLineWidth(1);
                yPosition -= 40;
                contentStream.moveTo(margin, yPosition);
                contentStream.lineTo(margin + tableWidth, yPosition);
                contentStream.stroke();

                // Encabezados de la tabla
                yPosition -= 20;
                String[] headers = {"ID", "Producto", "Cantidad", "Precio"};
                float colWidth = tableWidth / headers.length;

                contentStream.setFont(PDType1Font.HELVETICA_BOLD, 10);
                for (int i = 0; i < headers.length; i++) {
                    contentStream.beginText();
                    contentStream.newLineAtOffset(margin + i * colWidth + 5, yPosition - 10);
                    contentStream.showText(headers[i]);
                    contentStream.endText();
                }

                // Línea inferior del encabezado
                yPosition -= 20;
                contentStream.setLineWidth(0.5f);
                contentStream.moveTo(margin, yPosition);
                contentStream.lineTo(margin + tableWidth, yPosition);
                contentStream.stroke();

                // Datos de la tabla
                for (detalleOrden detalle : detalleOrden) {
                    String[] row = {
                        String.valueOf(detalle.getId()),
                        detalle.getProducto().getNombre(),
                        String.valueOf(detalle.getCantidad()),
                        String.format("$%.2f", detalle.getPrecio())
                    };

                    for (int i = 0; i < row.length; i++) {
                        contentStream.beginText();
                        contentStream.setFont(PDType1Font.HELVETICA, 10);
                        contentStream.newLineAtOffset(margin + i * colWidth + 5, yPosition - 10);
                        contentStream.showText(row[i] != null ? row[i] : "");
                        contentStream.endText();
                    }

                    // Línea inferior de cada fila
                    yPosition -= 20;
                    contentStream.moveTo(margin, yPosition);
                    contentStream.lineTo(margin + tableWidth, yPosition);
                    contentStream.stroke();

                    // Verificar si es necesario añadir una nueva página
                    if (yPosition < 50) {
                        contentStream.close();
                        page = new PDPage(PDRectangle.A4);
                        document.addPage(page);
                        yPosition = yStart;
                    }
                }

                // Pie de página con totales y notas
                yPosition -= 30;
                contentStream.beginText();
                contentStream.setFont(PDType1Font.HELVETICA_BOLD, 12);
                contentStream.newLineAtOffset(margin, yPosition);
                contentStream.showText("Total: " + calcularTotal(detalleOrden));
                contentStream.endText();

                // Notas o términos
                yPosition -= 20;
                contentStream.beginText();
                contentStream.setFont(PDType1Font.HELVETICA, 10);
                contentStream.newLineAtOffset(margin, yPosition);
                contentStream.showText("Gracias por su compra.");
                contentStream.endText();
            } finally {
                // Eliminar el archivo temporal
                if (tempImageFile != null && tempImageFile.exists()) {
                    tempImageFile.delete();
                }
            }

            document.save(outputStream);
            return outputStream.toByteArray();
        }
    }

    private String calcularTotal(List<detalleOrden> detalleOrden) {
        double total = detalleOrden.stream()
            .mapToDouble(detalle -> detalle.getCantidad() * detalle.getPrecio())
            .sum();
        
        // Usando DecimalFormat para evitar la notación científica y mostrar el total con comas y sin decimales
        DecimalFormat df = new DecimalFormat("$#,###");  // Formato sin decimales
        return df.format(total);  // Devuelve el total como un String formateado
    }

}
