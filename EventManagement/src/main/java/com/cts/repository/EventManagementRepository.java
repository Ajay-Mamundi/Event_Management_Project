package com.cts.repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cts.model.EventManagement;




public interface EventManagementRepository extends JpaRepository<EventManagement,Integer> {

    List<EventManagement> findByEventCategory(String category);

	List<EventManagement> findByEventDate(Date date);

	List<EventManagement> findByEventLocation(String location);

	

}
