package com.cts.dto;

import java.time.LocalDateTime;

import com.cts.model.TicketBooking;
import com.cts.model.TicketBooking.Status;

import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookingRequest {

private TicketBooking ticketBooking;
private EmailRequest emailRequest;

}
