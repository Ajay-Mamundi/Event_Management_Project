package com.cts.feignclient;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.cts.dto.EventManagement;

@FeignClient(name="EVENTMANAGEMENT",path="events")
public interface EventManagementClient {
     
	@GetMapping("/fetcheventbyid/{eid}")
	public EventManagement getEventById(@PathVariable("eid") int id);
}