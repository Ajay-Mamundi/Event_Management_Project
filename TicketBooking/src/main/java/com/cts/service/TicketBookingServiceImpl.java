package com.cts.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cts.dto.EmailRequest;
import com.cts.dto.EventManagement;
import com.cts.dto.UserRegistration;
import  com.cts.exceptions.TicketNotFoundException;
import com.cts.feignclient.EventManagementClient;
import com.cts.feignclient.UserRegistrationClient;
import com.cts.model.TicketBooking;
import com.cts.model.TicketBooking.Status;
import com.cts.repository.TicketBookingRepository;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class TicketBookingServiceImpl implements TicketBookingService {

    private static final Logger logger = LoggerFactory.getLogger(TicketBookingServiceImpl.class);

    @Autowired
    private TicketBookingRepository repository;
    @Autowired
    private EventManagementClient eventClient;
    @Autowired
    private UserRegistrationClient userClient;
    @Autowired
    private JavaMailSender javaMailSender;

    @Override
    public TicketBooking bookTicket(TicketBooking ticket, EmailRequest request) {
        logger.info("Booking ticket for event ID: {} and user ID: {}", ticket.getEventId(), ticket.getUserId());

        // Check if the event exists
        EventManagement eventDetails = eventClient.getEventById(ticket.getEventId());

        // Check if the user exists
        UserRegistration userDetails = userClient.getUserById(ticket.getUserId());

        // Decrease ticket count in Event Management
        eventClient.decreaseTicketCount(ticket.getEventId());
        logger.info("Decreased ticket count for event ID: {}", ticket.getEventId());

        // Set booking date and status
        ticket.setTicketBookingDate(LocalDateTime.now());
        ticket.setTicketStatus(TicketBooking.Status.BOOKED);

        UserRegistration user = userClient.getUserById(ticket.getUserId());
        String emailId = user.getUserEmail();
        sendEmail(emailId, request.getSubject(), request.getMessage());
        logger.info("Sent email to user ID: {}", ticket.getUserId());

        // Save the ticket booking
        TicketBooking savedTicket = repository.save(ticket);
        logger.info("Ticket booked successfully for event ID: {} and user ID: {}", ticket.getEventId(), ticket.getUserId());
        return savedTicket;
    }

    @Override
    public TicketBooking getTicketById(int ticketId) throws TicketNotFoundException {
        logger.info("Fetching ticket with ID: {}", ticketId);
        Optional<TicketBooking> optional = repository.findById(ticketId);
        if (optional.isPresent()) {
            logger.info("Ticket found with ID: {}", ticketId);
            return optional.get();
        } else {
            logger.error("Ticket not found with ID: {}", ticketId);
            throw new TicketNotFoundException(ticketId);
        }
    }

    @Override
    public List<TicketBooking> getAllTickets() {
        logger.info("Fetching all tickets");
        return repository.findAll();
    }

    @Override
    public List<TicketBooking> getTicketsByUserId(int userId) {
        logger.info("Fetching tickets for user ID: {}", userId);
        UserRegistration userDetails = userClient.getUserById(userId);
        return repository.findByUserId(userId);
    }

    @Override
    public List<TicketBooking> getTicketsByEventId(int eventId) {
        logger.info("Fetching tickets for event ID: {}", eventId);
        EventManagement eventDetails = eventClient.getEventById(eventId);

        return repository.findByEventId(eventId);
    }

    @Override
    public String cancelTicket(int ticketId) throws TicketNotFoundException {
        logger.info("Cancelling ticket with ID: {}", ticketId);
        TicketBooking ticket = getTicketById(ticketId);
        ticket.setTicketStatus(Status.CANCELLED);
        eventClient.increaseTicketCount(ticket.getEventId());
        repository.save(ticket);
        logger.info("Ticket cancelled with ID: {}", ticketId);
        return "Ticket cancelled";
    }

    public String sendEmail(String to, String subject, String body) {
        logger.info("Sending email to: {}", to);
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject(subject);
        message.setText(body);
        javaMailSender.send(message);
        logger.info("Email sent to: {}", to);
        return "final";
    }
}
