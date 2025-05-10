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
import com.cts.model.EventManagement;
import com.cts.repository.EventManagementRepository;

import jakarta.persistence.EntityNotFoundException;

@Service
public class EventManagementServiceImpl implements EventManagementService {

    // Logger instance for logging
    private static final Logger logger = LoggerFactory.getLogger(EventManagementServiceImpl.class);

    @Autowired
    private EventManagementRepository repository;

    /**
     * Saves an event.
     * 
     * @param event The event details.
     * @return A message indicating the event was created.
     */
    @Override
    public String saveEvent(EventManagement event) {
        logger.info("Saving event: {}", event);
        repository.save(event);
        logger.info("Event created successfully: {}", event);
        return "Event created successfully..!!";
    }

    /**
     * Retrieves an event by its ID.
     * 
     * @param id The ID of the event.
     * @return The event details.
     * @throws EventNotFoundException If the event is not found.
     */
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

    /**
     * Updates an event.
     * 
     * @param id The ID of the event.
     * @param updatedEvent The updated event details.
     * @return A message indicating the event was updated.
     */
    @Override
    public String updateEvent(int id, EventManagement updatedEvent) {
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
            throw new EntityNotFoundException("Event not found with id: " + id);
        }
    }

    /**
     * Deletes an event by its ID.
     * 
     * @param id The ID of the event.
     * @return A message indicating the event was deleted.
     * @throws EventNotFoundException If the event is not found.
     */
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

    /**
     * Retrieves all events.
     * 
     * @return A list of all events.
     */
    @Override
    public List<EventManagement> getAllEvents() {
        logger.info("Fetching all events");
        return repository.findAll();
    }

    /**
     * Retrieves events by category.
     * 
     * @param category The category of the events.
     * @return A list of events in the specified category.
     * @throws EventNotFoundException If no events are found in the category.
     */
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

    /**
     * Decreases the ticket count for an event.
     * 
     * @param eventId The ID of the event.
     */
    @Override
    public void decreaseTicketCount(int eventId) {
        logger.info("Decreasing ticket count for event ID: {}", eventId);
        EventManagement event = repository.findById(eventId)
                .orElseThrow(() -> new RuntimeException("Event not found with Id: " + eventId));
        int currentCount = event.getTicketCount();
        if (currentCount > 0) {
            event.setTicketCount(currentCount - 1);
            repository.save(event);
            logger.info("Ticket count decreased for event ID: {}", eventId);
        } else {
            logger.error("No tickets available for event with ID: {}", eventId);
            throw new RuntimeException("No tickets available for event with Id: " + eventId);
        }
    }

    /**
     * Increases the ticket count for an event.
     * 
     * @param eventId The ID of the event.
     */
    @Override
    public void increaseTicketCount(int eventId) {
        logger.info("Increasing ticket count for event ID: {}", eventId);
        EventManagement event = repository.findById(eventId)
                .orElseThrow(() -> new RuntimeException("Event not found with Id: " + eventId));
        int currentCount = event.getTicketCount();
        event.setTicketCount(currentCount + 1);
        repository.save(event);
        logger.info("Ticket count increased for event ID: {}", eventId);
    }

    /**
     * Retrieves events by location.
     * 
     * @param location The location of the events.
     * @return A list of events in the specified location.
     * @throws Exception If no events are found in the location.
     */
    @Override
    public List<EventManagement> getEventByLocation(String location) throws Exception {
        logger.info("Fetching events by location: {}", location);
        location = location.toLowerCase();
        List<EventManagement> events = repository.findByEventLocation(location);
        if (events.isEmpty()) {
            logger.error("No events found in location: {}", location);
            throw new Exception("Event not found with location: " + location);
        }
        return events;
    }

    /**
     * Retrieves events by date.
     * 
     * @param date The date of the events.
     * @return A list of events on the specified date.
     * @throws Exception If no events are found on the date.
     */
    @Override
    public List<EventManagement> getEventByDate(Date date) throws Exception {
        logger.info("Fetching events by date: {}", date);
        List<EventManagement> events = repository.findByEventDate(date);
        if (events.isEmpty()) {
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
            String dateStr = formatter.format(date);
            logger.error("No events found on date: {}", dateStr);
            throw new Exception("Event not found with date: " + dateStr);
        }
        return events;
    }
}
