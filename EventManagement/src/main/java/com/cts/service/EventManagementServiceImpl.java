package com.cts.service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cts.exceptions.EventNotFoundException;
import com.cts.model.EventManagement;
import com.cts.repository.EventManagementRepository;

import jakarta.persistence.EntityNotFoundException;

@Service
public class EventManagementServiceImpl implements EventManagementService {
	@Autowired
	private EventManagementRepository repository;

	@Override
	public String saveEvent(EventManagement event) {
		repository.save(event);
		return "Event created successfully..!!";
	}

	@Override
	public EventManagement getEventById(int id) throws EventNotFoundException {
		Optional<EventManagement> optional = repository.findById(id);
		if (optional.isPresent())
			return optional.get();
		else
			throw new EventNotFoundException("Invalid Event ID");
	}

	@Override
	public String updateEvent(int id, EventManagement updatedEvent) {

		Optional<EventManagement> optional = repository.findById(id);
		if (optional.isPresent()) {
			EventManagement existingEvent = optional.get();
			existingEvent.setEventName(updatedEvent.getEventName());
			existingEvent.setEventCategory(updatedEvent.getEventCategory());
			existingEvent.setEventLocation(updatedEvent.getEventLocation());
			existingEvent.setEventDate(updatedEvent.getEventDate());
			existingEvent.setEventOrganizerId(updatedEvent.getEventOrganizerId());
			repository.save(existingEvent);
			return "Event updated successfully..!";
		} else {
			throw new EntityNotFoundException("Event not found with id: " + id);
		}
	}

	@Override
	public String deleteEventById(int id) throws EventNotFoundException {
		if (!repository.existsById(id)) {			
			throw new EventNotFoundException("Event not found with id: "+id);
		}
		repository.deleteById(id);
        return "Event deleted successfully";
	}

	@Override
	public List<EventManagement> getAllEvents() {
		return repository.findAll();
	}

	@Override
	public List<EventManagement> getEventByCategory(String category) throws EventNotFoundException {
		List<EventManagement> events = repository.findByEventCategory(category);
		if(events.isEmpty()) {
			throw new EventNotFoundException(category);
		}
		return events;
	}
	
    @Override
    public void decreaseTicketCount(int eventId) {
        EventManagement event = repository.findById(eventId)
                .orElseThrow(() -> new RuntimeException("Event not found with Id: "+eventId));
        int currentCount = event.getTicketCount();
        if (currentCount > 0) {
            event.setTicketCount(currentCount - 1);
            repository.save(event);
        } else {
            throw new RuntimeException("No tickets available for event with Id: "+eventId);
        }
    }
 
    @Override
    public void increaseTicketCount(int eventId) {
        EventManagement event = repository.findById(eventId)
                .orElseThrow(() -> new RuntimeException("Event not found with Id: "+eventId));
        int currentCount = event.getTicketCount();
        event.setTicketCount(currentCount + 1);
        repository.save(event);
       
    }
    @Override
	public List<EventManagement> getEventByLocation(String location) throws Exception {
		location = location.toLowerCase();
		List<EventManagement> events = repository.findByEventLocation(location);
		if(events.isEmpty()) {
			throw new Exception("Event not found with location : "+location);
		}
		return events;
	}
	
	@Override
	public List<EventManagement> getEventByDate(Date date) throws Exception {
		List<EventManagement> events = repository.findByEventDate(date);
		if(events.isEmpty()) {
			
			SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
	        String dateStr = formatter.format(date);
			throw new Exception("Event not found with date : "+dateStr);
		}
		return events;
	}

}
