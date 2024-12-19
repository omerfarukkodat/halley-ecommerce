package com.kodat.of.halleyecommerce.email;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import java.util.Map;

@Service
public class EmailService {
    private final JavaMailSender mailSender;
    private final SpringTemplateEngine templateEngine;

    public EmailService(JavaMailSender mailSender, SpringTemplateEngine templateEngine) {
        this.mailSender = mailSender;
        this.templateEngine = templateEngine;
    }

    public void sendEmail(String to, String subject, String body) {
        try {
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true , "UTF-8");
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(body,true);
            mailSender.send(mimeMessage);
        }catch (MessagingException e) {
            throw new IllegalStateException("E-mail sending failed", e);
        }
    }

    public void sendOrderSummaryEmail(String to , Map<String, Object> orderData) {
        Context context = new Context();
        context.setVariables(orderData);
        String orderSummaryHtml = templateEngine.process("order-summary",context);
        sendEmail(to, "Sipariş Özetiniz", orderSummaryHtml);
    }

    public void sendOrderShippedEmail(String to , Map<String, Object> orderData) {
        Context context = new Context();
        context.setVariables(orderData);
        String orderShippedHtml = templateEngine.process("order-shipped",context);
        sendEmail(to,"Siparişiniz Kargoya Verildi", orderShippedHtml);
    }

    public void sendRegistrationEmail(String to ,  Map<String, Object> registrationData) {
        Context context = new Context();
        context.setVariables(registrationData);
        String registrationHtml = templateEngine.process("registration",context);
        sendEmail(to,"Kayıt İşleminiz Başarıyla Tamamlandı",registrationHtml);
    }

    public void sendPasswordResetEmail(String to , Map<String, Object> passwordResetData) {
        Context context = new Context();
        context.setVariables(passwordResetData);
        String passwordResetHtml = templateEngine.process("reset-password", context);
        sendEmail(to,"Şifre Sıfırlama Talebi" , passwordResetHtml);
    }


}
