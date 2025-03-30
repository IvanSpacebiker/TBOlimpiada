package com.kzkv.tbolimpiada.service.implementation;

import com.kzkv.tbolimpiada.entity.Booking;
import com.kzkv.tbolimpiada.entity.Ticket;
import com.kzkv.tbolimpiada.exception.EmailSendingException;
import com.kzkv.tbolimpiada.service.EmailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.time.format.DateTimeFormatter;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailServiceImpl implements EmailService {
	private final Session session;
	private final TemplateEngine templateEngine;
	private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");

	@Value("${app.host}")
	private String host;

	public void sendEmail(String toEmail, Booking booking) {
		try {
			Message message = new MimeMessage(session);
			message.setFrom(new InternetAddress("no-reply@booking-service"));
			message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmail));
			message.setSubject("Бронирование билета");
			message.setContent(buildContent(booking), "text/html; charset=utf-8");

			Transport.send(message);
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