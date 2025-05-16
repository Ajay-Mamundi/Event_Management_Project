package com.cts.service;

import java.util.List;


import com.cts.exceptions.TicketNotFoundException;
import com.cts.model.TicketBooking;

public interface TicketBookingService {
	public abstract TicketBooking bookTicket(TicketBooking ticket);
	 
	public abstract TicketBooking getTicketById(int ticketId) throws TicketNotFoundException;
 
	public abstract List<TicketBooking> getAllTickets();
 
	public abstract List<TicketBooking> getTicketsByUserId(int userId);
 
	public abstract List<TicketBooking> getTicketsByEventId(int eventId);
 
	public abstract String cancelTicket(int ticketId) throws TicketNotFoundException;

	//public String sendEmailForBooking(String emailId,EmailRequest request);
}
