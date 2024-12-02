package com.kodat.of.halleyecommerce.email;

import com.kodat.of.halleyecommerce.dto.auth.RegistrationEmailDto;
import com.kodat.of.halleyecommerce.user.User;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

@Service
public class RegistrationEmailUtils {

    private final EmailService emailService;

    public RegistrationEmailUtils(EmailService emailService) {
        this.emailService = emailService;
    }


    public void sendEmailForRegistration(RegistrationEmailDto registrationEmailDto) {
        String date = createDate();
        Map<String, Object> data = new HashMap<>();
        data.put("firstName", registrationEmailDto.getFirstName());
        data.put("lastName", registrationEmailDto.getLastName());
        data.put("userEmail", registrationEmailDto.getEmail());
        data.put("registrationDate", date);
        emailService.sendRegistrationEmail(registrationEmailDto.getEmail(), data);
    }

    public RegistrationEmailDto setRegistrationEmailDto(User user) {
        return RegistrationEmailDto.builder()
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .email(user.getEmail())
                .build();
    }

    private static String createDate() {
        Instant instant = Instant.now();
        ZoneId zoneId = ZoneId.of("Asia/Istanbul");
        ZonedDateTime zdt = instant.atZone(zoneId);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MMMM EEEE, HH:mm", Locale.forLanguageTag("tr"));
        return zdt.format(formatter);
    }
}