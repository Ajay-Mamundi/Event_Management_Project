package com.cts.feignclient;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.cts.dto.UserRegistration;

@FeignClient(name = "USERREGISTRATION", path = "users")
public interface UserRegistrationClient {
	@GetMapping("/{id}")
	public UserRegistration getUserById(@PathVariable int id);

}
