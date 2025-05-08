package com.cts.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cts.dto.EventManagement;
import com.cts.dto.UserRegistration;
import com.cts.exceptions.TicketNotFoundException;
import com.cts.feignclient.EventManagementClient;
import com.cts.feignclient.UserRegistrationClient;
import com.cts.model.TicketBooking;
import com.cts.model.TicketBooking.Status;
import com.cts.repository.TicketBookingRepository;

@Service
public class TicketBookingServiceImpl implements TicketBookingService {
	@Autowired
	private TicketBookingRepository repository;
	@Autowired
	private EventManagementClient eventClient;
	@Autowired
	private UserRegistrationClient userClient;

	@Override
	public TicketBooking bookTicket(TicketBooking ticket) {

		// Check if the event exists
		EventManagement eventDetails = eventClient.getEventById(ticket.getEventId());
		if (eventDetails == null) {
			throw new RuntimeException("Event not found with id: " + ticket.getEventId());
		}

		// Check if the user exists
		UserRegistration userDetails = userClient.getUserById(ticket.getUserId());
		if (userDetails == null) {
			throw new RuntimeException("User not found with id: " + ticket.getUserId());
		}

		// Set booking date and status
		ticket.setTicketBookingDate(LocalDateTime.now());
		ticket.setTicketStatus(TicketBooking.Status.BOOKED);

		// decrease ticket count in Event Management
		eventClient.decreaseTicketCount(ticket.getEventId());

		// Save the ticket booking
		return repository.save(ticket);

	}

	@Override
	public TicketBooking getTicketById(int ticketId) throws TicketNotFoundException {
		Optional<TicketBooking> optional = repository.findById(ticketId);
		if (optional.isPresent()) {
			return optional.get();
		} else {
			throw new TicketNotFoundException(ticketId);
		}

	}

	@Override
	public List<TicketBooking> getAllTickets() {
		return repository.findAll();

	}

	@Override
	public List<TicketBooking> getTicketsByUserId(int userId) {
		return repository.findByUserId(userId);
	}

	@Override
	public List<TicketBooking> getTicketsByEventId(int eventId) {
		// TODO Auto-generated method stub
		return repository.findByEventId(eventId);
	}

	@Override
	public String cancelTicket(int ticketId) throws TicketNotFoundException {
		TicketBooking ticket = getTicketById(ticketId);
		ticket.setTicketStatus(Status.CANCELLED);
		eventClient.increaseTicketCount(ticket.getEventId());
		repository.save(ticket);
		return "Ticket cancelled";

		
	}

}
