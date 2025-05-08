package com.cts.controller;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.cts.exceptions.EventNotFoundException;
import com.cts.model.EventManagement;
import com.cts.service.EventManagementService;

@RestController
@RequestMapping("/events")
public class EventManagementController {
	@Autowired
	private EventManagementService service;

	@PostMapping("/saveevent")
	public String saveEvent(@RequestBody EventManagement event) {
		return service.saveEvent(event);
	}

	@GetMapping("/fetcheventbyid/{eid}")
	public EventManagement getEventById(@PathVariable("eid") int id) throws EventNotFoundException {
		return service.getEventById(id);
	}

	@PutMapping("/updateevent/{eid}")
	public String updateEvent(@PathVariable("eid") int id, @RequestBody EventManagement updatedEvent) {
		return service.updateEvent(id, updatedEvent);

	}

	@DeleteMapping("/deleteevent/{eid}")
	public String deleteEventById(@PathVariable("eid") int id) throws EventNotFoundException {
		return service.deleteEventById(id);
	}

	@GetMapping("/fetchallevents")
	public List<EventManagement> getAllEvents() {
		return service.getAllEvents();
	}

	@GetMapping("/fetcheventbycategory/{category}")
	public List<EventManagement> getEventByCategory(@PathVariable("category") String category)
			throws EventNotFoundException {
		return service.getEventByCategory(category);
	}

	@PostMapping("decreaseTicketCount/{id}")
	public void decreaseTicketCount(@PathVariable("id") int id) {
		service.decreaseTicketCount(id);
	}

	@PostMapping("increaseTicketCount/{id}")
	public void increaseTicketCount(@PathVariable("id") int id) {
		service.increaseTicketCount(id);
	}

	@GetMapping("/fetcheventbylocation/{location}")
	public List<EventManagement> getEventByLocation(@PathVariable("location") String location) throws Exception {
		return service.getEventByLocation(location);
	}

	@GetMapping("/fetcheventbydate/{date}")
	public List<EventManagement> getEventByDate(@PathVariable("date") String date) throws Exception {

		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		Date formattedDate = formatter.parse(date);
		System.out.println(formattedDate);
		return service.getEventByDate(formattedDate);
	}

}
