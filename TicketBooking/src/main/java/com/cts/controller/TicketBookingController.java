package com.cts.controller;

import java.util.List;
import com.cts.service.TicketBookingServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cts.exceptions.TicketNotFoundException;
import com.cts.model.TicketBooking;
import com.cts.service.TicketBookingService;

@RestController
@RequestMapping("/tickets")
public class TicketBookingController {

    private final TicketBookingServiceImpl ticketBookingServiceImpl;

	@Autowired
	private TicketBookingService ticketBookingService;

    TicketBookingController(TicketBookingServiceImpl ticketBookingServiceImpl) {
        this.ticketBookingServiceImpl = ticketBookingServiceImpl;
    }

	@PostMapping("/book")
	public TicketBooking bookTicket(@RequestBody TicketBooking ticket) {
		return ticketBookingService.bookTicket(ticket);
	}

	@GetMapping("getticketbyid/{ticketId}")
	public TicketBooking getTicketById(@PathVariable("ticketId") int ticketId) throws TicketNotFoundException {
		return ticketBookingService.getTicketById(ticketId);
	}

	@GetMapping("/getalltickets")
	public List<TicketBooking> getAllTickets() {
		return ticketBookingService.getAllTickets();
	}
	
	@GetMapping("/getticketsbyuserid/{userid}")
	public List<TicketBooking> getTicketsByUserId(@PathVariable("userid") int userId) {
		return ticketBookingService.getTicketsByUserId(userId);
	}
	
	@GetMapping("/getticketsbyeventid/{eventid}")
	public List<TicketBooking> getTicketsByEventId(@PathVariable("eventid") int userId) {
		return ticketBookingService.getTicketsByEventId(userId);
	}
	
    @DeleteMapping("/cancelticket/{ticketid}")
    public String cancelTicket(@PathVariable("ticketid") int ticketid) throws TicketNotFoundException {
		return ticketBookingService.cancelTicket(ticketid);
    	
    }
	

}
