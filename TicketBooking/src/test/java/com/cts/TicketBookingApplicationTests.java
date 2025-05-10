package com.cts;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

import com.cts.dto.EmailRequest;
import com.cts.dto.EventManagement;
import com.cts.dto.UserRegistration;
import com.cts.exceptions.TicketNotFoundException;
import com.cts.feignclient.EventManagementClient;
import com.cts.feignclient.UserRegistrationClient;
import com.cts.model.TicketBooking;
import com.cts.repository.TicketBookingRepository;
import com.cts.service.TicketBookingServiceImpl;

class TicketBookingServiceImplTest {

    @Mock
    private TicketBookingRepository repository;

    @Mock
    EventManagementClient eventClient;

    @Mock
    private UserRegistrationClient userClient;

    @Mock
    private JavaMailSender javaMailSender;

    @InjectMocks
    private TicketBookingServiceImpl service;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testBookTicket() {
        TicketBooking ticket = new TicketBooking();
        ticket.setEventId(1);
        ticket.setUserId(1);
        EmailRequest request = new EmailRequest("Subject", "Message");

        EventManagement event = new EventManagement();
        UserRegistration user = new UserRegistration();
        user.setUserEmail("test@example.com");

        when(eventClient.getEventById(1)).thenReturn(event);
        when(userClient.getUserById(1)).thenReturn(user);
        when(repository.save(ticket)).thenReturn(ticket);

        TicketBooking result = service.bookTicket(ticket, request);
        assertEquals(ticket, result);
        verify(eventClient, times(1)).decreaseTicketCount(1);
        verify(javaMailSender, times(1)).send(any(SimpleMailMessage.class));
        verify(repository, times(1)).save(ticket);
    }

    @Test
    void testGetTicketById() throws TicketNotFoundException {
        TicketBooking ticket = new TicketBooking();
        when(repository.findById(1)).thenReturn(Optional.of(ticket));

        TicketBooking result = service.getTicketById(1);
        assertEquals(ticket, result);
        verify(repository, times(1)).findById(1);
    }

    @Test
    void testGetTicketByIdNotFound() {
        when(repository.findById(1)).thenReturn(Optional.empty());

        assertThrows(TicketNotFoundException.class, () -> service.getTicketById(1));
        verify(repository, times(1)).findById(1);
    }

    @Test
    void testGetAllTickets() {
        List<TicketBooking> tickets = Arrays.asList(new TicketBooking(), new TicketBooking());
        when(repository.findAll()).thenReturn(tickets);

        List<TicketBooking> result = service.getAllTickets();
        assertEquals(tickets, result);
        verify(repository, times(1)).findAll();
    }

    @Test
    void testGetTicketsByUserId() {
        List<TicketBooking> tickets = Arrays.asList(new TicketBooking(), new TicketBooking());
        when(repository.findByUserId(1)).thenReturn(tickets);

        List<TicketBooking> result = service.getTicketsByUserId(1);
        assertEquals(tickets, result);
        verify(repository, times(1)).findByUserId(1);
    }

    @Test
    void testGetTicketsByEventId() {
        List<TicketBooking> tickets = Arrays.asList(new TicketBooking(), new TicketBooking());
        when(repository.findByEventId(1)).thenReturn(tickets);

        List<TicketBooking> result = service.getTicketsByEventId(1);
        assertEquals(tickets, result);
        verify(repository, times(1)).findByEventId(1);
    }

    @Test
    void testCancelTicket() throws TicketNotFoundException {
        TicketBooking ticket = new TicketBooking();
        ticket.setEventId(1);
        when(repository.findById(1)).thenReturn(Optional.of(ticket));

        String result = service.cancelTicket(1);
        assertEquals("Ticket cancelled", result);
        verify(eventClient, times(1)).increaseTicketCount(1);
        verify(repository, times(1)).save(ticket);
    }

    @Test
    void testCancelTicketNotFound() {
        when(repository.findById(1)).thenReturn(Optional.empty());

        assertThrows(TicketNotFoundException.class, () -> service.cancelTicket(1));
        verify(repository, times(1)).findById(1);
    }

    @Test
    void testSendEmail() {
        String to = "test@example.com";
        String subject = "Subject";
        String body = "Body";

        String result = service.sendEmail(to, subject, body);
        assertEquals("final", result);
        verify(javaMailSender, times(1)).send(any(SimpleMailMessage.class));
    }
}
