package com.cts.exceptions;

public class TicketNotFoundException extends Exception{
	 public  TicketNotFoundException(int id) {
	        super("Ticket not found with id: " + id);
	    }

}
