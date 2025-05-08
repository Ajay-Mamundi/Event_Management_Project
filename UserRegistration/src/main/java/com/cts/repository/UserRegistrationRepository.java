package com.cts.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cts.model.UserRegistration;

public interface UserRegistrationRepository extends JpaRepository<UserRegistration, Integer> {
    UserRegistration findByUserEmail(String email);
}
