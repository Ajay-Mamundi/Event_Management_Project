package com.cts.service;

import java.util.*;

import com.cts.exceptions.EventNotFoundException;
import com.cts.model.EventManagement;

public interface EventManagementService {

	public abstract String saveEvent(EventManagement event);

	public abstract EventManagement getEventById(int id) throws EventNotFoundException;

	public abstract String updateEvent(int id, EventManagement updatedEvent);

	public abstract String deleteEventById(int id) throws EventNotFoundException;
	
	public void decreaseTicketCount(int id);
	
	public void increaseTicketCount(int id);
	
	public abstract List<EventManagement> getAllEvents();

	public abstract List<EventManagement> getEventByCategory(String category) throws EventNotFoundException;
	
	public abstract List<EventManagement> getEventByLocation(String location) throws Exception;
	
	public abstract List<EventManagement> getEventByDate(Date date) throws Exception;
	
}
