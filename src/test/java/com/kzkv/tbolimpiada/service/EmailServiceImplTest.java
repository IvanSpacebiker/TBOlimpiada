package com.kzkv.tbolimpiada.service;

import com.kzkv.tbolimpiada.entity.Booking;
import com.kzkv.tbolimpiada.entity.Ticket;
import com.kzkv.tbolimpiada.entity.TransportType;
import com.kzkv.tbolimpiada.exception.EmailSendingException;
import com.kzkv.tbolimpiada.service.implementation.EmailServiceImpl;
import jakarta.mail.internet.MimeMessage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.MailSendException;
import org.springframework.mail.javamail.JavaMailSender;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.time.ZonedDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EmailServiceImplTest {

    @Mock
    private JavaMailSender mailSender;

    @Mock
    private TemplateEngine templateEngine;

    @InjectMocks
    private EmailServiceImpl emailService;

    @Mock
    private Booking booking;

    @Mock
    private MimeMessage mimeMessage;

    @BeforeEach
    void setUp() {
        when(mailSender.createMimeMessage()).thenReturn(mimeMessage);
        when(templateEngine.process(eq("email-template"), any(Context.class))).thenReturn("Email Content");
    }

    @Test
    void testSendEmail_Success() {
        when(booking.getTicket()).thenReturn(createSampleTicket());
        assertDoesNotThrow(() -> emailService.sendEmail("test@example.com", booking));
        verify(mailSender, times(1)).send(any(MimeMessage.class));
    }

    @Test
    void testSendEmail_ThrowsExceptionOnMessagingError() {
        when(booking.getTicket()).thenReturn(createSampleTicket());
        doThrow(new MailSendException("Mail error")).when(mailSender).send(any(MimeMessage.class));
        assertThrows(EmailSendingException.class, () -> emailService.sendEmail("test@example.com", booking));
    }

    private Ticket createSampleTicket() {
        Ticket ticket = new Ticket();
        ticket.setId(UUID.fromString("123e4567-e89b-12d3-a456-426614174001"));
        ticket.setTransportType(TransportType.TRAIN);
        ticket.setPrice(100);
        ticket.setDeparture("CityA");
        ticket.setArrival("CityB");
        ticket.setDepartureDateTime(ZonedDateTime.now());
        ticket.setArrivalDateTime(ZonedDateTime.now().plusHours(2));
        return ticket;
    }
}