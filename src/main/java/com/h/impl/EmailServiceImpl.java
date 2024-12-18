package com.h.impl;

import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import com.h.entity.detalleOrden;
import com.h.service.IEmailService;
import com.h.service.IPDFService;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

import java.io.IOException;
import java.sql.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;



@Service
public class EmailServiceImpl implements IEmailService {

	private final JavaMailSender javaMailSender;
    private final TemplateEngine templateEngine;
    private final IPDFService pdfService;

    // Constructor para inyectar dependencias
    @Autowired // Esto es opcional si tienes un solo constructor
    public EmailServiceImpl(JavaMailSender javaMailSender, 
                            TemplateEngine templateEngine, 
                            IPDFService pdfService) {
        this.javaMailSender = javaMailSender;
        this.templateEngine = templateEngine;
        this.pdfService = pdfService; // Ahora Spring inyectará la implementación de IPDFService
    }

    @Override
    public void sendEmailfForgottenPassword(String correo ,String numeroRandom, String nombreUsuario) {
        try {
           
            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            helper.setTo(correo);
            helper.setSubject("Hola " + nombreUsuario+ " este es el codigo de recuperación de contraseña");
            Context context = new Context();
           
            context.setVariable("mensaje",numeroRandom);
            // Procesa la plantilla Thymeleaf
            String htmlContent = templateEngine.process("email", context);
            helper.setText(htmlContent, true); 
            javaMailSender.send(message);
            
            } catch (MessagingException e) {
            System.out.println("Error al enviar el correo:");
            e.printStackTrace();
        }
    }
    
    
    ////Metodo que envia el pdf de la factura a travez de correo
    @Override
    public void sendEmailWithPdf(String correo, String nombreUsuario ,List<detalleOrden> detalleOrden,String numeroOrden,String fechaCompra) {
        try {
            // Generar el PDF
        	byte[] pdfContent = pdfService.generateStyledPdf(detalleOrden,numeroOrden,nombreUsuario,fechaCompra);
        	
            // Configurar el correo
            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setTo(correo);
            helper.setSubject("Hola " + nombreUsuario + ", esta es la factura de tu compra en Virtual Shop");
            helper.setText("Adjunto encontrarás el archivo PDF", false);
            ByteArrayResource pdfAttachment = new ByteArrayResource(pdfContent);
            helper.addAttachment("Factura " +nombreUsuario+".pdf" , pdfAttachment);
            javaMailSender.send(message);
        
        } catch (MessagingException | IOException e) {
            throw new RuntimeException("Error al enviar el correo con el PDF adjunto", e);
        }
    }
}
