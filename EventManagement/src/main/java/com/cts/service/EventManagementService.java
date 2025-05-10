package com.cts.service;

import java.util.*;

import com.cts.exceptions.EventNotFoundException;
import com.cts.exceptions.InvalidDataException;
import com.cts.model.EventManagement;

public interface EventManagementService {

	public abstract String saveEvent(EventManagement event) throws InvalidDataException;

	public abstract EventManagement getEventById(int id) throws EventNotFoundException;

	public abstract String updateEvent(int id, EventManagement updatedEvent) throws InvalidDataException;

	public abstract String deleteEventById(int id) throws EventNotFoundException;
	
	public void decreaseTicketCount(int id) throws EventNotFoundException;
	
	public void increaseTicketCount(int id) throws EventNotFoundException;
	
	public abstract List<EventManagement> getAllEvents();

	public abstract List<EventManagement> getEventByCategory(String category) throws EventNotFoundException;
	
	public abstract List<EventManagement> getEventByLocation(String location) throws EventNotFoundException;
	
	public abstract List<EventManagement> getEventByDate(Date date) throws EventNotFoundException;
	
}
