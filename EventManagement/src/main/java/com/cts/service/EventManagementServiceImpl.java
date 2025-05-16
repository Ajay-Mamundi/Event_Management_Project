package com.cts.service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cts.exceptions.EventNotFoundException;
import com.cts.exceptions.InvalidDataException;
import com.cts.model.EventManagement;
import com.cts.repository.EventManagementRepository;

import jakarta.persistence.EntityNotFoundException;

@Service
public class EventManagementServiceImpl implements EventManagementService {

    // Logger instance for logging
    private static final Logger logger = LoggerFactory.getLogger(EventManagementServiceImpl.class);

    @Autowired
    private EventManagementRepository repository;


    @Override
    public String saveEvent(EventManagement event) throws InvalidDataException {
        logger.info("Saving event: {}", event);
        EventManagement e  = repository.save(event);
        if(e!=null) {
        	logger.info("Event created successfully: {}", event);
            return "Event created successfully..!!";
        }
        else {
        	throw new InvalidDataException("Enter the valid event data");
        }
    }


    @Override
    public EventManagement getEventById(int id) throws EventNotFoundException {
        logger.info("Fetching event with ID: {}", id);
        Optional<EventManagement> optional = repository.findById(id);
        if (optional.isPresent()) {
            logger.info("Event found with ID: {}", id);
            return optional.get();
        } else {
            logger.error("Event not found with ID: {}", id);
            throw new EventNotFoundException("Invalid Event ID");
        }    
    }


    @Override
    public String updateEvent(int id, EventManagement updatedEvent) throws InvalidDataException {
        logger.info("Updating event with ID: {}", id);
        Optional<EventManagement> optional = repository.findById(id);
        if (optional.isPresent()) {
            EventManagement existingEvent = optional.get();
            existingEvent.setEventName(updatedEvent.getEventName());
            existingEvent.setEventCategory(updatedEvent.getEventCategory());
            existingEvent.setEventLocation(updatedEvent.getEventLocation());
            existingEvent.setEventDate(updatedEvent.getEventDate());
            existingEvent.setEventOrganizerId(updatedEvent.getEventOrganizerId());
            repository.save(existingEvent);
            logger.info("Event updated successfully with ID: {}", id);
            return "Event updated successfully..!";
        } else {
            logger.error("Event not found with ID: {}", id);
            throw new InvalidDataException("Event not found with id: " + id);
        }
    }


    @Override
    public String deleteEventById(int id) throws EventNotFoundException {
        logger.info("Deleting event with ID: {}", id);
        if (!repository.existsById(id)) {
            logger.error("Event not found with ID: {}", id);
            throw new EventNotFoundException("Event not found with id: " + id);
        }
        repository.deleteById(id);
        logger.info("Event deleted successfully with ID: {}", id);
        return "Event deleted successfully";
    }


    @Override
    public List<EventManagement> getAllEvents() {
        logger.info("Fetching all events");
        return repository.findAll();
    }


    @Override
    public List<EventManagement> getEventByCategory(String category) throws EventNotFoundException {
        logger.info("Fetching events by category: {}", category);
        List<EventManagement> events = repository.findByEventCategory(category);
        if (events.isEmpty()) {
            logger.error("No events found in category: {}", category);
            throw new EventNotFoundException(category);
        }
        return events;
    }

    @Override
    public void decreaseTicketCount(int eventId) throws EventNotFoundException {
        logger.info("Decreasing ticket count for event ID: {}", eventId);
        EventManagement event = repository.findById(eventId)
                .orElseThrow(() -> new EventNotFoundException("Event not found with Id: " + eventId));
        int currentCount = event.getTicketCount();
        if (currentCount > 0) {
            event.setTicketCount(currentCount - 1);
            repository.save(event);
            logger.info("Ticket count decreased for event ID: {}", eventId);
        }
    }


    @Override
    public void increaseTicketCount(int eventId) throws EventNotFoundException {
        logger.info("Increasing ticket count for event ID: {}", eventId);
        EventManagement event = repository.findById(eventId)
                .orElseThrow(() -> new EventNotFoundException("Event not found with Id: " + eventId));
        int currentCount = event.getTicketCount();
        event.setTicketCount(currentCount + 1);
        repository.save(event);
        logger.info("Ticket count increased for event ID: {}", eventId);
    }


    @Override
    public List<EventManagement> getEventByLocation(String location) throws EventNotFoundException {
        logger.info("Fetching events by location: {}", location);
        location = location.toLowerCase();
        List<EventManagement> events = repository.findByEventLocation(location);
        if (events.isEmpty()) {
            logger.error("No events found in location: {}", location);
            throw new EventNotFoundException("Event not found with location: " + location);
        }
        return events;
    }

   
    @Override
    public List<EventManagement> getEventByDate(Date date) throws EventNotFoundException {
        logger.info("Fetching events by date: {}", date);
        List<EventManagement> events = repository.findByEventDate(date);
        if (events.isEmpty()) {
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
            String dateStr = formatter.format(date);
            logger.error("No events found on date: {}", dateStr);
            throw new EventNotFoundException("Event not found with date: " + dateStr);
        }
        return events;
    }
}
