package com.kzkv.tbolimpiada.service.implementation;

import com.kzkv.tbolimpiada.entity.Booking;
import com.kzkv.tbolimpiada.exception.EmailSendingException;
import com.kzkv.tbolimpiada.service.EmailService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.time.format.DateTimeFormatter;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailServiceImpl implements EmailService {
	private final JavaMailSender mailSender;
	private final TemplateEngine templateEngine;
	private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");

	@Value("${app.host}")
	private String host;

	@Async
	public void sendEmail(String toEmail, Booking booking) {
		try {
			MimeMessage message = mailSender.createMimeMessage();
			MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
			helper.setFrom("no-reply@booking-service");
			helper.setTo(toEmail);
			helper.setSubject("Бронирование билета");
			helper.setText(buildContent(booking), true);

			mailSender.send(message);
			log.info("Email sent successfully.");
		} catch (MessagingException e) {
			log.error("Error sending email: {}", e.getMessage());
			throw new EmailSendingException("Error sending email.", e);
		}
	}

	private String buildContent(Booking booking) {
		Context context = new Context();
		context.setVariable("ticket", booking.getTicket());
		context.setVariable("departureDateTime", booking.getTicket().getDepartureDateTime().format(formatter));
		context.setVariable("arrivalDateTime", booking.getTicket().getArrivalDateTime().format(formatter));
		context.setVariable("baseUrl", getBaseUrl(booking));
		return templateEngine.process("email-template", context);
	}

	private String getBaseUrl(Booking booking) {
		return host + "/bookings/" + booking.getId();
	}
}