package com.kzkv.tbolimpiada.service.implementation;

import com.kzkv.tbolimpiada.entity.Booking;
import com.kzkv.tbolimpiada.entity.Ticket;
import com.kzkv.tbolimpiada.service.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.time.format.DateTimeFormatter;

@Service
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {
    private final Session session;

    public void sendEmail(String toEmail, Booking booking) {
        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress("no-reply@localhost"));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmail));
            message.setSubject("Бронирование билета");
            message.setText(buildContent(booking));

            Transport.send(message);
            System.out.println("Email sent successfully.");
        } catch (MessagingException e) {
            System.err.println("Error sending email: " + e.getMessage());
        }
    }

    private String buildContent(Booking booking) {
        Ticket ticket = booking.getTicket();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");
        String departureDateTimeFormatted = ticket.getDepartureDateTime().format(formatter);
        String arrivalDateTimeFormatted = ticket.getArrivalDateTime().format(formatter);

        return "Ваш билет успешно забронирован!\n" +
                "-----------------------------\n" +
                "Тип транспорта: " + ticket.getTransportType() + "\n" +
                "Отправление: " + ticket.getDeparture() + " (" + departureDateTimeFormatted + ")\n" +
                "Прибытие: " + ticket.getArrival() + " (" + arrivalDateTimeFormatted + ")\n" +
                "Цена: " + ticket.getPrice() + " руб.\n" +
                "-----------------------------\n" +
                "Чтобы отменить бронирование, перейдите по ссылке: " +
                "http://localhost/api/bookings/" + booking.getId();
    }
}