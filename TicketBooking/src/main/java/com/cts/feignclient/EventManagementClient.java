package com.cts.feignclient;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import com.cts.dto.EventManagement;

@FeignClient(name="EVENTMANAGEMENT",path="events")
public interface EventManagementClient {
     
	@GetMapping("/fetcheventbyid/{eid}")
	public EventManagement getEventById(@PathVariable("eid") int id);
	
	@PostMapping("increaseTicketCount/{eid}")
	public void increaseTicketCount(@PathVariable("eid") int id);
	
	@PostMapping("decreaseTicketCount/{eid}")
	public void decreaseTicketCount(@PathVariable("eid") int id);
}
